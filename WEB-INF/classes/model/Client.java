package model;

import model.data.bindings.order.Address;
import model.data.bindings.order.Customer;




// Client
public class Client extends Customer
{
	private String pin;
	
	public Client(String name, String account, 
			Address address, String pin)
	{
		super.setName(name);
		super.setAccount(account);
		super.setAddress(address);
		
		this.pin = pin;
	}
	
	public String getPin()
	{
		return pin;
	}
}
