package org.cloudfoundry.multiapps.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class DigestHelperTest {

    @Test
    void testComputeFileChecksum() throws Exception {
        assertEquals("92CB62F85B7C1AB36729D35C96FCF875CBEC1C7ECCBAAD3B173CF53A00EFD2EC",
                     DigestHelper.computeFileChecksum(Paths.get("src/test/resources/org/cloudfoundry/multiapps/common/util/web.zip"),
                                                      "SHA256"));
    }

    @Test
    void testComputeDirectoryChecksum() throws Exception {
        assertEquals("6631688B333F6D2EBEC3666671685509D00707DE0992E6C2A4C88D7F4F8443BB",
                     DigestHelper.computeDirectoryCheckSum(Paths.get("src/test/resources/org/cloudfoundry/multiapps/common/util"), "SHA256"));
    }

}
