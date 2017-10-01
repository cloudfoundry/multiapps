
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for valueMessage.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="valueMessage">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="normal"/>
 *     &lt;enumeration value="error"/>
 *     &lt;enumeration value="warning"/>
 *     &lt;enumeration value="success"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "valueMessage")
@XmlEnum
public enum ValueMessage1 {

    @XmlEnumValue("normal")
    NORMAL("normal"),
    @XmlEnumValue("error")
    ERROR("error"),
    @XmlEnumValue("warning")
    WARNING("warning"),
    @XmlEnumValue("success")
    SUCCESS("success");
    private final String value;

    ValueMessage1(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ValueMessage1 fromValue(String v) {
        for (ValueMessage1 c: ValueMessage1 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
