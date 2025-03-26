package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ReferencePattern implements ValueMatcher {

    PLACEHOLDER("(?<!\\\\)\\$\\{(.+?)\\}", "${%s}"), SHORT("(?<!\\\\)~\\{(.+?)\\}", "~{%s}"), FULLY_QUALIFIED("(?<!\\\\)~\\{(.+?)/(.+?)\\}",
                                                                                                              "~{%s/%s}");

    public static final List<Pattern> COMPILED_PATTERNS = ReferencePattern.getCompiledPatterns();

    private String pattern;
    private String patternFormat;

    ReferencePattern(String pattern, String patternFormat) {
        this.pattern = pattern;
        this.patternFormat = patternFormat;
    }

    protected boolean hasPropertySetSegment() {
        return FULLY_QUALIFIED.equals(this);
    }

    protected boolean hasDepthOfReference() {
        return PLACEHOLDER.equals(this);
    }

    private String getPattern() {
        return pattern;
    }

    private static List<Pattern> getCompiledPatterns() {
        List<Pattern> compiledPatterns = new ArrayList<>();
        for (ReferencePattern referencePattern : ReferencePattern.values()) {
            compiledPatterns.add(Pattern.compile(referencePattern.getPattern()));
        }
        return compiledPatterns;
    }

    @Override
    public List<Reference> match(String line) {
        Matcher matcher = Pattern.compile(this.pattern)
                                 .matcher(line);
        List<Reference> references = new ArrayList<>();
        while (matcher.find()) {
            String matchedValue = matcher.group(0);
            if (this.hasPropertySetSegment()) {
                String providesName = matcher.group(1);
                String keyName = matcher.group(2);
                references.add(new Reference(matchedValue, keyName, providesName));
            } else {
                String keyName = matcher.group(1);
                references.add(new Reference(matchedValue, keyName));
            }
        }
        return references;
    }

    public String toString(Reference reference) {
        if (this.hasPropertySetSegment()) {
            return String.format(this.patternFormat, reference.getDependencyName(), reference.getKey());
        }
        return String.format(this.patternFormat, reference.getKey());
    }

}
