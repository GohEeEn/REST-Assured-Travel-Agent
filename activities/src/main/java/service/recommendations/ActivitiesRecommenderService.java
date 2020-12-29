package service.recommendations;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.amadeus.resources.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import service.core.Destination;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ActivitiesRecommenderService {


    private final String page = "/activities";
    private final Amadeus amadeus = Amadeus
                                    .builder(System.getenv("AMADEUS_CLIENT_ID"), System.getenv("AMADEUS_CLIENT_SECRET"))
                                    .build();                   // Amadeus client initialization
    // Reference : https://developers.amadeus.com/blog/best-practices-api-key-storage

    /**
     * Method that find all the cities and airports starting by the given keywords
     * @param city  City code of the city/airport
     * @param country Country code of the where the city/airport located
     * @return a list of destinations in the given city/airport, else null
     * @throws ResponseException
     */
    public Location[] getDestinations(String city, String country) throws ResponseException {
         Location[] dest = amadeus.referenceData.recommendedLocations
                                            .get(Params.with("cityCodes", city)
                                            .and("countryCode", country));
        // use Locations.ANY if there is no destination given

        if(dest.length != 0) {
            if(dest[0].getResponse().getStatusCode() != 200) {
                System.out.println("Wrong status code: " + dest[0].getResponse().getStatusCode());
                return null;
            }
        } else {
            System.out.println("No recommendations found");
            return null;
        }

        return dest;
    }

    /**
     * Method to retrieve a list of activities available in given destination with its exact geographic coordinates
     * @param destination Instance of destination
     * @return list of activities to do in given destination, empty if the given location is unavailable or no activities can be found
     * @throws ResponseException
     */

    @Autowired
	private RestTemplate restTemplate;

    
    @GetMapping(value = page)
    public Activity[] getActivities(Destination destination) throws ResponseException {
        Activity[] activities = amadeus.shopping.activities.bySquare.get(Params
                .with("north", destination.getLatitudeNorth())
                .and("west", destination.getLongitudeWest())
                .and("south", destination.getLatitudeSouth())
                .and("east", destination.getLongitudeEast())
        );

        // It only supports several places in a test environment
        // Reference : https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md
        if(activities.length == 0) {
            System.out.println("Either the location is not supported, or no activity is going to happen there");
        }

        return activities;
    }

    /**
     * Method to retrieve a list of activities available in given destination with its latitude and longitude
     * @param destination Destination in terms of a Location object
     * @return list of activities to do in given destination, empty if the given location is unavailable or no activities can be found
     * @throws ResponseException
     */
    @GetMapping(value = page)
    public Activity[] getActivities(Location destination) throws ResponseException {
        Activity[] activities = amadeus.shopping.activities.get(Params
                .with("latitude", destination.getGeoCode().getLatitude())
                .and("longitude", destination.getGeoCode().getLongitude())
        );

        // It only supports several places in a test environment
        // Reference : https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md
        if(activities.length == 0) {
            System.out.println("Either the location is not supported, or no activity is going to happen there");
        }

        return activities;
    }

    public static final Map<String, Destination> destinations = new HashMap<>();
    static {
        destinations.put("Bangalore", new Destination("Bangalore", 13.023577, 77.536856, 12.923210, 77.642256));
        destinations.put("Barcelona", new Destination("Barcelona", 41.42, 2.11, 41.347463, 2.228208));
        destinations.put("Berlin", new Destination("Berlin", 52.541755, 13.354201, 52.490569, 13.457198));
        destinations.put("Dallas", new Destination("Dallas", 32.806993, -96.836857, 32.740310, -96.737293));
        destinations.put("London", new Destination("London", 51.520180, -0.169882, 51.484703, -0.061048));
        destinations.put("New York", new Destination("New York", 40.792027, -74.058204, 40.697607, -73.942847));
        destinations.put("Paris", new Destination("Paris", 48.91, 2.25, 48.80, 2.46));
        destinations.put("San Francisco", new Destination("San Francisco", 37.810980, -122.483716, 37.732007, -122.370076));
    }   // Reference : https://github.com/amadeus4dev/data-collection/blob/master/data/pois.md
}
