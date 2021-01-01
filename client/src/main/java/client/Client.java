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
 import service.core.ClientRequest;
 import service.core.HotelRequest;
 import service.core.TravelPackage;

 public class Client {
	
 	public static final String newArgs = "http://localhost:8081/bookings";
 	public static int referenceNumber = 0;
	
 //	public static void main(String[] args) {
		
    public static void sendBookingToTravelAgent(FlightRequest flightRequest, HotelRequest hotelRequest) {	
 		
    	Flight[] flights = new Flight[10];
 		RestTemplate restTemplate = new RestTemplate();

// 		FlightRequest flightRequest = new FlightRequest("Donald Trump", "Dublin", "Ireland", "Paris", "France", false,
// 				 "2021-01-09", "2021-01-17", "EUR");
// 		HotelRequest hotelRequest = new HotelRequest();
// 		hotelRequest.setCityCode("PAR");
// 		hotelRequest.setNumberOfGuests(1);
 		ClientRequest clientRequest = new ClientRequest();
 		clientRequest.setFlightRequest(flightRequest);
 		clientRequest.setHotelRequest(hotelRequest);
 		
 //		Uncomment this after testing my Code to get same result as Barry
	/*
	 * referenceNumber++; clientRequest.setReferenceNumber(referenceNumber);
	 */


                   HttpEntity<ClientRequest> request = new HttpEntity<>(clientRequest);
                  
                   TravelPackage travelPackage = new TravelPackage();
                   travelPackage = restTemplate.postForObject(newArgs,request,TravelPackage.class);
		
 			// flights = restTemplate.postForObject("http://localhost:8081/bookings",request,Flight[].class);

                  
                   Hotel [] hotels2 = travelPackage.getHotels();
                   System.out.println(hotels2);
                   Flight [] flights2 = travelPackage.getFlights();
                   System.out.println(flights2);
 			for (int i=0; i < 1; i++){
                         Flight f = flights2[i];
                         Hotel h = hotels2[i];
 				if (f != null){
 					System.out.println("City of Destination is: " + f.getCityOfDestination());
 					System.out.println("Price of flight is: " + f.getPrice());
                         }	
                         System.out.println("\n");
                         if (h != null){
                               System.out.println("Description of hotel is: " + h.getDescription());
 					System.out.println("Price of hotel is: " + h.getPrice());
                         }
                         System.out.println("\n");
 			}

// 		/**
// 		 *  Barry's testing code below
// 		 *  */ 
// 		// HotelRequest h = clientRequest.getHotelRequest();
// 		// h.setNumberOfGuests(14);
// 		// clientRequest.setHotelRequest(h);
// 		//  HttpEntity<ClientRequest> request2 = new HttpEntity<>(clientRequest);
// 		//  restTemplate.put(newArgs+"/1",request2);
// 		//   HttpEntity<Integer> request2 = new HttpEntity<>(767);
// 		//   restTemplate.put(newArgs+"/1",request2);
		 
		 
 	}
 }
   
