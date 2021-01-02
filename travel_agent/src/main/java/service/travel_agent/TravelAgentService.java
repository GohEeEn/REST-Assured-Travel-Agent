package service.travel_agent;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;


import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.web.client.RestTemplate; 
import org.springframework.http.HttpEntity;

import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
 
import java.text.NumberFormat;

import service.core.FlightRequest;
import service.core.Flight;

import service.core.TravelPackage;
import service.core.ClientRequest;
import service.core.HotelRequest;
import service.core.Travel;
import service.core.ActivityRequest;
import service.core.Activity;
import service.core.Hotel;
import service.core.AttractionRequest;
import service.core.Attraction;
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
	// private Map<Integer, Booking> bookings = new TreeMap();            // all bookings for all clients 
	private Map<Integer, TravelPackage> travelPackages = new TreeMap();
	private Map<Integer, ClientRequest> clientRequests = new TreeMap();
	private int referenceNumber = 0;

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
		 * POST request to Flight service
		 */
		Flight[] flights = new Flight[10];	
		HttpEntity<FlightRequest> request = new HttpEntity<>(clientRequest.getFlightRequest());
		flights = restTemplate.postForObject("http://flights-service/flights",request, Flight[].class);

		clientRequests.put(referenceNumber, clientRequest);  // create a new ClientRequest

		/**
		 * POST request to Hotel Service
		 */

		Hotel [] hotels = new Hotel[10];
		HttpEntity<HotelRequest> request2 = new HttpEntity<>(clientRequest.getHotelRequest());
		hotels = restTemplate.postForObject("http://hotels-service/hotels",request2, Hotel[].class);
		System.out.println("TESTING TA");
		System.out.println("\n"+hotels[0].getPrice()+"\n");

		/**
		 * POST request to Activities Service
		 * TODO (Barry & Sean): Insert code below
		 */

		// Activity[] activities = new Activity[10];
		// HttpEntity<ActivityRequest> activityRequest = new HttpEntity<>(clientRequest.getActivityRequest());
		// activities = restTemplate.postForObject("http://activity-service/activities", activityRequest, Activity[].class);
		// System.out.println("\n"+activities[0].getDescription()+"\n");



		/**
		 * POST request to Attractions Service
		 * TODO (Barry & Sean): Insert code below
		 */

		// Attraction[] attractions = new Attraction[10];
		// HttpEntity<AttractionRequest> attractionRequest = new HttpEntity<>(clientRequest.getAttractionRequest());
		// attractions = restTemplate.postForObject("http://attraction-service/activities", attractionRequest, Attraction[].class);
		// System.out.println("\n"+attractions[0].toString()+"\n");

		/**
		 * Create a new TravelPackage for client
		 */
		TravelPackage travelPackage = new TravelPackage();
		travelPackage.setFlights(flights);
		travelPackage.setHotels(hotels);
		travelPackages.put(referenceNumber,travelPackage);

		// storeBookingInMongo();
		
		/**
		 * Send response back to the client
		 */
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/bookings/"+referenceNumber;  // Create new URI for this newly created ClientApplication
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(travelPackage, headers, HttpStatus.CREATED);  // return the newly created Client Application to client class
		
	} 

	public void storeBookingInMongo(){
		// Booking b = new Booking("try", "ni");
		// mongoRepository.insertBooking(b);
	}

	public Booking getBookingFromMongo(String referenceId){
		Booking b = new Booking();
		try{
			b = mongoRepository.getBookingFromMongo(referenceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
	/**
	 * PUT REQUEST
	 * 
	 * @param referenceNumber
	 * @param clientRequest
	 * @return travelPackage
	 * @throws URISyntaxException
	 */
	@RequestMapping(value="/bookings/{referenceNumber}", method=RequestMethod.PUT)
    	public ResponseEntity<TravelPackage> replaceTravelPackage(@PathVariable int referenceNumber, @RequestBody ClientRequest clientRequest) throws URISyntaxException  {
		TravelPackage travelPackage = travelPackages.get(referenceNumber);
		if (travelPackage == null) throw new NoSuchTravelPackageException();
		
		System.out.println("\n Testing PUT \n");
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/bookings/"+referenceNumber;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Location", path); 
	  
	  	return new ResponseEntity<>(travelPackage,headers, HttpStatus.NO_CONTENT);
    }

    /**
	 * PATCH REQUEST
	 * 
	 * @param referenceNumber
	 * @param clientRequest
	 * @return travelPackage
	 * @throws URISyntaxException
	 */
	@RequestMapping(value="/bookings/{referenceNumber}", method=RequestMethod.PATCH)
	public ResponseEntity<TravelPackage> updateTravelPackage(@PathVariable int referenceNumber, @RequestBody ClientRequest clientRequest) throws URISyntaxException  {
	  TravelPackage travelPackage = travelPackages.get(referenceNumber);
	  if (travelPackage == null) throw new NoSuchTravelPackageException();
	  
	  System.out.println("\n Testing PATCH \n");
	  String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/booking/"+referenceNumber;
	  HttpHeaders headers = new HttpHeaders();
	  headers.set("Content-Location", path); 
    
	    return new ResponseEntity<>(travelPackage,headers, HttpStatus.NO_CONTENT);
}


	/**
	 * GET REQUEST (single instance)
	 * 
	 * @param referenceNumber
	 * @return travelPackage
	 */
	@RequestMapping(value="/bookings/{referenceNumber}",method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public TravelPackage getTravelPackage(@PathVariable int referenceNumber) {
		TravelPackage travelPackage = travelPackages.get(referenceNumber);  // Create new TravelPackage with given reference
		if (travelPackage == null) throw new NoSuchTravelPackageException();  // If no TravelPackage exists matching this reference then throw an exception
		return travelPackage;
	}

	/**
	 * GET REQUEST (all instances)
	 * 
	 * @return travelPackages
	 */
	@RequestMapping(value="/bookings",method=RequestMethod.GET)
	public @ResponseBody Collection<TravelPackage> listEntries() {
		if (travelPackages.size() == 0) throw new NoSuchTravelPackageException();
		return travelPackages.values();
	}

	/**
	 * DELETE REQUEST
	 * 
	 * @param referenceNumber
	 */
	@RequestMapping(value="/bookings/{referenceNumber}", method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public @ResponseBody void deleteTravelPackage(@PathVariable int referenceNumber) {
		System.out.println("DELETE METHOD");
		TravelPackage travelPackage = travelPackages.remove(referenceNumber);
		if (travelPackage == null) throw new NoSuchTravelPackageException();
	}

	// If there is no TravelPackage listed with the given reference then throw this exception
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class NoSuchTravelPackageException extends RuntimeException {
		static final long serialVersionUID = -6516152229878843037L;
	}
}
