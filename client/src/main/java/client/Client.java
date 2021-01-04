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
import service.core.Booking;
import service.core.Attraction;
import service.core.ActivityRequest;
import service.core.ActivityItem;
import service.core.AttractionRequest;
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
	
	// Use ip address found through "minikube ip" in format"IP:31500" to access kubernetes travel_agent
	public static final String argsRequest = "http://192.168.49.2:31500/travelagent/travelpackagerequests";
	public static final String argsResponse = "http://192.168.49.2:31500/travelagent/bookings";

	// public static final String argsRequest = "http://localhost:8081/travelagent/travelpackagerequests";
	// public static final String argsResponse = "http://localhost:8081/travelagent/bookings";

	public static int referenceNumber = 0;

	final static String locale = "en-GB";     // need to call Skycanner API
	
	// public static void main(String[] args) {

		public static TravelPackage sendBookingToTravelAgent(FlightRequest flightRequest, HotelRequest hotelRequest, ActivityRequest activityRequest, AttractionRequest attractionRequest) {	
 		
			    	Flight[] flights = new Flight[10];
			 		RestTemplate restTemplate = new RestTemplate();
			 		ClientRequest clientRequest = new ClientRequest();
			 		clientRequest.setFlightRequest(flightRequest);
					 clientRequest.setHotelRequest(hotelRequest);
					 clientRequest.setActivityRequest(activityRequest);
					 clientRequest.setAttractionRequest(attractionRequest);
			 //		Uncomment this after testing my Code to get same result as Barry
				/*
				 * referenceNumber++; clientRequest.setReferenceNumber(referenceNumber);
				 */
				System.out.println("TEST1");
			
							   HttpEntity<ClientRequest> request = new HttpEntity<>(clientRequest);
							   System.out.println("TEST2");
							  
							   TravelPackage travelPackage = new TravelPackage();
							   System.out.println("TEST3");
							   travelPackage = restTemplate.postForObject(argsRequest,request,TravelPackage.class);
							   System.out.println("TEST4");

							   ActivityItem [] activities2 = travelPackage.getActivities();
							   ActivityItem a = activities2[0];
							   System.out.println("\n");
								if (a != null){
									System.out.println("Description of activity is: " + a.getDescription());
									System.out.println("Booking link of activity is: " + a.getBookingLink());
								}
								Attraction [] attractions2 = travelPackage.getAttractions();
								Attraction at = attractions2[0];
								if (at != null){
									System.out.println("Category of attraction is: " + at.getCategory());
									System.out.println("Name of attraction is: " + at.getName());
								}

							//    System.out.println(travelPackage.getActivities());
			 			// flights = restTemplate.postForObject("http://localhost:8081/bookings",request,Flight[].class);
			
							  //106, 111, 121-125
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
	public static void sendBookingChoicesToTravelAgent(ClientResponse clientResponse){

		HttpEntity<ClientResponse> requestClientResponse = new HttpEntity<>(clientResponse);
		Booking booking = new Booking();
		RestTemplate restTemplate = new RestTemplate();
            booking = restTemplate.postForObject(argsResponse,requestClientResponse,Booking.class);

		/**
		 * TESTING code for clientResponse sent to travel agent
		 */
            System.out.println("\nAirline: "+booking.getFlight().getAirline());
            System.out.println("Hotel Address: "+booking.getHotel().getAddress());
		System.out.println("Booking ref Num: "+booking.getReferenceNumber());
		System.out.println("Activity 1: "+booking.getActivities()[0]);
            System.out.println("Attraction 1: "+booking.getAttractions()[0]);
                  
	}
}
