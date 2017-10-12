
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for valueSelectorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="valueSelectorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="radioButton"/>
 *     &lt;enumeration value="checkBox"/>
 *     &lt;enumeration value="dropDown"/>
 *     &lt;enumeration value="listBox"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "valueSelectorType")
@XmlEnum
public enum ValueSelectorType {

    @XmlEnumValue("radioButton")
    RADIO_BUTTON("radioButton"),
    @XmlEnumValue("checkBox")
    CHECK_BOX("checkBox"),
    @XmlEnumValue("dropDown")
    DROP_DOWN("dropDown"),
    @XmlEnumValue("listBox")
    LIST_BOX("listBox");
    private final String value;

    ValueSelectorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ValueSelectorType fromValue(String v) {
        for (ValueSelectorType c: ValueSelectorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
