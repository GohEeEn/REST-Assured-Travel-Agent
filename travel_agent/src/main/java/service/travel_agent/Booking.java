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

    public void setFlightDetails(String flightDetails){
        this.flightDetails = flightDetails;
    }

    public String getFlightDetails(){
        return flightDetails;
    }

    public void setHotelDetails(String hotelDetails){
        this.hotelDetails = hotelDetails;
    }

    public String getHotelDetails(){
        return hotelDetails;
    }
    public void setActivitiesDetails(String activitiesDetails){
        this.activitiesDetails = activitiesDetails;
    }

    public String getActivitiesDetails(){
        return activitiesDetails;
    }
}
