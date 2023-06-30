package org.cloudfoundry.multiapps.common.util.yaml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.ObjectUtils;
import org.cloudfoundry.multiapps.common.Messages;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class YamlRepresenter extends Representer {

    public YamlRepresenter(DumperOptions options) {
        super(options);
        getPropertyUtils().setBeanAccess(BeanAccess.FIELD);
    }

    @Override
    protected Set<Property> getProperties(Class<?> type) {
        Set<Property> properties = super.getProperties(type);
        if (type.isAnnotationPresent(YamlElementOrder.class)) {
            List<String> fieldOrder = Arrays.asList(type.getAnnotation(YamlElementOrder.class)
                                                        .value());
            Set<Property> sortedProperties = new TreeSet<>(new YamlPropertyComparator(fieldOrder));
            sortedProperties.addAll(properties);
            return sortedProperties;
        }
        return properties;
    }

    @Override
    protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
        if (ObjectUtils.isEmpty(propertyValue)) {
            return null;
        }
        Field field = getField(javaBean.getClass(), property.getName());
        String nodeName = field.isAnnotationPresent(YamlElement.class) ? field.getAnnotation(YamlElement.class)
                                                                              .value()
            : property.getName();

        if (field.isAnnotationPresent(YamlAdapter.class)) {
            return getAdaptedTuple(propertyValue, field, nodeName);
        }

        NodeTuple defaultNode = super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        return new NodeTuple(representData(nodeName), defaultNode.getValueNode());
    }

    private Field getField(Class<?> type, String fieldName) {
        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // If this field was not declared by the current class, then it must have been declared in a super class
            return getField(type.getSuperclass(), fieldName);
        }
    }

    @SuppressWarnings("unchecked")
    private NodeTuple getAdaptedTuple(Object propertyValue, Field field, String nodeName) {
        Class<? extends YamlConverter<?, ?>> converterClass = field.getAnnotation(YamlAdapter.class)
                                                                   .value();
        try {
            YamlConverter<Object, ?> converter = (YamlConverter<Object, ?>) converterClass.getDeclaredConstructor()
                                                                                          .newInstance();
            Object converted = converter.convert(propertyValue);
            return new NodeTuple(representData(nodeName), represent(converted));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException
            | NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException(MessageFormat.format(Messages.COULD_NOT_CONSTRUCT_YAML_CONVERTER_0_BECAUSE_OF_1,
                                                                 converterClass.getName(), e.getMessage()),
                                            e);
        }
    }
}
