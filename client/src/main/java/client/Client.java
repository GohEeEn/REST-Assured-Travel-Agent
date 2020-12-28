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
	
	public static final String newArgs = "http://localhost:8080/bookings";
	
//	public static void main(String[] args) {
		
	public static void bookingAdventure(ClientBooking[] clients) {	
		Flight[] flights = new Flight[10];
		RestTemplate restTemplate = new RestTemplate();
		for (ClientBooking booking : clients){
			HttpEntity<ClientBooking> request = new HttpEntity<>(booking);
			
//			flights = restTemplate.postForObject(args[0],request,Flight[].class);
			flights = restTemplate.postForObject(newArgs,request,Flight[].class);
			
			System.out.println(flights[0]);

			for (int i=0; i < flights.length; i++){
				Flight f = flights[i];
				if (f != null){
					System.out.println("City of Destination is: " + f.getCityOfDestination());
					System.out.println("Price of flight is: " + f.getPrice());
				}
				
			}
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
//	public static final ClientBooking[] clients = {
//		new ClientBooking("Donald Trump", "Dublin", "Ireland", "Paris", "France", false, "2021-01-09", "2021-01-17", "EUR")	
//	};
}
