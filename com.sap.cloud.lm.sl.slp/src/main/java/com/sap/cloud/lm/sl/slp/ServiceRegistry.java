package com.sap.cloud.lm.sl.slp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;

/**
 * Registry class that provides facilities for registering SL Processes
 *
 */
public class ServiceRegistry {

    private static ServiceRegistry INSTANCE = new ServiceRegistry();

    private Set<ServiceMetadata> services = new HashSet<ServiceMetadata>();

    private ServiceRegistry() {
    };

    /**
     * Returns a singleton instance of the class
     * 
     * @return singleton registry instance instance
     */
    public static ServiceRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a process
     * 
     * @param service the process to register
     * @return true if the process was registered, false otherwise
     */
    public boolean addService(ServiceMetadata service) {
        if (service == null)
            throw new IllegalArgumentException(Messages.SERVICE_METADATA_MUST_NOT_BE_NULL);
        return services.add(service);
    }

    public boolean removeService(ServiceMetadata metadata) {
        return services.remove(metadata);
    }

    /**
     * Returns a set of all services registered in the registry
     * 
     * @return set of all services registered in the registry
     */
    public Set<ServiceMetadata> getServices() {
        return Collections.unmodifiableSet(services);
    }

    /**
     * Returns a service by id
     * 
     * @param id id of the service to return
     * @return the service or null if no service with the specified id was found in the registry
     */
    public ServiceMetadata getService(String id) {
        for (ServiceMetadata service : services) {
            if (service.getId().equals(id)) {
                return service;
            }
        }

        return null;
    }

}
