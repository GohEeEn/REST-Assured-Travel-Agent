package service.recommendations;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.amadeus.resources.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.core.Destination;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ActivitiesRecommenderService {

    private final Amadeus amadeus = Amadeus
                                    .builder(System.getenv("AMADEUS_CLIENT_ID"), System.getenv("AMADEUS_CLIENT_SECRET"))
//                                    .builder("cZvajJ1F0MnOAw1xxkducwdH2BXGZJj2", "jMQ8DDcDZGuLUZTy")
                                    .build();                   // Amadeus client initialization
    // Reference : https://developers.amadeus.com/blog/best-practices-api-key-storage

    private static final String PAGE = "/activities";
    private static final String STATUS_CODE_ERROR = "Wrong status code: ";
    private static final String EMPTY_RECOMMENDATION = "No recommendation found / Location not supported";
    private static final String INVALID_LOCATION = "Invalid destination given";

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
     * Method that find all the cities and airports starting by the given city
     * @param city  IATA airport/city code
     * @param country Country code of the where the city/airport located
     * @return a list of destinations in the given city/airport, else null
     * @throws ResponseException
     */
    public Location[] getDestinations(String city, String country) throws ResponseException {
         Location[] dest = amadeus.referenceData.recommendedLocations
                                            .get(Params.with("cityCodes", city)
                                            .and("countryCode", country));

        if(dest.length != 0) {
            if(dest[0].getResponse().getStatusCode() != 200) {
                System.out.println(STATUS_CODE_ERROR + dest[0].getResponse().getStatusCode());
            }
        } else {
            System.out.println(EMPTY_RECOMMENDATION);
        }

        return dest;
    }

    /**
     * Method to retrieve a list of activities available in given destination with its exact geographic coordinates
     * @param destination Instance of destination
     * @return list of activities to do in given destination, empty if the given location is unavailable or no activities can be found
     * @throws ResponseException
     */
    @GetMapping(value = PAGE)
    public Activity[] getActivities(Destination destination) throws ResponseException {

        if(destination == null) {
            System.out.println(INVALID_LOCATION);
            return new Activity[0];
        }

        Activity[] activities = amadeus.shopping.activities.bySquare.get(Params
                .with("north", destination.getLatitudeNorth())
                .and("west", destination.getLongitudeWest())
                .and("south", destination.getLatitudeSouth())
                .and("east", destination.getLongitudeEast())
        );

        if(activities.length != 0) {
            if(activities[0].getResponse().getStatusCode() != 200) {
                System.out.println(STATUS_CODE_ERROR + activities[0].getResponse().getStatusCode());
            }
        } else {
            // It only supports several places in a test environment
            // Reference : https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md
            System.out.println(EMPTY_RECOMMENDATION);
        }

        return activities;
    }

    /**
     * Method to retrieve a list of activities available in given destination with its latitude and longitude
     * @param destination Destination in terms of a Location object
     * @return list of activities to do in given destination, empty if the given location is unavailable or no activities can be found
     * @throws ResponseException
     */
    @GetMapping(value = PAGE)
    public Activity[] getActivities(Location destination) throws ResponseException {

        if(destination == null) {
            System.out.println(INVALID_LOCATION);
            return new Activity[0];
        }

        Activity[] activities = amadeus.shopping.activities.get(Params
                .with("latitude", destination.getGeoCode().getLatitude())
                .and("longitude", destination.getGeoCode().getLongitude())
        );

        if(activities.length != 0) {
            if(activities[0].getResponse().getStatusCode() != 200) {
                System.out.println(STATUS_CODE_ERROR + activities[0].getResponse().getStatusCode());
            }
        } else {
            // It only supports several places in a test environment
            // Reference : https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md
            System.out.println(EMPTY_RECOMMENDATION);
        }
        return activities;
    }

    // A list of key-value pairs for supported locations
    // Key - IATA Airport Code (City Code), Value - Corresponding Destination Object
    public static final Map<String, Destination> destinations = new HashMap<>();
    static {
        destinations.put("BLR", new Destination("Bangalore", 13.023577, 77.536856, 12.923210, 77.642256)); // 45.28, 45.28
        destinations.put("BCN", new Destination("Barcelona", 41.42, 2.11, 41.347463, 2.228208));
        destinations.put("BER", new Destination("Berlin", 52.541755, 13.354201, 52.490569, 13.457198));
        destinations.put("DAL", new Destination("Dallas", 32.806993, -96.836857, 32.740310, -96.737293));
        destinations.put("LON", new Destination("London", 51.520180, -0.169882, 51.484703, -0.061048));
        destinations.put("NY", new Destination("New York", 40.792027, -74.058204, 40.697607, -73.942847));
        destinations.put("PAR", new Destination("Paris", 48.91, 2.25, 48.80, 2.46));
        destinations.put("SFO", new Destination("San Francisco", 37.810980, -122.483716, 37.732007, -122.370076));
    }   // Reference : https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md
}
