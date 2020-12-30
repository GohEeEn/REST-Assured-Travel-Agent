package service.travel_agent;

import service.core.Flight;
import service.core.Hotel;

public class TravelQuotation {
      
      public TravelQuotation() {}

      private Flight [] flights;
      private Hotel [] hotels;

      public Flight [] getFlights(){
      return flights;
      }

      public void setFlights(Flight [] flights){
            this.flights = flights;
      }

      public Hotel [] getHotels(){
            return hotels;
      }

      public void setHotels(Hotel [] hotels){
            this.hotels = hotels;
      }
}
