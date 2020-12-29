package service.core;

import service.core.ClientBooking;
import service.core.HotelRequest;

public class ClientRequest {

      public ClientRequest() {
            clientBooking = new ClientBooking();
            hotelRequest = new HotelRequest();
      }

      private ClientBooking clientBooking;
      private HotelRequest hotelRequest;
      private int referenceNumber;

      public ClientBooking getClientBooking(){
            return clientBooking;
      }

      public void setClientBooking(ClientBooking clientBooking){
            this.clientBooking = clientBooking;
      }

      public HotelRequest getHotelRequest(){
            return hotelRequest;
      }

      public void setHotelRequest(HotelRequest hotelRequest){
            this.hotelRequest = hotelRequest;
      }

      public int getReferenceNumber(){
            return referenceNumber;
      }

      public void setReferenceNumber(int referenceNumber){
            this.referenceNumber = referenceNumber;
      }
      
}
