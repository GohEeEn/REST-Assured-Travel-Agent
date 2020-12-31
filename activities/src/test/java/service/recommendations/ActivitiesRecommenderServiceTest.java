package service.recommendations;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import com.amadeus.resources.Location;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ActivitiesRecommenderServiceTest {

    private static ActivitiesRecommenderService recommender;

    @Before
    public void setUp() {
        recommender = new ActivitiesRecommenderService();
    }

    @Test
    public void getDestinationWithIATACode() throws ResponseException {
        Location location = recommender.getDestination("CBER");
        System.out.println(location.toString());
        assertNotNull("Error : This Location object should be available", location);
    }

//    @Test
//    public void getDestinations() throws ResponseException {
//        Location[] locations = recommender.getDestinations("LON", "UK"); // ("PAR", "FR")
//        System.out.println(Arrays.toString(locations));
//        assertNotEquals("Error : Location objects should be returned", 0, locations.length);
//    }

    // @Test
    // public void getActivitiesWithGeolocation() throws ResponseException {

    //     Activity[] barcelona = recommender.getActivities(41.39715, 2.160873); // Barcelona
    //     assertNotNull("Error : There should have activity instances available", barcelona);
    //     assertNotEquals("Error : This list shouldn't be empty", 0, barcelona.length);

    //     Activity[] beijing = recommender.getActivities(39.9020803, 116.7185213); // Beijing
    //     assertNotNull("Error : The activity array instance should be returned", beijing);
    //     assertEquals("Error : This list should be empty since this location is not supported", 0, beijing.length);
    // }

//    @Test
//    public void getActivities() {
//    }
}