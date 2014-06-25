package control.servlets;

import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class GenericServlet
 */
public abstract class GenericServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1242837583275304867L;

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
