package control.validators;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import model.Catalogue;
import model.data.bindings.catalog.Product;

/**
 * Servlet Filter implementation class CashierValidationFilter
 */
public class CashierValidationFilter implements Filter {
	
	private ServletContext sc;
	private Catalogue catalog;
	
    /**
     * Default constructor. 
     */
    public CashierValidationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (isArgumentValid(request.getParameter("add")) || 
				isArgumentValid(request.getParameter("update")))
		{
			if (isArgumentValid(request.getParameter("productID"))
					&& isArgumentValid(request.getParameter("quantity"))
					&& (request.getParameterValues("quantity").length == request
							.getParameterValues("productID").length)) 
			{
				
				for (int i = 0; i < request.getParameterValues("productID").length; i++) 
				{
					Product product = catalog.getProductByProductID((request
							.getParameterValues("productID"))[i]);
					
					if (product == null)
					{
						request.setAttribute("error",
								"Invalid product ID");
						
						this.sc.getRequestDispatcher("/Catalogue.jspx").forward(request, response);
						return;
					}
					else
					{
						try 
						{
							BigInteger n = new BigInteger(
									request.getParameterValues("quantity")[i]);
							
							if (n.compareTo( BigInteger.ZERO) == -1)
							{
								request.setAttribute("error",
										"Quantity must be positive " );
								
								if (isArgumentValid(request.getParameter("add")) )
								{

									request.setAttribute("categoryID", request.getParameter("categoryID"));
									request.setAttribute("productID", request.getParameter("productID"));
									this.sc.getRequestDispatcher("/Catalogue.jspx").forward(request, response);
								}
								else
								{
									this.sc.getRequestDispatcher("/ShoppingCart.jspx").forward(request, response);
								}
								return;
							}
						} catch (NumberFormatException e) {
							request.setAttribute("error",
									"Invalid quantity for " + 
									product.getProductName() );
							
							if (isArgumentValid(request.getParameter("add")) )
							{

								request.setAttribute("categoryID", request.getParameter("categoryID"));
								request.setAttribute("productID", request.getParameter("productID"));
								this.sc.getRequestDispatcher("/Catalogue.jspx").forward(request, response);
							}
							else
							{
								this.sc.getRequestDispatcher("/ShoppingCart.jspx").forward(request, response);
							}
							return;
						}
					}
				}
				
				
			}
			else
			{
				request.setAttribute("error",
						"Invalid number of quantities" );
				
				if (isArgumentValid(request.getParameter("add")) )
				{
					request.setAttribute("categoryID", request.getParameter("categoryID"));
					request.setAttribute("productID", request.getParameter("productID"));

					this.sc.getRequestDispatcher("/Catalogue.jspx").forward(request, response);
				}
				else
				{
					this.sc.getRequestDispatcher("/ShoppingCart.jspx").forward(request, response);
				}
				
				return;
			}
			
		}

		chain.doFilter(request, response);
		
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		sc = fConfig.getServletContext();
		catalog = (Catalogue)fConfig.getServletContext().getAttribute(
			"catalog");
	}
	
	protected boolean isArgumentValid(String arg)
    {
    	if (isArgumentPresent(arg) && !(arg.trim().isEmpty()) )
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    protected boolean isArgumentPresent(String arg)
    {
    	if (arg != null)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

}
