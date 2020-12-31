package service.recommendations;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.amadeus.resources.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivitiesRecommenderService {

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
     * Method that find all the cities and airports starting by the given city
     * @param city  IATA airport/city code
     * @param country Country code of the where the city/airport located
     * @return a list of destinations in the given city/airport, else null
     * @throws ResponseException
     */
    public Location[] getDestinations(String city, String country) throws ResponseException {
         Location[] dest = amadeus.referenceData.recommendedLocations
                                            .get(Params.with("cityCodes", city).and("travelerCountryCode", country));

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
            System.out.println(EMPTY_RECOMMENDATION);
        }
        return activities;
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
