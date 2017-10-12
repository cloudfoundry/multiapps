
package com.sap.lmsl.slp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
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
 *         &lt;element name="caption" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataBinding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tableColumnValueProvider">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence minOccurs="0">
 *                   &lt;element ref="{http://www.sap.com/lmsl/slp}instanceValueProvider" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="dynamic" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="renderType" type="{http://www.sap.com/lmsl/slp}cellRenderer" />
 *       &lt;attribute name="sortable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="sortingOrder" type="{http://www.sap.com/lmsl/slp}sortingOrder" />
 *       &lt;attribute name="filter" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "caption",
    "dataBinding",
    "tableColumnValueProvider"
})
@XmlRootElement(name = "tableColumnView")
public class TableColumnView {

    protected String toolTip;
    @XmlElement(required = true)
    protected String caption;
    protected String dataBinding;
    @XmlElement(required = true)
    protected TableColumnView.TableColumnValueProvider tableColumnValueProvider;
    @XmlAttribute(name = "enable")
    protected Boolean enable;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "width")
    protected String width;
    @XmlAttribute(name = "renderType")
    protected CellRenderer renderType;
    @XmlAttribute(name = "sortable")
    protected Boolean sortable;
    @XmlAttribute(name = "sortingOrder")
    protected SortingOrder sortingOrder;
    @XmlAttribute(name = "filter")
    protected Boolean filter;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "parentId", required = true)
    protected String parentId;

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
     * Gets the value of the dataBinding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataBinding() {
        return dataBinding;
    }

    /**
     * Sets the value of the dataBinding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataBinding(String value) {
        this.dataBinding = value;
    }

    /**
     * Gets the value of the tableColumnValueProvider property.
     * 
     * @return
     *     possible object is
     *     {@link TableColumnView.TableColumnValueProvider }
     *     
     */
    public TableColumnView.TableColumnValueProvider getTableColumnValueProvider() {
        return tableColumnValueProvider;
    }

    /**
     * Sets the value of the tableColumnValueProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableColumnView.TableColumnValueProvider }
     *     
     */
    public void setTableColumnValueProvider(TableColumnView.TableColumnValueProvider value) {
        this.tableColumnValueProvider = value;
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
     * Gets the value of the renderType property.
     * 
     * @return
     *     possible object is
     *     {@link CellRenderer }
     *     
     */
    public CellRenderer getRenderType() {
        return renderType;
    }

    /**
     * Sets the value of the renderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CellRenderer }
     *     
     */
    public void setRenderType(CellRenderer value) {
        this.renderType = value;
    }

    /**
     * Gets the value of the sortable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSortable() {
        if (sortable == null) {
            return false;
        } else {
            return sortable;
        }
    }

    /**
     * Sets the value of the sortable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSortable(Boolean value) {
        this.sortable = value;
    }

    /**
     * Gets the value of the sortingOrder property.
     * 
     * @return
     *     possible object is
     *     {@link SortingOrder }
     *     
     */
    public SortingOrder getSortingOrder() {
        return sortingOrder;
    }

    /**
     * Sets the value of the sortingOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link SortingOrder }
     *     
     */
    public void setSortingOrder(SortingOrder value) {
        this.sortingOrder = value;
    }

    /**
     * Gets the value of the filter property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFilter() {
        if (filter == null) {
            return false;
        } else {
            return filter;
        }
    }

    /**
     * Sets the value of the filter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFilter(Boolean value) {
        this.filter = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence minOccurs="0">
     *         &lt;element ref="{http://www.sap.com/lmsl/slp}instanceValueProvider" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *       &lt;attribute name="dynamic" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "content"
    })
    public static class TableColumnValueProvider {

        @XmlElementRef(name = "instanceValueProvider", namespace = "http://www.sap.com/lmsl/slp", type = InstanceValueProvider.class, required = false)
        @XmlMixed
        protected List<Object> content;
        @XmlAttribute(name = "dynamic")
        protected Boolean dynamic;

        /**
         * Gets the value of the content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * {@link InstanceValueProvider }
         * 
         * 
         */
        public List<Object> getContent() {
            if (content == null) {
                content = new ArrayList<Object>();
            }
            return this.content;
        }

        /**
         * Gets the value of the dynamic property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isDynamic() {
            if (dynamic == null) {
                return false;
            } else {
                return dynamic;
            }
        }

        /**
         * Sets the value of the dynamic property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setDynamic(Boolean value) {
            this.dynamic = value;
        }

    }

}
