// package client;

// import java.text.NumberFormat;
// import java.util.List;
// import java.util.ArrayList;
// import java.util.LinkedList;

// import org.springframework.web.client.RestTemplate; 
// import org.springframework.http.HttpEntity;
 
// import service.core.ClientBooking;
// import service.core.Flight;
// import service.core.ClientRequest;
// import service.core.HotelRequest;

// public class ClientB {
	
// 	public static final String newArgs = "http://localhost:8081/bookings";
// 	public static int referenceNumber = 0;
	
// 	public static void main(String[] args) {
		
// 	// public static void bookingAdventure(ClientBooking[] clients) {	
// 		Flight[] flights = new Flight[10];
// 		RestTemplate restTemplate = new RestTemplate();

// 		ClientBooking clientBooking = new ClientBooking("Donald Trump", "Dublin", "Ireland", "Paris", "France", false,
// 				 "2021-01-09", "2021-01-17", "EUR");
// 		HotelRequest hotelRequest = new HotelRequest();
// 		hotelRequest.setCityCode("PAR");
// 		hotelRequest.setNumberOfGuests(1);
// 		int[] stars = {5,4,3};
// 		hotelRequest.setNumberOfStarsRequiredForHotel(stars);
// 		ClientRequest clientRequest = new ClientRequest();
// 		clientRequest.setClientBooking(clientBooking);
// 		clientRequest.setHotelRequest(hotelRequest);
// 		referenceNumber++;
// 		clientRequest.setReferenceNumber(referenceNumber);


// 			HttpEntity<ClientRequest> request = new HttpEntity<>(clientRequest);
			
// 			flights = restTemplate.postForObject(newArgs,request,Flight[].class);
// 			// flights = restTemplate.postForObject("http://localhost:8081/bookings",request,Flight[].class);


// 			for (int i=0; i < flights.length; i++){
// 				Flight f = flights[i];
// 				if (f != null){
// 					System.out.println("City of Destination is: " + f.getCityOfDestination());
// 					System.out.println("Price of flight is: " + f.getPrice());
// 				}	
// 			}

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
		 
		 
// 	} 
   
// 	// /**
// 	//  * Display the client info nicely.
// 	//  * 
// 	//  * @param info
// 	//  */
// 	// public static void displayProfile(ClientInfo info) {
// 	// 	System.out.println("|=================================================================================================================|");
// 	// 	System.out.println("|                                     |                                     |                                     |");
// 	// 	System.out.println(
// 	// 			"| Name: " + String.format("%1$-29s", info.getName()) + 
// 	// 			" | Gender: " + String.format("%1$-27s", (info.getGender()==ClientInfo.MALE?"Male":"Female")) +
// 	// 			" | Age: " + String.format("%1$-30s", info.getAge())+" |");
// 	// 	System.out.println(
// 	// 			"| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) + 
// 	// 			" | No Claims: " + String.format("%1$-24s", info.getNoClaims() +" years") +
// 	// 			" | Penalty Points: " + String.format("%1$-19s", info.getPoints())+" |");
// 	// 	System.out.println("|                                     |                                     |                                     |");
// 	// 	System.out.println("|=================================================================================================================|");
// 	// }

// 	// /**
// 	//  * Display a quotation nicely - note that the assumption is that the quotation will follow
// 	//  * immediately after the profile (so the top of the quotation box is missing).
// 	//  * 
// 	//  * @param quotation
// 	//  */
// 	// public static void displayQuotation(Quotation quotation) {
// 	// 	System.out.println(
// 	// 			"| Company: " + String.format("%1$-26s", quotation.getCompany()) + 
// 	// 			" | Reference: " + String.format("%1$-24s", quotation.getReference()) +
// 	// 			" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice()))+" |");
// 	// 	System.out.println("|=================================================================================================================|");
// 	// }
	
// 	/**
// 	 * Test Data
// 	 */
	
// }
