package service.core;

import java.util.List;
import java.util.ArrayList;

public class Hotel{

      public Hotel() {}

      private String price;  //offers
      private String roomType; // offers (category)
      private String bedType;  // offers
      private String description; // offers
      private String address;  // hotel
      private List<String> amenities = new ArrayList();  // hotel
      private String rating;
      private String phoneNumber;
      private String name;

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

      public String getRating(){
            return rating;
      }

      public void setRating(String rating){
            this.rating = rating;
      }

      public String getPhoneNumber(){
            return phoneNumber;
      }

      public void setPhoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
      }

      public String getName(){
            return name;
      }

      public void setName(String name){
            this.name = name;
      }

      public String toString(){
            String string = "\n******************* HOTEL *******************\n";
            string += "\nPrice: " + price +"\n";
            string += "Room Type: " + roomType +"\n";
            string += "Bed Type: " + bedType +"\n";
            string += "Description: " + description +"\n";
            string += "Address: " + address +"\n";
            string += "Amenities: " + amenities +"\n";;
            string += "Rating: " + rating +"\n";
            string += "PhoneNumber: " + phoneNumber +"\n";
            string += "Name: " + name +"\n";
            string += "\n******************* HOTEL *******************\n";
            
            return string;
      }

}
