package org.cloudfoundry.multiapps.mta.resolvers;

public class Reference {

    private final String key;
    private final String matchedPattern;
    private final String dependencyName;

    public Reference(String matchedPattern, String key) {
        this(matchedPattern, key, null);
    }

    public Reference(String matchedPattern, String key, String dependencyName) {
        this.matchedPattern = matchedPattern;
        this.key = key;
        this.dependencyName = dependencyName;
    }

    public String getKey() {
        return key;
    }

    public String getMatchedPattern() {
        return matchedPattern;
    }

    public String getDependencyName() {
        return dependencyName;
    }

}
