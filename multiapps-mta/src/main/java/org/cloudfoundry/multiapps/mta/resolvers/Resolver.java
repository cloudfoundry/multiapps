package org.cloudfoundry.multiapps.mta.resolvers;

public interface Resolver<T> {

    T resolve();

}