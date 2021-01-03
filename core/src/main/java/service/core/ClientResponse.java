package service.core;

public class ClientResponse {
      
      public ClientResponse(){}

      private int flightReferenceNumber;        // holds client's choice of flight
      private int hotelReferenceNumber;         // holds client's choice of hotel
      private int travelPackageReferenceNumber;     // holds client's TravelPackageReference;
      
      public int getFlightReferenceNumber(){
            return flightReferenceNumber;
      }

      public void setFlightReferenceNumber(int flightReferenceNumber){
            this.flightReferenceNumber = flightReferenceNumber;
      }

      public int getHotelReferenceNumber(){
            return hotelReferenceNumber;
      }

      public void setHotelReferenceNumber(int hotelReferenceNumber){
            this.hotelReferenceNumber = hotelReferenceNumber;
      }

      public int getTravelPackageReferenceNumber(){
            return travelPackageReferenceNumber;
      }

      public void setTravelPackageReferenceNumber(int travelPackageReferenceNumber){
            this.travelPackageReferenceNumber = travelPackageReferenceNumber;
      }
}
