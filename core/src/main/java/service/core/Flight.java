package service.core;

public class Flight {

      public Flight(String cityOfOrigin, String cityOfDestination, String outboundDate, String returnDate, String airline,
      String price, String originAirportName, String destinationAirportName){
            this.cityOfOrigin = cityOfOrigin;
            this.cityOfDestination = cityOfDestination;
            this.outboundDate = outboundDate;
            this.returnDate = returnDate;
            this.airline = airline;
            this.price = price;
            this.originAirportName = originAirportName;
            this.destinationAirportName = destinationAirportName;
      }

      public Flight() {}

      private String cityOfOrigin;
      private String cityOfDestination;
      private String outboundDate;
      private String returnDate;
      private String airline;
      private String price;
      private String originAirportName;
      private String destinationAirportName;

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

      public String getPrice(){
            return price;
      }

      public void setPrice(String price){
            this.price = price;
      }

      public String getOriginAirportName(){
            return originAirportName;
      }

      public void setOriginAirportName(String originAirportName){
            this.originAirportName = originAirportName;
      }

      public String getDestinationAirportName(){
            return destinationAirportName;
      }

      public void setDestinationAirportName(String destinationAirportName){
            this.destinationAirportName = destinationAirportName;
      }

      public String toString(){
            String flightInfo = "";
            flightInfo += cityOfOrigin + "\n" + cityOfDestination  + "\n" + outboundDate + "\n" + returnDate  + "\n";
            flightInfo += airline  + "\n" + price  + "\n" + originAirportName  + "\n" + destinationAirportName;
            return flightInfo;
      }
      
}
