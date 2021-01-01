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

import service.core.FlightRequest;
import service.core.Hotel;
import service.hotels.NoSuchHotelQuoteException;
import service.core.HotelRequest;

import java.util.Iterator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;



@RestController
public class HotelService2 {

      @Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	
      private Map<Integer, Hotel[]> hotels = new HashMap<>();      // Map of all hotels created with hotel.reference as key
      private int referenceNumber = 0;

	// POST request from travel agent
	@RequestMapping(value="/hotels",method=RequestMethod.POST)
	public ResponseEntity<Hotel[]> getHotelInfo(@RequestBody HotelRequest hotelRequest)  throws URISyntaxException {
		
		List<Hotel> clientHotels = new ArrayList<>();
            System.out.println("getHotelInfo method \n");
            System.out.println("Testidge");
            clientHotels = findHotels(hotelRequest);
            
            /**
             * Converts Hotel ArrayList to Hotel array as it is not possible to send a list so it must be converted 
             */
            Hotel [] hotelsArray = new Hotel[clientHotels.size()];
            Hotel h = new Hotel();
            h.setAddress("Camden st.");
            hotelsArray[0] = h;
            
            int index = 0;
            
            while (index < clientHotels.size()){
                  System.out.println("TESTING");
                  hotelsArray[index] = clientHotels.get(index);
                  index++;
            }



            referenceNumber++;
            System.out.println("\n TESTING \n");
            System.out.println(hotelsArray.length);
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/hotels/"+referenceNumber;     // Create URI for this hotel
		HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(path));
            
            
		return new ResponseEntity<>(hotelsArray, headers, HttpStatus.CREATED);     // Returns hotels to travel agent
	} 

	public List<Hotel> findHotels(HotelRequest hotelRequest){

            List<Hotel> hotelList = new ArrayList<>();
            // Hotel [] testArray = new Hotel[10];
		System.out.println("findHotels method \n");
		try{
                  HttpRequest request = HttpRequest.newBuilder()
                  .uri(URI.create("https://test.api.amadeus.com/v2/shopping/hotel-offers?cityCode=PAR&roomQuantity=1&adults=2&radius=5&radiusUnit=KM&paymentPolicy=NONE&includeClosed=false&bestRateOnly=true&view=FULL&sort=NONE"))
                  .header("Authorization", "Bearer " + getToken())
                  .method("GET", HttpRequest.BodyPublishers.noBody())
                  .build();
                  HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                 
                  JSONObject hotelInfo = new JSONObject();
                  hotelInfo = parseJSONObject(response.body());

                  JSONArray hotelInfoArray = new JSONArray();
	    		hotelInfoArray = (JSONArray) hotelInfo.get("data");

                  System.out.println(hotelInfoArray + "\n");
                  System.out.println("Response \n");
                  int index = 0;

                  
                  while(index < hotelInfoArray.size()){
                        JSONObject jsonObject = (JSONObject) hotelInfoArray.get(index);

                        JSONObject hotel = (JSONObject) jsonObject.get("hotel");
                        System.out.println("hotel: "+hotel+"\n");

                        JSONArray offers = (JSONArray) jsonObject.get("offers");
                        System.out.println("offers: "+offers +"\n");
                        jsonObject = (JSONObject) offers.get(0);

                        Hotel currentHotel = new Hotel();
                        
                        /**
                         * Finds price of room
                         */
                        JSONObject price = (JSONObject) jsonObject.get("price");
                        System.out.println("Price: "+ price.get("total"));
                        currentHotel.setPrice(String.valueOf(price.get("total")));

                        /**
                         * Finds Room Type and Bed Type
                         */
                        JSONObject room = (JSONObject) jsonObject.get("room");
                        Object typeEstimated = room.get("typeEstimated");
                        String typeEstimatedString = String.valueOf(typeEstimated);
                        JSONObject typeEstimatedJSON = parseJSONObject(typeEstimatedString);
                        System.out.println("Room Type: " + typeEstimatedJSON.get("category"));
                        System.out.println("Bed Type: " + typeEstimatedJSON.get("bedType"));
                        currentHotel.setRoomType(String.valueOf(typeEstimatedJSON.get("category")));
                        currentHotel.setBedType(String.valueOf(typeEstimatedJSON.get("bedType")));

                        /**
                         * Finds description of room
                         */
                        Object description = room.get("description");
                        String descriptionString = String.valueOf(description);
                        JSONObject descriptionJSON = parseJSONObject(descriptionString);
                        System.out.println("Description: " + descriptionJSON.get("text") + "\n");
                        currentHotel.setDescription(String.valueOf(descriptionJSON.get("text")));

                        /**
                         * Finds address of hotel
                         */
                        JSONObject address = (JSONObject) hotel.get("address");
                        JSONArray addressArray = (JSONArray) address.get("lines");
                        System.out.println(addressArray.get(0));
                        currentHotel.setAddress(String.valueOf(addressArray.get(0)));

                        /**
                         * Finds hotel amenities
                         */
                        JSONArray amenities = (JSONArray) hotel.get("amenities");
                        int j = 0;
                        String [] amenitiesList = new String[amenities.size()];
                        while(j < amenities.size()){
                              System.out.println(amenities.get(j));
                              amenitiesList[j] = String.valueOf(amenities.get(j));
                              j++;
                        }
                        currentHotel.setAmenities(amenitiesList);                        

                        /**
                         * Finds hotel rating 
                         */
                        System.out.println("Rating: "+hotel.get("rating"));
                        currentHotel.setRating(String.valueOf(hotel.get("rating")));

                        /**
                         * Finds uri for hotel
                         */
                        // JSONArray media = (JSONArray) hotel.get("media");
                        // int k = 0;
                        // while (k < media.size()){
                        //       System.out.println(media.get(k));
                        //       k++;
                        // }

                        /**
                         * Finds phone number of hotel
                         */
                        JSONObject contact = (JSONObject) hotel.get("contact");
                        System.out.println(contact.get("phone"));
                        currentHotel.setPhoneNumber(String.valueOf(contact.get("phone")));

                        /**
                         * Finds name of hotel
                         */
                        System.out.println("Name of hotel: "+hotel.get("name"));
                        currentHotel.setName(String.valueOf(hotel.get("name")));

                        System.out.println("\n" + currentHotel.toString());

                        hotelList.add(currentHotel);
                        index++;
                  }
                        
            }
            catch(IOException e) {
                  e.printStackTrace();
            }
            catch(InterruptedException e) {
                  e.printStackTrace();
            }
		return hotelList;
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
