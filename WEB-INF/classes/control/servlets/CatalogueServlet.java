package control.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CatalogServlet
 */
public class CatalogueServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;


	@Override
	public void init() throws ServletException {
		super.init();
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

		String to = "/Catalogue.jspx";

		if (isArgumentValid(request.getParameter("categoryID"))) 
		{
			request.setAttribute("categoryID", request.getParameter("categoryID"));
		}
		
		if ( isArgumentValid(request.getParameter("productID")))
		{

			request.setAttribute("productID", request.getParameter("productID"));
		}
		
		if (isArgumentValid(request.getParameter("searchByName"))) 
		{
		    	request.setAttribute("searchResults", request.getParameter("searchByName"));    
		} 


		request.getRequestDispatcher(to).forward(request, response);
	}

}
