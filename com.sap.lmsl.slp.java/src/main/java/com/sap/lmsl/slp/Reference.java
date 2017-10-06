
package com.sap.lmsl.slp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="refProperty" type="{http://www.sap.com/lmsl/slp}propertyType"/>
 *         &lt;element name="refValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="refParamIds">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="refControlType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "refProperty",
    "refValue",
    "refParamIds",
    "refControlType"
})
@XmlRootElement(name = "reference")
public class Reference {

    @XmlElement(required = true)
    protected PropertyType refProperty;
    @XmlElement(required = true)
    protected String refValue;
    @XmlElement(required = true)
    protected Reference.RefParamIds refParamIds;
    @XmlElement(defaultValue = "groupView")
    protected String refControlType;

    /**
     * Gets the value of the refProperty property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyType }
     *     
     */
    public PropertyType getRefProperty() {
        return refProperty;
    }

    /**
     * Sets the value of the refProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyType }
     *     
     */
    public void setRefProperty(PropertyType value) {
        this.refProperty = value;
    }

    /**
     * Gets the value of the refValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefValue() {
        return refValue;
    }

    /**
     * Sets the value of the refValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefValue(String value) {
        this.refValue = value;
    }

    /**
     * Gets the value of the refParamIds property.
     * 
     * @return
     *     possible object is
     *     {@link Reference.RefParamIds }
     *     
     */
    public Reference.RefParamIds getRefParamIds() {
        return refParamIds;
    }

    /**
     * Sets the value of the refParamIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference.RefParamIds }
     *     
     */
    public void setRefParamIds(Reference.RefParamIds value) {
        this.refParamIds = value;
    }

    /**
     * Gets the value of the refControlType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefControlType() {
        return refControlType;
    }

    /**
     * Sets the value of the refControlType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefControlType(String value) {
        this.refControlType = value;
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
     *       &lt;choice>
     *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id"
    })
    public static class RefParamIds {

        protected List<String> id;

        /**
         * Gets the value of the id property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the id property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getId().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getId() {
            if (id == null) {
                id = new ArrayList<String>();
            }
            return this.id;
        }

    }

}
