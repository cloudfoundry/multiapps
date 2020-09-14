open module org.cloudfoundry.multiapps.common.test {

    exports org.cloudfoundry.multiapps.common.test;

    requires transitive org.apache.commons.lang3;
    requires transitive org.mockito;

    requires org.cloudfoundry.multiapps.common;
    requires org.apache.commons.io;
    requires org.junit.jupiter.api;

    requires static java.compiler;
    requires static org.immutables.value;

}