
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for linkViewType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="linkViewType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="link"/>
 *     &lt;enumeration value="sapNote"/>
 *     &lt;enumeration value="sapHelp"/>
 *     &lt;enumeration value="download"/>
 *     &lt;enumeration value="report"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "linkViewType")
@XmlEnum
public enum LinkViewType {

    @XmlEnumValue("link")
    LINK("link"),
    @XmlEnumValue("sapNote")
    SAP_NOTE("sapNote"),
    @XmlEnumValue("sapHelp")
    SAP_HELP("sapHelp"),
    @XmlEnumValue("download")
    DOWNLOAD("download"),
    @XmlEnumValue("report")
    REPORT("report");
    private final String value;

    LinkViewType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LinkViewType fromValue(String v) {
        for (LinkViewType c: LinkViewType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
