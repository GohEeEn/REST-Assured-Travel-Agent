package service.attractions;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import service.core.Attraction;
import service.core.Geocode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@RestController
public class AttractionsService {

    @Autowired
	private RestTemplate restTemplate;


    private static final Amadeus amadeus= Amadeus.builder( "3qFG1Vf9IQTAvMAFQUUAXZeJbE8KAAjm",
            "f9qzap835Rv0PtCg").build();
    private static final String PAGE = "/attractions";
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
     * Method to retrieve a list of attractions available in given destination with its latitude and longitude
     * @param latitude Double value in range of -90 to 90
     * @param longitude Double value in range of -90 to 90
     * @return list of attractions in a given destination, empty if the given location is unavailable or no attractions can be found
     */
    @GetMapping(value = PAGE)
    public PointOfInterest[] getAttractions(double latitude, double longitude) {
        if(Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
            System.out.println("Invalid coordinate(s) given (Lat=" + latitude + ", Lon=" + longitude + ")");
            return new PointOfInterest[0];
        }

        try {
            PointOfInterest[] pois = amadeus.referenceData.locations.pointsOfInterest.get(Params
                    .with("latitude", Double.toString(latitude))
                    .and("longitude", Double.toString(longitude)));

            if(pois[0]!=null) {
                if(pois[0].getResponse().getStatusCode() != 200) {
                    System.out.println(STATUS_CODE_ERROR + pois[0].getResponse().getStatusCode());
                }
            } else {
                System.out.println(EMPTY_RECOMMENDATION);
            }

            return pois;

        } catch(ResponseException e) {
            System.out.println("Error " + e.getCode() + " : " + e.getDescription());
        }

        return new PointOfInterest[0];
    }

    /**
     * Method to retrieve a list of attraction available in given destination with the city and country full name
     * @param city Full city name in string, eg. Dublin instead of DUB
     * @param country Full country name in string, eg. Ireland instead of IRE
     * @return list of attractions in given destination, empty if the given location is unavailable or no attraction can be found
     */
    public Attraction[] getAttractionsWithQueries(String city, String country) {
        Geocode destination = getDestinationGeocode(city, country);
        if(destination == null) {
            System.out.println("Invalid destination (" + city + ", " + country + ")");
            return new Attraction[0];
        }
        PointOfInterest[] activities = getAttractions(destination.getLatitude(), destination.getLongitude());
        if(activities == null) {
            System.out.println("No activity found in (" + city + ", " + country + ")");
            return new Attraction[0];
        }
        LinkedList<Attraction> translated = new LinkedList<>();
        for(PointOfInterest pointOfInterest : activities) {
            translated.add(toCoreAttraction(pointOfInterest));
        }
        return listToArray(translated);
    }

    public Attraction toCoreAttraction(PointOfInterest poi) {
        return new Attraction(poi.getName(), poi.getCategory(), poi.getType(), poi.getSubType());
    }

    public Attraction[] listToArray(List<Attraction> activities) {

        Attraction[] attractionsArray = new Attraction[activities.size()];
        int index = 0;
        while (index < activities.size()){
            attractionsArray[index] = activities.get(index);
            index++;
        }

        return attractionsArray;
    }
}
