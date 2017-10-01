
package com.sap.lmsl.slp;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="toolTip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infoDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="caption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defaultValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element ref="{http://www.sap.com/lmsl/slp}valueMessage" minOccurs="0"/>
 *         &lt;element name="clientRegExp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.sap.com/lmsl/slp}clientEventReference" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="type" type="{http://www.sap.com/lmsl/slp}textInputType" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="noOfLines" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="secure" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="maxLength" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="minValue" type="{http://www.w3.org/2001/XMLSchema}integer" default="0" />
 *       &lt;attribute name="maxValue" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="minCharLength" type="{http://www.w3.org/2001/XMLSchema}integer" default="0" />
 *       &lt;attribute name="maxCharLength" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="stringCase">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="lower"/>
 *             &lt;enumeration value="upper"/>
 *             &lt;enumeration value="none"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "toolTip",
    "infoDescription",
    "caption",
    "defaultValue",
    "value",
    "valueMessage",
    "clientRegExp",
    "clientEventReference"
})
@XmlRootElement(name = "textInputView")
public class TextInputView {

    protected String toolTip;
    protected String infoDescription;
    protected String caption;
    protected String defaultValue;
    protected Object value;
    protected ValueMessage valueMessage;
    protected String clientRegExp;
    protected ClientEventReference clientEventReference;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "parentId", required = true)
    protected String parentId;
    @XmlAttribute(name = "enable")
    protected Boolean enable;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "type")
    protected TextInputType type;
    @XmlAttribute(name = "width")
    protected String width;
    @XmlAttribute(name = "noOfLines")
    protected BigInteger noOfLines;
    @XmlAttribute(name = "secure")
    protected Boolean secure;
    @XmlAttribute(name = "required")
    protected Boolean required;
    @XmlAttribute(name = "maxLength")
    protected BigInteger maxLength;
    @XmlAttribute(name = "minValue")
    protected BigInteger minValue;
    @XmlAttribute(name = "maxValue")
    protected BigInteger maxValue;
    @XmlAttribute(name = "minCharLength")
    protected BigInteger minCharLength;
    @XmlAttribute(name = "maxCharLength")
    protected BigInteger maxCharLength;
    @XmlAttribute(name = "stringCase")
    protected String stringCase;

    /**
     * Gets the value of the toolTip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * Sets the value of the toolTip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToolTip(String value) {
        this.toolTip = value;
    }

    /**
     * Gets the value of the infoDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfoDescription() {
        return infoDescription;
    }

    /**
     * Sets the value of the infoDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfoDescription(String value) {
        this.infoDescription = value;
    }

    /**
     * Gets the value of the caption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the value of the caption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaption(String value) {
        this.caption = value;
    }

    /**
     * Gets the value of the defaultValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the value of the defaultValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultValue(String value) {
        this.defaultValue = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets the value of the valueMessage property.
     * 
     * @return
     *     possible object is
     *     {@link ValueMessage }
     *     
     */
    public ValueMessage getValueMessage() {
        return valueMessage;
    }

    /**
     * Sets the value of the valueMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueMessage }
     *     
     */
    public void setValueMessage(ValueMessage value) {
        this.valueMessage = value;
    }

    /**
     * Gets the value of the clientRegExp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientRegExp() {
        return clientRegExp;
    }

    /**
     * Sets the value of the clientRegExp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientRegExp(String value) {
        this.clientRegExp = value;
    }

    /**
     * Gets the value of the clientEventReference property.
     * 
     * @return
     *     possible object is
     *     {@link ClientEventReference }
     *     
     */
    public ClientEventReference getClientEventReference() {
        return clientEventReference;
    }

    /**
     * Sets the value of the clientEventReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClientEventReference }
     *     
     */
    public void setClientEventReference(ClientEventReference value) {
        this.clientEventReference = value;
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TextInputType }
     *     
     */
    public TextInputType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextInputType }
     *     
     */
    public void setType(TextInputType value) {
        this.type = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWidth(String value) {
        this.width = value;
    }

    /**
     * Gets the value of the noOfLines property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNoOfLines() {
        return noOfLines;
    }

    /**
     * Sets the value of the noOfLines property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNoOfLines(BigInteger value) {
        this.noOfLines = value;
    }

    /**
     * Gets the value of the secure property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSecure() {
        if (secure == null) {
            return false;
        } else {
            return secure;
        }
    }

    /**
     * Sets the value of the secure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSecure(Boolean value) {
        this.secure = value;
    }

    /**
     * Gets the value of the required property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRequired() {
        if (required == null) {
            return false;
        } else {
            return required;
        }
    }

    /**
     * Sets the value of the required property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequired(Boolean value) {
        this.required = value;
    }

    /**
     * Gets the value of the maxLength property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the value of the maxLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxLength(BigInteger value) {
        this.maxLength = value;
    }

    /**
     * Gets the value of the minValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinValue() {
        if (minValue == null) {
            return new BigInteger("0");
        } else {
            return minValue;
        }
    }

    /**
     * Sets the value of the minValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinValue(BigInteger value) {
        this.minValue = value;
    }

    /**
     * Gets the value of the maxValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the value of the maxValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxValue(BigInteger value) {
        this.maxValue = value;
    }

    /**
     * Gets the value of the minCharLength property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinCharLength() {
        if (minCharLength == null) {
            return new BigInteger("0");
        } else {
            return minCharLength;
        }
    }

    /**
     * Sets the value of the minCharLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinCharLength(BigInteger value) {
        this.minCharLength = value;
    }

    /**
     * Gets the value of the maxCharLength property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxCharLength() {
        return maxCharLength;
    }

    /**
     * Sets the value of the maxCharLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxCharLength(BigInteger value) {
        this.maxCharLength = value;
    }

    /**
     * Gets the value of the stringCase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStringCase() {
        return stringCase;
    }

    /**
     * Sets the value of the stringCase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStringCase(String value) {
        this.stringCase = value;
    }

}
