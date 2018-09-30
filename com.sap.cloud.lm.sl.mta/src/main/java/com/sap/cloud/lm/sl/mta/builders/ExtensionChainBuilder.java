package com.sap.cloud.lm.sl.mta.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ExtensionElement;
import com.sap.cloud.lm.sl.mta.model.IdentifiableElement;

public class ExtensionChainBuilder<T extends ExtensionElement> {

    private final boolean isStrict;

    public ExtensionChainBuilder() {
        this(true);
    }

    public ExtensionChainBuilder(boolean isStrict) {
        this.isStrict = isStrict;
    }

    public List<T> build(IdentifiableElement rootElement, List<T> extensionElements) throws ContentException {
        Map<String, T> extensionElementsPerParent = getExtensionElementsPerParent(extensionElements);
        return build(rootElement, extensionElementsPerParent);
    }

    private List<T> build(IdentifiableElement rootElement, Map<String, T> extensionElementsPerParent) {
        List<T> chain = new ArrayList<>();
        IdentifiableElement currentElement = rootElement;
        while (currentElement != null) {
            T nextElement = extensionElementsPerParent.remove(currentElement.getId());
            ListUtil.addNonNull(chain, nextElement);
            currentElement = nextElement;
        }
        if (!extensionElementsPerParent.isEmpty() && isStrict) {
            throw new ContentException(Messages.CANNOT_BUILD_EXTENSION_DESCRIPTOR_CHAIN_BECAUSE_DESCRIPTORS_0_HAVE_AN_UNKNOWN_PARENT,
                String.join(",", getIds(extensionElementsPerParent.values())));
        }
        return chain;
    }

    private Map<String, T> getExtensionElementsPerParent(List<T> extensionElements) {
        Map<String, List<T>> extensionElementsPerParent = extensionElements.stream()
            .collect(Collectors.groupingBy(ExtensionElement::getParentId));
        return prune(extensionElementsPerParent);
    }

    private Map<String, T> prune(Map<String, List<T>> extensionElementsPerParent) {
        validateSingleExtensionElementPerParent(extensionElementsPerParent);
        return extensionElementsPerParent.entrySet()
            .stream()
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()
                .get(0)));
    }

    private void validateSingleExtensionElementPerParent(Map<String, List<T>> extensionElementsPerParent) {
        for (Map.Entry<String, List<T>> extensionElementsForParent : extensionElementsPerParent.entrySet()) {
            String parent = extensionElementsForParent.getKey();
            List<T> extensionElements = extensionElementsForParent.getValue();
            if (extensionElements.size() > 1 && isStrict) {
                throw new ContentException(Messages.MULTIPLE_EXTENSION_DESCRIPTORS_EXTEND_THE_PARENT_0, parent);
            }
        }
    }

    private List<String> getIds(Collection<T> extensionElements) {
        return extensionElements.stream()
            .map(ExtensionElement::getId)
            .collect(Collectors.toList());
    }

}
