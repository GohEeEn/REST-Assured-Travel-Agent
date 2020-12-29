package service.travel_agent;

import service.core.Flight;
import service.core.HotelQuote;

public class Booking {
      
      public Booking() {}

      private Flight [] flights;
      private HotelQuote [] hotels;

      public Flight [] getFlights(){
      return flights;
      }

      public void setFlights(Flight [] flights){
            this.flights = flights;
      }

      public HotelQuote [] getHotels(){
            return hotels;
      }

      public void setHotels(HotelQuote [] hotels){
            this.hotels = hotels;
      }
}
