
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_parameter_validation_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_parameter_validation_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.parameter.validation.status.ERROR"/>
 *     &lt;enumeration value="slp.parameter.validation.status.OK"/>
 *     &lt;enumeration value="slp.parameter.validation.status.WARNING"/>
 *     &lt;enumeration value="slp.parameter.validation.status.INFO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_parameter_validation_status")
@XmlEnum
public enum SlpParameterValidationStatus {

    @XmlEnumValue("slp.parameter.validation.status.ERROR")
    SLP_PARAMETER_VALIDATION_STATUS_ERROR("slp.parameter.validation.status.ERROR"),
    @XmlEnumValue("slp.parameter.validation.status.OK")
    SLP_PARAMETER_VALIDATION_STATUS_OK("slp.parameter.validation.status.OK"),
    @XmlEnumValue("slp.parameter.validation.status.WARNING")
    SLP_PARAMETER_VALIDATION_STATUS_WARNING("slp.parameter.validation.status.WARNING"),
    @XmlEnumValue("slp.parameter.validation.status.INFO")
    SLP_PARAMETER_VALIDATION_STATUS_INFO("slp.parameter.validation.status.INFO");
    private final String value;

    SlpParameterValidationStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpParameterValidationStatus fromValue(String v) {
        for (SlpParameterValidationStatus c: SlpParameterValidationStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
