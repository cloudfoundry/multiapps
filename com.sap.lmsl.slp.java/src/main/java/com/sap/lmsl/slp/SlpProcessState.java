
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_process_state.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_process_state">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.process.state.ACTIVE"/>
 *     &lt;enumeration value="slp.process.state.FINISHED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_process_state")
@XmlEnum
public enum SlpProcessState {

    @XmlEnumValue("slp.process.state.ACTIVE")
    SLP_PROCESS_STATE_ACTIVE("slp.process.state.ACTIVE"),
    @XmlEnumValue("slp.process.state.FINISHED")
    SLP_PROCESS_STATE_FINISHED("slp.process.state.FINISHED");
    private final String value;

    SlpProcessState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpProcessState fromValue(String v) {
        for (SlpProcessState c: SlpProcessState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
