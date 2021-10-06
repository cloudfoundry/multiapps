package org.cloudfoundry.multiapps.mta.model;

public class WeightedResource {

    private final Resource resource;
    private final int resourceWeight;

    public WeightedResource(Resource resource, int resourceWeight) {
        this.resource = resource;
        this.resourceWeight = resourceWeight;
    }

    public int getResourceWeight() {
        return resourceWeight;
    }

    public String getResourceName() {
        return this.resource.getName();
    }

    public Resource getResource() {
        return this.resource;
    }
}
