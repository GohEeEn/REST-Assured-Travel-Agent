// package client;

// import java.io.IOException;
// import java.io.PrintWriter;
// import java.util.*;
// import java.io.File;
// import java.io.FileNotFoundException;
// import javax.servlet.http.HttpServletResponse;
// import service.core.FlightRequest;
// import service.core.HotelRequest;
// import service.core.TravelPackage;
// import client.Client;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ResponseBody;

// import org.json.simple.JSONObject;
// import org.json.simple.parser.JSONParser;

// import org.json.simple.*;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import org.json.simple.parser.ParseException; 






// /**
//  * Implementation of the AuldFellas insurance quotation service.
//  * 
//  * @author Rem
//  *
//  */
// @Controller
// public class ClientController { 
//     private HashMap<String, String> cityCodes = new HashMap<String, String>();
//     private FlightRequest flightRequest = new FlightRequest();
//     private HotelRequest hotelRequest = new HotelRequest();
//     private TravelPackage tp = new TravelPackage();
// //	@RequestMapping(value="/",method=RequestMethod.GET)
// //	@ResponseBody 
// 	@GetMapping("/")
// 	public String greeting(){
// 		return "index.html";
// 	}
	
// 	@GetMapping("/hotels")
// 	public String hotelsForm(){
// 		return "hotels.html";
// 	}
	
// 	@RequestMapping(value="/processFlightsForm",method=RequestMethod.POST)  
// 	public void processFlightsForm(String name, String cityOfOrigin, String countryOfOrigin, String cityOfDestination, String countryOfDestination, boolean oneWayTrip, String returnDate, String outboundDate, String currency, HttpServletResponse response) throws IOException {

// 			// ClientBooking[] clientArray = new ClientBooking[1] ;
// 			// ClientBooking clientBooking = new ClientBooking();
// 		flightRequest.setName(name);
// 		flightRequest.setCityOfOrigin(cityOfOrigin);
// 		flightRequest.setCountryOfOrigin(countryOfOrigin);
// 		flightRequest.setCityOfDestination(cityOfDestination);
// 		flightRequest.setCountryOfDestination(countryOfDestination);
// 		flightRequest.setOneWayTrip(oneWayTrip);
// 		flightRequest.setReturnDate(returnDate);
// 		flightRequest.setOutboundDate(outboundDate);
// 		flightRequest.setCurrency(currency);
//             // clientArray[0] = clientBooking;
//             cityCodeGenerator();
// 			response.sendRedirect("/hotels");
//     }
    
//     private void cityCodeGenerator(){
//         File file = new File("city_codes.txt");
//         try{
//             int i = 0;
//             Scanner sc = new Scanner(file);
//             while(sc.hasNextLine()) 
//             {   i++;
//                 String line = sc.nextLine();
//                 int index = line.lastIndexOf(" ")+1;
//                 String cityCode = line.substring(index);
//                 String cityName = line.substring(0,index);
//                 cityCodes.put(cityName.toLowerCase().trim() , cityCode.trim() );
//             }
//         }   
//         catch (FileNotFoundException e) {
//             e.printStackTrace();
//         }
//     }

//     @RequestMapping(value="/processHotelsForm",method=RequestMethod.POST)
//     public void processHotelsForm(String location, String guests, String checkIn, String checkOut, String minRatings, HttpServletResponse response) throws IOException
//     {

//         System.out.println("minrating is "+minRatings);
//         if(cityCodes.get(location.toLowerCase())==null){
//             PrintWriter out = response.getWriter();
//             out.println("<script>");
//             out.println("alert('" + "invalid city entered, enter again" + "');");
//             out.println("window.location.replace('" + "/hotels" + "');");
//             out.println("</script>");
//         }else {
//             hotelRequest.setCityCode(cityCodes.get(location.toLowerCase()));
//             hotelRequest.setNumberOfGuests(Integer.parseInt(guests));
            
//             int minNumOfStars = 0;
//             switch (minRatings){
//                 case "oneStar":
//                     minNumOfStars = 1;
//                     break;
//                 case "twoStar":
//                     minNumOfStars = 2;
//                     break;
//                 case "threeStar":
//                     minNumOfStars = 3;
//                     break;
//                 case "fourStar":
//                     minNumOfStars = 4;
//                     break;
//                 case "fiveStar":
//                     minNumOfStars = 5;
//                     break;
//             }

//             hotelRequest.setMinNumberOfStarsRequiredForHotel(minNumOfStars);
//             tp = Client.sendBookingToTravelAgent(flightRequest, hotelRequest);
// 			response.sendRedirect("/displayFlights");
//         } 
//     }

//     @GetMapping("/displayFlights")
//     public String displayflights(Model model){
//         model.addAttribute("flightDetails", tp.getFlights());
//         return "displayFlights.html";
//     }


//     /**
// 	 * The following method converts the array list of airport IDs to an array of airport IDs as we cannot pass a list
// 	 * using REST
// 	 * 
// 	 * @param countryName
// 	 * @return
// 	 */

// 	 public static String [] convertAirportIDsListToAirportIDsArray(ArrayList<String> airportIDsList){
		 
// 		String [] airportIDsArray = new String[airportIDsList.size()];

// 		int index = 0;
// 		for (String id : airportIDsList){
			
// 			airportIDsArray[index] = airportIDsList.get(index);
// 			index++;
// 		}
// 		return airportIDsArray;
// 	 }

// 	/**
// 	 * The following code is used to retrieve the country code for the country name given as this is needed for the Skyscanner API request
// 	 * 
// 	 * @param countryName
// 	 * @return countryCode
// 	 */

// 	public static String getListMarkets(String countryName) { 
		
// 		String countryCode = "";

// 		try {
// 			HttpRequest requestCode = HttpRequest.newBuilder()
// 				.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/reference/v1.0/countries/en-GB"))
// 				.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
// 				.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
// 				.method("GET", HttpRequest.BodyPublishers.noBody())
// 				.build();
// 			HttpResponse<String> response = HttpClient.newHttpClient().send(requestCode, HttpResponse.BodyHandlers.ofString());

// 			/**
// 			 * TODO may need to delete this response if examples.json isn't needed going forward
// 			 */
// 			// HttpResponse<Path> response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("countryCodes.json")));

// 			String countryCodes = response.body();
// 			JSONObject countryCodesJson = parseJSONObject(countryCodes);

// 			JSONArray countryCodesArray = new JSONArray();
// 	    		countryCodesArray = (JSONArray) countryCodesJson.get("Countries");
			
// 			//loop through array to find the country code 
// 			int index = 0;
// 			while (index < countryCodesArray.size()) {

// 				JSONObject jsonObject = (JSONObject) countryCodesArray.get(index);
// 				String name = (String) jsonObject.get("Name");
				
// 				if (name.equals(countryName)){
// 					countryCode = (String) jsonObject.get("Code");
// 				}
// 				index++;
// 			}	

// 		} catch(IOException e) {
//                   e.printStackTrace();
// 		}
// 		catch(InterruptedException e) {
//                   e.printStackTrace();
// 		}  
// 		return countryCode;
// 	}

// 	/**
// 	 * The following method will retrieve the airport IDs 
// 	 * 
// 	 * @param cityOfDestination
// 	 * @param countryOfDestination
// 	 * @param countryOfOriginCode
// 	 * @param currency
// 	 * @return
// 	 */

// 	// GET ListPlaces (Skyscanner API)
// 	public static ArrayList<String> getListPlaces(String cityOfDestination, String countryOfDestination, String locale, String countryOfOriginCode, String currency) { 
		
// 		ArrayList<String> airportIDs = new ArrayList();

// 		try {
// 			HttpRequest request = HttpRequest.newBuilder()
// 					.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/"+
// 						countryOfOriginCode+"/"+currency+"/"+locale+"/?query="+cityOfDestination+"%20"+countryOfDestination))
// 					.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
// 					.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
// 					.method("GET", HttpRequest.BodyPublishers.noBody())
// 					.build();
// 			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
// 			/**
// 			 * TODO may need to delete this response if airports.json isn't needed going forward
// 			 */
// 			// HttpResponse<Path> response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("airports.json")));
			
// 			System.out.println("Get ListPlaces: "+response.body());

// 			String places = response.body();
// 			JSONObject placesJson = parseJSONObject(places);

// 			JSONArray placesArray = new JSONArray();
// 		      placesArray = (JSONArray) placesJson.get("Places");
// 			System.out.println("Places array: "+placesArray);
			
// 			int index = 0;
// 			while (index < placesArray.size()) {
// 				JSONObject jsonObject = (JSONObject) placesArray.get(index);
// 				airportIDs.add((String) jsonObject.get("PlaceId"));
// 				index++;
// 			}	

// 		} catch(IOException e) {
//                   e.printStackTrace();
// 		}
// 		catch(InterruptedException e) {
//                   e.printStackTrace();
// 		}  
// 		return airportIDs;
// 	}

// 	/**
// 	 * The following code converts a given string to a JSON object
// 	 * 
// 	 * @param response
// 	 * @return jsonObject
// 	 */

// 	public static JSONObject parseJSONObject(String response){

// 		JSONObject jsonObject = new JSONObject();
// 		try{
// 			JSONParser parser = new JSONParser();
// 			jsonObject = (JSONObject) parser.parse(response);
// 		} catch (ParseException e) {
// 			e.printStackTrace();
// 		}
// 		return jsonObject;
// 	}




// }

// // //	@RequestMapping(value="/",method=RequestMethod.GET)
// // //	@ResponseBody 
// // 	@GetMapping("/")
// // 	public String greeting(){
// // 		return "index.html";
// // 	}

// // 	@RequestMapping(value="/processForm",method=RequestMethod.POST)  
// // 	public void processForm(String name, String cityOfOrigin, String countryOfOrigin, String cityOfDestination, String countryOfDestination, boolean oneWayTrip, String returnDate, String outboundDate, String currency, HttpServletResponse response) throws IOException {

// // 			ClientBooking[] clientArray = new ClientBooking[1] ;
// // 			ClientBooking clientBooking = new ClientBooking();
// // 			clientBooking.setName(name);
// // 			clientBooking.setCityOfOrigin(cityOfOrigin);
// // 			clientBooking.setCountryOfOrigin(countryOfOrigin);
// // 			clientBooking.setCityOfDestination(cityOfDestination);
// // 			clientBooking.setCountryOfDestination(countryOfDestination);
// // 			clientBooking.setOneWayTrip(oneWayTrip);
// // 			clientBooking.setReturnDate(returnDate);
// // 			clientBooking.setOutboundDate(outboundDate);
// // 			clientBooking.setCurrency(currency);
// // 			clientArray[0] = clientBooking;
// // 			Client.bookingAdventure(clientArray);
// // 			response.sendRedirect("/");
// // 	}
// // }
// // 





