package service.travel_agent;

public class Booking {
    // private String id;
    private String flightDetails;
    private String hotelDetails;
    private String activitiesDetails;
    public Booking(){}

    public Booking(String flightDetails, String hotelDetails, String activitiesDetails){
        this.flightDetails = flightDetails;
        this.hotelDetails = hotelDetails;
        this.activitiesDetails = activitiesDetails;
    }
}
