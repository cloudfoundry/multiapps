
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sortingOrder.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sortingOrder">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ascending"/>
 *     &lt;enumeration value="descending"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sortingOrder")
@XmlEnum
public enum SortingOrder {

    @XmlEnumValue("ascending")
    ASCENDING("ascending"),
    @XmlEnumValue("descending")
    DESCENDING("descending");
    private final String value;

    SortingOrder(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SortingOrder fromValue(String v) {
        for (SortingOrder c: SortingOrder.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
