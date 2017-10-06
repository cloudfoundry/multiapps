
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tableDynamicity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tableDynamicity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="none"/>
 *     &lt;enumeration value="addAndRemove"/>
 *     &lt;enumeration value="addOnly"/>
 *     &lt;enumeration value="removeOnly"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tableDynamicity")
@XmlEnum
public enum TableDynamicity {

    @XmlEnumValue("none")
    NONE("none"),
    @XmlEnumValue("addAndRemove")
    ADD_AND_REMOVE("addAndRemove"),
    @XmlEnumValue("addOnly")
    ADD_ONLY("addOnly"),
    @XmlEnumValue("removeOnly")
    REMOVE_ONLY("removeOnly");
    private final String value;

    TableDynamicity(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableDynamicity fromValue(String v) {
        for (TableDynamicity c: TableDynamicity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
