open module org.cloudfoundry.multiapps.common {

    exports org.cloudfoundry.multiapps.common;
    exports org.cloudfoundry.multiapps.common.tags;
    exports org.cloudfoundry.multiapps.common.util;
    exports org.cloudfoundry.multiapps.common.util.yaml;

    requires transitive org.yaml.snakeyaml;
    requires transitive tools.jackson.core;
    requires transitive tools.jackson.databind;

    requires com.fasterxml.jackson.annotation;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;

    requires static java.compiler;
    requires static org.immutables.value;
    requires jakarta.xml.bind;
}
