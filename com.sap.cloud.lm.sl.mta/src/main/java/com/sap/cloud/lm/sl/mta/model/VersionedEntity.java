package com.sap.cloud.lm.sl.mta.model;

import java.text.MessageFormat;

import com.sap.cloud.lm.sl.mta.message.Messages;

public class VersionedEntity {

    protected final int majorSchemaVersion;

    public VersionedEntity(int majorSchemaVersion) {
        this.majorSchemaVersion = majorSchemaVersion;
    }

    protected void supportedSince(int requiredMajorSchemaVersion) {
        if (this.majorSchemaVersion < requiredMajorSchemaVersion) {
            throw new UnsupportedOperationException(MessageFormat.format(Messages.ENTITIES_FROM_CLASS_0_WITH_SCHEMA_VERSION_1_DO_NOT_SUPPORT_THIS_OPERATION_AT_LEAST_2_IS_REQUIRED,
                                                                         getClass().getSimpleName(), this.majorSchemaVersion,
                                                                         requiredMajorSchemaVersion));
        }
    }

    public int getMajorSchemaVersion() {
        return majorSchemaVersion;
    }

}
