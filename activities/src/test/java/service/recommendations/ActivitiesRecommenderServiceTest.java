package service.recommendations;

import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActivitiesRecommenderServiceTest {

    private static Amadeus amadeus;

    @Before
    public void setUp() {
        amadeus = Amadeus
//                    .builder(System.getenv("AMADEUS_CLIENT_ID"), System.getenv("AMADEUS_CLIENT_SECRET"))
                    .builder("cZvajJ1F0MnOAw1xxkducwdH2BXGZJj2", "jMQ8DDcDZGuLUZTy")
                    .build();
    }

    @Test
    public void getDestination() throws ResponseException {
        Location london = amadeus.referenceData.location("ALHR").get();
        System.out.println(london.toString());
        assertNotNull("Error : This Location object should be available", london);
    }

//    @Test
//    public void getDestinations() {
//    }

//    @Test
//    public void getActivities() {
//    }

//    @Test
//    public void testGetActivities() {
//    }
}