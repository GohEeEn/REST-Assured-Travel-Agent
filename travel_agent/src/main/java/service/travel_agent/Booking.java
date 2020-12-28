package service.travel_agent;

public class Booking {
      
      public Booking() {}

      private Flights [] flights;
      private Hotels [] hotels;

      public Flights [] getFlights(){
      return flights;
      }

      public void setFlights(Flight [] flights){
            this.flights = flights
      }

      public Hotels [] getHotels(){
            return hotels;
      }

      public void setHotels(Hotel [] hotels){
            this.hotels = hotels;
      }
}
