package service.core;

import java.util.List;
import java.util.ArrayList;

public class Hotel{

      public Hotel() {}

      private String price;
      private String roomType;
      private String bedType;
      private String description;
      private String address;
      private List<String> amenities = new ArrayList();

      public String getPrice(){
            return price;
      }

      public void setPrice(String price){
            this.price = price;
      }

      public String getRoomType(){
            return roomType;
      }

      public void setRoomType(String roomType){
            this.roomType = roomType;
      }

      public String getBedType(){
            return bedType;
      }

      public void setBedType(String bedType){
            this.bedType = bedType;
      }

      public String getDescription(){
            return description;
      }

      public void setDescription(String description){
            this.description = description;
      }

      public String getAddress(){
            return address;
      }

      public void setAddress(String address){
            this.address = address;
      }

      public List<String> getAmenities(){
            return amenities;
      }

      public void setAmenities(String amenity){
            amenities.add(amenity);
      }

}
