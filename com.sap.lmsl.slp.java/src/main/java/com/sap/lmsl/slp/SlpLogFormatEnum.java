
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_log_format_enum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_log_format_enum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.log.format.TEXT"/>
 *     &lt;enumeration value="slp.log.format.HTML"/>
 *     &lt;enumeration value="slp.log.format.GLF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_log_format_enum")
@XmlEnum
public enum SlpLogFormatEnum {

    @XmlEnumValue("slp.log.format.TEXT")
    SLP_LOG_FORMAT_TEXT("slp.log.format.TEXT"),
    @XmlEnumValue("slp.log.format.HTML")
    SLP_LOG_FORMAT_HTML("slp.log.format.HTML"),
    @XmlEnumValue("slp.log.format.GLF")
    SLP_LOG_FORMAT_GLF("slp.log.format.GLF");
    private final String value;

    SlpLogFormatEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpLogFormatEnum fromValue(String v) {
        for (SlpLogFormatEnum c: SlpLogFormatEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
