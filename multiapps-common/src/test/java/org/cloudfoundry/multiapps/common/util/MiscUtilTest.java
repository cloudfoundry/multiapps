package org.cloudfoundry.multiapps.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MiscUtilTest {

    @Test
    void testReplaceAll_withMultipleOccurrences() {
        StringBuilder builder = new StringBuilder("Hello World! Hello Everyone!");
        String original = "Hello";
        String replacement = "Hi";

        MiscUtil.replaceAll(builder, original, replacement);

        assertEquals("Hi World! Hi Everyone!", builder.toString());
    }

    @Test
    void testReplaceAll_withNoOccurrences() {
        StringBuilder builder = new StringBuilder("Hello World!");
        String original = "Hi";
        String replacement = "Bye";

        MiscUtil.replaceAll(builder, original, replacement);

        assertEquals("Hello World!", builder.toString());
    }

    @Test
    void testReplaceAll_withEmptyStringBuilder() {
        StringBuilder builder = new StringBuilder();
        String original = "Hello";
        String replacement = "Hi";

        MiscUtil.replaceAll(builder, original, replacement);

        assertEquals("", builder.toString());
    }

    @Test
    void testGetCharacterRange_withStartLessThanEnd() {
        char start = 'a';
        char end = 'e';

        char[] result = MiscUtil.getCharacterRange(start, end);

        assertArrayEquals(new char[]{'a', 'b', 'c', 'd', 'e'}, result);
    }

    @Test
    void testGetCharacterRange_withStartGreaterThanEnd() {
        char start = 'e';
        char end = 'a';

        char[] result = MiscUtil.getCharacterRange(start, end);

        assertArrayEquals(new char[]{'a', 'b', 'c', 'd', 'e'}, result);
    }

    @Test
    void testGetCharacterRange_withEqualStartAndEnd() {
        char start = 'a';
        char end = 'a';

        char[] result = MiscUtil.getCharacterRange(start, end);

        assertArrayEquals(new char[]{'a'}, result);
    }

    @Test
    void testGetCharacterRange_withInvalidRange() {
        char start = 'z';
        char end = 'a';

        char[] result = MiscUtil.getCharacterRange(start, end);

        assertArrayEquals(new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}, result);
    }
}
