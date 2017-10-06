
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_action_enum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_action_enum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.action.STOP"/>
 *     &lt;enumeration value="slp.action.ABORT"/>
 *     &lt;enumeration value="slp.action.RESUME"/>
 *     &lt;enumeration value="slp.action.SUBMIT"/>
 *     &lt;enumeration value="slp.action.REPEAT"/>
 *     &lt;enumeration value="slp.action.START"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_action_enum")
@XmlEnum
public enum SlpActionEnum {

    @XmlEnumValue("slp.action.STOP")
    SLP_ACTION_STOP("slp.action.STOP"),
    @XmlEnumValue("slp.action.ABORT")
    SLP_ACTION_ABORT("slp.action.ABORT"),
    @XmlEnumValue("slp.action.RESUME")
    SLP_ACTION_RESUME("slp.action.RESUME"),
    @XmlEnumValue("slp.action.SUBMIT")
    SLP_ACTION_SUBMIT("slp.action.SUBMIT"),
    @XmlEnumValue("slp.action.REPEAT")
    SLP_ACTION_REPEAT("slp.action.REPEAT"),
    @XmlEnumValue("slp.action.START")
    SLP_ACTION_START("slp.action.START");
    private final String value;

    SlpActionEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpActionEnum fromValue(String v) {
        for (SlpActionEnum c: SlpActionEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
