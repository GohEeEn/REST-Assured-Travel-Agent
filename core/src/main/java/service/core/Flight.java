package service.core;

public class Flight {

      public Flight(String cityOfOrigin, String cityOfDestination, String outboundDate, String returnDate, String airline,
      int price){
            this.cityOfOrigin = cityOfOrigin;
            this.cityOfDestination = cityOfDestination;
            this.outboundDate = outboundDate;
            this.returnDate = returnDate;
            this.airline = airline;
            this.price = price;
      }

      public Flight() {}

      private String cityOfOrigin;
      private String cityOfDestination;
      private String outboundDate;
      private String returnDate;
      private String airline;
      private int price;

      public String getCityOfOrigin(){
            return cityOfOrigin;
      }

      public void setCityOfOrigin(String city){
            this.cityOfOrigin = city;
      }

      public String getCityOfDestination(){
            return cityOfDestination;
      }

      public void setCityOfDestination(String city){
            this.cityOfDestination = city;
      }

      public String getOutboundDate(){
            return outboundDate;
      }

      public void setOutBoundDate(String date){
            this.outboundDate = date;
      }

      public String getReturnDate(){
            return returnDate;
      }

      public void setReturnDate(String date){
            this.returnDate = date;
      }

      public String getAirline(){
            return airline;
      }

      public void setAirline(String airline){
            this.airline = airline;
      }

      public int getPrice(){
            return price;
      }

      public void setPrice(int price){
            this.price = price;
      }
      
}
