package service.core;

public class HotelRequest {
      
      public HotelRequest() {
            numberOfStarsRequiredForHotel = new int[4];
      }

      private String cityCode;
      private int numberOfGuests;
      private int[] numberOfStarsRequiredForHotel; //hotel stars. Up to four values can be requested at the same time in a comma separated list. Permitted values: 5,4,3,2,1

      public String getCityCode(){
            return cityCode;
      }

      public void setCityCode(String cityCode){
            this.cityCode = cityCode;
      }

      public int getNumberOfGuests(){
            return numberOfGuests;
      }

      public void setNumberOfGuests(int numberOfGuests){
            this.numberOfGuests = numberOfGuests;
      }

      public int[] getNumberOfStarsRequiredForHotel(){
            return numberOfStarsRequiredForHotel;
      }
      
      public Boolean setNumberOfStarsRequiredForHotel(int[] numberOfStarsRequiredForHotel){
            if (numberOfStarsRequiredForHotel.length < 5){
                  this.numberOfStarsRequiredForHotel = numberOfStarsRequiredForHotel;
                  return true;
            }
            return false;
      }

      public Boolean equalsNumberOfStarsRequiredForHotel(int[] numberOfStarsRequiredForHotel){
            if (this.numberOfStarsRequiredForHotel.length != numberOfStarsRequiredForHotel.length){
                  return false;
            }
      
            for (int i=0; i<numberOfStarsRequiredForHotel.length; i++){
                  if(this.numberOfStarsRequiredForHotel[i] != numberOfStarsRequiredForHotel[i]){
                       return false;
                  }
            }

            return true;
      }

      public Boolean equals(HotelRequest hotelRequest){
            if (this.getNumberOfGuests() == hotelRequest.getNumberOfGuests() &&
                  this.getCityCode().equals(hotelRequest.getCityCode()) &&
                  this.equalsNumberOfStarsRequiredForHotel(hotelRequest.getNumberOfStarsRequiredForHotel()) ) {
                         return true;
                   }
            return false;
      }
}
