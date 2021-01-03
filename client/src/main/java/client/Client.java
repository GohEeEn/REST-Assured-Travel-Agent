<<<<<<< HEAD
//  package client;

//  import java.text.NumberFormat;
//  import java.util.List;
//  import java.util.ArrayList;
//  import java.util.LinkedList;

//  import org.springframework.web.client.RestTemplate; 
//  import org.springframework.http.HttpEntity;
 
//  import service.core.FlightRequest;
//  import service.core.Flight;
//  import service.core.Hotel;
//  import service.core.ClientRequest;
//  import service.core.HotelRequest;
//  import service.core.TravelPackage;

//  public class Client {
	
// 	public static final String newArgs = "http://localhost:8081/bookings";

// 	//Kubernetes IP
//  	// public static final String newArgs = "http://10.99.26.248:80/bookings";
//  	public static int referenceNumber = 0;
	
//  //	public static void main(String[] args) {
		
//     public static TravelPackage sendBookingToTravelAgent(FlightRequest flightRequest, HotelRequest hotelRequest) {	
 		
//     	Flight[] flights = new Flight[10];
//  		RestTemplate restTemplate = new RestTemplate();

// // 		FlightRequest flightRequest = new FlightRequest("Donald Trump", "Dublin", "Ireland", "Paris", "France", false,
// // 				 "2021-01-09", "2021-01-17", "EUR");
// // 		HotelRequest hotelRequest = new HotelRequest();
// // 		hotelRequest.setCityCode("PAR");
// // 		hotelRequest.setNumberOfGuests(1);
//  		ClientRequest clientRequest = new ClientRequest();
//  		clientRequest.setFlightRequest(flightRequest);
//  		clientRequest.setHotelRequest(hotelRequest);
 		
//  //		Uncomment this after testing my Code to get same result as Barry
// 	/*
// 	 * referenceNumber++; clientRequest.setReferenceNumber(referenceNumber);
// 	 */
=======
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
	
	public static final String newArgs = "http://localhost:8081/bookings";
	public static int referenceNumber = 0;
	final static String locale = "en-GB";     // need to call Skycanner API
	
	// public static void main(String[] args) {

		public static TravelPackage sendBookingToTravelAgent(FlightRequest flightRequest, HotelRequest hotelRequest) {	
 		
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
					 
					// ArrayList<String> originAirportIDs = new ArrayList();          // Holds all airports for the given origin city
					// ArrayList<String> destinationAirportIDs = new ArrayList();      //Holds all airports for the given destination city

					// flightRequest.setCountryOfOriginCode(getListMarkets(flightRequest.getCountryOfOrigin()));
					// System.out.println(flightRequest.getCountryOfOriginCode());
					// flightRequest.setCountryOfDestinationCode(getListMarkets(flightRequest.getCountryOfDestination()));
					// System.out.println(flightRequest.getCountryOfDestinationCode());

					// originAirportIDs = getListPlaces(flightRequest.getCityOfOrigin(), flightRequest.getCountryOfOrigin(),   // Find airport IDs for origin
					// 			flightRequest.getCountryOfOriginCode(), flightRequest.getCurrency());
					// String [] originAirportIDsArray = convertAirportIDsListToAirportIDsArray(originAirportIDs);    // converts list to array
					
					// destinationAirportIDs = getListPlaces(flightRequest.getCityOfDestination(), flightRequest.getCountryOfDestination(),   // Find airport IDs for destination
					// 			flightRequest.getCountryOfDestinationCode(), flightRequest.getCurrency()); 
					// String [] destAirportIDsArray = convertAirportIDsListToAirportIDsArray(destinationAirportIDs);    // converts list to array
					
					// System.out.println("\n ORIGIN AIRPORT IDS: "+originAirportIDsArray+"\n");
					// for(String s : originAirportIDsArray){
					// 	System.out.println(s);
					// }
					// System.out.println("\n ORIGIN AIRPORT IDS: "+destAirportIDsArray+"\n");
					// for(String s : destAirportIDsArray){
					// 	System.out.println(s);
					// }
					// flightRequest.setOriginAirortIDs(originAirportIDsArray);
					// flightRequest.setDestAirortIDs(destAirportIDsArray);
			
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
					
			 			// flights = restTemplate.postForObject("http://localhost:8081/bookings",request,Flight[].class);
			
							  
			                   Hotel [] hotels2 = travelPackage.getHotels();
			                   System.out.println(hotels2);
							   Flight [] flights2 = travelPackage.getFlights();
							   System.out.println("SIZE OF FLIGHST ARRAY is = "+flights2.length);
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
						 return travelPackage;
						}
		
	// public static void bookingAdventure(ClientRequest[] clients) {	
	// 	Flight[] flights = new Flight[10];
	// 	RestTemplate restTemplate = new RestTemplate();

	// 	FlightRequest flightRequest = new FlightRequest("Donald Trump", "Dublin", "Ireland", "Paris", "France", false,
	// 			 "2021-01-17", "2021-01-09", "EUR");
		
	// 	flightRequest.setCountryOfOriginCode(getListMarkets("Ireland"));
	// 	flightRequest.setCountryOfDestinationCode(getListMarkets("France"));

	// 	System.out.println("DEST CODE: "+getListMarkets("France"));

	// 	HotelRequest hotelRequest = new HotelRequest();
	// 	hotelRequest.setCityCode("PAR");
	// 	hotelRequest.setNumberOfGuests(2);
	// 	ClientRequest clientRequest = new ClientRequest();
	// 	clientRequest.setFlightRequest(flightRequest);
	// 	clientRequest.setHotelRequest(hotelRequest);

		
	// 	ArrayList<String> originAirportIDs = new ArrayList();          // Holds all airports for the given origin city
	// 	ArrayList<String> destinationAirportIDs = new ArrayList();      //Holds all airports for the given destination city

>>>>>>> 3efc7b1a9722e247bee71b8436ce6aa6fc4865d4

	// 	originAirportIDs = getListPlaces(flightRequest.getCityOfOrigin(), flightRequest.getCountryOfOrigin(),   // Find airport IDs for origin
	// 				flightRequest.getCountryOfOriginCode(), flightRequest.getCurrency());
	// 	String [] originAirportIDsArray = convertAirportIDsListToAirportIDsArray(originAirportIDs);    // converts list to array
		
	// 	destinationAirportIDs = getListPlaces(flightRequest.getCityOfDestination(), flightRequest.getCountryOfDestination(),   // Find airport IDs for destination
	// 				flightRequest.getCountryOfDestinationCode(), flightRequest.getCurrency()); 
	// 	String [] destAirportIDsArray = convertAirportIDsListToAirportIDsArray(destinationAirportIDs);    // converts list to array
		
	// 	System.out.println("\n ORIGIN AIRPORT IDS: "+originAirportIDsArray+"\n");
	// 	for(String s : originAirportIDsArray){
	// 		System.out.println(s);
	// 	}
	// 	System.out.println("\n ORIGIN AIRPORT IDS: "+destAirportIDsArray+"\n");
	// 	for(String s : destAirportIDsArray){
	// 		System.out.println(s);
	// 	}

	// 	flightRequest.setOriginAirortIDs(originAirportIDsArray);
	// 	flightRequest.setDestAirortIDs(destAirportIDsArray);;

<<<<<<< HEAD
//                    HttpEntity<ClientRequest> request = new HttpEntity<>(clientRequest);
                  
//                    TravelPackage travelPackage = new TravelPackage();
//                    travelPackage = restTemplate.postForObject(newArgs,request,TravelPackage.class);
		
//  			// flights = restTemplate.postForObject("http://localhost:8081/bookings",request,Flight[].class);

                  
//                    Hotel [] hotels2 = travelPackage.getHotels();
//                    System.out.println(hotels2);
// 				   Flight [] flights2 = travelPackage.getFlights();
// 				   System.out.println("SIZE OF FLIGHST ARRAY is = "+flights2.length);
//                    System.out.println(flights2);
//  			for (int i=0; i < 1; i++){
//                          Flight f = flights2[i];
//                          Hotel h = hotels2[i];
//  				if (f != null){
//  					System.out.println("City of Destination is: " + f.getCityOfDestination());
//  					System.out.println("Price of flight is: " + f.getPrice());
//                          }	
//                          System.out.println("\n");
//                          if (h != null){
//                                System.out.println("Description of hotel is: " + h.getDescription());
//  					System.out.println("Price of hotel is: " + h.getPrice());
//                          }
//                          System.out.println("\n");
// 			 }
			 
// 			 return travelPackage;
=======

    //               HttpEntity<ClientRequest> request = new HttpEntity<>(clientRequest);
                  
	// 		TravelPackage travelPackage = new TravelPackage();
	// 		System.out.println("\n Country Code: "+getListMarkets("Ireland"));
    //               travelPackage = restTemplate.postForObject(newArgs,request,TravelPackage.class);
		
		

                  
            //        Hotel [] hotels2 = travelPackage.getHotels();
            //        System.out.println(hotels2.toString());
			// 	   Flight [] flights2 = travelPackage.getFlights();
			// 	   System.out.println("SIZE OF FLIGHST ARRAY is = "+flights2.length);
            //        System.out.println(flights2);
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
			 
			//  return travelPackage;
>>>>>>> 3efc7b1a9722e247bee71b8436ce6aa6fc4865d4

// // 		/**
// // 		 *  Barry's testing code below
// // 		 *  */ 
// // 		// HotelRequest h = clientRequest.getHotelRequest();
// // 		// h.setNumberOfGuests(14);
// // 		// clientRequest.setHotelRequest(h);
// // 		//  HttpEntity<ClientRequest> request2 = new HttpEntity<>(clientRequest);
// // 		//  restTemplate.put(newArgs+"/1",request2);
// // 		//   HttpEntity<Integer> request2 = new HttpEntity<>(767);
// // 		//   restTemplate.put(newArgs+"/1",request2);
		 
<<<<<<< HEAD
		 
//  	}
//  }
=======
    //               Hotel [] hotels2 = travelPackage.getHotels();
    //               Flight [] flights2 = travelPackage.getFlights();
	// 		for (int i=0; i < 1; i++){
    //                     Flight f = flights2[i];
    //                     Hotel h = hotels2[i];
	// 			if (f != null){
	// 				System.out.println("City of Destination is: " + f.getCityOfDestination());
	// 				System.out.println("Price of flight is: " + f.getPrice());
    //                     }	
    //                     System.out.println("\n");
    //                     if (h != null){
    //                           System.out.println("Description of hotel is: " + h.getDescription());
	// 				System.out.println("Price of hotel is: " + h.getPrice());
    //                     }
    //                     System.out.println("\n");
	// 		}

	// 	/**
	// 	 * Testing code for DELETE below
	// 	 */
	// 	// restTemplate.delete("http://localhost:8081/bookings/1");


	// 	/**
	// 	 *  Testing code for PUT below
	// 	 *  */ 
	// 	// HotelRequest h = clientRequest.getHotelRequest();
	// 	// h.setNumberOfGuests(14);
	// 	// clientRequest.setHotelRequest(h);
	// 	//  HttpEntity<ClientRequest> request2 = new HttpEntity<>(clientRequest);
	// 	// restTemplate.put(newArgs+"/1",request2);

	// 	 /**
	// 	 *  Testing code for PATCH below
	// 	 *  */ 
	// 	//  HotelRequest h2 = clientRequest.getHotelRequest();
	// 	// h2.setNumberOfGuests(26);
	// 	// clientRequest.setHotelRequest(h2);
	// 	//  HttpEntity<ClientRequest> request3 = new HttpEntity<>(clientRequest);
	// 	//  TravelPackage travelPackage2 = new TravelPackage();
	// 	//  travelPackage2 = restTemplate.patchForObject(newArgs+"/1",request3,TravelPackage.class);
	// 	// Flight [] testFly = new Flight[10];
	// 	// testFly = travelPackage2.getFlights();
	// 	// System.out.println("\n "+testFly[0].getAirline()+"\n");		

		
	// } 

>>>>>>> 3efc7b1a9722e247bee71b8436ce6aa6fc4865d4
   
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
	
}
