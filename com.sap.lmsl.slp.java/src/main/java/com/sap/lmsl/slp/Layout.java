
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for layout.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="layout">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="vertical"/>
 *     &lt;enumeration value="horizontal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "layout")
@XmlEnum
public enum Layout {

    @XmlEnumValue("vertical")
    VERTICAL("vertical"),
    @XmlEnumValue("horizontal")
    HORIZONTAL("horizontal");
    private final String value;

    Layout(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Layout fromValue(String v) {
        for (Layout c: Layout.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
