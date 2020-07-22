package org.cloudfoundry.multiapps.mta.handlers;

import java.util.List;

import org.cloudfoundry.multiapps.mta.model.Module;

public interface ModulesSorter {

    List<Module> sort();
}
