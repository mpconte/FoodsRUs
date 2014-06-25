
//
//This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-520 
//See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
//Any modifications to this file will be lost upon recompilation of the source schema. 
//Generated on: 2009.02.12 at 11:14:12 AM EST 
//
package model.data.bindings.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
* <p>Java class for procurementList complex type.
* 
* <p>The following schema fragment specifies the expected content contained within this class.
* 
* <pre>
* &lt;complexType name="procurementList">
*   &lt;complexContent>
*     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
*       &lt;sequence>
*         &lt;element name="procurements" type="{}procurement" maxOccurs="unbounded"/>
*       &lt;/sequence>
*     &lt;/restriction>
*   &lt;/complexContent>
* &lt;/complexType>
* </pre>
* 
* 
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "procurementList", propOrder = {
 "procurements",
 "grandTotal"
})
public class ProcurementList {

 @XmlElement(required = true)
 protected List<Procurement> procurements;
 @XmlElement(required = true) 
 protected BigDecimal grandTotal;
 
 /**
  * Gets the value of the procurements property.
  * 
  * <p>
  * This accessor method returns a reference to the live list,
  * not a snapshot. Therefore any modification you make to the
  * returned list will be present inside the JAXB object.
  * This is why there is not a <CODE>set</CODE> method for the procurements property.
  * 
  * <p>
  * For example, to add a new item, do as follows:
  * <pre>
  *    getProcurements().add(newItem);
  * </pre>
  * 
  * 
  * <p>
  * Objects of the following type(s) are allowed in the list
  * {@link Procurement }
  * 
  * 
  */
 public List<Procurement> getProcurements() {
     if (procurements == null) {
         procurements = new ArrayList<Procurement>();
     }
     return this.procurements;
 }

 public BigDecimal getGrandTotal()
 {
	 return grandTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
 }
 
 public void setGrandTotal(BigDecimal newGrandTotal)
 {
	 grandTotal = newGrandTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
 }
 
}
