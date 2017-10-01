
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for selectionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="selectionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="noSelection"/>
 *     &lt;enumeration value="singleSelection"/>
 *     &lt;enumeration value="multiSelection"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "selectionType")
@XmlEnum
public enum SelectionType {

    @XmlEnumValue("noSelection")
    NO_SELECTION("noSelection"),
    @XmlEnumValue("singleSelection")
    SINGLE_SELECTION("singleSelection"),
    @XmlEnumValue("multiSelection")
    MULTI_SELECTION("multiSelection");
    private final String value;

    SelectionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SelectionType fromValue(String v) {
        for (SelectionType c: SelectionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
