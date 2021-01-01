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
            String flightInfo = "\n******************* FLIGHT *******************\n";
            flightInfo += "\nCity of Origin: " + cityOfOrigin + "\n";
            flightInfo += "City of Destination: " + cityOfDestination  + "\n";
            flightInfo += "Date of Departure: " + outboundDate + "\n";
            flightInfo += "Date of return: " + returnDate  + "\n";
            flightInfo += "Airline: " + airline  + "\n";
            flightInfo += "Price: " + price  + "\n";
            flightInfo += "Departure flight airport: " + originAirportName  + "\n";
            flightInfo += "Return flight airport: " + destinationAirportName + "\n";
            flightInfo += "\n******************* FLIGHT *******************\n";
            return flightInfo;
      }

      /**
	 * TODO (Barry): Must iimplement equals method 
	 */
      
}
