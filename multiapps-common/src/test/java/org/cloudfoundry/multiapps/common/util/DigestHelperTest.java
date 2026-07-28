package org.cloudfoundry.multiapps.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class DigestHelperTest {

    @Test
    void testComputeFileChecksum() throws Exception {
        assertEquals("1FB3C1C828D35B250A193885392A95B9C750AF74CC1DE959FFDA8CFF1929ECEE02015A8C28D4075678FD04CF523C675D",
                     DigestHelper.computeFileChecksum(Paths.get("src/test/resources/org/cloudfoundry/multiapps/common/util/web.zip"),
                                                      "SHA-384"));
    }

    @Test
    void testComputeDirectoryChecksum() throws Exception {
        assertEquals("BC7AEA1205F45FAF81C9120EAD1FE704F84EABA4C502AAD9F4A99A347EBB71D39D80AF975AC0F1C825DDD9A7C66DC012",
                     DigestHelper.computeDirectoryCheckSum(Paths.get("src/test/resources/org/cloudfoundry/multiapps/common/util"), "SHA-384"));
    }

}
