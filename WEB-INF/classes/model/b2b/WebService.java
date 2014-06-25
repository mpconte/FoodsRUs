package model.b2b;

import org.apache.axis.client.Service;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;
import java.net.URL;
import java.net.MalformedURLException;

public class WebService 
{
	private String TNS;	
	private URL WSDL_URL;
	private String name;	
	private String serviceName;
	private String portName;
	private Service service;
	private Call quoteCall;
	private Call orderCall;
	
	
	public WebService(String tns, String Name, String ServiceName, String PortName) throws ServiceException, MalformedURLException
	{
		TNS = tns;
		WSDL_URL = new URL(tns + "?wsdl");
		name = Name;
		serviceName = ServiceName;
		portName = PortName;
		
		service = new Service(WSDL_URL, new QName(TNS, serviceName));
		service.setMaintainSession(true);
		
		quoteCall = service.createCall(new QName(portName), "quote");
		orderCall = service.createCall(new QName(portName), "order");
	}
	
	public String getName()
	{
		return name;
	}
	
	public Call getQuoteCall()
	{
		return quoteCall;
	}
	
	public Call getOrderCall()
	{
		return orderCall;
	}
}
