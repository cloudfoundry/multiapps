package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class VersionComponent {

    private static final int DEFAULT_VERSION = 0;
    private static final String VERSION_SEPARATOR = ".";

    private TreeMap<Integer, Object> builtVersions;

    private VersionComponent(TreeMap<Integer, Object> builtVersions) {
        this.builtVersions = builtVersions;
    }

    @SuppressWarnings("unchecked")
    public VersionComponent getSubVersions(Integer version) {
        return new VersionComponent((TreeMap<Integer, Object>) builtVersions.get(version));
    }

    public Integer getLatest() {
        if (builtVersions == null || builtVersions.isEmpty()) {
            return DEFAULT_VERSION;
        }
        return builtVersions.lastKey();
    }

    public static class VersionComponentBuilder {
        private Map<Integer, List<Integer>> versions;

        public VersionComponentBuilder(Map<Integer, List<Integer>> versions) {
            this.versions = versions;
        }

        public VersionComponent build() {
            return new VersionComponent(buildVersionsMap(versions));
        }

        private static TreeMap<Integer, Object> buildVersionsMap(Map<Integer, List<Integer>> versions) {
            TreeMap<Integer, Object> versionsMap = new TreeMap<>();
            for (Entry<Integer, List<Integer>> mtaVersion : versions.entrySet()) {
                for (Integer minorVersion : mtaVersion.getValue()) {
                    String supportedMtaVersion = mtaVersion.getKey()+"."+minorVersion;
                        StringTokenizer tokenizer = new StringTokenizer(supportedMtaVersion, VERSION_SEPARATOR);
                        buildVersionsMap(tokenizer, versionsMap);
                }
            }
            return versionsMap;
        }

        @SuppressWarnings("unchecked")
        private static TreeMap<Integer, Object> buildVersionsMap(StringTokenizer tokenizirer, TreeMap<Integer, Object> result) {
            if (!tokenizirer.hasMoreTokens()) {
                result.put(0, Collections.emptyMap());
                return result;
            }

            TreeMap<Integer, Object> subversions = new TreeMap<>();
            String version = tokenizirer.nextToken();
            int versionInteger = Integer.parseInt(version);
            if (!result.containsKey(versionInteger)) {
                result.put(versionInteger, subversions);
            } else {
                buildVersionsMap(tokenizirer, (TreeMap<Integer, Object>) result.get(versionInteger));
            }
            buildVersionsMap(tokenizirer, subversions);
            return result;
        }
    }
}
