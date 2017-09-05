package com.sap.cloud.lm.sl.mta.resolvers;

public interface Resolver<T, E extends Exception> {

    T resolve() throws E;

}