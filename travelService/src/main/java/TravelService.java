package service.recommendations;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.core.ClientBooking;
import service.core.Destination;
import service.core.Travel;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TravelService {

    private static int referenceNumber = 0;     // unique reference number for each booking
    private final String page = "/recommendation";
    private final Amadeus amadeus = Amadeus
                                    // .builder("cZvajJ1F0MnOAw1xxkducwdH2BXGZJj2", "jMQ8DDcDZGuLUZTy")
                                    .builder(System.getenv())   // env AMADEUS_CLIENT_ID & AMADEUS_CLIENT_SECRET
                                    .build();                   // Amadeus client initialization
    // Reference : https://developers.amadeus.com/blog/best-practices-api-key-storage

    /**
     * Method that find all the cities and airports starting by the given keywords
     * @param city  City code of the city/airport
     * @param country Country code of the where the city/airport located
     * @return a list of destinations in the given city/airport, else null
     * @throws ResponseException
     */
    @RequestMapping(value = page, method = RequestMethod.GET)
    public Location[] getDestinations(String city, String country) throws ResponseException {
         Location[] destinations = amadeus.referenceData.recommendedLocations
                                            .get(Params.with("cityCodes", city)
                                            .and("countryCode", country));
        // use Locations.ANY if there is no destination given

        if(destinations.length != 0) {
            if(destinations[0].getResponse().getStatusCode() != 200) {
                System.out.println("Wrong status code: " + destinations[0].getResponse().getStatusCode());
                return null;
            }
        } else {
            System.out.println("No recommendations found");
            return null;
        }

        return destinations;
    }

    @RequestMapping(value = page, method = RequestMethod.POST)
    public ResponseEntity<Travel[]> createBooking(@RequestBody ClientBooking clientBooking) throws URISyntaxException {

        Travel[] travels = new Travel[10];
        String path = ServletUriComponentsBuilder.fromCurrentContextPath().
                build().toUriString()+ page + "/"+referenceNumber;     // Create URI for this vehicle
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(travels, headers, HttpStatus.CREATED);
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
