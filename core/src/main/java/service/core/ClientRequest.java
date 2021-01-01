package service.core;

import service.core.FlightRequest;
import service.core.HotelRequest;

public class ClientRequest {

      public ClientRequest() {
            flightRequest = new FlightRequest();
            hotelRequest = new HotelRequest();
      }

      private FlightRequest flightRequest;
      private HotelRequest hotelRequest;

      public FlightRequest getFlightRequest(){
            return flightRequest;
      }

      public void setFlightRequest(FlightRequest flightRequest){
            this.flightRequest = flightRequest;
      }

      public HotelRequest getHotelRequest(){
            return hotelRequest;
      }

      public void setHotelRequest(HotelRequest hotelRequest){
            this.hotelRequest = hotelRequest;
      }
      
}
