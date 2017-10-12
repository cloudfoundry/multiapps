
package com.sap.lmsl.slp;

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
 *         &lt;element name="metaDialog" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element ref="{http://www.sap.com/lmsl/slp}dialogHeader" minOccurs="0"/>
 *                   &lt;element ref="{http://www.sap.com/lmsl/slp}dialogBody" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "metaDialog"
})
@XmlRootElement(name = "metaDialogs")
public class MetaDialogs {

    protected MetaDialogs.MetaDialog metaDialog;

    /**
     * Gets the value of the metaDialog property.
     * 
     * @return
     *     possible object is
     *     {@link MetaDialogs.MetaDialog }
     *     
     */
    public MetaDialogs.MetaDialog getMetaDialog() {
        return metaDialog;
    }

    /**
     * Sets the value of the metaDialog property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetaDialogs.MetaDialog }
     *     
     */
    public void setMetaDialog(MetaDialogs.MetaDialog value) {
        this.metaDialog = value;
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
     *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element ref="{http://www.sap.com/lmsl/slp}dialogHeader" minOccurs="0"/>
     *         &lt;element ref="{http://www.sap.com/lmsl/slp}dialogBody" minOccurs="0"/>
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
        "id",
        "dialogHeader",
        "dialogBody"
    })
    public static class MetaDialog {

        @XmlElement(required = true)
        protected String id;
        protected DialogHeader dialogHeader;
        protected DialogBody dialogBody;

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
         * Gets the value of the dialogHeader property.
         * 
         * @return
         *     possible object is
         *     {@link DialogHeader }
         *     
         */
        public DialogHeader getDialogHeader() {
            return dialogHeader;
        }

        /**
         * Sets the value of the dialogHeader property.
         * 
         * @param value
         *     allowed object is
         *     {@link DialogHeader }
         *     
         */
        public void setDialogHeader(DialogHeader value) {
            this.dialogHeader = value;
        }

        /**
         * Gets the value of the dialogBody property.
         * 
         * @return
         *     possible object is
         *     {@link DialogBody }
         *     
         */
        public DialogBody getDialogBody() {
            return dialogBody;
        }

        /**
         * Sets the value of the dialogBody property.
         * 
         * @param value
         *     allowed object is
         *     {@link DialogBody }
         *     
         */
        public void setDialogBody(DialogBody value) {
            this.dialogBody = value;
        }

    }

}
