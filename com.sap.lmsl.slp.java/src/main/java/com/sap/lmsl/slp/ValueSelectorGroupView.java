
package com.sap.lmsl.slp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="caption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="selectorValueProvider" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.sap.com/lmsl/slp}instanceValueProvider" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.sap.com/lmsl/slp}clientEventReference" minOccurs="0"/>
 *         &lt;element ref="{http://www.sap.com/lmsl/slp}valueMessage" minOccurs="0"/>
 *         &lt;element name="toolTip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infoDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clientRegExp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="selectedIndex" type="{http://www.w3.org/2001/XMLSchema}integer" default="-1" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="childType" type="{http://www.sap.com/lmsl/slp}valueSelectorType" />
 *       &lt;attribute name="selectionType" type="{http://www.sap.com/lmsl/slp}selectionType" />
 *       &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="noOfRows" type="{http://www.w3.org/2001/XMLSchema}integer" default="1" />
 *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="noOfColumns" type="{http://www.w3.org/2001/XMLSchema}integer" default="1" />
 *       &lt;attribute name="layoutType" type="{http://www.sap.com/lmsl/slp}layout" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "caption",
    "selectorValueProvider",
    "clientEventReference",
    "valueMessage",
    "toolTip",
    "infoDescription",
    "clientRegExp"
})
@XmlRootElement(name = "valueSelectorGroupView")
public class ValueSelectorGroupView {

    protected String caption;
    protected ValueSelectorGroupView.SelectorValueProvider selectorValueProvider;
    protected ClientEventReference clientEventReference;
    protected ValueMessage valueMessage;
    protected String toolTip;
    protected String infoDescription;
    protected String clientRegExp;
    @XmlAttribute(name = "selectedIndex")
    protected BigInteger selectedIndex;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "parentId", required = true)
    protected String parentId;
    @XmlAttribute(name = "childType")
    protected ValueSelectorType childType;
    @XmlAttribute(name = "selectionType")
    protected SelectionType selectionType;
    @XmlAttribute(name = "enable")
    protected Boolean enable;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "noOfRows")
    protected BigInteger noOfRows;
    @XmlAttribute(name = "required")
    protected Boolean required;
    @XmlAttribute(name = "noOfColumns")
    protected BigInteger noOfColumns;
    @XmlAttribute(name = "layoutType")
    protected Layout layoutType;

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
     * Gets the value of the selectorValueProvider property.
     * 
     * @return
     *     possible object is
     *     {@link ValueSelectorGroupView.SelectorValueProvider }
     *     
     */
    public ValueSelectorGroupView.SelectorValueProvider getSelectorValueProvider() {
        return selectorValueProvider;
    }

    /**
     * Sets the value of the selectorValueProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueSelectorGroupView.SelectorValueProvider }
     *     
     */
    public void setSelectorValueProvider(ValueSelectorGroupView.SelectorValueProvider value) {
        this.selectorValueProvider = value;
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
     * Gets the value of the selectedIndex property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSelectedIndex() {
        if (selectedIndex == null) {
            return new BigInteger("-1");
        } else {
            return selectedIndex;
        }
    }

    /**
     * Sets the value of the selectedIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSelectedIndex(BigInteger value) {
        this.selectedIndex = value;
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
     * Gets the value of the childType property.
     * 
     * @return
     *     possible object is
     *     {@link ValueSelectorType }
     *     
     */
    public ValueSelectorType getChildType() {
        return childType;
    }

    /**
     * Sets the value of the childType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueSelectorType }
     *     
     */
    public void setChildType(ValueSelectorType value) {
        this.childType = value;
    }

    /**
     * Gets the value of the selectionType property.
     * 
     * @return
     *     possible object is
     *     {@link SelectionType }
     *     
     */
    public SelectionType getSelectionType() {
        return selectionType;
    }

    /**
     * Sets the value of the selectionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SelectionType }
     *     
     */
    public void setSelectionType(SelectionType value) {
        this.selectionType = value;
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
     * Gets the value of the noOfRows property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNoOfRows() {
        if (noOfRows == null) {
            return new BigInteger("1");
        } else {
            return noOfRows;
        }
    }

    /**
     * Sets the value of the noOfRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNoOfRows(BigInteger value) {
        this.noOfRows = value;
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
     * Gets the value of the noOfColumns property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNoOfColumns() {
        if (noOfColumns == null) {
            return new BigInteger("1");
        } else {
            return noOfColumns;
        }
    }

    /**
     * Sets the value of the noOfColumns property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNoOfColumns(BigInteger value) {
        this.noOfColumns = value;
    }

    /**
     * Gets the value of the layoutType property.
     * 
     * @return
     *     possible object is
     *     {@link Layout }
     *     
     */
    public Layout getLayoutType() {
        return layoutType;
    }

    /**
     * Sets the value of the layoutType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Layout }
     *     
     */
    public void setLayoutType(Layout value) {
        this.layoutType = value;
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
     *       &lt;sequence>
     *         &lt;element ref="{http://www.sap.com/lmsl/slp}instanceValueProvider" maxOccurs="unbounded"/>
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
        "instanceValueProvider"
    })
    public static class SelectorValueProvider {

        @XmlElement(required = true)
        protected List<InstanceValueProvider> instanceValueProvider;

        /**
         * Gets the value of the instanceValueProvider property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the instanceValueProvider property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInstanceValueProvider().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InstanceValueProvider }
         * 
         * 
         */
        public List<InstanceValueProvider> getInstanceValueProvider() {
            if (instanceValueProvider == null) {
                instanceValueProvider = new ArrayList<InstanceValueProvider>();
            }
            return this.instanceValueProvider;
        }

    }

}
