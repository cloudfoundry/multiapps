package com.sap.cloud.lm.sl.slp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.stub;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;

public class ServiceRegistryTest {

    @Mock
    ServiceMetadata metadata;
    ServiceRegistry registry;

    String id = TestUtils.generateRandomString();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        stub(metadata.getId()).toReturn(TestUtils.generateRandomString());

        registry = ServiceRegistry.getInstance();
    }

    @After
    public void after() {
        // unregister all services
        for (ServiceMetadata metadata : registry.getServices()) {
            registry.removeService(metadata);
        }
    }

    @Test
    public void singleton() {
        ServiceRegistry instance1 = ServiceRegistry.getInstance();
        ServiceRegistry instance2 = ServiceRegistry.getInstance();
        // check that the same object is returned
        assertSame(instance1, instance2);
    }

    @Test
    public void add() {
        assertTrue(registry.addService(metadata));
        assertEquals(1, registry.getServices().size());
        assertSame(metadata, registry.getService(metadata.getId()));
    }

    @Test
    public void addTwiceTheSame() {
        assertTrue(registry.addService(metadata));
        assertEquals(1, registry.getServices().size());
        // adding the same instance again should not add to the list
        assertFalse(registry.addService(metadata));
        assertEquals(1, registry.getServices().size());
    }

    @Test
    public void getNonRegisteredService() {
        String nonExistingId = TestUtils.generateRandomString();
        assertNull(registry.getService(nonExistingId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNull() {
        registry.addService(null);
    }

}
