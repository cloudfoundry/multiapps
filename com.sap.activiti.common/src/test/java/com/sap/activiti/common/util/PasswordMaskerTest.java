package com.sap.activiti.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PasswordMaskerTest {
    private static final String PASSWORD1 = "password1";
    private static final String PASSWORD2 = "password2";
    private static final String PASSWORD3 = "password3";

    private PasswordMasker masker;

    @Before
    public void setUp() {
        masker = new PasswordMasker();
    }

    @Test
    public void testWhenNullIsPassed_thenNullIsReturned() {
        assertNull(masker.maskPasswordsFromJson(null));
    }

    @Test
    public void testEmptyStringReturnsEmptyString() {
        String emptyString = "";
        String result = masker.maskPasswordsFromJson(emptyString);
        assertEquals("Empty string must not be changed by the method", emptyString, result);
    }

    @Test
    public void testCustomMaskingFragments() {
        PasswordMasker customMasker = new PasswordMasker(new String[] { "testFragment" });
        String jsonStringWithoutPasswords = "{\"WSDtestFragmentQWE\": \"" + PASSWORD1 + "\"}";
        String result = customMasker.maskPasswordsFromJson(jsonStringWithoutPasswords);

        assertFalse("JSON String should not contain the passwords after masking", result.contains(PASSWORD1));
    }

    @Test
    public void testJsonWithoutPasswordsIsNotChanged() {
        String jsonStringWithoutPasswords = "{\"test\":\"value\"}";
        String result = masker.maskPasswordsFromJson(jsonStringWithoutPasswords);
        assertEquals("JSON String without passwords must not be changed", jsonStringWithoutPasswords, result);
    }

    @Test
    public void testStringDoesntChangeStructureAfterPasswordIsMasked() {
        String jsonStringWithoutPasswords = "{\"test_pass\":\"value\"}";
        String result = masker.maskPasswordsFromJson(jsonStringWithoutPasswords);
        assertEquals("JSON string should keep it's structure after masking", "{\"test_pass\":\"***\"}", result);
    }

    @Test
    public void testOnePasswordJsonIsMasked() {
        String jsonStringWithoutPasswords = "{\"test_pwd\": \"" + PASSWORD1 + "\"}";
        String result = masker.maskPasswordsFromJson(jsonStringWithoutPasswords);
        assertFalse("JSON String should not contain the password after masking", result.contains(PASSWORD1));
    }

    @Test
    public void testMultiplePaswordsInJsonAreMasked() {
        String jsonStringWithoutPasswords = "{\"test_pwd\": \"" + PASSWORD1 + "\",\"test_pwd2\": \"" + PASSWORD2 + "\"}";
        String result = masker.maskPasswordsFromJson(jsonStringWithoutPasswords);
        assertFalse("JSON String should not contain the passwords after masking", result.contains(PASSWORD1) && result.contains(PASSWORD2));
    }

    @Test
    public void testPasswordsInComplexJsonAreMasked() {
        String complexJson = "{\"action\":\"update\",\"host_name\":\"<host_name>\",\"vm_size\":\"small\",\"EBS_volume\":\"no\",\"account\":\"SAP\",\"application\":\"<SID>\",\"component\":\"hanaxs_dedicated\",\"sid\":\"<SID>\",\"certificate_attributes\":{\"O\":\"defaultTenant\",\"UN\":\"setUser,setTenant\"},\"access_points\":[],\"run_list\":[\"role[dbpool-hanaxs-default-backup]\"],\"additional_parameters\":{\"hdb\":{\"sid\":\"<SID>\",\"system_pass\":\""
            + PASSWORD1 + "\",\"psadba_pwd\":\"" + PASSWORD2
            + "\",\"password_layout\":\"A1a\",\"certadm_user\":\"CERTADM\",\"certadm_password\":\"" + PASSWORD3
            + "\",\"security_location\":\"<security_location>\",\"identity_provider_name\":\"<identity_provider_name>\",\"sps\":\"<sps>\",\"hostname_prefix\":\"<hostname_prefix>\",\"location\":\"<location>\",\"client_location\":\"<client_location>\",\"afl_location\":\"<afl_location>\"},\"persistenceaas_maxdb\":{\"repository_url\":\"<repository_url>\"},\"ngp\":{\"repository_url\":\"<repository_url>\"},\"nagios\":{\"checks\":{\"monitoring_host\":\"<monitoring_host>\"}}}}";

        String result = masker.maskPasswordsFromJson(complexJson);

        assertFalse("JSON String should not contain the passwords after masking",
            result.contains(PASSWORD1) && result.contains(PASSWORD2) && result.contains(PASSWORD3));
    }
}
