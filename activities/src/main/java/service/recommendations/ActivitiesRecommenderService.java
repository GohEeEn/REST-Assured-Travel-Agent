package service.recommendations;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import service.core.Geocode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
public class ActivitiesRecommenderService {

    @Autowired
	private RestTemplate restTemplate;

    private static final Amadeus amadeus= Amadeus.builder( "06t9AsvC4fSxHQuM0VGPkYwBbpfCLNkj",
                                                        "2ARuN5wPvtHDAKmZ").build();
    private static final String PAGE = "/activities";
    private static final String QUERY_REGEX = "^([a-z\\u0080-\\u024F]+(?:. |-| |'))*[a-z\\u0080-\\u024F]*$";
    private static final Pattern QUERY_PATTERN_CHECKER = Pattern.compile(QUERY_REGEX,Pattern.CASE_INSENSITIVE);
    private static final String STATUS_CODE_ERROR = "Wrong status code: ";
    private static final String EMPTY_RECOMMENDATION = "No recommendation found / Location not supported";

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
     @GetMapping(value = PAGE)
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
}
