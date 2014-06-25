package control.listeners;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBException;

import model.Catalogue;
import model.ClientBook;

import org.xml.sax.SAXException;


/**
 * Application Lifecycle Listener implementation class ApplicationInitializationListener
 *
 */
public class ContextListener implements ServletContextListener {

	/**
     * Default constructor. 
     */
    public ContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
      
    	Catalogue catalog;
    	ClientBook clientBook;
    	
		try {
			
			catalog = new Catalogue(
					new File(arg0.getServletContext().getRealPath(
							"/WEB-INF/catalog.data/Catalog.xsd")),
					new File(arg0.getServletContext().getRealPath(
							"/WEB-INF/catalog.data/Catalog.xml"))); 

			clientBook = new ClientBook(
					new File(arg0.getServletContext().getRealPath("/WEB-INF/client.data/accounts.txt")));
			
			arg0.getServletContext().setAttribute("catalog", catalog);
			arg0.getServletContext().setAttribute("clientBook", clientBook);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
