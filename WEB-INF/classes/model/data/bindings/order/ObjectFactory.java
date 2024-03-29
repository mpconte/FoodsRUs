//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.17 at 04:42:04 PM EST 
//


package model.data.bindings.order;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Order_QNAME = new QName("", "order");
    private final static QName _ProcurementList_QNAME = new QName("", "procurementList");
    
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddressType() {
        return new Address();
    }

    /**
     * Create an instance of {@link PurchasingOrder }
     * 
     */
    public PurchasingOrder createOrderType() {
        return new PurchasingOrder();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItemType() {
        return new Item();
    }

    /**
     * Create an instance of {@link Customer }
     * 
     */
    public Customer createCustomerType() {
        return new Customer();
    }

    /**
     * Create an instance of {@link Items }
     * 
     */
    public Items createItemsType() {
        return new Items();
    }
    
    /**
     * Create an instance of {@link ProcurementList }
     * 
     */
    public ProcurementList createProcurementList() {
        return new ProcurementList();
    }
    
    
    /**
     * Create an instance of {@link Procurement }
     * 
     */
    public Procurement createProcurement() {
        return new Procurement();
    }
    

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PurchasingOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "order")
    public JAXBElement<PurchasingOrder> createOrder(PurchasingOrder value) {
        return new JAXBElement<PurchasingOrder>(_Order_QNAME, PurchasingOrder.class, null, value);
    }
    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcurementList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "procurementList")
    public JAXBElement<ProcurementList> createProcurementList(ProcurementList value) {
        return new JAXBElement<ProcurementList>(_ProcurementList_QNAME, ProcurementList.class, null, value);
    }

}
