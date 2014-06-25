package control.servlets.b2b;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.rpc.ServiceException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.transform.dom.DOMSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import model.data.bindings.order.*;
import model.b2b.WebService;

/**
 * Servlet implementation class ConsolidateServlet
 */
public class ConsolidateServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
       
	// Key required to place an order
	private static String key = "cse73080";
    	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public ConsolidateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub

		// Read all purchase orders
		File PODirectory = new File(request.getSession().getServletContext().getRealPath("/purchasing.orders/"));				
		
		File[] PurchaseOrders = PODirectory.listFiles();
		
		ProcurementList procurements = new ProcurementList();		
								
		try
		{				
			WebService[] webServices = { 
			
			 new WebService("http://indigo.cse.yorku.ca:4413/axis/YVR.jws",
							"Vancouver",
							"YVRService",
							"YVR"),
			 new WebService("http://indigo.cse.yorku.ca:4413/axis/YYZ.jws",
							 "Toronto",
							 "YYZService",
							 "YYZ"),
			 new WebService("http://indigo.cse.yorku.ca:4413/axis/YHZ.jws",
							 "Halifax",
							 "YHZService",
							 "YHZ")															
			};			
			

			// Consolidate Purchase Orders
			for (int i = 0; i < PurchaseOrders.length; i++)
			{								
				// Don`t attempt to consolidate non-consolidated xmls or Procurement Reports
				if (PurchaseOrders[i].getName().contains("xml") && 
					!PurchaseOrders[i].getName().contains("X.xml") &&
					!PurchaseOrders[i].getName().contains("PR_"))
				{
					JAXBContext jc = JAXBContext.newInstance("model.data.bindings.order");
					Unmarshaller unmarshaller = jc.createUnmarshaller();																			
					
					JAXBElement<PurchasingOrder> poElement = (JAXBElement<PurchasingOrder>) unmarshaller.unmarshal(PurchaseOrders[i]);															
					
					PurchasingOrder po = (PurchasingOrder) poElement.getValue();															

					Item item;
					String wholesaler = "";
					int itemNumber, itemQuantity, WS_index, P_index;
					double quote, min;					
					Procurement procurement;
										
					// Consolidate each item in the purchase order
					for (int j = 0; j < po.getItems().getItemsList().size(); j++)
					{			
						procurement = new Procurement();
						
						item = po.getItems().getItemsList().get(j);

						itemNumber = item.getNumber().intValue();
						itemQuantity = item.getQuantity().intValue(); 
						WS_index = -1; min = -1; P_index = 0;																

						// Scan through Web Services						
						for (int k = 0; k < webServices.length; k++)
						{
							quote = (Double) webServices[k].getQuoteCall().invoke(new Object[]{itemNumber});

							if ((quote <= min && quote != -1) ||
								(quote != -1 && min == -1))
							{
									min = quote;
									WS_index = k;
							}
						}												
						
						// If Item was found on a wholesaler
						if (min != -1)
						{
							wholesaler = webServices[WS_index].getName();
							
							// Place the order
							webServices[WS_index].getOrderCall().invoke(new Object[]{itemNumber, itemQuantity, key});																																			

							Iterator<Procurement> it = procurements.getProcurements().iterator();

							// If item already exists in the procurement list, update quantity, price and extended							
							while(it.hasNext())
							{								
								if (it.next().getItem().getNumber().equals(item.getNumber()))
								{
									// set its (possibly) new price
									procurements.getProcurements().get(P_index).getItem().setPrice(
									new BigDecimal(min).round(new MathContext(3)).setScale(2));
									
									// set its new quantity
									procurements.getProcurements().get(P_index).getItem().setQuantity(
									procurements.getProcurements().get(P_index).getItem().getQuantity().add(		   
									item.getQuantity()));
									
									// set its new extended
									procurements.getProcurements().get(P_index).getItem().setExtended(
											new BigDecimal(
									procurements.getProcurements().get(P_index).getItem().getQuantity().doubleValue() * min).round(new MathContext(3)).setScale(2));									
									
									P_index = -1;
									break;
								}
								P_index++;
							}
							
							// If item wasn't found in Procurement List, add it
							if (P_index != -1)
							{								
								item.setPrice(new BigDecimal(min));									
								item.setExtended(new BigDecimal(min * itemQuantity));

								procurement.setItem(item);																	
								procurement.setWholesaler(wholesaler);
								procurements.getProcurements().add(procurement);
							}
						}						
					}	
					
					// Rename PurchaseOrders file name by adding an X at the end to show it
					// has been consolidated
					PurchaseOrders[i].renameTo(new File(request.getSession().getServletContext().getRealPath("/purchasing.orders/") + File.separator +
							PurchaseOrders[i].getName().replace(".xml", "X.xml")));
				}
			}

			// Determine Grand Total
			Iterator<Procurement> it = procurements.getProcurements().iterator();
			double grandTotal = 0.0;
			while(it.hasNext())
			{
				grandTotal += it.next().getItem().getExtended().doubleValue();
			}
			
			procurements.setGrandTotal(new BigDecimal(grandTotal));
			
			// Create date for today for naming procurement report file (html) on disk
			XMLGregorianCalendar date = DatatypeFactory.newInstance().
					newXMLGregorianCalendar(new GregorianCalendar());			
								
			FileOutputStream stream = null;
						
			//Create xml document of Procurement List
			stream = new FileOutputStream(new File(request.getSession().getServletContext()
					.getRealPath("/purchasing.orders/") + File.separator + 	"PR_" +					 
					date.getYear() + "_" + date.getMonth() + "_" + date.getDay() + ".html"));						
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			JAXBContext jc = JAXBContext.newInstance("model.data.bindings.order");
			JAXBElement<ProcurementList> pListElement = (new ObjectFactory())
					.createProcurementList(procurements);

			Marshaller m = jc.createMarshaller();
			m.marshal(pListElement, doc);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
						
			Source xslSource = new StreamSource(new File(request.getSession().getServletContext()
					.getRealPath("/purchasing.orders/") + File.separator + "pr.xsl"));			

			Transformer transformer = transformerFactory.newTransformer(xslSource);
						
			transformer.transform(new DOMSource(doc), 
					new StreamResult(stream));
			
			stream.flush();
			stream.close();	
			
			// Forward to Procurement JSP stating procurement report is complete
			request.getRequestDispatcher("/Procurement.jspx").forward(request, response);
		}
		catch (UnmarshalException e)
		{			
			e.printStackTrace();
		}		
		catch (ServiceException e)
		{			
			e.printStackTrace();
		}
		catch (JAXBException e)
		{			
			e.printStackTrace();
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		}	
		catch (TransformerException e)
		{
			e.printStackTrace();
		}		
		catch (DatatypeConfigurationException e)
		{		
			e.printStackTrace();
		}
	}

}
