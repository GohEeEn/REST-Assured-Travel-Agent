package client;

import org.apache.commons.lang3.SystemUtils;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.web.client.RestTemplate; 
import org.springframework.http.HttpEntity;
 
import service.core.ClientBooking;
import service.core.Flight;
import service.core.ClientRequest;
import service.core.HotelRequest;

public class Client {
	
	public static final String newArgs = "http://localhost:8081/bookings";

	public static int referenceNumber = 0;
//	public static final String winArgs = "http://localhost:192.168.99.100:8081/bookings";
	public static final String winArgs = "http://localhost:192.168.99.100:8081/bookings";
	
	public static void main(String[] args) {
		
//	 public static void bookingAdventure(ClientBooking[] clients) {	
		Flight[] flights = new Flight[10];
		RestTemplate restTemplate = new RestTemplate();

		ClientBooking clientBooking = new ClientBooking("Donald Trump", "Dublin", "Ireland", "Paris", "France", false,
				 "2021-01-09", "2021-01-17", "EUR");
		HotelRequest hotelRequest = new HotelRequest();
		hotelRequest.setCityCode("PAR");
		hotelRequest.setNumberOfGuests(1);
		int[] stars = {5,4,3};
		hotelRequest.setNumberOfStarsRequiredForHotel(stars);
		ClientRequest clientRequest = new ClientRequest();
		clientRequest.setClientBooking(clientBooking);
		clientRequest.setHotelRequest(hotelRequest);
		referenceNumber++;
		clientRequest.setReferenceNumber(referenceNumber);

			HttpEntity<ClientRequest> request = new HttpEntity<>(clientRequest);
			
			// if(SystemUtils.IS_OS_WINDOWS){
			// 	flights = restTemplate.postForObject(winArgs,request,Flight[].class);
			// }
			// else{
				flights = restTemplate.postForObject(newArgs,request,Flight[].class);

			// }
			// flights = restTemplate.postForObject("http://localhost:8081/bookings",request,Flight[].class);

			for (int i=0; i < flights.length; i++){
				Flight f = flights[i];
				if (f != null){
					System.out.println("City of Destination is: " + f.getCityOfDestination());
					System.out.println("Price of flight is: " + f.getPrice());
				}	
			}

		/**
		 *  Barry's testing code below
		 *  */ 
		// HotelRequest h = clientRequest.getHotelRequest();
		// h.setNumberOfGuests(14);
		// clientRequest.setHotelRequest(h);
		 HttpEntity<ClientRequest> request2 = new HttpEntity<>(clientRequest);
		 restTemplate.put(newArgs+"/1",request2);
		//   HttpEntity<Integer> request2 = new HttpEntity<>(767);
		//   restTemplate.put(newArgs+"/1",request2);
		 
		/*
		 * HttpEntity<String> request = new HttpEntity<>("Johnson");
		 * 
		 * restTemplate.put(args[0],request);
		 */

	} 
   
	
	/**
	 * Test Data
	 */
	

}

