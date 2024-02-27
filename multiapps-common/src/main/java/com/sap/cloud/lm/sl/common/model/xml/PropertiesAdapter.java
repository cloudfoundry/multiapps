package com.sap.cloud.lm.sl.common.model.xml;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PropertiesAdapter extends XmlAdapter<MapWrapper, Map<String, Object>> {

    @Override
    public MapWrapper marshal(Map<String, Object> map) {
        return new MapWrapper(map);
    }

    @Override
    public Map<String, Object> unmarshal(MapWrapper mapWrapper) {
        return mapWrapper.unwrap();
    }

}
