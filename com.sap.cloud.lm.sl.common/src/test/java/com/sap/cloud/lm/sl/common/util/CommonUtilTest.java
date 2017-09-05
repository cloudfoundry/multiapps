package com.sap.cloud.lm.sl.common.util;

import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.message.Messages;

@RunWith(Parameterized.class)
public class CommonUtilTest {

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) When the string can successfully be trimmed:
            {
                "blablabla", 6, "bla...", null,
            },
            // (1) When the limit is too short to hold even the '...':
            {
                "blablabla", 1, null, MessageFormat.format(Messages.LIMIT_IS_TOO_SMALL, 4),
            },
            // (2) When the limit is just long enough to hold the '...':
            {
                "blablabla", 3, null, MessageFormat.format(Messages.LIMIT_IS_TOO_SMALL, 4),
            },
            // (3) When the string's length is smaller than the limit:
            {
                "blabla", 9, "blabla", null,
            },
            // (4) When the string's length is equal to the limit:
            {
                "blabla", 6, "blabla", null,
            },
// @formatter:on
        });
    }

    private String stringToAbbreviate;
    private int limit;
    private String expectedExceptionMessage;
    private String expectedResult;

    public CommonUtilTest(String stringToAbbreviate, int limit, String expectedResult, String expectedExceptionMessage) {
        this.stringToAbbreviate = stringToAbbreviate;
        this.limit = limit;
        this.expectedExceptionMessage = expectedExceptionMessage;
        this.expectedResult = expectedResult;
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testAbbreviate() {
        if (expectedExceptionMessage != null) {
            expectedException.expectMessage(expectedExceptionMessage);
        }
        assertEquals(expectedResult, CommonUtil.abbreviate(stringToAbbreviate, limit));
    }

}
