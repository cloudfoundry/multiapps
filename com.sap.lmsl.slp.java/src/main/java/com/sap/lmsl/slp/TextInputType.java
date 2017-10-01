
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for textInputType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="textInputType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="standardInput"/>
 *     &lt;enumeration value="htmlInput"/>
 *     &lt;enumeration value="dateInput"/>
 *     &lt;enumeration value="timeInput"/>
 *     &lt;enumeration value="numberInput"/>
 *     &lt;enumeration value="stringInput"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "textInputType")
@XmlEnum
public enum TextInputType {

    @XmlEnumValue("standardInput")
    STANDARD_INPUT("standardInput"),
    @XmlEnumValue("htmlInput")
    HTML_INPUT("htmlInput"),
    @XmlEnumValue("dateInput")
    DATE_INPUT("dateInput"),
    @XmlEnumValue("timeInput")
    TIME_INPUT("timeInput"),
    @XmlEnumValue("numberInput")
    NUMBER_INPUT("numberInput"),
    @XmlEnumValue("stringInput")
    STRING_INPUT("stringInput");
    private final String value;

    TextInputType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TextInputType fromValue(String v) {
        for (TextInputType c: TextInputType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
