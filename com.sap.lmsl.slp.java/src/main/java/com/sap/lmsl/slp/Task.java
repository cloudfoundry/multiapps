
package com.sap.lmsl.slp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type" type="{http://www.sap.com/lmsl/slp}slp_task_type"/>
 *         &lt;element name="technicalName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{http://www.sap.com/lmsl/slp}slp_task_state"/>
 *         &lt;element name="parent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startedAt" type="{http://www.sap.com/lmsl/slp}slp_timestamp" minOccurs="0"/>
 *         &lt;element name="finishedAt" type="{http://www.sap.com/lmsl/slp}slp_timestamp" minOccurs="0"/>
 *         &lt;element name="progress" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="refreshRate" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="logs" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="progressMessages" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{http://www.sap.com/lmsl/slp}ProgressMessage"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalInfo" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;any processContents='skip' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Task")
public class Task {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected SlpTaskType type;
    @XmlElement(required = true)
    protected String technicalName;
    @XmlElement(required = true)
    protected SlpTaskState status;
    protected String parent;
    protected String startedAt;
    protected String finishedAt;
    protected Integer progress;
    protected Integer refreshRate;
    @XmlSchemaType(name = "anyURI")
    protected String logs;
    @XmlSchemaType(name = "anyURI")
    protected String error;
    protected Task.ProgressMessages progressMessages;
    protected String displayName;
    protected String description;
    @XmlSchemaType(name = "anyURI")
    protected String externalInfo;
    @XmlAnyElement
    protected List<Element> any;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link SlpTaskType }
     *     
     */
    public SlpTaskType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link SlpTaskType }
     *     
     */
    public void setType(SlpTaskType value) {
        this.type = value;
    }

    /**
     * Gets the value of the technicalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTechnicalName() {
        return technicalName;
    }

    /**
     * Sets the value of the technicalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTechnicalName(String value) {
        this.technicalName = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link SlpTaskState }
     *     
     */
    public SlpTaskState getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link SlpTaskState }
     *     
     */
    public void setStatus(SlpTaskState value) {
        this.status = value;
    }

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParent(String value) {
        this.parent = value;
    }

    /**
     * Gets the value of the startedAt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartedAt() {
        return startedAt;
    }

    /**
     * Sets the value of the startedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartedAt(String value) {
        this.startedAt = value;
    }

    /**
     * Gets the value of the finishedAt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinishedAt() {
        return finishedAt;
    }

    /**
     * Sets the value of the finishedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinishedAt(String value) {
        this.finishedAt = value;
    }

    /**
     * Gets the value of the progress property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * Sets the value of the progress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProgress(Integer value) {
        this.progress = value;
    }

    /**
     * Gets the value of the refreshRate property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRefreshRate() {
        return refreshRate;
    }

    /**
     * Sets the value of the refreshRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRefreshRate(Integer value) {
        this.refreshRate = value;
    }

    /**
     * Gets the value of the logs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogs() {
        return logs;
    }

    /**
     * Sets the value of the logs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogs(String value) {
        this.logs = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

    /**
     * Gets the value of the progressMessages property.
     * 
     * @return
     *     possible object is
     *     {@link Task.ProgressMessages }
     *     
     */
    public Task.ProgressMessages getProgressMessages() {
        return progressMessages;
    }

    /**
     * Sets the value of the progressMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link Task.ProgressMessages }
     *     
     */
    public void setProgressMessages(Task.ProgressMessages value) {
        this.progressMessages = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the externalInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalInfo() {
        return externalInfo;
    }

    /**
     * Sets the value of the externalInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalInfo(String value) {
        this.externalInfo = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * 
     * 
     */
    public List<Element> getAny() {
        if (any == null) {
            any = new ArrayList<Element>();
        }
        return this.any;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{http://www.sap.com/lmsl/slp}ProgressMessage"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressMessage"
    })
    public static class ProgressMessages {

        @XmlElement(name = "ProgressMessage")
        protected List<ProgressMessage> progressMessage;

        /**
         * Gets the value of the progressMessage property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the progressMessage property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProgressMessage().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProgressMessage }
         * 
         * 
         */
        public List<ProgressMessage> getProgressMessage() {
            if (progressMessage == null) {
                progressMessage = new ArrayList<ProgressMessage>();
            }
            return this.progressMessage;
        }

    }

}
