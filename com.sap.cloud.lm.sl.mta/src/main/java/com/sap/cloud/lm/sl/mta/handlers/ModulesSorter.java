package com.sap.cloud.lm.sl.mta.handlers;

import java.util.List;

import com.sap.cloud.lm.sl.mta.model.v2.Module;


public interface ModulesSorter {

    List<? extends Module> sort();
}
