package model;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.data.bindings.catalog.Category;
import model.data.bindings.catalog.Dataroot;
import model.data.bindings.catalog.Product;
import model.data.bindings.catalog.Supplier;

import org.xml.sax.SAXException;

public class Catalogue {

	private Map<BigInteger, Product> products;
	private Map<BigInteger, Category> categories;
	private Map<BigInteger, Supplier> suppliers;

	// private Map<String, List<Product>> productsByCategories;
	private Map<BigInteger, Map<BigInteger, Product>> productsByCategories;

	//private File xmlCatalogSchema;
	private File xmlCatalog;

	public Catalogue(File xmlCatalogSchema, File xmlCatalog)
			throws JAXBException, SAXException {
		//this.xmlCatalogSchema = xmlCatalogSchema;
		this.xmlCatalog = xmlCatalog;

		validateAndUnmarshallDataroot();
	}

	private void validateAndUnmarshallDataroot() throws JAXBException,
			SAXException {

		//SchemaFactory factory = SchemaFactory
		//		.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		//Schema schema = factory.newSchema(xmlCatalogSchema);

		JAXBContext jc = JAXBContext.newInstance("model.data.bindings.catalog");
		Unmarshaller u = jc.createUnmarshaller();

		//u.setSchema(schema);
		Dataroot o = (Dataroot) u.unmarshal(xmlCatalog);
		constructCatalogFromDataroot(o);
	}

	private void constructCatalogFromDataroot(Dataroot root) {
		products = new HashMap<BigInteger, Product>();
		categories = new HashMap<BigInteger, Category>();
		suppliers = new HashMap<BigInteger, Supplier>();

		productsByCategories = new HashMap<BigInteger, Map<BigInteger, Product>>();

		Iterator<Category> itCategories = root.getCategories().iterator();
		while (itCategories.hasNext()) {
			Category category = itCategories.next();
			categories.put(category.getCategoryID(), category);
		}

		Iterator<Supplier> itSuppliers = root.getSuppliers().iterator();
		while (itSuppliers.hasNext()) {
			Supplier supplier = itSuppliers.next();
			suppliers.put(supplier.getSupplierID(), supplier);
		}

		Iterator<Product> itProducts = root.getProducts().iterator();
		while (itProducts.hasNext()) {
			Product product = itProducts.next();
			products.put(product.getProductID(), product);
		}
		
		sortProductHashMapByValues();
		sortCategoryHashMapByValues();

		for (BigInteger categoryID : categories.keySet()) {

			Map<BigInteger, Product> productIDsList = new HashMap<BigInteger, Product>();

			List<Product> pList = root.getProducts();
			for (int i = 0; i < pList.size(); i++) {
				Product product = pList.get(i);

				BigInteger pCategoryID = (product.getCategoryID());
				BigInteger productID = product.getProductID();

				if (pCategoryID.equals(categoryID)) {
					productIDsList.put(productID, product);

				}
			}
			productsByCategories.put(categoryID, sortProductHashMapByValues(productIDsList));
		}
	}
	
	private LinkedHashMap<BigInteger, Product> sortProductHashMapByValues(Map<BigInteger, Product> passedMap) {

		List<BigInteger> mapKeys = new ArrayList<BigInteger>(passedMap.keySet());
		List<Product> mapValues = new ArrayList<Product>(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap<BigInteger, Product> someMap = 
			new LinkedHashMap<BigInteger, Product>();
		Iterator<Product> valueIt = mapValues.iterator();
		
		while (valueIt.hasNext()) {
			Product val = valueIt.next();
			Iterator<BigInteger> keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				BigInteger key = keyIt.next();
				if (passedMap.get(key).toString().equals(val.toString())) {
					passedMap.remove(key);
					mapKeys.remove(key);
					someMap.put(key, val);
					break;
				}
			}
		}
		
		return someMap;
		
	} 
	
	private void sortProductHashMapByValues() {

		List<BigInteger> mapKeys = new ArrayList<BigInteger>(products.keySet());
		List<Product> mapValues = new ArrayList<Product>(products.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap<BigInteger, Product> someMap = 
			new LinkedHashMap<BigInteger, Product>();
		Iterator<Product> valueIt = mapValues.iterator();
		
		while (valueIt.hasNext()) {
			Product val = valueIt.next();
			Iterator<BigInteger> keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				BigInteger key = keyIt.next();
				if (products.get(key).toString().equals(val.toString())) {
					products.remove(key);
					mapKeys.remove(key);
					someMap.put(key, val);
					break;
				}
			}
		}
		products = someMap;
		
	} 
	
	private void sortCategoryHashMapByValues() {

		List<BigInteger> mapKeys = new ArrayList<BigInteger>(categories.keySet());
		List<Category> mapValues = new ArrayList<Category>(categories.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);


		LinkedHashMap<BigInteger, Category> someMap = 
			new LinkedHashMap<BigInteger, Category>();
		Iterator<Category> valueIt = mapValues.iterator();
		
		while (valueIt.hasNext()) {
			Category val = valueIt.next();
			Iterator<BigInteger> keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				BigInteger key = keyIt.next();
				if (categories.get(key).toString().equals(val.toString())) {
					categories.remove(key);
					mapKeys.remove(key);
					someMap.put(key, val);
					break;
				}
			}
		}
		categories = someMap;
	} 
	
	public Map<BigInteger, Product> getProducts() {
		return products;
	}

	public Map<BigInteger, Category> getCategories() {
		return categories;
	}

	public Map<BigInteger, Supplier> getSuppliers() {
		return suppliers;
	}

	public Map<BigInteger, Product> getProductsByCategoryID(String categoryID) {

		Map<BigInteger, Product> products = productsByCategories.get(new BigInteger(
				categoryID));

		return products;
	}	
	public Map<BigInteger, Map<BigInteger, Product>> getProductsByCategories() {

		return productsByCategories;
	}

	public Product getProductByProductID(String productID) {
		Product product = products.get(new BigInteger(productID));
		return product;
	}

	public Map <BigInteger, Product> searchProductsByName(String keyword) {
	    
	    Map <BigInteger,Product> listProducts = new HashMap<BigInteger, Product>();
	    Iterator<Product> it = products.values().iterator();
	    while (it.hasNext())
		{
		    Product product = it.next();
		    if (product.getProductName().contains(keyword)){
		    	BigInteger productID = product.getProductID();

			    listProducts.put(productID, product);
			}
		}
	    
	    return listProducts;
	}
	

}
