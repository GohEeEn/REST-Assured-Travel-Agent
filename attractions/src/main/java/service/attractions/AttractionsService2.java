package service.attractions;

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

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import service.core.Attraction;
import service.core.Geocode;
import service.core.AttractionRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Map;
import java.util.HashMap;



@RestController
public class AttractionsService2 {

    @Autowired
	private RestTemplate restTemplate;


    private static final Amadeus amadeus= Amadeus.builder( "3qFG1Vf9IQTAvMAFQUUAXZeJbE8KAAjm",
            "f9qzap835Rv0PtCg").build();
    private static final String PAGE = "/attractions";
    private static final String QUERY_REGEX = "^([a-z\\u0080-\\u024F]+(?:. |-| |'))*[a-z\\u0080-\\u024F]*$";
    private static final Pattern QUERY_PATTERN_CHECKER = Pattern.compile(QUERY_REGEX,Pattern.CASE_INSENSITIVE);
    private static final String STATUS_CODE_ERROR = "Wrong status code: ";
    private static final String EMPTY_RECOMMENDATION = "No recommendation found / Location not supported";

    private static int attractionRequestReferenceNumber = 0;    // unique reference number for each attractionRequest
	private static int searchedAttractionReferenceNumber = 0;           // unique reference number for each array of attractions found by AttractionService that matches requirements from attractionRequest
	private static int bookedAttractionReferenceNumber = 0;           // unique reference number for each attraction list booked by a client
	private Map<Integer, Attraction> bookedAttractions = new HashMap<>();      // Map of all attractions created with new reference number as key
    private Map<Integer, Attraction> searchedAttractions = new HashMap<>();    // Map of all attractions that AttractionsService searched for


     /**
	 * POST REQUEST: handles all attraction requests from travel agent
	 * 
	 * @param flightRequest
	 * @return attractions
	 * @throws URISyntaxException
	 */

	@RequestMapping(value="/attractionservice/attractionrequests",method=RequestMethod.POST)
	public ResponseEntity<Attraction []> searchAttractions(@RequestBody AttractionRequest attractionRequest)  throws URISyntaxException {

        System.out.println("\nTesting AttractionService POST Request\n");
        Geocode geocode = getDestinationGeocode(attractionRequest.getCity(), attractionRequest.getCountry());

        Attraction[] attractions = getAttractions(geocode.getLatitude(), geocode.getLongitude());
        
        /** 
		 * The following code prints all attractions which were found through the Amadeus API
		 */
        

        System.out.println("\nATTRACTIONS\n");
         for (int i=0; i<3; i++){
            
                System.out.println("\n"+attractions[i].toString()+"\n");
            
         }
         
         /**
          * TODO: Important
          */
    
          

		attractionRequestReferenceNumber++;

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/attractionservice/attractionrequests/"+attractionRequestReferenceNumber;     // Create URI for this attraction
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(attractions, headers, HttpStatus.CREATED);     // Returns attractions to travel agent
    }


    /**
	 * POST REQUEST: Once the client has chosen their attractions then these choices will be passed on to TravelAgentService which
	 * will then pass them on to this method
	 * 
	 * @param clientChoiceOfActivities
	 * @return attractions
	 */

	@RequestMapping(value="/attractionservice/attractions",method=RequestMethod.POST)
	public ResponseEntity<Attraction[]> createAttraction(@RequestBody ClientChoices clientChoicesOfAttractions)  throws URISyntaxException {

        System.out.println("\nTESTING ATTraction POST BOOKING)");
		Attraction attraction = searchedAttractions.get(clientChoicesOfAttractions.getReferenceNumbers()[0]);        // find attraction the client wishes to book
        Attraction[] attractions = new Attraction[1];
        attractions[0] = attraction;
		System.out.println("\nTesting /attractionservice/attractions\n");
		System.out.println(attraction.toString());
		
		// Add a new attraction for this client to bookedAttractions map (which contains booked attractions for all clients)
		bookedAttractionReferenceNumber++;
		bookedAttractions.put(bookedAttractionReferenceNumber,attraction);

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/attractionservice/attractions/"+bookedAttractionReferenceNumber;     // Create URI for this attraction
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(attractions, headers, HttpStatus.CREATED);     // Returns attractions to travel agent
	}


    /**
     * Method to validate the user string query, to prevent unwanted characters used<br/>
     * Reference : https://stackoverflow.com/questions/11757013/regular-expressions-for-city-name
     * @param query user query in string
     * @return boolean value shows if the given user query valid
     */
    public boolean isValidLocationName(String query) {
        if(query.isBlank()) return false;
        Matcher matcher = QUERY_PATTERN_CHECKER.matcher(query);
        return matcher.find();
    }

    /**
     * Method to get the geo-coordinate of a given city and country query with Nominatim REST API<br/>
     * Reference : https://nominatim.org/
     * @param city      Full city name in string, eg. Dublin instead of DUB
     * @param country   Full country name in string, eg. Ireland instead of IRE
     * @return A Geocode object, and null if the parameter(s) are invalid or processing issue
     */
    public Geocode getDestinationGeocode(String city, String country) {

        if(!isValidLocationName(city) || !isValidLocationName(country)) {
            System.out.println("Invalid City or Country Queries (City : '" + city + "', Country : '" + country + "')");
            return null;
        }

        Scanner sc = null;

        try {
            URL url = new URL("https://nominatim.openstreetmap.org/" +
                    "search?city=" + city + "&country=" + country + "&format=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) throw new RuntimeException("Unexpected HTTP Response Code: " + responseCode);
            else {
                sc = new Scanner(url.openStream());
                StringBuilder raw = new StringBuilder();
                while (sc.hasNext()) {
                    raw.append(sc.nextLine());
                }
                JSONParser parser = new JSONParser();
                JSONArray jsonObject = (JSONArray) parser.parse(String.valueOf(raw));

                JSONObject first = (JSONObject) jsonObject.get(0);
                double longitude = Double.parseDouble((String) first.get("lon"));
                double latitude = Double.parseDouble((String) first.get("lat"));

                return new Geocode(latitude, longitude);
            }
        } catch(IOException e) {
            System.out.println(e.toString());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid location query given (City : '" + city + "', Country : '" + country + "')");
        } catch (ParseException e) {
            System.out.println("Unrecognized JSON pattern found in JSONString from the API");
            System.out.println(e.toString());
        } finally {
            assert sc != null;
            sc.close();
        }
        return null;
    }

    /**
     * Method to retrieve a list of attractions available in given destination with its latitude and longitude
     * @param latitude Double value in range of -90 to 90
     * @param longitude Double value in range of -90 to 90
     * @return list of attractions in a given destination, empty if the given location is unavailable or no attractions can be found
     */
    // @GetMapping(value = PAGE)
    public Attraction[] getAttractions(double latitude, double longitude) {
        if(Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
            System.out.println("Invalid coordinate(s) given (Lat=" + latitude + ", Lon=" + longitude + ")");
            return new Attraction[0];
        }

        try {
            PointOfInterest[] pois = amadeus.referenceData.locations.pointsOfInterest.get(Params
                    .with("latitude", Double.toString(latitude))
                    .and("longitude", Double.toString(longitude)));
            Attraction[] attractions = new Attraction[100];

            for(int i = 0;i<pois.length;i++){
                PointOfInterest poi = pois[i];
                attractions[i] = new Attraction(poi.getName(),poi.getCategory(),poi.getType(),poi.getSubType());
            }

            if(attractions[0]!=null) {
                if(pois[0].getResponse().getStatusCode() != 200) {
                    System.out.println(STATUS_CODE_ERROR + pois[0].getResponse().getStatusCode());
                }
            } else {
                System.out.println(EMPTY_RECOMMENDATION);
            }
            return attractions;


        } catch(ResponseException e) {
            System.out.println("Error " + e.getCode() + " : " + e.getDescription());
        }

        return new Attraction[0];
    }

      /**
	 * The following method adds all attractions found by AttractionsService to searchedAttractions map
	 */

	 public Attraction [] addAttractionsToSearchAttractionsMap(Attraction [] attractions){

		for (Attraction attraction : attractions){

			searchedAttractionReferenceNumber++; 
			attraction.setReferenceNumber(searchedAttractionReferenceNumber);           // set the ref number in Attraction so that we can cross reference 
                                                                                        // with the client choice of attraction booking
            searchedAttractions.put(searchedAttractionReferenceNumber,attraction);          // add new attraction to map with new ref number
		}
		return attractions;
	 }


}
