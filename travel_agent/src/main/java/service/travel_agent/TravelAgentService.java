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

import service.core.ClientBooking;
import service.core.Flight;
import service.core.HotelQuote;
// import service.travel_agent.Booking;

import service.travel_agent.TravelQuotation;
import service.core.ClientRequest;
import service.core.HotelRequest;
/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
@RestController
public class TravelAgentService {

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	public static LinkedList<String> URIs = new LinkedList();        // Holds our URI's that will be passed as an argument when running broker
	// private Map<Integer, Booking> bookings = new TreeMap();            // all bookings for all clients 
	private Map<Integer, TravelQuotation> travelQuotations = new TreeMap();
	private Map<Integer, ClientRequest> clientRequests = new TreeMap();

	// POST Method, handles requests from client for quotations with given clientInfo
	@RequestMapping(value="/bookings",method=RequestMethod.POST)
	public ResponseEntity<Flight[]> getFlightInfo(@RequestBody ClientRequest clientRequest) throws URISyntaxException {

	
		Flight[] flights = new Flight[10];
		// for(String uri : URIs){   // Iterate through list of URIs and send clientInfo to each quotation service (1 per URI)
		// 		RestTemplate restTemplate = new RestTemplate();
		// 		HttpEntity<ClientBooking> request = new HttpEntity<>(clientBooking);
		// 		flights = restTemplate.postForObject("http://flights-service/flights/",request, Flight[].class);
		// 	}
		// RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ClientBooking> request = new HttpEntity<>(clientRequest.getClientBooking());
		flights = restTemplate.postForObject("http://flights-service/flights",request, Flight[].class);

		clientRequests.put(clientRequest.getReferenceNumber(), clientRequest);  // create a new ClientRequest

		TravelQuotation travelQuotation = new TravelQuotation();
		travelQuotation.setFlights(flights);
		travelQuotations.put(clientRequest.getReferenceNumber(),travelQuotation);

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/bookings/"+clientRequest.getReferenceNumber();  // Create new URI for this newly created ClientApplication
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(flights, headers, HttpStatus.CREATED);  // return the newly created Client Application to client class
		
	} 


	@RequestMapping(value="/bookings/{referenceNumber}", method=RequestMethod.PUT)
    public ResponseEntity<ClientRequest> updateClientBooking(@PathVariable int referenceNumber, @RequestBody ClientRequest clientRequest) throws URISyntaxException  {
	  TravelQuotation travelQuotation = travelQuotations.get(referenceNumber);
      //   if (booking == null) throw new NoSuchFlightQuoteException();
		System.out.println(clientRequest + "\n");
		
		ClientRequest previousClientRequest = clientRequests.get(clientRequest.getReferenceNumber());
		if(clientRequest.getClientBooking().equals(previousClientRequest.getClientBooking())){
			System.out.println("Client Bookings are the same!\n");
			// If not the same then call getFlightInfo() 
		}

		if(clientRequest.getHotelRequest().equals(previousClientRequest.getHotelRequest()) ){
			System.out.println("HotelRequests are the same!\n");
			// If not the same then call getHotelInfo()
		}

		Flight [] fs = new Flight[5];
        String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/booking/"+referenceNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Location", path);
	  return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

//     @RequestMapping(value="/book/{phone_number}", method=RequestMethod.PATCH)
//     public ResponseEntity<PhoneBookEntry> updateEntity(@PathVariable String phone_number, @RequestBody PhoneBookEntry entry) {
//         PhoneBookEntry phoneBookEntry = directory.get(phone_number);
//         if (phoneBookEntry == null) throw new NoSuchPhoneEntryException();

//         if (entry.getPhone() != null && !phone_number.equals(entry.getPhone())) 
//             throw new InvalidPhoneEntryException();
        
//         directory.get(phone_number).update(entry);

//         String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/book/"+phone_number;
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Content-Location", path);
//         return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
//     }

    










	// // GET request, returns the ClientApplication with the given reference 
	// @RequestMapping(value="/applications/{reference}",method=RequestMethod.GET)
	// @ResponseStatus(value=HttpStatus.OK)
	// public ClientApplication getEntity(@PathVariable int reference) {
	// 	ClientApplication clientApplication = clientApplications.get(reference);  // Create new ClientApplication with giver reference
	// 	if (clientApplication == null) throw new NoSuchClientApplicationException();  // If no ClientApplication exists matching this reference then throw an exception
	// 	return clientApplication;
	// }

	// // GET request, returns the ClientApplication with the given reference (passed as URI)
	// @RequestMapping(value="/applications",method=RequestMethod.GET)
	// public @ResponseBody Collection<ClientApplication> listEntries() {
	// 	if (clientApplications.size() == 0) throw new NoSuchClientApplicationException();
	// 	return clientApplications.values();
	// }

	// // If there is no ClientApplication listed with the given reference after calling GET method (for single instance) then throw this exception
	// @ResponseStatus(value = HttpStatus.NOT_FOUND)
	// public class NoSuchClientApplicationException extends RuntimeException {
	// 	static final long serialVersionUID = -6516152229878843037L;
	// }

	// // If there is no ClientApplications listed (i.e. size = 0) after calling GET method (for all instances) then throw this exception
	// @ResponseStatus(value = HttpStatus.NOT_FOUND)
	// public class NoClientApplicationsExistException extends RuntimeException {
	// 	static final long serialVersionUID = -6516152229878843036L;
	// }	
}
