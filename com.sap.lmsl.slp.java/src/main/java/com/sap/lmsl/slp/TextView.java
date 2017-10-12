
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="parentId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="behavior">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.sap.com/lmsl/slp}textViewBehavior">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.sap.com/lmsl/slp}textViewType">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="htmlEnabled" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="wrap" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value",
    "tooltip"
})
@XmlRootElement(name = "textView")
public class TextView {

    @XmlElement(required = true)
    protected String value;
    @XmlElement(required = true)
    protected String tooltip;
    @XmlAttribute(name = "parentId")
    protected String parentId;
    @XmlAttribute(name = "behavior")
    protected TextViewBehavior behavior;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "type")
    protected TextViewType type;
    @XmlAttribute(name = "htmlEnabled")
    protected String htmlEnabled;
    @XmlAttribute(name = "wrap")
    protected Boolean wrap;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "enable")
    protected Boolean enable;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the tooltip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Sets the value of the tooltip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTooltip(String value) {
        this.tooltip = value;
    }

    /**
     * Gets the value of the parentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentId(String value) {
        this.parentId = value;
    }

    /**
     * Gets the value of the behavior property.
     * 
     * @return
     *     possible object is
     *     {@link TextViewBehavior }
     *     
     */
    public TextViewBehavior getBehavior() {
        return behavior;
    }

    /**
     * Sets the value of the behavior property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextViewBehavior }
     *     
     */
    public void setBehavior(TextViewBehavior value) {
        this.behavior = value;
    }

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
     *     {@link TextViewType }
     *     
     */
    public TextViewType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextViewType }
     *     
     */
    public void setType(TextViewType value) {
        this.type = value;
    }

    /**
     * Gets the value of the htmlEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHtmlEnabled() {
        return htmlEnabled;
    }

    /**
     * Sets the value of the htmlEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHtmlEnabled(String value) {
        this.htmlEnabled = value;
    }

    /**
     * Gets the value of the wrap property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isWrap() {
        if (wrap == null) {
            return false;
        } else {
            return wrap;
        }
    }

    /**
     * Sets the value of the wrap property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWrap(Boolean value) {
        this.wrap = value;
    }

    /**
     * Gets the value of the visible property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isVisible() {
        if (visible == null) {
            return true;
        } else {
            return visible;
        }
    }

    /**
     * Sets the value of the visible property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVisible(Boolean value) {
        this.visible = value;
    }

    /**
     * Gets the value of the enable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isEnable() {
        if (enable == null) {
            return true;
        } else {
            return enable;
        }
    }

    /**
     * Sets the value of the enable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnable(Boolean value) {
        this.enable = value;
    }

}
