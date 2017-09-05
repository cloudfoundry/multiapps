package com.sap.cloud.lm.sl.slp.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.commons.io.output.NullOutputStream;

import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Parameter.Tablevalue;
import com.sap.lmsl.slp.Parameters;
import com.sap.lmsl.slp.Tuple;

public class SlpParameterTablevalueToMapTypeConverterTest {

    SlpParameterTablevalueToMapTypeConverter converter;

    private static JAXBContext jaxbcontext;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jaxbcontext = JAXBContext.newInstance("com.sap.lmsl.slp");
    }

    @Before
    public void init() {
        converter = new SlpParameterTablevalueToMapTypeConverter();
    }

    @Test
    public void testConvertForwardAnyValue() throws JAXBException {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The 'value' element of a SLP Tuple element can't be 'null'");

        InputStream input = this.getClass().getClassLoader().getResourceAsStream("slp_parameter_any_value.xml");
        Unmarshaller u = jaxbcontext.createUnmarshaller();
        JAXBElement<Parameter> slpParameterElement = u.unmarshal(new StreamSource(input), Parameter.class);

        converter.convertForward(slpParameterElement.getValue().getTablevalue());
    }

    @Test
    public void testConvertForwardParametersType() throws JAXBException {

        InputStream input = this.getClass().getClassLoader().getResourceAsStream("slp_parameter_params_value.xml");
        Unmarshaller u = jaxbcontext.createUnmarshaller();
        JAXBElement<Parameter> slpParameterElement = u.unmarshal(new StreamSource(input), Parameter.class);

        List<Map<String, Map<String, Object>>> result = converter.convertForward(slpParameterElement.getValue().getTablevalue());
        assertNotNull(result);
        assertEquals("There should be one tuple in the map", 1, result.size());
        Map<String, Map<String, Object>> toupleRow = result.get(0);
        assertTrue("The map should contain key '1'", toupleRow.containsKey("1"));
        assertTrue("There should be a key 'param1' in the resulted map", toupleRow.get("1").containsKey("param1"));
        assertTrue("There should be a key 'param2' in the resulted map", toupleRow.get("1").containsKey("param2"));
        assertEquals("There should be used the default value in case no other value has been defined", "value2",
            toupleRow.get("1").get("param2"));
        assertTrue("There should be a key 'param3' in the resulted map", toupleRow.get("1").containsKey("param3"));
        assertEquals("There should be used the default value in case no other value has been defined", "defaultValue3",
            toupleRow.get("1").get("param3"));
    }
    
    @Test
    public void testConvertForwardParametersType2() throws JAXBException {

        Parameter table = new Parameter();
        table.setId("table");
        Tablevalue tableValue = new Tablevalue();
        
        Tuple tuple = new Tuple();
        tuple.setId("1");
        
        Parameter param1 = new Parameter();
        param1.setId("param1");
        param1.setValue("value1");
        Parameters tupleValue = new Parameters();
        tuple.setValue(tupleValue);
        tupleValue.getParameter().add(param1);
         
        Parameter param2 = new Parameter();
        param2.setId("param2");
        param2.setValue("value2");
        tupleValue.getParameter().add(param2);
         
        Parameter param3 = new Parameter();
        param3.setId("param3");
        param3.setValue("value3");
        tupleValue.getParameter().add(param3);
        
        Parameter innerTable = new Parameter();
        innerTable.setId("inner_table");
        innerTable.setValue("inner_table");
        Tablevalue tableValue_inner = new Tablevalue();
        Tuple tuple1 = new Tuple();
        tuple1.setId("1");
        tableValue_inner.getTuple().add(tuple1);
        innerTable.setTablevalue(tableValue_inner);
        Parameter param = new Parameter();
        param.setId("inner_table_param");
        param.setValue("1.0");
        Parameters tupleValue1 = new Parameters();
        tuple1.setValue(tupleValue1);
        tupleValue1.getParameter().add(param);        
        
        tupleValue.getParameter().add(innerTable);
         
        tableValue.getTuple().add(tuple);
        table.setTablevalue(tableValue);
        
        List<Map<String, Map<String, Object>>> result = converter.convertForward(table.getTablevalue());
        assertNotNull(result);
        assertEquals("There should be one tuple in the map", 1, result.size());
        Map<String, Map<String, Object>> toupleRow = result.get(0);
        assertTrue("The map should contain key '1'", toupleRow.containsKey("1"));
        assertTrue("There should be a key 'param1' in the resulted map", toupleRow.get("1").containsKey("param1"));
        assertTrue("There should be a key 'param2' in the resulted map", toupleRow.get("1").containsKey("param2"));
        assertTrue("There should be a key 'param2' in the resulted map", toupleRow.get("1").containsKey("inner_table"));
        
        List<Map<String, Map<String, Object>>> inner_table = (List<Map<String, Map<String, Object>>>) toupleRow.get("1").get("inner_table");
        assertEquals("There should be one tuple in the map", 1, inner_table.size());
        Map<String, Map<String, Object>> toupleRow_inner = inner_table.get(0);
        assertTrue("The map should contain key '1'", toupleRow_inner.containsKey("1"));
        assertTrue("There should be a key 'param1' in the resulted map", toupleRow_inner.get("1").containsKey("inner_table_param"));
        assertEquals("1.0", toupleRow_inner.get("1").get("inner_table_param"));
        
    }
    
    @Test
    public void testConvertForwardNoValue() throws JAXBException {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The 'value' element of a SLP Tuple element can't be 'null'");

        InputStream input = this.getClass().getClassLoader().getResourceAsStream("slp_parameter_no_value.xml");
        Unmarshaller u = jaxbcontext.createUnmarshaller();
        JAXBElement<Parameter> slpParameterElement = u.unmarshal(new StreamSource(input), Parameter.class);

        converter.convertForward(slpParameterElement.getValue().getTablevalue());
    }

    @Test
    public void testConvertBackward() throws JAXBException {

        List<Map<String, Map<String, Object>>> tableList = new ArrayList<Map<String, Map<String, Object>>>();
        Map<String, Map<String, Object>> touple = new HashMap<String, Map<String, Object>>();
        Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("param1", "value1");
        parametersMap.put("param2", "value2");

        touple.put("1", parametersMap);
        tableList.add(touple);

        Parameter.Tablevalue result = converter.convertBackward(tableList);
        assertEquals("There should be exactly 1 tuple in the result", 1, result.getTuple().size());
        assertEquals("There should be exactly 2 parameters in the parameters result", 2,
            result.getTuple().get(0).getValue().getParameter().size());

        Parameter parameter = new Parameter();
        parameter.setTablevalue(result);
        Marshaller m = jaxbcontext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(parameter, new NullOutputStream());
        // m.marshal(parameter, System.out);
    }
    
    @Test
    public void testConvertBackwardTableInTable() throws JAXBException {

        List<Map<String, Map<String, Object>>> tableList = new ArrayList<Map<String, Map<String, Object>>>();
        Map<String, Map<String, Object>> touple = new HashMap<String, Map<String, Object>>();
        Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("param1", "value1");
        parametersMap.put("param2", "value2");

        List<Map<String, Map<String, Object>>> tableList_inner = new ArrayList<Map<String, Map<String, Object>>>();
        Map<String, Map<String, Object>> touple_inner = new HashMap<String, Map<String, Object>>();
        Map<String, Object> parametersMap_inner = new HashMap<String, Object>();
        parametersMap_inner.put("version", "1.0");
        parametersMap_inner.put("type", "contentpackage");
        touple_inner.put("1", parametersMap_inner);
        tableList_inner.add(touple_inner);
        parametersMap.put("param3", tableList_inner);
        
        touple.put("1", parametersMap);
        tableList.add(touple);

        Parameter.Tablevalue result = converter.convertBackward(tableList);
        assertEquals("There should be exactly 1 tuple in the result", 1, result.getTuple().size());
        assertEquals("There should be exactly 3 parameters in the parameters result", 3,
            result.getTuple().get(0).getValue().getParameter().size());
        List<Parameter> parameters = result.getTuple().get(0).getValue().getParameter();
        for (Parameter parameter : parameters) {
            if (parameter.getId().equals("param3")) {
                assertNotNull(parameter.getTablevalue());
            }
        }

        Parameter parameter = new Parameter();
        parameter.setTablevalue(result);
        Marshaller m = jaxbcontext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(parameter, new NullOutputStream());
        m.marshal(parameter, System.out);
    }
}
