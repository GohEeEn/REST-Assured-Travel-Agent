package client;

import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.web.client.RestTemplate; 
import org.springframework.http.HttpEntity;
 
import service.core.FlightRequest;
import service.core.Flight;
import service.core.Hotel;
import service.core.ActivityRequest;
import service.core.ClientRequest;
import service.core.ClientResponse;
import service.core.HotelRequest;
import service.core.TravelPackage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class Client {
	
/	// public static final String newArgs = "http://192.168.49.2:31500/bookings";
	public static final String newArgs = "http://localhost:8081/bookings";
	public static int referenceNumber = 0;

	final static String locale = "en-GB";     // need to call Skycanner API
	
	// public static void main(String[] args) {

		public static TravelPackage sendBookingToTravelAgent(FlightRequest flightRequest, HotelRequest hotelRequest, ActivityRequest activityRequest) {	
 		
			    	Flight[] flights = new Flight[10];
			 		RestTemplate restTemplate = new RestTemplate();
			 		ClientRequest clientRequest = new ClientRequest();
			 		clientRequest.setFlightRequest(flightRequest);
					 clientRequest.setHotelRequest(hotelRequest);
					 clientRequest.setActivityRequest(activityRequest);
			 //		Uncomment this after testing my Code to get same result as Barry
				/*
				 * referenceNumber++; clientRequest.setReferenceNumber(referenceNumber);
				 */
				System.out.println("TEST1");
			
							   HttpEntity<ClientRequest> request = new HttpEntity<>(clientRequest);
							   System.out.println("TEST2");
							  
							   TravelPackage travelPackage = new TravelPackage();
							   System.out.println("TEST3");
							   travelPackage = restTemplate.postForObject(newArgs,request,TravelPackage.class);
							   System.out.println("TEST4");

							//    System.out.println(travelPackage.getActivities());
			 			// flights = restTemplate.postForObject("http://localhost:8081/bookings",request,Flight[].class);
			
							  
			                   Hotel [] hotels2 = travelPackage.getHotels();
			                   System.out.println(hotels2);
							   Flight [] flights2 = travelPackage.getFlights();
							   System.out.println("SIZE OF FLIGHST ARRAY is = "+flights2.length);
			                   System.out.println(flights2);
			 			// for (int i=0; i < 1; i++){
			            //              Flight f = flights2[i];
			            //              Hotel h = hotels2[i];
			 			// 	if (f != null){
			 			// 		System.out.println("City of Destination is: " + f.getCityOfDestination());
			 			// 		System.out.println("Price of flight is: " + f.getPrice());
			            //              }	
			            //              System.out.println("\n");
			            //              if (h != null){
			            //                    System.out.println("Description of hotel is: " + h.getDescription());
			 			// 		System.out.println("Price of hotel is: " + h.getPrice());
			            //              }
			            //              System.out.println("\n");
						//  }
						 return travelPackage;
						}

	//Send the clientResponse to the travel agent
	public static void sendBookinChoicesToTravelTragent(ClientResponse cr){

	}
}
