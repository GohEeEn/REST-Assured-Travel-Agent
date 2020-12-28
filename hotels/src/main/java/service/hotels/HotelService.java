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

import service.core.ClientBooking;
import service.hotels.NoSuchHotelQuoteException;
import service.hotels.HotelQuote;

import java.util.Iterator;

import java.io.BufferedReader;
import java.io.InputStreamReader;


@RestController
public class HotelService {
	
	static int referenceNumber = 0;    // unique reference number for each booking
	private Map<Integer, HotelQuote[]> hotels = new HashMap<>();      // Map of all hotels created with hotel.reference as key

	// POST request from travel agent
	@RequestMapping(value="/hotels",method=RequestMethod.POST)
	public ResponseEntity<HotelQuote[]> createBooking(@RequestBody ClientBooking clientBooking)  throws URISyntaxException {
		
		HotelQuote [] clientHotels = new HotelQuote[10];



		referenceNumber++;

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/hotels/"+referenceNumber;     // Create URI for this hotel
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(clientHotels, headers, HttpStatus.CREATED);     // Returns hotels to travel agent
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

	/**
	 * Sends a get request for an access token. An access token is always needed to make a call to Amadeus hotel api 
	 */
	public String getToken(){

		String token = "";  // holds our token to gain access to subsequent api calls
		try {

                  List<String> tokenCommand = new ArrayList<String>();         // list to hold CURL command

                  tokenCommand.add("curl");
                  tokenCommand.add("https://test.api.amadeus.com/v1/security/oauth2/token");
                  tokenCommand.add("-H");
                  tokenCommand.add("Content-Type: application/x-www-form-urlencoded");
                  tokenCommand.add("-d");
                  tokenCommand.add("grant_type=client_credentials&client_id=u7gTwvqxHbRyEUbKASMPfdTaHfVFPY7k&client_secret=y1NpUer8LSzWvwNc");

                  ProcessBuilder processBuilder = new ProcessBuilder(tokenCommand);
                  processBuilder.redirectErrorStream(true);
                  Process process = processBuilder.start();  

                  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                  String line = "";
                  List<String> response = new ArrayList();   // holds response of curl command taken through bufferedReader
                  int i = 0;      // tells us when we can start adding lines to response as the first few lines are meaningless
                  while ((line = bufferedReader.readLine()) != null) {
                  i++;
                        if(i>8 && i<15 ){
                              response.add(line.trim());
                        }
                  }

                  for(String s : response){
                        if (s.substring(1,7).equals("access")){   // when we find "access" then we know our token will follow
                              token = s.substring(17,s.length()-2);  // remove unnecessary characters to leave only the required token
                        }
                  }
                  System.out.println("Final token: " + token);
      
            } catch(IOException e) {
                  e.printStackTrace();
		}
		return token;
	}
	

}
