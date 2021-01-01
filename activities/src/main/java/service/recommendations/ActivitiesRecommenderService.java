package service.recommendations;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


@RestController
public class ActivitiesRecommenderService {

    @Autowired
	private RestTemplate restTemplate;

    private final Amadeus amadeus;

    private static final String PAGE = "/activities";
    private static final String STATUS_CODE_ERROR = "Wrong status code: ";
    private static final String EMPTY_RECOMMENDATION = "No recommendation found / Location not supported";
    private static final String INVALID_LOCATION = "Invalid destination given";

    public ActivitiesRecommenderService() {
//        amadeus = Amadeus.builder(System.getenv()).build(); // Amadeus client initialization
        amadeus = Amadeus.builder("06t9AsvC4fSxHQuM0VGPkYwBbpfCLNkj", "2ARuN5wPvtHDAKmZ").build();
    }

    /**
     * Method that get a specific city or airport based on its id
     * Reference : https://en.wikipedia.org/wiki/IATA_airport_code
     *
     * @param city  IATA airport/city code
     * @return Instance of Location of. the given city/airport, and null if not found
     * @throws ResponseException
     */
    public Location getDestination(String city) throws ResponseException {
        Location dest = amadeus.referenceData.location(city).get();
        if(dest == null) {
            System.out.println("Destination not found");
        } else if(dest.getResponse().getStatusCode() != 200) {
            System.out.println(STATUS_CODE_ERROR + dest.getResponse().getStatusCode());
        }
        return dest;
    }

    /**
     * Method to get the geo-coordinate of a given city and country query with Nominatim REST API
     * Reference : https://nominatim.org/
     * @param city      Full city name in string, eg. Dublin/dublin instead of DUB
     * @param country   Full country name in string, eg. Ireland/ireland instead of IRE
     * @return A Geocode object with the latitude and longitude values in double
     */
    public Geocode getDestinationGeocode(String city, String country) {
        try {
            URL url = new URL("https://nominatim.openstreetmap.org/" +
                    "search?city=" + city + "&country=" + country + "&format=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) throw new RuntimeException("Unexpected HTTP Response Code: " + responseCode);
            else {
                Scanner sc = new Scanner(url.openStream());
                StringBuilder raw = new StringBuilder();
                while(sc.hasNext()) {
                    raw.append(sc.nextLine());
                }
                sc.close();
                JSONParser parser = new JSONParser();
                JSONArray jsonObject = (JSONArray) parser.parse(String.valueOf(raw));
                JSONObject first = (JSONObject) jsonObject.get(0);
                double longitude = Double.parseDouble((String) first.get("lon"));
                double latitude = Double.parseDouble((String) first.get("lat"));

                return new Geocode(latitude, longitude);
            }
        } catch(MalformedURLException e) {
            System.out.println("Invalid URL given");
        } catch(IOException e) {
            System.out.println("There is some issue with I/O");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to retrieve a list of activities available in given destination with its latitude and longitude
     * @param latitude Double value in range of -90 to 90
     * @param longitude Double value in range of -90 to 90
     * @return list of activities to do in given destination, empty if the given location is unavailable or no activities can be found
     * @throws ResponseException
     */
     @GetMapping(value = PAGE)
     public Activity[] getActivities(double latitude, double longitude) throws ResponseException {
         if(Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
             System.out.println("Invalid coordinate(s) given");
             return new Activity[0];
         }

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
     }
}
