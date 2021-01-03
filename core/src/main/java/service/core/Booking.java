package service.core;

public class Booking {
     
      public Booking() {}

      private Flight flight;
      private Hotel hotel;
      private int referenceNumber;

      public Flight getFlight(){
            return flight;
      } 

      public void setFlight(Flight flight){
            this.flight = flight;
      }

      public Hotel getHotel(){
            return hotel;
      }

      public void setHotel(Hotel hotel){
            this.hotel = hotel;     
       }

      public int getReferenceNumber(){
            return referenceNumber;
      }

      public void setReferenceNumber(int referenceNumber){
            this.referenceNumber = referenceNumber;
      }
}
