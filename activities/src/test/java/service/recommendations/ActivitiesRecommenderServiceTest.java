package service.recommendations;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import org.junit.Before;
import org.junit.Test;
import service.core.Geocode;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ActivitiesRecommenderServiceTest {

    private static ActivitiesRecommenderService recommender;

    @Before
    public void setUp() {
        recommender = new ActivitiesRecommenderService();
    }

    @Test
    public void getDestinationWithCityAndCountryTest() {
        assertNotNull("Error : A Geocode object should be returned", recommender.getDestinationGeocode("dublin", "ireland"));
        assertEquals("Error : It should be a Geocode object", Geocode.class, recommender.getDestinationGeocode("malacca", "malaysia").getClass());
    }

     @Test
     public void getActivitiesTest() throws ResponseException {

         Activity[] barcelona = recommender.getActivities(41.39715, 2.160873); // Barcelona
         assertNotNull("Error : There should have activity instances available", barcelona);
         assertNotEquals("Error : This list shouldn't be empty", 0, barcelona.length);

         Geocode dest = recommender.getDestinationGeocode("dublin", "ireland");
         Activity[] dublin = recommender.getActivities(dest.getLatitude(), dest.getLongitude());
         assertNotNull("Error : There should have activity instances available", dublin);
         assertNotEquals("Error : This list should be empty since this location is not supported", 0, dublin.length);

         dest = recommender.getDestinationGeocode("beijing", "china");
         Activity[] beijing = recommender.getActivities(dest.getLatitude(), dest.getLongitude());
         assertNotNull("Error : The activity array instance should be returned", beijing);
         assertEquals("Error : This list should be empty since this location is not supported", 0, beijing.length);
     }
}