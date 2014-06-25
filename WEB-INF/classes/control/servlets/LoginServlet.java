package control.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Client;
import model.ClientBook;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;
	private ClientBook cb;

	@Override
	public void init() throws ServletException {
		super.init();
		this.cb = (ClientBook) this.getServletContext().getAttribute(
				"clientBook");
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		String to;


		if (isArgumentPresent(request.getParameter("logout"))) 
		{
			request.getSession().invalidate();
			request.setAttribute("error", "You have been successfuly log out");
			to = "/Login.jspx";
		}
		else if ( ( isArgumentPresent(request.getParameter("login")) || 
				isArgumentPresent(request.getParameter("vieworders")))
 				&& isArgumentValid(request.getParameter("username")) 
				&& isArgumentValid(request.getParameter("password")) )
		{
			if (!cb.isRealClient(request.getParameter("username"))) {
				request.setAttribute("error", "No client with such username!");
				to = "/Login.jspx";
			} 
			else 
			{
				Client client = cb.getClient(request.getParameter("username"),
						request.getParameter("password"));
				if (client != null) 
				{
					request.getSession().setAttribute("client", client);

					to = "/Catalogue.jspx";
					
				} 
				else 
				{
					request.setAttribute("error", "Incorrect Login!");
					to = "/Login.jspx";
				}
			}
		}
		else 
		{
			to = "/Login.jspx";
		}
		
		request.getRequestDispatcher(to).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
