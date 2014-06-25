package control.servlets;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import model.Catalogue;
import model.Client;
import model.ShoppingCart;
import model.data.bindings.catalog.Product;

/**
 * Servlet implementation class CashierServlet
 */
public class CashierServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;

	private ShoppingCart cart;
	private Catalogue catalog;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CashierServlet() {
		super();

	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */

	public void init() throws ServletException {
		this.catalog = (Catalogue) this.getServletContext().getAttribute(
				"catalog");

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
		this.cart = (ShoppingCart) request.getSession().getAttribute("cart");
		
		if (this.cart == null)
		{
			this.cart = new ShoppingCart(new File(
					request.getSession().getServletContext()
					.getRealPath("/purchasing.orders/")));
			
			request.getSession().setAttribute(
					"cart",
					this.cart);
		}
		
		String to = "/ShoppingCart.jspx";

		if (isArgumentValid(request.getParameter("add")))
		{
			
			for (int i = 0; i < request.getParameterValues("productID").length; i++) {
				Product product = catalog.getProductByProductID((request
						.getParameterValues("productID"))[i]);

				if (!request.getParameterValues("quantity")[i].equals("0")) 
				{
					// Will overwrite the old object with the new one
					if (this.cart.getItems().containsKey(
							product.getProductID())) {
						this.cart.add(product, new BigInteger(request
								.getParameterValues("quantity")[i])
								.add(this.cart.getItems().get(
										product.getProductID())
										.getQuantity()));

					} 
					else 
					{
						this.cart.add(product, new BigInteger(request
								.getParameterValues("quantity")[i]));
					}
				}
			}
			
		} 
		else if (isArgumentValid(request.getParameter("update"))) 
		{
		
			for (int i = 0; i < request.getParameterValues("productID").length; i++) 
			{
				Product product = catalog.getProductByProductID((request
						.getParameterValues("productID"))[i]);

				if (request.getParameterValues("quantity")[i].equals("0")) 
				{
					// Will remove only if the value is present
					this.cart.remove(product);
				} 
				else 
				{

					// Will overwrite the old object with the new
					// one
					this.cart.add(product, new BigInteger(request
							.getParameterValues("quantity")[i]));
				} 
					
			}

		}
		else if (isArgumentPresent(request.getParameter("checkout"))) {
			
			if (this.cart.isEmpty())
			{
					request
						.setAttribute(
						"error",
						"Nothing to checkout, the cart is empty ");
					
			}
			else
			{
				Client client = (Client) request.getSession()
						.getAttribute("client");
				if (client != null) 
				{
					String sOrderID;
					try {
	
						sOrderID = this.cart.checkout(client);
						/* Create a new empty cart for a customer,
						 * the old one will be garbage collected.
						 */
						request.getSession().setAttribute(
								"cart",
								new ShoppingCart(new File(
										request.getSession().getServletContext()
										.getRealPath("/purchasing.orders/"))));
						
						request.setAttribute("orderID", sOrderID);
						to = "/PurchaseConfirmation.jspx";
	
					} catch (DatatypeConfigurationException e) {
						request
								.setAttribute(
										"error",
										"We have encountered an error while "
												+ "processing your request. You will not "
												+ "be billed. Please try again or contact us.");
					} catch (ParserConfigurationException e) {
						request
								.setAttribute(
										"error",
										"We have encountered an error while "
												+ "processing your request. You will not "
												+ "be billed. Please try again or contact us.");
					} catch (JAXBException e) {
						request
								.setAttribute(
										"error",
										"We have encountered an error while "
												+ "processing your request. You will not "
												+ "be billed. Please try again or contact us.");
					} catch (TransformerException e) {
						request
								.setAttribute(
										"error",
										"We have encountered an error while "
												+ "processing your request. You will not "
												+ "be billed. Please try again or contact us.");
					}
				}
				else 
				{
					request
						.setAttribute(
								"error",
								"You need to be logged in to checkout a shopping cart. "
								);
					
					to = "/Login.jspx";
				}
			} 
		} else if (isArgumentValid(request.getParameter("orderID"))) {
			to = "/purchasing.orders/" + request.getParameter("orderID")
					+ ".xml";
		} else if (isArgumentPresent(request.getParameter("showOrders"))) {
			Client client = (Client) request.getSession().getAttribute("client");
			if (client != null) 
			{
				request.setAttribute("orderList", 
						this.cart.getOrdersIDs(client));
				to ="/ShowOrders.jspx";
			}
			else
			{
				to = "/Login";
			}
		}
		else {
			request.setAttribute("error", "Invalid request!");
		}
		request.getRequestDispatcher(to).forward(request, response);
	}
}
