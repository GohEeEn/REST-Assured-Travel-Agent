package service.flights;

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

import service.core.Flight; 
import service.core.ClientBooking;

@RestController
public class FlightService {
	
	static int referenceNumber = 0;    // unique reference number for each booking

	@Autowired
	private RestTemplate restTemplate;

	// POST request, handles all booking requests from travel agent
	@RequestMapping(value="/flights",method=RequestMethod.POST)
	public ResponseEntity<Flight[]> createBooking(@RequestBody ClientBooking clientBooking)  throws URISyntaxException {

		Flight[] flights = new Flight[10];
		flights = getflightInfo(clientBooking.getCityOfOrigin());
		referenceNumber++;

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/flights/"+referenceNumber;     // Create URI for this flight
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(flights, headers, HttpStatus.CREATED);     // Returns flights to travel agent
	} 


	public Flight[] getflightInfo(String location){
		
		Flight[] list = new Flight[10];

		try { // GET List Places (rapidapi.com)
                  HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/IE/EUR/en-GB/?query=Paris%20France"))
				.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
				.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
                  HttpResponse<Path> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("example.json")));

                  try (FileReader reader = new FileReader("example.json"))
                  {
				/***
				 * TODO Iterate through the JSON file and extract names of every airport for that region
				 * Team Member: Barry
				 */

				JSONParser jsonParser = new JSONParser();
                        //Read JSON file
                        Object obj = jsonParser.parse(reader);
                        System.out.println(obj);

                        //Create json array from json file
                        // JSONArray array = new JSONArray();
                        // array.add(obj);
				
				list = parseFlightObject( (JSONObject) obj);     // add flight info to list
            
                  } catch (FileNotFoundException e) {
                        e.printStackTrace();
                  } catch (IOException e) {
                        e.printStackTrace();
                  } catch (ParseException e) {
                        e.printStackTrace();
                  }
            
            } catch(IOException e) {
                  e.printStackTrace();
            }
            catch(InterruptedException e) {
                  e.printStackTrace();
		}    
		
		return list;        
	}
	
	private static Flight[] parseFlightObject(JSONObject flight) 
	{
	    Flight[] list2 = new Flight[10];

	    //Get places array within outer array
	    JSONArray jsonArray = new JSONArray();
	    jsonArray = (JSONArray) flight.get("Places");
	     
	    JSONObject ob= (JSONObject) jsonArray.get(0);    // testing
	    System.out.println("Object1: " + ob);
  
	    JSONObject ob2= (JSONObject) jsonArray.get(1);  // testing
	    System.out.println("Object2: " + ob2);
  
	    //Get airport id
	    String place = (String) ob.get("PlaceId");    
	    System.out.println("1st Airport ID is: " + place);
   
	    //Get airport id
	    String place2 = (String) ob2.get("PlaceId");  
	    System.out.println("2nd Airport ID is: " + place2);
	    
	    Flight f = new Flight(place, place, "BBBB", "BBBB", "BBBB", 125);    // test input
	    Flight f2 = new Flight(place2, place2, "CCCC", "CCCC", "CCCC", 375);    // test input
	 
	    list2[0] =f;
	    list2[1] = f2;

	    return list2;
	}





	
	// // GET request, returns the quotation with the reference passed as argument
	// @RequestMapping(value="/quotations/{reference}",method=RequestMethod.GET)
	// 	public Quotation getResource(@PathVariable("reference") String reference) {
	// 	Quotation quotation = quotations.get(reference);
	// 	if (quotation == null) throw new NoSuchQuotationException();
	// 	return quotation;
	// }

	// // If there is no quotation listed with the given reference after calling GET method then throw this exception
	// @ResponseStatus(value = HttpStatus.NOT_FOUND)
	// public class NoSuchQuotationException extends RuntimeException {
	// 	static final long serialVersionUID = -6516152229878843037L;
	// } 

	

}
