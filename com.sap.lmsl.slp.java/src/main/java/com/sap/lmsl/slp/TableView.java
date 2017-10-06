
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
 *         &lt;element name="tableColumns">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.sap.com/lmsl/slp}tableColumnView" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="tableValueProvider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="caption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="toolTip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infoDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rowBinding" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="dynamic" type="{http://www.sap.com/lmsl/slp}tableDynamicity" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="visibleNoOfRows" type="{http://www.w3.org/2001/XMLSchema}integer" default="7" />
 *       &lt;attribute name="selectedIndex" type="{http://www.w3.org/2001/XMLSchema}integer" default="-1" />
 *       &lt;attribute name="selectionType" type="{http://www.sap.com/lmsl/slp}selectionType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tableColumns",
    "tableValueProvider",
    "caption",
    "toolTip",
    "infoDescription"
})
@XmlRootElement(name = "tableView")
public class TableView {

    @XmlElement(required = true)
    protected TableView.TableColumns tableColumns;
    protected String tableValueProvider;
    protected String caption;
    protected String toolTip;
    protected String infoDescription;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "parentId", required = true)
    protected String parentId;
    @XmlAttribute(name = "rowBinding", required = true)
    protected String rowBinding;
    @XmlAttribute(name = "enable")
    protected Boolean enable;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "dynamic")
    protected TableDynamicity dynamic;
    @XmlAttribute(name = "width")
    protected BigInteger width;
    @XmlAttribute(name = "visibleNoOfRows")
    protected BigInteger visibleNoOfRows;
    @XmlAttribute(name = "selectedIndex")
    protected BigInteger selectedIndex;
    @XmlAttribute(name = "selectionType")
    protected SelectionType selectionType;

    /**
     * Gets the value of the tableColumns property.
     * 
     * @return
     *     possible object is
     *     {@link TableView.TableColumns }
     *     
     */
    public TableView.TableColumns getTableColumns() {
        return tableColumns;
    }

    /**
     * Sets the value of the tableColumns property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableView.TableColumns }
     *     
     */
    public void setTableColumns(TableView.TableColumns value) {
        this.tableColumns = value;
    }

    /**
     * Gets the value of the tableValueProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableValueProvider() {
        return tableValueProvider;
    }

    /**
     * Sets the value of the tableValueProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableValueProvider(String value) {
        this.tableValueProvider = value;
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
     * Gets the value of the rowBinding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRowBinding() {
        return rowBinding;
    }

    /**
     * Sets the value of the rowBinding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRowBinding(String value) {
        this.rowBinding = value;
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
     * Gets the value of the dynamic property.
     * 
     * @return
     *     possible object is
     *     {@link TableDynamicity }
     *     
     */
    public TableDynamicity getDynamic() {
        return dynamic;
    }

    /**
     * Sets the value of the dynamic property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableDynamicity }
     *     
     */
    public void setDynamic(TableDynamicity value) {
        this.dynamic = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWidth(BigInteger value) {
        this.width = value;
    }

    /**
     * Gets the value of the visibleNoOfRows property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVisibleNoOfRows() {
        if (visibleNoOfRows == null) {
            return new BigInteger("7");
        } else {
            return visibleNoOfRows;
        }
    }

    /**
     * Sets the value of the visibleNoOfRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVisibleNoOfRows(BigInteger value) {
        this.visibleNoOfRows = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://www.sap.com/lmsl/slp}tableColumnView" maxOccurs="unbounded"/>
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
        "tableColumnView"
    })
    public static class TableColumns {

        @XmlElement(required = true)
        protected List<TableColumnView> tableColumnView;

        /**
         * Gets the value of the tableColumnView property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tableColumnView property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTableColumnView().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TableColumnView }
         * 
         * 
         */
        public List<TableColumnView> getTableColumnView() {
            if (tableColumnView == null) {
                tableColumnView = new ArrayList<TableColumnView>();
            }
            return this.tableColumnView;
        }

    }

}
