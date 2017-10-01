
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for textViewType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="textViewType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="standardView"/>
 *     &lt;enumeration value="informationView"/>
 *     &lt;enumeration value="errorView"/>
 *     &lt;enumeration value="warningView"/>
 *     &lt;enumeration value="successView"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "textViewType")
@XmlEnum
public enum TextViewType {

    @XmlEnumValue("standardView")
    STANDARD_VIEW("standardView"),
    @XmlEnumValue("informationView")
    INFORMATION_VIEW("informationView"),
    @XmlEnumValue("errorView")
    ERROR_VIEW("errorView"),
    @XmlEnumValue("warningView")
    WARNING_VIEW("warningView"),
    @XmlEnumValue("successView")
    SUCCESS_VIEW("successView");
    private final String value;

    TextViewType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TextViewType fromValue(String v) {
        for (TextViewType c: TextViewType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
