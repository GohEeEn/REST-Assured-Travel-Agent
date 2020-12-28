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
import service.flights.NoSuchFlightQuoteException;

import java.util.Iterator;

@RestController
public class FlightService {
	
	static int referenceNumber = 0;    // unique reference number for each booking
	final String locale = "en-GB";
	private Map<Integer, Flight []> flights = new HashMap<>();      // Map of all flights created with flight.reference as key

	// POST request, handles all booking requests from travel agent
	@RequestMapping(value="/flights",method=RequestMethod.POST)
	public ResponseEntity<Flight[]> createBooking(@RequestBody ClientBooking clientBooking)  throws URISyntaxException {

		ArrayList<String> originAirportIDs = new ArrayList();
		ArrayList<String> destinationAirportIDs = new ArrayList();
		
		String countryOfOriginCode = getListMarkets(clientBooking.getCountryOfOrigin());         //find country code
		String countryOfDestinationCode = getListMarkets(clientBooking.getCountryOfDestination());         //find country code 

		originAirportIDs = getListPlaces(clientBooking.getCityOfOrigin(), clientBooking.getCountryOfOrigin(),
					countryOfOriginCode, clientBooking.getCurrency());
		destinationAirportIDs = getListPlaces(clientBooking.getCityOfDestination(), clientBooking.getCountryOfDestination(),
					countryOfDestinationCode, clientBooking.getCurrency());

		ArrayList<FlightQuote> flightQuotes = new ArrayList();

		for (String airportID : destinationAirportIDs){
			System.out.println(airportID);
			flightQuotes.addAll(findFlights(countryOfOriginCode,clientBooking.getCurrency(),locale,originAirportIDs.get(0),airportID,
				clientBooking.getOutboundDate()));
		}

		ArrayList<FlightQuote> flightQuotesCopy = new ArrayList();
		for (FlightQuote flightQuote : flightQuotes){
			flightQuotesCopy.add(flightQuote);
		}

		for (Iterator<FlightQuote> flightQuo = flightQuotes.iterator(); flightQuo.hasNext();){

			boolean isADuplicate = false;
			FlightQuote flightQuote = flightQuo.next();
			flightQuotesCopy.remove(flightQuote);

			for(FlightQuote flightQ : flightQuotesCopy){
				if (flightQ.equals(flightQuote)){
					System.out.println("We found a copy");
					isADuplicate = true;
				}
			}
			if(isADuplicate){
				flightQuo.remove();
			}
		}

		for (FlightQuote flightQuote : flightQuotes){
			System.out.println("Price is " + flightQuote.getPrice());
		}

		Flight[] flights = new Flight[flightQuotes.size()];

		int i = 0;
		for (FlightQuote flightQuote : flightQuotes){
			Flight flight = new Flight(clientBooking.getCityOfOrigin(), clientBooking.getCityOfDestination(),
			clientBooking.getOutboundDate(), clientBooking.getReturnDate(), flightQuote.getAirline(), flightQuote.getPrice(),
			flightQuote.getOriginAirportName(), flightQuote.getDestAirportName());
			flights[i] = flight;
			i++;
		}

		for (Flight m : flights){
			System.out.println("Next Flight: " +m.toString());
		}

		// flights = getAirportID(clientBooking.getCityOfOrigin(), clientBooking.getCountryOfOrigin(), countryOfOriginCode, 
		// 	clientBooking.getCurrency(), locale);
		referenceNumber++;

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/flights/"+referenceNumber;     // Create URI for this flight
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(flights, headers, HttpStatus.CREATED);     // Returns flights to travel agent
	} 

	// GET request, returns the flight with the reference passed as argument
	@RequestMapping(value="/flights/{reference}",method=RequestMethod.GET)
		public Flight[] getResource(@PathVariable("reference") int reference) {
		Flight[] clientFlights = flights.get(reference);
		if (clientFlights == null) throw new NoSuchQuotationException();
		return clientFlights;
	}

	@RequestMapping(value="/flights/{referenceNumber}", method=RequestMethod.PUT)
    public ResponseEntity<Flight []> replaceEntity(@PathVariable int referenceNumber, @RequestBody ClientBooking clientBooking) throws URISyntaxException  {
	  Flight [] clientFlights = flights.get(referenceNumber);
        if (clientFlights == null) throw new NoSuchFlightQuoteException();

	  ResponseEntity<Flight []> f= createBooking(clientBooking);   // update set of flights for this client

        String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/flights/"+referenceNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Location", path);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="/flights/{referenceNumber}", method=RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteEntity(@PathVariable int referenceNumber) {
        Flight [] clientFlights = flights.remove(referenceNumber);
        if (clientFlights == null) throw new NoSuchFlightQuoteException();
    }

	// If there is no quotation listed with the given reference after calling GET method then throw this exception
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class NoSuchQuotationException extends RuntimeException {
		static final long serialVersionUID = -6516152229878843037L;
	} 


	/**
	 * TODO Perhaps we should call this method once and then persist the data in MongoDb 
	 * 	  instead of calling it every time we want to find a countryCode
	 */

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

	public String getListMarkets(String countryName) { 
		
		String countryCode = "";

		try {
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/reference/v1.0/countries/en-GB"))
				.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
				.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

			/**
			 * TODO may need to delete this response if examples.json isn't needed going forward
			 */
			HttpResponse<Path> response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("countryCodes.json")));
			String countryCodes = response.body();
			JSONObject countryCodesJson = parseJSONObject(countryCodes);

			JSONArray countryCodesArray = new JSONArray();
	    		countryCodesArray = (JSONArray) countryCodesJson.get("Countries");
			
			//loop through array to find the country code 
			int index = 0;
			while (index < countryCodesArray.size()) {

				JSONObject jsonObject = (JSONObject) countryCodesArray.get(index);
				String name = (String) jsonObject.get("Name");
				
				if (name.equals(countryName)){
					countryCode = (String) jsonObject.get("Code");
				}
				index++;
			}	

		} catch(IOException e) {
                  e.printStackTrace();
		}
		catch(InterruptedException e) {
                  e.printStackTrace();
		}  
		return countryCode;
	}

	public ArrayList<String> getListPlaces(String cityOfDestination, String countryOfDestination, String countryOfOriginCode, String currency) { 
		
		ArrayList<String> airportIDs = new ArrayList();

		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/"+
						countryOfOriginCode+"/"+currency+"/"+locale+"/?query="+cityOfDestination+"%20"+countryOfDestination))
					.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
					.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			/**
			 * TODO may need to delete this response if airports.json isn't needed going forward
			 */
			// HttpResponse<Path> response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("airports.json")));
			
			System.out.println("Get ListPlaces: "+response.body());

			String places = response.body();
			JSONObject placesJson = parseJSONObject(places);

			JSONArray placesArray = new JSONArray();
		      placesArray = (JSONArray) placesJson.get("Places");
			System.out.println("Places array: "+placesArray);
			
			int index = 0;
			while (index < placesArray.size()) {
				JSONObject jsonObject = (JSONObject) placesArray.get(index);
				airportIDs.add((String) jsonObject.get("PlaceId"));
				index++;
			}	

		} catch(IOException e) {
                  e.printStackTrace();
		}
		catch(InterruptedException e) {
                  e.printStackTrace();
		}  
		return airportIDs;
	}

	/**
	 * TODO implement this from getListMarkets code when countryCodes.json remains persistent
	 */
	public String findCountryCode(Object json){
		String s = "";
		return s;
	}
	
	public ArrayList<FlightQuote> findFlights(String countryOfOriginCode, String currency, String locale, String originAirportCode, String destAirportCode, 
							String outboundDate) {

		ArrayList<FlightQuote> flightQuotes = new ArrayList();

		try{
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/"+
						countryOfOriginCode+"/"+currency+"/"+locale+"/"+originAirportCode+"/"+destAirportCode+"/"+outboundDate))
					.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
					.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println("FindFlights: "+ response.body());

			String flightQuotations = response.body();
			JSONObject placesJson = parseJSONObject(flightQuotations);

			JSONArray flightQuotesArray = new JSONArray();
			flightQuotesArray = (JSONArray) placesJson.get("Quotes");
			
			JSONArray carriersArray = new JSONArray();
			carriersArray = (JSONArray) placesJson.get("Carriers");

			JSONArray placesArray = new JSONArray();
			placesArray = (JSONArray) placesJson.get("Places");

			

			int index = 0;
			while (index < flightQuotesArray.size()) {

				FlightQuote flightQuote = new FlightQuote();
				JSONObject quotes = (JSONObject) flightQuotesArray.get(index);
				Long price = (Long) quotes.get("MinPrice");
				flightQuote.setPrice(Long.toString(price));

				JSONObject outboundLeg = (JSONObject) quotes.get("OutboundLeg");
				JSONArray carrierIds = (JSONArray) outboundLeg.get("CarrierIds");
				Long carrierId = (Long) carrierIds.get(0);
				flightQuote.setCarrierId(Long.toString(carrierId));
				
				Long originId = (Long) outboundLeg.get("OriginId");
				flightQuote.setOriginId(Long.toString(originId));
				Long destId = (Long) outboundLeg.get("DestinationId");
				flightQuote.setDestinationId(Long.toString(destId));

				for(int j=0; j<carriersArray.size(); j++){
					JSONObject carriers = (JSONObject) carriersArray.get(j);
					Long carId = (Long) carriers.get("CarrierId");
					System.out.println("Carrier name: "+carriers.get("Name"));
					System.out.println("Carrierid 1: "+carId);
					System.out.println("Carrierid 2: "+carrierId);
					if (carId.equals(carrierId)){
						flightQuote.setAirline((String) carriers.get("Name"));
					}
				}

				for(int k=0; k<placesArray.size(); k++){

					JSONObject places = (JSONObject) placesArray.get(k);
					Long placeId = (Long) places.get("PlaceId");
					String placeName = (String) places.get("Name");
					
					if (placeId.equals(originId)){
						
						flightQuote.setOriginAirportName(placeName);
						System.out.println("placename: "+placeName+" placeid: "+placeId);
					}

					if (placeId.equals(destId)){
						flightQuote.setDestAirportName(placeName);
					}
				}
				

				flightQuotes.add(flightQuote);
				
				index++;
			}	

		} catch(IOException e) {
                  e.printStackTrace();
            }
            catch(InterruptedException e) {
                  e.printStackTrace();
		}    

		return flightQuotes;
	}

	public Flight createFlight(FlightQuote flightQuote){
		Flight s = new Flight();
		return s;

	}

	// GET List Places (skyscanner call)
	public Flight[] getAirportID (String city, String country, String countryCode, String currency, String locale){
		
		Flight[] list = new Flight[10];

		try { // GET List Places (rapidapi.com)
                  HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/"+countryCode+
					"/"+currency+"/"+locale+"/?query="+city+"%20"+country))
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
  
	//     JSONObject ob2= (JSONObject) jsonArray.get(1);  // testing
	//     System.out.println("Object2: " + ob2);
  
	    //Get airport id
	    String place = (String) ob.get("PlaceId");    
	    System.out.println("1st Airport ID is: " + place);
   
	//     //Get airport id
	//     String place2 = (String) ob2.get("PlaceId");  
	//     System.out.println("2nd Airport ID is: " + place2);
	    
	//     Flight f = new Flight(place, place, "BBBB", "BBBB", "BBBB", );    // test input
	//     Flight f2 = new Flight(place, place, "CCCC", "CCCC", "CCCC", 375);    // test input
	 
	//     list2[0] =f;
	//     list2[1] = f2;

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
