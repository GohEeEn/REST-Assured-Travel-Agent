package service.hotels;

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

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
// import java.text.ParseException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 

// import service.core.Flight; 
import service.core.ClientBooking;
import service.flights.NoSuchHotelQuoteException;

import java.util.Iterator;

@RestController
public class HotelService {
	
	static int referenceNumber = 0;    // unique reference number for each booking
	private Map<Integer, Flight []> flights = new HashMap<>();      // Map of all flights created with flight.reference as key

	// POST request, handles all booking requests from travel agent
	@RequestMapping(value="/flights",method=RequestMethod.POST)
	public ResponseEntity<Flight[]> createBooking(@RequestBody ClientBooking clientBooking)  throws URISyntaxException {

		
		referenceNumber++;

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/flights/"+referenceNumber;     // Create URI for this flight
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(flights, headers, HttpStatus.CREATED);     // Returns flights to travel agent
	} 

	

// 	// GET request, returns the flight with the reference passed as argument
// 	@RequestMapping(value="/flights/{reference}",method=RequestMethod.GET)
// 		public Quotation getResource(@PathVariable("reference") int reference) {
// 		Flight flight = flights.get(reference);
// 		if (flight == null) throw new NoSuchQuotationException();
// 		return flight;
// 	}

// 	@RequestMapping(value="/flights/{referenceNumber}", method=RequestMethod.PUT)
//     public ResponseEntity<Flight []> replaceEntity(@PathVariable int referenceNumber, @RequestBody ClientBooking clientBooking) {
// 	  Flight [] clientFlights = flights.get(referenceNumber);
//         if (clientFlights == null) throw new NoSuchFlightQuoteException();

// 	  clientFlights = createBooking(clientBooking);   // update set of flights for this client

//         String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/flights/"+referenceNumber;
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Content-Location", path);
//         return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
//     }

//     @RequestMapping(value="/flights/{referenceNumber}", method=RequestMethod.DELETE)
//     @ResponseStatus(value=HttpStatus.NO_CONTENT)
//     public void deleteEntity(@PathVariable int referenceNumber) {
//         Flight [] clientFlights = flights.remove(referenceNumber);
//         if (clientFlights == null) throw new NoSuchFlighQuoteException();
//     }

// 	// If there is no quotation listed with the given reference after calling GET method then throw this exception
// 	@ResponseStatus(value = HttpStatus.NOT_FOUND)
// 	public class NoSuchQuotationException extends RuntimeException {
// 		static final long serialVersionUID = -6516152229878843037L;
// 	} 


	public JSONObject parseJSONObject(String response){

		JSONObject jsonObject = new JSONObject();
		try{
			JSONParser parser = new JSONParser();
			jsonObject = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	

}
