package com.sap.cloud.lm.sl.mta.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ExtensionElement;
import com.sap.cloud.lm.sl.mta.model.IdentifiableElement;

public class ExtensionChainBuilder<T extends ExtensionElement> {

    private IdentifiableElement rootElement;
    private List<T> extensionElements;
    private boolean isStrict;

    private List<T> chain;

    public ExtensionChainBuilder(IdentifiableElement rootElement, List<T> extensionElements) {
        this(rootElement, extensionElements, true);
    }

    public ExtensionChainBuilder(IdentifiableElement rootElement, List<T> extensionElements, boolean isStrict) {
        this.rootElement = rootElement;
        this.extensionElements = extensionElements;
        this.isStrict = isStrict;
    }

    public List<T> build() throws ContentException {
        Map<String, T> parents = getParents();
        chain = new ArrayList<>();
        while (chain.size() < extensionElements.size()) {
            T next = getNextExtensionElement(parents);
            if (next == null) {
                if (isStrict) {
                    throw new ContentException(Messages.CANNOT_BUILD_EXTENSION_DESCRIPTOR_CHAIN, getLastExtensionElementName());
                }
                break;
            }
            chain.add(next);
        }
        return chain;
    }

    private T getNextExtensionElement(Map<String, T> parents) {
        for (T extensionElement : extensionElements) {
            T parent = parents.get(extensionElement.getId());
            if (parentIsPreviousExtensionElement(parent)) {
                return extensionElement;
            }
        }
        return null;
    }

    private Map<String, T> getParents() throws ContentException {
        Map<String, T> parents = new HashMap<>();
        for (T extensionElement : extensionElements) {
            parents.put(extensionElement.getId(), getParent(extensionElement));
        }
        return parents;
    }

    private T getParent(T extensionElement) throws ContentException {
        String parentName = extensionElement.getParentId();
        if (parentName.equals(rootElement.getId())) {
            return null;
        }
        for (T current : extensionElements) {
            if (parentName.equals(current.getId())) {
                return current;
            }
        }
        if (isStrict) {
            String extensionDescriptorId = extensionElement.getId();
            throw new ContentException(Messages.UNKNOWN_PARENT_DESCRIPTOR, parentName, extensionDescriptorId);
        }
        return extensionElement;
    }

    private boolean parentIsPreviousExtensionElement(ExtensionElement parent) {
        return parent == getLastExtensionElement();
    }

    private String getLastExtensionElementName() {
        ExtensionElement last = getLastExtensionElement();
        if (last != null) {
            return last.getId();
        }
        return "";
    }

    private ExtensionElement getLastExtensionElement() {
        if (chain.size() == 0) {
            return null;
        }
        return chain.get(chain.size() - 1);
    }

}
