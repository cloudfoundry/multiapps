
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_breakpoint_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_breakpoint_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.breakpoint.status.BEFORE"/>
 *     &lt;enumeration value="slp.breakpoint.status.AT"/>
 *     &lt;enumeration value="slp.breakpoint.status.AFTER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_breakpoint_status")
@XmlEnum
public enum SlpBreakpointStatus {

    @XmlEnumValue("slp.breakpoint.status.BEFORE")
    SLP_BREAKPOINT_STATUS_BEFORE("slp.breakpoint.status.BEFORE"),
    @XmlEnumValue("slp.breakpoint.status.AT")
    SLP_BREAKPOINT_STATUS_AT("slp.breakpoint.status.AT"),
    @XmlEnumValue("slp.breakpoint.status.AFTER")
    SLP_BREAKPOINT_STATUS_AFTER("slp.breakpoint.status.AFTER");
    private final String value;

    SlpBreakpointStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpBreakpointStatus fromValue(String v) {
        for (SlpBreakpointStatus c: SlpBreakpointStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
