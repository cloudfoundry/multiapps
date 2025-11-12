open module org.cloudfoundry.multiapps.mta {

    exports org.cloudfoundry.multiapps.mta;
    exports org.cloudfoundry.multiapps.mta.builders;
    exports org.cloudfoundry.multiapps.mta.builders.v2;
    exports org.cloudfoundry.multiapps.mta.builders.v3;
    exports org.cloudfoundry.multiapps.mta.handlers;
    exports org.cloudfoundry.multiapps.mta.handlers.v2;
    exports org.cloudfoundry.multiapps.mta.handlers.v3;
    exports org.cloudfoundry.multiapps.mta.helpers;
    exports org.cloudfoundry.multiapps.mta.mergers;
    exports org.cloudfoundry.multiapps.mta.mergers.v2;
    exports org.cloudfoundry.multiapps.mta.mergers.v3;
    exports org.cloudfoundry.multiapps.mta.model;
    exports org.cloudfoundry.multiapps.mta.parsers;
    exports org.cloudfoundry.multiapps.mta.parsers.v2;
    exports org.cloudfoundry.multiapps.mta.parsers.v3;
    exports org.cloudfoundry.multiapps.mta.resolvers;
    exports org.cloudfoundry.multiapps.mta.resolvers.v2;
    exports org.cloudfoundry.multiapps.mta.resolvers.v3;
    exports org.cloudfoundry.multiapps.mta.schema;
    exports org.cloudfoundry.multiapps.mta.util;
    exports org.cloudfoundry.multiapps.mta.validators;
    exports org.cloudfoundry.multiapps.mta.validators.v2;
    exports org.cloudfoundry.multiapps.mta.validators.v3;

    requires transitive org.cloudfoundry.multiapps.common;

    requires org.apache.commons.collections4;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.semver4j;
    requires org.apache.commons.compress;

    requires static java.compiler;
    requires static org.immutables.value;

}