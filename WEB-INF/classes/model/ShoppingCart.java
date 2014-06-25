package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.data.bindings.catalog.Product;
import model.data.bindings.order.Customer;
import model.data.bindings.order.Item;
import model.data.bindings.order.Items;
import model.data.bindings.order.ObjectFactory;
import model.data.bindings.order.PurchasingOrder;

import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;

public class ShoppingCart {
	
	private Map<BigInteger, Item> items;
	private MathContext mc;
	private final File sWorkingPath;
	
	public ShoppingCart(File sWorkingDir) 
	{
		this.items = new HashMap<BigInteger, Item>();
		mc = new MathContext(3);
		this.sWorkingPath = sWorkingDir;

	}
	
	public File getWorkDirectory()
	{
		return this.sWorkingPath;
	}
	
	public Map<BigInteger, Item> getItems() {
		return items;
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}
	
	public Item add(Product product, BigInteger quantity)
	{
		Item item = new Item();
		item.setName(product.getProductName());
		item.setNumber(product.getProductID());
		item.setPrice(product.getUnitPrice());
		item.setQuantity(quantity);
		
		return items.put(item.getNumber(), item);
	}
	
	public Item remove(Product product)
	{
		return items.remove(product.getProductID());
	}
	
	public String checkout(Customer customer)
		throws 	DatatypeConfigurationException, 
			ParserConfigurationException, 
			JAXBException, 
			TransformerException, 
			IOException
	{
		PurchasingOrder order = new PurchasingOrder();
		Items items = new Items();
		
		order.setCustomer(customer);
		
		Iterator<Item> it = getItems().values().iterator();
		while(it.hasNext())
		{
			Item item = it.next();
			items.getItemsList().add(item);
		}
		
		order.setItems(items);
		order.setTotal(getTotal());
		order.setShipping(getShippingCosts());
		order.setGST(getGST());
		order.setPST(getGST());
		order.setGrandTotal(getGrandTotal());
		order.setId(getNumberOfOrders(customer).add(BigInteger.valueOf(1)));
		

		writeOrder(order);

		
		return customer.getAccount() +  order.getId();
	}
	
	private void writeOrder(PurchasingOrder order) 
		throws 	DatatypeConfigurationException, 
				ParserConfigurationException, 
				JAXBException, 
				TransformerException, 
				IOException
	{

		FileOutputStream stream = null;

			order.setSubmitted(
					DatatypeFactory.newInstance().
					newXMLGregorianCalendar(new GregorianCalendar()));
			
			stream = new FileOutputStream(new File(getWorkDirectory() + 
					File.separator +
					order.getCustomer().getAccount() +  
					order.getId() + ".xml"));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			JAXBContext jc = JAXBContext.newInstance("model.data.bindings.order");
			JAXBElement<PurchasingOrder> poElement = (new ObjectFactory())
					.createOrder(order);

			Marshaller m = jc.createMarshaller();
			m.marshal(poElement, doc);

			ProcessingInstruction processingInstruction = doc
					.createProcessingInstruction("xml-stylesheet",
							"type=\"text/xsl\" href=\"purchasing.orders/po.xsl\"");

			doc.insertBefore(processingInstruction, doc.getDocumentElement());
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();


			transformer.transform(new DOMSource(doc), 
					new StreamResult(stream));

			stream.flush();
			stream.close();

		
	}
	
	private static String getFileName(String path){

		String fileName = null;
		String separator = File.separator;

		int pos = path.lastIndexOf(separator);
		int pos2 = path.lastIndexOf(".");

		if(pos2>-1)
		fileName =path.substring(pos+1, pos2);
		else
		fileName =path.substring(pos+1);

		return fileName;
		}
	
	public List<String> getOrdersIDs(Customer customer)
	{

		File[] listOrderFiles = getWorkDirectory().listFiles();
		
		List<String> toReturn = new Vector<String>();
		for (int i = 0; i < listOrderFiles.length; i++)
		{
			File order = listOrderFiles[i];
			if (order.getName().contains(customer.getAccount()))
			{
				toReturn.add(getFileName(order.getName()));
			}
		}
		return toReturn;
	}
	
	public BigInteger getNumberOfOrders(Customer customer)
	{
		return BigInteger.valueOf((getOrdersIDs(customer).size()));
	}
	
	public BigDecimal getTotal()
	{
		BigDecimal overallPrice = new BigDecimal(0.0, mc);
		Iterator<Item> it = items.values().iterator();
		
		while (it.hasNext())
		{
			Item item = it.next();
			overallPrice = overallPrice.add(
					item.getPrice().multiply(
							new BigDecimal(item.getQuantity(), mc)));
		}

		return overallPrice.setScale(2);
	}
	
	public BigDecimal getShippingCosts()
	{
		if (getTotal().compareTo(new BigDecimal(100.0)) == -1 )
		{
			return new BigDecimal(0.0, mc).setScale(2);
		}
		else
		{
			return new BigDecimal(5.0, mc).setScale(2);
		}
	}
	
	public BigDecimal getGST()
	{
		BigDecimal gst = new BigDecimal(0.0, mc);
		gst = gst.add(getTotal());
		gst = gst.add(getShippingCosts());
		
		gst = gst.multiply(new BigDecimal(0.06, mc));
		
		return gst.round(mc).setScale(2);
	}
	
	public BigDecimal getPST()
	{
		BigDecimal pst = new BigDecimal(0.0, mc);
		pst = pst.add(getTotal());
		pst = pst.add(getShippingCosts());
		
		pst = pst.multiply(new BigDecimal(0.08, mc));
		
		return pst.round(mc).setScale(2);
	}
	
	public BigDecimal getGrandTotal()
	{
		BigDecimal total = new BigDecimal(0.0, mc);
		total = total.add(getTotal());
		total = total.add(getShippingCosts());
		total = total.add(getGST());
		total = total.add(getPST());
		
		return total.setScale(2);
	}

}
