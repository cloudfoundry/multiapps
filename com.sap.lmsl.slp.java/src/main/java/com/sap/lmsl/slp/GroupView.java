
package com.sap.lmsl.slp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;element name="groupCaption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="toolTip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="children" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice maxOccurs="unbounded">
 *                     &lt;element ref="{http://www.sap.com/lmsl/slp}textView" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element ref="{http://www.sap.com/lmsl/slp}valueSelectorGroupView" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element ref="{http://www.sap.com/lmsl/slp}groupView" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element ref="{http://www.sap.com/lmsl/slp}textInputView" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element ref="{http://www.sap.com/lmsl/slp}tableView" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element ref="{http://www.sap.com/lmsl/slp}relatedLinkView" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "groupCaption",
    "toolTip",
    "children"
})
@XmlRootElement(name = "groupView")
public class GroupView {

    protected String groupCaption;
    protected String toolTip;
    protected GroupView.Children children;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "parentId", required = true)
    protected String parentId;
    @XmlAttribute(name = "enable")
    protected Boolean enable;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "width")
    protected String width;

    /**
     * Gets the value of the groupCaption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupCaption() {
        return groupCaption;
    }

    /**
     * Sets the value of the groupCaption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupCaption(String value) {
        this.groupCaption = value;
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
     * Gets the value of the children property.
     * 
     * @return
     *     possible object is
     *     {@link GroupView.Children }
     *     
     */
    public GroupView.Children getChildren() {
        return children;
    }

    /**
     * Sets the value of the children property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupView.Children }
     *     
     */
    public void setChildren(GroupView.Children value) {
        this.children = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;choice maxOccurs="unbounded">
     *           &lt;element ref="{http://www.sap.com/lmsl/slp}textView" maxOccurs="unbounded" minOccurs="0"/>
     *           &lt;element ref="{http://www.sap.com/lmsl/slp}valueSelectorGroupView" maxOccurs="unbounded" minOccurs="0"/>
     *           &lt;element ref="{http://www.sap.com/lmsl/slp}groupView" maxOccurs="unbounded" minOccurs="0"/>
     *           &lt;element ref="{http://www.sap.com/lmsl/slp}textInputView" maxOccurs="unbounded" minOccurs="0"/>
     *           &lt;element ref="{http://www.sap.com/lmsl/slp}tableView" maxOccurs="unbounded" minOccurs="0"/>
     *           &lt;element ref="{http://www.sap.com/lmsl/slp}relatedLinkView" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;/choice>
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
        "textViewOrValueSelectorGroupViewOrGroupView"
    })
    public static class Children {

        @XmlElements({
            @XmlElement(name = "textView", type = TextView.class),
            @XmlElement(name = "valueSelectorGroupView", type = ValueSelectorGroupView.class),
            @XmlElement(name = "groupView", type = GroupView.class),
            @XmlElement(name = "textInputView", type = TextInputView.class),
            @XmlElement(name = "tableView", type = TableView.class),
            @XmlElement(name = "relatedLinkView", type = RelatedLinkView.class)
        })
        protected List<Object> textViewOrValueSelectorGroupViewOrGroupView;

        /**
         * Gets the value of the textViewOrValueSelectorGroupViewOrGroupView property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the textViewOrValueSelectorGroupViewOrGroupView property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTextViewOrValueSelectorGroupViewOrGroupView().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TextView }
         * {@link ValueSelectorGroupView }
         * {@link GroupView }
         * {@link TextInputView }
         * {@link TableView }
         * {@link RelatedLinkView }
         * 
         * 
         */
        public List<Object> getTextViewOrValueSelectorGroupViewOrGroupView() {
            if (textViewOrValueSelectorGroupViewOrGroupView == null) {
                textViewOrValueSelectorGroupViewOrGroupView = new ArrayList<Object>();
            }
            return this.textViewOrValueSelectorGroupViewOrGroupView;
        }

    }

}
