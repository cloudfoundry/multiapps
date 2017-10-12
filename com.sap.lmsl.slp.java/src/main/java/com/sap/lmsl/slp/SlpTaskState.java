
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_task_state.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_task_state">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.task.state.INITIAL"/>
 *     &lt;enumeration value="slp.task.state.RUNNING"/>
 *     &lt;enumeration value="slp.task.state.ERROR"/>
 *     &lt;enumeration value="slp.task.state.DIALOG"/>
 *     &lt;enumeration value="slp.task.state.FINISHED"/>
 *     &lt;enumeration value="slp.task.state.ABORTED"/>
 *     &lt;enumeration value="slp.task.state.SKIPPED"/>
 *     &lt;enumeration value="slp.task.state.ACTION_REQUIRED"/>
 *     &lt;enumeration value="slp.task.state.BREAKPOINT"/>
 *     &lt;enumeration value="slp.task.state.STOPPED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_task_state")
@XmlEnum
public enum SlpTaskState {

    @XmlEnumValue("slp.task.state.INITIAL")
    SLP_TASK_STATE_INITIAL("slp.task.state.INITIAL"),
    @XmlEnumValue("slp.task.state.RUNNING")
    SLP_TASK_STATE_RUNNING("slp.task.state.RUNNING"),
    @XmlEnumValue("slp.task.state.ERROR")
    SLP_TASK_STATE_ERROR("slp.task.state.ERROR"),
    @XmlEnumValue("slp.task.state.DIALOG")
    SLP_TASK_STATE_DIALOG("slp.task.state.DIALOG"),
    @XmlEnumValue("slp.task.state.FINISHED")
    SLP_TASK_STATE_FINISHED("slp.task.state.FINISHED"),
    @XmlEnumValue("slp.task.state.ABORTED")
    SLP_TASK_STATE_ABORTED("slp.task.state.ABORTED"),
    @XmlEnumValue("slp.task.state.SKIPPED")
    SLP_TASK_STATE_SKIPPED("slp.task.state.SKIPPED"),
    @XmlEnumValue("slp.task.state.ACTION_REQUIRED")
    SLP_TASK_STATE_ACTION_REQUIRED("slp.task.state.ACTION_REQUIRED"),
    @XmlEnumValue("slp.task.state.BREAKPOINT")
    SLP_TASK_STATE_BREAKPOINT("slp.task.state.BREAKPOINT"),
    @XmlEnumValue("slp.task.state.STOPPED")
    SLP_TASK_STATE_STOPPED("slp.task.state.STOPPED");
    private final String value;

    SlpTaskState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpTaskState fromValue(String v) {
        for (SlpTaskState c: SlpTaskState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
