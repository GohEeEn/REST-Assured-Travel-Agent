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

	// POST request, handles all booking requests from travel agent
	@RequestMapping(value="/flights",method=RequestMethod.POST)
	public ResponseEntity<Flight[]> createBooking(@RequestBody ClientBooking clientBooking)  throws URISyntaxException {

		Flight[] flights = new Flight[10];
		flights = getflightInfo(clientBooking.getCityOfOrigin());
		Flight f = flights[0];
		System.out.println("Yeah yeah: "+ f.getCityOfOrigin());
		referenceNumber++;
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/flights/"+referenceNumber;     // Create URI for this flight
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(flights, headers, HttpStatus.CREATED);     // Returns flights to travel agent
	} 


	public Flight[] getflightInfo(String location){
		System.out.println("Location: "+ location);
		Flight[] list = new Flight[10];

		try { // GET List Places (rapidapi.com)
                  HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/?query="+location))
                        .header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
                        .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                  HttpResponse<Path> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("example.json")));
			System.out.println(response.body());
                  

                  try (FileReader reader = new FileReader("example.json"))
                  {
				JSONParser jsonParser = new JSONParser();
				System.out.println("try statement");
                        //Read JSON file
                        Object obj = jsonParser.parse(reader);
                        System.out.println(obj);

                        //Create json array from json file
                        JSONArray array = new JSONArray();
                        array.add(obj);
				
				System.out.println("try statement2");
				list = parseFlightObject( (JSONObject) obj);
            
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
	     
	    JSONObject ob= (JSONObject) jsonArray.get(0);
	    System.out.println("Object1: " + ob);
  
	    JSONObject ob2= (JSONObject) jsonArray.get(1);
	    System.out.println("Object2: " + ob2);
  
	    //Get PlaceName
	    String place = (String) ob.get("PlaceName");    
	    System.out.println("place name is: " + place);
   
	    //Get CountryName
	    String countryName = (String) ob.get("CountryName");  
	    System.out.println(countryName);
	     
	    //Get CountryId
	    String countryId = (String) ob.get("CountryId");    
	    System.out.println(countryId);

	    
	    Flight f = new Flight(place, "BBBB", "BBBB", "BBBB", "BBBB", 125);
	    Flight f2 = new Flight(countryName, "CCCC", "CCCC", "CCCC", "CCCC", 375);
	 
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
