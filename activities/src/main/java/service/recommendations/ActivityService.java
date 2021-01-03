package service.recommendations;

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
import com.amadeus.resources.Activity;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
<<<<<<< HEAD:activities/src/main/java/service/recommendations/ActivityService.java

import service.core.ActivityRequest;
=======
import service.core.ActivityItem;
>>>>>>> refs/remotes/origin/master:activities/src/main/java/service/recommendations/ActivitiesRecommenderService.java
import service.core.Geocode;
import service.core.ActivityItem;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Map;
import java.util.HashMap;


@RestController
public class ActivityService {

    @Autowired
	private RestTemplate restTemplate;

    private static final Amadeus amadeus= Amadeus.builder( "06t9AsvC4fSxHQuM0VGPkYwBbpfCLNkj",
                                                        "2ARuN5wPvtHDAKmZ").build();
    private static final String PAGE = "/activities";
    private static final String QUERY_REGEX = "^([a-z\\u0080-\\u024F]+(?:. |-| |'))*[a-z\\u0080-\\u024F]*$";
    private static final Pattern QUERY_PATTERN_CHECKER = Pattern.compile(QUERY_REGEX,Pattern.CASE_INSENSITIVE);
    private static final String STATUS_CODE_ERROR = "Wrong status code: ";
    private static final String EMPTY_RECOMMENDATION = "No recommendation found / Location not supported";

    private static int activityRequestReferenceNumber = 0;    // unique reference number for each activityRequest
	private static int searchedActivityReferenceNumber = 0;           // unique reference number for each list of activities found by ActivitiesRecommenderService that matches requirements from activityRequest
	private static int bookedActivityReferenceNumber = 0;           // unique reference number for each activity list booked by a client
	private Map<Integer, ActivityItem> bookedActivites = new HashMap<>();      // Map of all activities created with new reference number as key
    private Map<Integer, ActivityItem> searchedActivities = new HashMap<>();    // Map of all activites that ActivityRecommenderService searched for
    
    /**
	 * POST REQUEST: handles all flight requests from travel agent
	 * 
	 * @param flightRequest
	 * @return clientFlghts
	 * @throws URISyntaxException
	 */

	@RequestMapping(value="/activityservice/activityrequests",method=RequestMethod.POST)
	public ResponseEntity<ActivityItem []> searchActivities(@RequestBody ActivityRequest activityRequest)  throws URISyntaxException {

        System.out.println("\nTesting ActivityService POST Request\n");
        Activity [] activities = getActivities(activityRequest.getLatitude(), activityRequest.getLongitude());

        ActivityItem[] activityItems = convertActivitiesToActivitiyItems(activities);
        
        /** 
		 * The following code prints all activites which were found through the Amadeus API
		 */

		for (ActivityItem activityItem : activityItems){
			System.out.println("\n ActivityItem: "+activityItem.toString());
		}

		activityItems = addActivitiesToSearchActivitiesMap(activityItems);

		activityRequestReferenceNumber++;

		String path = ServletUriComponentsBuilder.fromCurrentContextPath().
			build().toUriString()+ "/activityservice/activityrequests/"+activityRequestReferenceNumber;     // Create URI for this activity
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI(path));
		return new ResponseEntity<>(activityItems, headers, HttpStatus.CREATED);     // Returns activities to travel agent
    }
    
    public ActivityItem[] convertActivitiesToActivitiyItems(Activity[] activities){

        ActivityItem [] activityItems = new ActivityItem[activities.length];

        int index = 0;
        for (Activity activity : activities){

            ActivityItem activityItem = new ActivityItem();
            activityItem.setName(activity.getName());
            activityItem.setDescription(activity.getDescription());
            activityItem.setRating(activity.getRating());
            activityItem.setBookingLink(activity.getBookingLink());
            activityItem.setPriceOfActivity(activity.getPrice().getAmount());
            activityItem.setPictures(activity.getPictures());
            activityItems[index] = activityItem;
            index++;
        }
        return activityItems;
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
     * Method to retrieve a list of activities available in given destination with its latitude and longitude
     * @param latitude Double value in range of -90 to 90
     * @param longitude Double value in range of -90 to 90
     * @return list of activities to do in given destination, empty if the given location is unavailable or no activities can be found
     */
     public Activity[] getActivities(double latitude, double longitude) {
         if(Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
             System.out.println("Invalid coordinate(s) given (Lat=" + latitude + ", Lon=" + longitude + ")");
             return new Activity[0];
         }

         try {
             Activity[] activities = amadeus.shopping.activities.get(Params
                     .with("latitude", Double.toString(latitude))
                     .and("longitude", Double.toString(longitude)));

             if(activities.length != 0) {
                 if(activities[0].getResponse().getStatusCode() != 200) {
                     System.out.println(STATUS_CODE_ERROR + activities[0].getResponse().getStatusCode());
                 }
             } else {
                 System.out.println(EMPTY_RECOMMENDATION);
             }
             return activities;

         } catch(ResponseException e) {
            System.out.println("Error " + e.getCode() + " : " + e.getDescription());
         }

         return new Activity[0];
     }

    /**
     * Method to retrieve a list of activities available in given destination with the city and country full name
     * @param city Full city name in string, eg. Dublin instead of DUB
     * @param country Full country name in string, eg. Ireland instead of IRE
     * @return list of activities to do in given destination, empty if the given location is unavailable or no activities can be found
     */
    @GetMapping(value = PAGE + "/{country}/{city}")
     public ActivityItem[] getActivitiesWithQueries(@RequestParam("city") String city, @RequestParam("country") String country) {
        Geocode destination = getDestinationGeocode(city, country);
        if(destination == null) {
            System.out.println("Invalid destination (" + city + ", " + country + ")");
            return new ActivityItem[0];
        }
        Activity[] activities = getActivities(destination.getLatitude(), destination.getLongitude());
        if(activities == null) {
            System.out.println("No activity found in (" + city + ", " + country + ")");
            return new ActivityItem[0];
        }
        LinkedList<ActivityItem> translated = new LinkedList<>();
        for(Activity activity : activities) {
            translated.add(toCoreActivity(activity));
        }
        return listToArray(translated);
     }

    /**
     * Method to translate an Amadeus Java SDK Activity class instance into a Java Bean Activity class object
     * @param act an Amadeus Java SDK Activity class instance
     * @return a Java Bean ActivityItem class object
     */
     public ActivityItem toCoreActivity(Activity act) {
        return new ActivityItem(
                                act.getName(),
                                act.getShortDescription(),
                                act.getRating(),
                                act.getBookingLink(),
                                act.getPictures(),
                                act.getPrice().getAmount(),
                                act.getPrice().getCurrencyCode()
        );
     }

     public ActivityItem[] listToArray(List<ActivityItem> activities) {

         ActivityItem[] activitiesArray = new ActivityItem[activities.size()];
         int index = 0;
         while (index < activities.size()){
             activitiesArray[index] = activities.get(index);
             index++;
         }

         return activitiesArray;
     }


     /**
	 * The following method adds all new activities found by ActivitesRecommenderService to searchedActivites map
	 */

	 public ActivityItem [] addActivitiesToSearchActivitiesMap(ActivityItem [] activities){

		for (ActivityItem activity : activities){

			searchedActivityReferenceNumber++; 
			activity.setReferenceNumber(searchedActivityReferenceNumber);           // set the ref number in Activity so that we can cross reference 
                                                                                    // with the client choice of activity booking
            searchedActivities.put(searchedActivityReferenceNumber,activity);          // add new activity to map with new ref number
		}
		return activities;
	 }

}
