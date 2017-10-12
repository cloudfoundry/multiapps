
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;choice>
 *         &lt;element name="slppversion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="slmpversion" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "slppversion",
    "slmpversion"
})
@XmlRootElement(name = "Metadata")
public class Metadata {

    protected String slppversion;
    protected String slmpversion;

    /**
     * Gets the value of the slppversion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSlppversion() {
        return slppversion;
    }

    /**
     * Sets the value of the slppversion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSlppversion(String value) {
        this.slppversion = value;
    }

    /**
     * Gets the value of the slmpversion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSlmpversion() {
        return slmpversion;
    }

    /**
     * Sets the value of the slmpversion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSlmpversion(String value) {
        this.slmpversion = value;
    }

}
