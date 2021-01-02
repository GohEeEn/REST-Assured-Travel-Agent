package service.core;

import service.core.Flight;
import service.core.Hotel;

public class TravelPackage {
      
      public TravelPackage() {}

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
