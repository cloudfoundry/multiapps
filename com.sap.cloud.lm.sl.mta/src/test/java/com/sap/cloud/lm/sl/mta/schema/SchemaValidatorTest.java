package com.sap.cloud.lm.sl.mta.schema;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Runnable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.Schemas;

@RunWith(value = Parameterized.class)
public class SchemaValidatorTest {

    private final Element schema;
    private final String file;
    private final String expected;

    private SchemaValidator validator;

    @Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Valid extension descriptor:
            {
                "/mta/sample/v1_0/config-01.mtaext", Schemas.MTAEXT, "",
            },
            // (01) Valid deployment descriptor:
            {
                "/mta/sample/v1_0/mtad-01.yaml", Schemas.MTAD, "",
            },
            // (02) Valid platforms JSON:
            {
                "/mta/sample/v1_0/platforms.json", Schemas.PLATFORMS, "",
            },
            // (03) Valid platform types JSON:
            {
                "/mta/sample/v1_0/platform-types.json", Schemas.PLATFORM_TYPES, "",
            },
            // (04) Deployment descriptor contains invalid key:
            {
                "mtad-01.yaml", Schemas.MTAD, "E:Invalid key \"test\"",
            },
            // (05) Deployment descriptor module contains an invalid key:
            {
                "mtad-02.yaml", Schemas.MTAD, "E:Invalid key \"modules#0#test\"",
            },
            // (06) Deployment descriptor is missing a required key:
            {
                "mtad-03.yaml", Schemas.MTAD, "E:Missing required key \"ID\"",
            },
            // (07) Deployment descriptor module has invalid content for requires dependency:
            {
                "mtad-04.yaml", Schemas.MTAD, "E:Invalid type for key \"modules#0#requires\", expected \"List\" but got \"String\"",
            },
            // (08) Deployment descriptor has an invalid ID:
            {
                "mtad-06.yaml", Schemas.MTAD, "E:Invalid value for key \"ID\", matching failed at \"com[/]sap/mta/sample\"",
            },
            // (09) Deployment descriptor has a too long ID:
            {
                "mtad-07.yaml", Schemas.MTAD, "E:Invalid value for key \"ID\", maximum length is 128",
            },
            // (10) Deployment descriptor provided dependency has an invalid name:
            {
                "mtad-08.yaml", Schemas.MTAD, "E:Invalid value for key \"modules#0#provides#0#name\", matching failed at \"internal-od[@]ta\"",
            },
            // (11) Deployment descriptor module has an invalid name:
            {
                "mtad-09.yaml", Schemas.MTAD, "E:Invalid value for key \"modules#0#name\", matching failed at \"web[ ]server\"",
            },
            // (12) Deployment descriptor module provides public has String, but not a Boolean value:
            {
                "mtad-10.yaml", com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.MTAD, "E:Invalid type for key \"modules#0#provides#0#public\", expected \"Boolean\" but got \"String\"",
            },
            // (13) Null content:
            {
                null, Schemas.MTAD, "E:Null content",
            },
// @formatter:on
        });
    }

    public SchemaValidatorTest(String file, Element schema, String expected) {
        this.schema = schema;
        this.file = file;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        validator = new SchemaValidator(schema);
    }

    @Test
    public void testValidateSchema() throws Exception {
        TestUtil.test(new Runnable() {
            @Override
            public void run() throws Exception {
                if (schema instanceof MapElement) {
                    validator.validate(MtaTestUtil.getMap(file, getClass()));
                } else if (schema instanceof ListElement) {
                    validator.validate(MtaTestUtil.getList(file, getClass()));
                }
            }
        }, expected);
    }

}
