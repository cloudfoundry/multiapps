
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for textViewBehavior.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="textViewBehavior">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="h1"/>
 *     &lt;enumeration value="bold"/>
 *     &lt;enumeration value="italics"/>
 *     &lt;enumeration value="underlined"/>
 *     &lt;enumeration value="h2"/>
 *     &lt;enumeration value="h3"/>
 *     &lt;enumeration value="h4"/>
 *     &lt;enumeration value="h5"/>
 *     &lt;enumeration value="h6"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "textViewBehavior")
@XmlEnum
public enum TextViewBehavior {

    @XmlEnumValue("h1")
    H_1("h1"),
    @XmlEnumValue("bold")
    BOLD("bold"),
    @XmlEnumValue("italics")
    ITALICS("italics"),
    @XmlEnumValue("underlined")
    UNDERLINED("underlined"),
    @XmlEnumValue("h2")
    H_2("h2"),
    @XmlEnumValue("h3")
    H_3("h3"),
    @XmlEnumValue("h4")
    H_4("h4"),
    @XmlEnumValue("h5")
    H_5("h5"),
    @XmlEnumValue("h6")
    H_6("h6");
    private final String value;

    TextViewBehavior(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TextViewBehavior fromValue(String v) {
        for (TextViewBehavior c: TextViewBehavior.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
