package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import model.data.bindings.order.Address;

public class ClientBook {
	
	private Map<String, Client> clients;

	public ClientBook(File clientFile) throws NumberFormatException, IOException {
		
		clients = new HashMap<String, Client>();
		BufferedReader clientReader = 
			new BufferedReader(new FileReader(clientFile));

		String line = null;
		
		while ((line = clientReader.readLine()) != null
			&& !line.equals(""))
		{
			StringTokenizer st = new StringTokenizer(line,"\t");
			
			String account = st.nextToken();
			String name = st.nextToken();
			
			List<String> street = new ArrayList<String>();
			street.add(st.nextToken());
			
			String city = st.nextToken();
			String province = st.nextToken();
			String postal = st.nextToken();
			String pin = st.nextToken();
			
			Address address = new Address();
			address.setStreet(street);
			address.setCity(city);
			address.setProvince(province);
			address.setPostal(postal);
			
			Client customer = new Client(name, account, address, pin);

			clients.put(account, customer);
		}

	}
	
	public Client getClient(String account, String pin)
	{
		Client client = clients.get(account);
		
		if ((client != null) && (client.getPin()).equals(pin))
		{
			return client;
		}
		else
		{
			return null;
		}
	}
	
	public boolean isRealClient(String account)
	{
		return clients.get(account) != null;
	}
}
