package client;

import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.web.client.RestTemplate; 
import org.springframework.http.HttpEntity;
 
import service.core.ClientBooking;
import service.core.Flight;

public class Client {
	
	public static void main(String[] args) {
		
		// RestTemplate restTemplate = new RestTemplate();
		// String s = "Stockholm";
		// HttpEntity<String> request = new HttpEntity<>(s);
		// ArrayList<String> arrayStrings = restTemplate.postForObject(args[0],request,ArrayList.class);  // ClientApplicati.on received from calling POST method in broker
		
		// for (String str : arrayStrings){
		// 	System.out.println(str);
		// }
		
		ArrayList<Flight> flights = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		for (ClientBooking booking : clients){
			HttpEntity<ClientBooking> request = new HttpEntity<>(booking);
			flights = restTemplate.postForObject(args[0],request,ArrayList.class);
		}
		
		for (Flight flight : flights ){
			System.out.println(flight.getCityOfOrigin());
			System.out.println(flight.getCityOfDestination());
			System.out.println(flight.getPrice());
		}
	} 
	     
	// /**
	//  * Display the client info nicely.
	//  * 
	//  * @param info
	//  */
	// public static void displayProfile(ClientInfo info) {
	// 	System.out.println("|=================================================================================================================|");
	// 	System.out.println("|                                     |                                     |                                     |");
	// 	System.out.println(
	// 			"| Name: " + String.format("%1$-29s", info.getName()) + 
	// 			" | Gender: " + String.format("%1$-27s", (info.getGender()==ClientInfo.MALE?"Male":"Female")) +
	// 			" | Age: " + String.format("%1$-30s", info.getAge())+" |");
	// 	System.out.println(
	// 			"| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) + 
	// 			" | No Claims: " + String.format("%1$-24s", info.getNoClaims() +" years") +
	// 			" | Penalty Points: " + String.format("%1$-19s", info.getPoints())+" |");
	// 	System.out.println("|                                     |                                     |                                     |");
	// 	System.out.println("|=================================================================================================================|");
	// }

	// /**
	//  * Display a quotation nicely - note that the assumption is that the quotation will follow
	//  * immediately after the profile (so the top of the quotation box is missing).
	//  * 
	//  * @param quotation
	//  */
	// public static void displayQuotation(Quotation quotation) {
	// 	System.out.println(
	// 			"| Company: " + String.format("%1$-26s", quotation.getCompany()) + 
	// 			" | Reference: " + String.format("%1$-24s", quotation.getReference()) +
	// 			" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice()))+" |");
	// 	System.out.println("|=================================================================================================================|");
	// }
	
	/**
	 * Test Data
	 */
	public static final ClientBooking[] clients = {
		new ClientBooking("Donald Trump", "Dublin", "IE", "Paris", "FR", true, "2020-12-17", null, "EUR")	
	};
}
