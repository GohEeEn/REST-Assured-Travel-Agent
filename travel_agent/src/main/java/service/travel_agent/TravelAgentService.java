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

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
@RestController
public class TravelAgentService {

	public static LinkedList<String> URIs = new LinkedList();        // Holds our URI's that will be passed as an argument when running broker
	static int referenceNumber = 0;
	// POST Method, handles requests from client for quotations with given clientInfo
	@RequestMapping(value="/bookings",method=RequestMethod.POST)
	public ResponseEntity<ArrayList<String>> getFlightInfo(@RequestBody String location) throws URISyntaxException {
	
		ArrayList<String> flightInfo = new ArrayList();
		for(String uri : URIs){   // Iterate through list of URIs and send clientInfo to each quotation service (1 per URI)
				RestTemplate restTemplate = new RestTemplate();
				HttpEntity<String> request = new HttpEntity<>(location);
				flightInfo = restTemplate.postForObject(uri,request, ArrayList.class);
			}
		
		referenceNumber++;
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/bookings/"+referenceNumber;  // Create new URI for this newly created ClientApplication
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(flightInfo, headers, HttpStatus.CREATED);  // return the newly created Client Application to client class
		
	} 









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
