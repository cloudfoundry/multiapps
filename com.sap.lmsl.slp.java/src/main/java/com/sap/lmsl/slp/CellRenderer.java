
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cellRenderer.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cellRenderer">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="textView"/>
 *     &lt;enumeration value="textInput"/>
 *     &lt;enumeration value="secureInput"/>
 *     &lt;enumeration value="radioButton"/>
 *     &lt;enumeration value="checkBox"/>
 *     &lt;enumeration value="dropDown"/>
 *     &lt;enumeration value="link"/>
 *     &lt;enumeration value="image"/>
 *     &lt;enumeration value="button"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cellRenderer")
@XmlEnum
public enum CellRenderer {

    @XmlEnumValue("textView")
    TEXT_VIEW("textView"),
    @XmlEnumValue("textInput")
    TEXT_INPUT("textInput"),
    @XmlEnumValue("secureInput")
    SECURE_INPUT("secureInput"),
    @XmlEnumValue("radioButton")
    RADIO_BUTTON("radioButton"),
    @XmlEnumValue("checkBox")
    CHECK_BOX("checkBox"),
    @XmlEnumValue("dropDown")
    DROP_DOWN("dropDown"),
    @XmlEnumValue("link")
    LINK("link"),
    @XmlEnumValue("image")
    IMAGE("image"),
    @XmlEnumValue("button")
    BUTTON("button");
    private final String value;

    CellRenderer(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CellRenderer fromValue(String v) {
        for (CellRenderer c: CellRenderer.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
