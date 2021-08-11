open module org.cloudfoundry.multiapps.common {

    exports org.cloudfoundry.multiapps.common;
    exports org.cloudfoundry.multiapps.common.tags;
    exports org.cloudfoundry.multiapps.common.util;
    exports org.cloudfoundry.multiapps.common.util.yaml;

    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive org.yaml.snakeyaml;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.xml.bind;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;

    requires static java.compiler;
    requires static org.immutables.value;

}
