package service.travel_agent;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;


import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;

import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.LinkedList;

import service.core.FlightRequest;
import service.core.Flight;

import service.core.TravelPackage;
import service.core.ClientRequest;
import service.core.HotelRequest;
import service.core.Hotel;
import service.core.Booking;
import service.core.ClientResponse;
import service.core.ClientChoice;
import service.core.ReplaceBooking;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
@RestController
public class TravelAgentService {
	private final MongoRepository mongoRepository;

    @Autowired
    public TravelAgentService(MongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }


	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	public static LinkedList<String> URIs = new LinkedList();        // Holds our URI's that will be passed as an argument when running broker

	private Map<Integer, Booking> clientBookings = new TreeMap();
	private static int travelPackageRequestReferenceNumber = 0;
	private static int clientBookingReferenceNumber = 0;
	private static int referenceNumber = 0;

	/**
	 * POST REQUEST: handles requests from client for Hotels, Flights, Activities and Attractions
	 * 
	 * @param clientRequest
	 * @return
	 * @throws URISyntaxException
	 */
	@RequestMapping(value="/bookings",method=RequestMethod.POST)
	public ResponseEntity<TravelPackage> getFlightInfo(@RequestBody ClientRequest clientRequest) throws URISyntaxException {
		System.out.println("TEST5");
		referenceNumber++;
		/**
		 * POST request to Flight service for a FlightRequest which will return a list of available flights
		 */
		Flight[] flights = new Flight[50];	
		HttpEntity<FlightRequest> request = new HttpEntity<>(clientRequest.getFlightRequest());
		flights = restTemplate.postForObject("http://flights-service/flightservice/flightrequests",request, Flight[].class);

		/**
		 * POST request to Hotel Service for a HotelRequest which will return a list of available hotels
		 */

		Hotel [] hotels = new Hotel[50];
		HttpEntity<HotelRequest> request2 = new HttpEntity<>(clientRequest.getHotelRequest());
		hotels = restTemplate.postForObject("http://hotels-service/hotelservice/hotelrequests",request2, Hotel[].class);
		System.out.println("TESTING TA");
		// System.out.println("\n"+hotels[0].getPrice()+"\n");

		/**
		 * POST request to Activities Service
		 * TODO (Barry & Sean): Insert code below
		 */

		// System.out.println("CALLING ACTIVITIESA");
		// ActivityItem[] activities = new ActivityItem[50];
		// HttpEntity<ActivityRequest> activityRequest = new HttpEntity<>(clientRequest.getActivityRequest());
		// System.out.println("LINE 116");
		// activities = restTemplate.postForObject("http://activities-service/activities", activityRequest, ActivityItem[].class);
		// System.out.println("117");
		// System.out.println("\n"+activities[0].getDescription()+"\n");



		// /**
		//  * POST request to Attractions Service
		//  * TODO (Barry & Sean): Insert code below
		//  */

		// Attraction[] attractions = new Attraction[10];
		// HttpEntity<AttractionRequest> attractionRequest = new HttpEntity<>(clientRequest.getAttractionRequest());
		// attractions = restTemplate.postForObject("http://attraction-service/activities", attractionRequest, Attraction[].class);
		// System.out.println("\n"+attractions[0].toString()+"\n");

		/**
		 * Create a new TravelPackage for client
		 */
		travelPackageRequestReferenceNumber++;                      // create new ref num for this travel package
		TravelPackage travelPackage = new TravelPackage();
		travelPackage.setFlights(flights);
		travelPackage.setHotels(hotels);
		// travelPackage.setActivities(activities);

		// storeBookingInMongo();
		
		/**
		 * Send response back to the client
		 */
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/travelagent/travelpackagerequests/"+travelPackageRequestReferenceNumber;  // Create new URI for this newly created ClientApplication
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(travelPackage, headers, HttpStatus.CREATED);  // return the newly created Client Application to client class
		
	} 

	// public void storeBookingInMongo(){
	// 	// Booking b = new Booking("try", "ni");
	// 	// mongoRepository.insertBooking(b);
	// }

	// public Booking getBookingFromMongo(String referenceId){
	// 	Booking b = new Booking();
	// 	try{
	// 		b = mongoRepository.getBookingFromMongo(referenceId);
	// 	}
	// 	catch(Exception e){
	// 		e.printStackTrace();
	// 	}
	// 	return b;
	// }
	/**
	 * POST REQUEST: handles requests from client for a booking (after they have made their choice of flight, hotel etc.)
	 * 
	 * @param clientRequest
	 * @return
	 * @throws URISyntaxException
	 */
	
	@RequestMapping(value="/travelagent/bookings",method=RequestMethod.POST)
	public ResponseEntity<Booking> createBooking(@RequestBody ClientResponse clientResponse) throws URISyntaxException {

		/**
		 * POST request to Flight service
		 */
		Flight flight = new Flight();	
		ClientChoice clientChoiceOfFlight = new ClientChoice();      // create ClientChoice to hold ref number
		clientChoiceOfFlight.setReferenceNumber(clientResponse.getFlightReferenceNumber());
		HttpEntity<ClientChoice> requestFlight = new HttpEntity<>(clientChoiceOfFlight);
		flight = restTemplate.postForObject("http://flights-service/flightservice/flights",requestFlight, Flight.class);

		/**
		 * POST request to Hotel Service
		 */

		Hotel hotel = new Hotel();
		ClientChoice clientChoiceOfHotel = new ClientChoice();
		clientChoiceOfHotel.setReferenceNumber(clientResponse.getHotelReferenceNumber());
		HttpEntity<ClientChoice> requestHotel = new HttpEntity<>(clientChoiceOfHotel);
		hotel = restTemplate.postForObject("http://hotels-service/hotelservice/hotels",requestHotel, Hotel.class);

		/**
		 * Create a new Booking for client
		 */
		Booking booking = new Booking();

		System.out.println("\n"+flight.toString());
		System.out.println("\n"+hotel.toString());

		booking.setFlight(flight);
		booking.setHotel(hotel);
		clientBookingReferenceNumber++; // create unique ref num
		booking.setReferenceNumber(clientBookingReferenceNumber);   // give booking this unique ref num
		clientBookings.put(clientBookingReferenceNumber,booking);

		/**
		 * Send response back to the client
		 */
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/travelagent/bookings/"+clientBookingReferenceNumber;  // Create new URI for this newly created ClientApplication
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(booking, headers, HttpStatus.CREATED);  // return the newly created Client Application to client class
		
	} 

	
	/**
	 * GET REQUEST (single instance)
	 * 
	 * @param referenceNumber
	 * @return booking
	 */
	@RequestMapping(value="/travelagent/bookings/{referenceNumber}",method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public Booking getBooking(@PathVariable int referenceNumber) {

		System.out.println("\nTEsting GET \n");
		Booking booking = clientBookings.get(referenceNumber);  // Find booking with given reference
		if (booking == null) throw new NoSuchBookingException();  // If no booking exists matching this reference then throw an exception
		return booking;
	}


	/**
	 * GET REQUEST (all instances)
	 * 
	 * @return clientBookings
	 */
	@RequestMapping(value="/travelagent/bookings",method=RequestMethod.GET)
	public @ResponseBody Collection<Booking> listEntries() {

		if (clientBookings.size() == 0) throw new NoSuchBookingException();
		return clientBookings.values();
	}

	
	/**
	 * PUT REQUEST
	 * 
	 * @param referenceNumber
	 * @param clientResponse
	 * @return 
	 * @throws URISyntaxException
	 */
	@RequestMapping(value="/travelagent/bookings/{referenceNumber}", method=RequestMethod.PUT)
    	public ResponseEntity<Booking> replaceBooking(@PathVariable int referenceNumber, @RequestBody ReplaceBooking replaceBooking) throws URISyntaxException  {

		Booking booking = clientBookings.get(referenceNumber);
		if (booking == null) throw new NoSuchBookingException();
		System.out.println("\n Testing PUT \n");

		/**
		 * POST request to Flight service
		 */
		Flight flight = new Flight();	
		ClientChoice clientChoiceOfFlight = new ClientChoice();      // create ClientChoice to hold ref number
		clientChoiceOfFlight.setReferenceNumber(replaceBooking.getNewChoiceOfBooking().getFlightReferenceNumber());
		HttpEntity<ClientChoice> requestFlight = new HttpEntity<>(clientChoiceOfFlight);
		restTemplate.put("http://flights-service/flightservice/flights/"+replaceBooking.getPreviousBooking().getFlight().getReferenceNumber(),requestFlight);
		flight = restTemplate.getForObject("http://flights-service/flightservice/flights/"+replaceBooking.getPreviousBooking().getFlight().getReferenceNumber(),Flight.class);
		System.out.println("\n GET FLIGHT:" +flight.toString());

		/**
		 * POST request to Hotel Service
		 */

		Hotel hotel = new Hotel();
		ClientChoice clientChoiceOfHotel = new ClientChoice();
		clientChoiceOfHotel.setReferenceNumber(replaceBooking.getNewChoiceOfBooking().getHotelReferenceNumber());
		HttpEntity<ClientChoice> requestHotel = new HttpEntity<>(clientChoiceOfHotel);
		restTemplate.put("http://hotels-service/hotelservice/hotels/"+replaceBooking.getPreviousBooking().getHotel().getReferenceNumber(),requestHotel);
		hotel = restTemplate.getForObject("http://hotels-service/hotelservice/hotels/"+replaceBooking.getPreviousBooking().getHotel().getReferenceNumber(), Hotel.class);
		System.out.println("\n GET HOTEL:" +hotel.toString());

		/**
		 * Create a new Booking for client
		 */

		System.out.println("\n"+flight.toString());
		System.out.println("\n"+hotel.toString());

		Booking previousBooking = clientBookings.remove(referenceNumber);
		Booking newBooking = new Booking();
		newBooking.setFlight(flight);
		newBooking.setHotel(hotel);
		newBooking.setReferenceNumber(referenceNumber);   // give booking this unique ref num
		clientBookings.put(referenceNumber,newBooking);

		System.out.println("\n PUT successful \n");
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/travelagent/bookings/"+clientBookingReferenceNumber;  // Create new URI for this newly created ClientApplication
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Location", path);
		return new ResponseEntity<>(headers, HttpStatus.OK);
    }

	
// 	/**
// 	 * DELETE REQUEST
// 	 * 
// 	 * @param referenceNumber
// 	 */
// 	@RequestMapping(value="/bookings/{referenceNumber}", method=RequestMethod.DELETE)
// 	@ResponseStatus(value=HttpStatus.NO_CONTENT)
// 	public @ResponseBody void deleteTravelPackage(@PathVariable int referenceNumber) {
// 		System.out.println("DELETE METHOD");
// 		TravelPackage travelPackage = travelPackageRequests.remove(referenceNumber);
// 		if (travelPackage == null) throw new NoSuchTravelPackageException();
// 	}


	// If there is no booking listed with the given reference then throw this exception
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class NoSuchBookingException extends RuntimeException {
		static final long serialVersionUID = -6516152229878843037L;
	}

}
