
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_task_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_task_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.task.type.PROCESS"/>
 *     &lt;enumeration value="slp.task.type.ROADMAP.TECH"/>
 *     &lt;enumeration value="slp.task.type.ROADMAP.USER"/>
 *     &lt;enumeration value="slp.task.type.STEP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_task_type")
@XmlEnum
public enum SlpTaskType {

    @XmlEnumValue("slp.task.type.PROCESS")
    SLP_TASK_TYPE_PROCESS("slp.task.type.PROCESS"),
    @XmlEnumValue("slp.task.type.ROADMAP.TECH")
    SLP_TASK_TYPE_ROADMAP_TECH("slp.task.type.ROADMAP.TECH"),
    @XmlEnumValue("slp.task.type.ROADMAP.USER")
    SLP_TASK_TYPE_ROADMAP_USER("slp.task.type.ROADMAP.USER"),
    @XmlEnumValue("slp.task.type.STEP")
    SLP_TASK_TYPE_STEP("slp.task.type.STEP");
    private final String value;

    SlpTaskType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpTaskType fromValue(String v) {
        for (SlpTaskType c: SlpTaskType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
