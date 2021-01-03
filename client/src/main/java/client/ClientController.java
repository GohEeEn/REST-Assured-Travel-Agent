package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletResponse;

import service.core.ActivityRequest;
import service.core.ClientResponse;
import service.core.FlightRequest;
import service.core.HotelRequest;
import service.core.TravelPackage;
import client.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// import org.json.simple.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;





/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
@Controller
public class ClientController { 
    private HashMap<String, String> cityCodes = new HashMap<String, String>();
    private FlightRequest flightRequest = new FlightRequest();
	private HotelRequest hotelRequest = new HotelRequest();
	private ActivityRequest activityRequest = new ActivityRequest();
	private TravelPackage tp = new TravelPackage();
	private ClientResponse cr = new ClientResponse();
	final static String locale = "en-GB";
//	@RequestMapping(value="/",method=RequestMethod.GET)
//	@ResponseBody 
	@GetMapping("/")
	public String greeting(){
		return "index.html";
	}
	
	@GetMapping("/hotels")
	public String hotelsForm(){
		return "hotels.html";
	}
	
	@RequestMapping(value="/processFlightsForm",method=RequestMethod.POST)  
	public void processFlightsForm(String name, String cityOfOrigin, String countryOfOrigin, String cityOfDestination, String countryOfDestination, boolean oneWayTrip, String returnDate, String outboundDate, String currency, HttpServletResponse response) throws IOException {

		cityCodeGenerator();
			// ClientBooking[] clientArray = new ClientBooking[1] ;
			// ClientBooking clientBooking = new ClientBooking();
		
			String capOriginCountry = countryOfOrigin.substring(0, 1).toUpperCase() + countryOfOrigin.substring(1).toLowerCase();
			String capDestinaptionCountry = countryOfDestination.substring(0,1).toUpperCase() + countryOfDestination.substring(1).toLowerCase();
			System.out.println("caporigincountry is = "+capOriginCountry);
			System.out.println("capDestncountry is = "+capDestinaptionCountry);
			String originCountryCode = getListMarkets(capOriginCountry);
			String destinatonCountryCode = getListMarkets(capDestinaptionCountry);

			if(originCountryCode.isEmpty()){
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('" + "invalid country of origin entered, enter again" + "');");
				out.println("window.location.replace('" + "/" + "');");
				out.println("</script>");
			}
			else{
				if(destinatonCountryCode.isEmpty()){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('" + "invalid country of destination entered, enter again" + "');");
					out.println("window.location.replace('" + "/" + "');");
					out.println("</script>");
				}
				else{
					if(cityCodes.get(cityOfOrigin.toLowerCase())==null){
						PrintWriter out = response.getWriter();
						out.println("<script>");
						out.println("alert('" + "invalid city of origin entered, enter again" + "');");
						out.println("window.location.replace('" + "/" + "');");
						out.println("</script>");
					}
					else{
						if(cityCodes.get(cityOfDestination.toLowerCase())==null){
							PrintWriter out = response.getWriter();
							out.println("<script>");
							out.println("alert('" + "invalid city of destination entered, enter again" + "');");
							out.println("window.location.replace('" + "/" + "');");
							out.println("</script>");
						}
						else{
							activityRequest.setCountry(capDestinaptionCountry);

					ArrayList<String> originAirportIDs = new ArrayList();          // Holds all airports for the given origin city
					ArrayList<String> destinationAirportIDs = new ArrayList();      //Holds all airports for the given destination city

					flightRequest.setCountryOfOriginCode(originCountryCode);
					System.out.println(flightRequest.getCountryOfOriginCode());
					flightRequest.setCountryOfDestinationCode(destinatonCountryCode);
					System.out.println(flightRequest.getCountryOfDestinationCode());

					originAirportIDs = getListPlaces(cityOfOrigin, countryOfOrigin, originCountryCode, currency);
					String [] originAirportIDsArray = convertAirportIDsListToAirportIDsArray(originAirportIDs);    // converts list to array
					
					destinationAirportIDs = getListPlaces(cityOfDestination, countryOfDestination, destinatonCountryCode, currency); 
					String [] destAirportIDsArray = convertAirportIDsListToAirportIDsArray(destinationAirportIDs);    // converts list to array
					
					System.out.println("\n ORIGIN AIRPORT IDS: "+originAirportIDsArray+"\n");
					for(String s : originAirportIDsArray){
						System.out.println(s);
					}
					System.out.println("\n ORIGIN AIRPORT IDS: "+destAirportIDsArray+"\n");
					for(String s : destAirportIDsArray){
						System.out.println(s);
					}
					flightRequest.setOriginAirortIDs(originAirportIDsArray);
					flightRequest.setDestAirortIDs(destAirportIDsArray);
					flightRequest.setName(name);
					flightRequest.setCityOfOrigin(cityOfOrigin);
					flightRequest.setCountryOfOrigin(countryOfOrigin);
					flightRequest.setCityOfDestination(cityOfDestination);
					flightRequest.setCountryOfDestination(countryOfDestination);
					flightRequest.setOneWayTrip(oneWayTrip);
					flightRequest.setReturnDate(returnDate);
					flightRequest.setOutboundDate(outboundDate);
					flightRequest.setCurrency(currency);
            // clientArray[0] = clientBooking;
					response.sendRedirect("/hotels");
	}
}
				}
			}
		}
    
    private void cityCodeGenerator(){
        File file = new File("city_codes.txt");
        try{
            int i = 0;
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) 
            {   i++;
                String line = sc.nextLine();
                int index = line.lastIndexOf(" ")+1;
                String cityCode = line.substring(index);
                String cityName = line.substring(0,index);
                cityCodes.put(cityName.toLowerCase().trim() , cityCode.trim() );
            }
        }   
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value="/processHotelsForm",method=RequestMethod.POST)
    public void processHotelsForm(String location, String guests, String checkIn, String checkOut, String minRatings, HttpServletResponse response) throws IOException
    {

        System.out.println("minrating is "+minRatings);
        if(cityCodes.get(location.toLowerCase())==null){
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('" + "invalid city entered, enter again" + "');");
            out.println("window.location.replace('" + "/hotels" + "');");
            out.println("</script>");
        }else {
			activityRequest.setCity(location.toLowerCase());
            hotelRequest.setCityCode(cityCodes.get(location.toLowerCase()));
            hotelRequest.setNumberOfGuests(Integer.parseInt(guests));
            
            int minNumOfStars = 0;
            switch (minRatings){
                case "oneStar":
                    minNumOfStars = 1;
                    break;
                case "twoStar":
                    minNumOfStars = 2;
                    break;
                case "threeStar":
                    minNumOfStars = 3;
                    break;
                case "fourStar":
                    minNumOfStars = 4;
                    break;
                case "fiveStar":
                    minNumOfStars = 5;
                    break;
            }

            hotelRequest.setMinNumberOfStarsRequiredForHotel(minNumOfStars);
            tp = Client.sendBookingToTravelAgent(flightRequest, hotelRequest, activityRequest);
			response.sendRedirect("/displayFlights");
        } 
    }

	@RequestMapping(value="/userFlightSelection",method=RequestMethod.POST)
	public void userFlightSelection(String inputFlightIndex, HttpServletResponse response) throws IOException
	{
		boolean isNumeric = inputFlightIndex.chars().allMatch( Character::isDigit );
		if(!isNumeric){
			PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('" + "invalid index entered, enter again" + "');");
            out.println("window.location.replace('" + "/displayFlights" + "');");
            out.println("</script>");
		}
		else{
			int i = Integer.parseInt(inputFlightIndex);
			if(i<0 || (i == 0 && tp.getFlights().length!=0) || (i!=0 && i>tp.getFlights().length)){
				PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('" + "invalid index entered, enter again" + "');");
            out.println("window.location.replace('" + "/displayFlights" + "');");
            out.println("</script>");
			}
			else{
				System.out.println("chosen index is = "+i);
				if (i==0){
					response.sendRedirect("/");
				}
				else{
					cr.setFlightReferenceNumber(tb.getFlights()[i-1].getReferenceNumber());
					response.sendRedirect("/displayHotels");
				}
			}
		}
	}

	@RequestMapping(value="/userHotelSelection",method=RequestMethod.POST)
	public void userHotelSelection(String inputHotelIndex, HttpServletResponse response) throws IOException
	{
		boolean isNumeric = inputHotelIndex.chars().allMatch( Character::isDigit );
		if(!isNumeric){
			PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('" + "invalid index entered, enter again" + "');");
            out.println("window.location.replace('" + "/displayHotels" + "');");
            out.println("</script>");
		}
		else{
			int i = Integer.parseInt(inputHotelIndex);
			if(i<0 || (i == 0 && tp.getHotels().length!=0) || (i!=0 && i>tp.getHotels().length)){
				PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('" + "invalid index entered, enter again" + "');");
            out.println("window.location.replace('" + "/displayHotels" + "');");
            out.println("</script>");
			}
			else{
				System.out.println("chosen index is = "+i);
				if (i!=0){
					cr.setHotelReferenceNumber(tb.getHotels()[i-1].getReferenceNumber());
				}
				else{
					cr.setHotelReferenceNumber(0);
				}
				Client.sendBookinChoicesToTravelTragent(cr);
				response.sendRedirect("/");
			}
		}
	}

    @GetMapping("/displayFlights")
    public String displayflights(Model model){
        model.addAttribute("flightDetails", tp.getFlights());
        return "displayFlights.html";
    }
    
    @GetMapping("/displayHotels")
    public String displayhotels(Model model){
        model.addAttribute("hotelDetails", tp.getHotels());
        return "displayHotels.html";
    }
    
    

    /**
	 * The following method converts the array list of airport IDs to an array of airport IDs as we cannot pass a list
	 * using REST
	 * 
	 * @param countryName
	 * @return
	 */

	 public static String [] convertAirportIDsListToAirportIDsArray(ArrayList<String> airportIDsList){
		 
		String [] airportIDsArray = new String[airportIDsList.size()];

		int index = 0;
		for (String id : airportIDsList){
			
			airportIDsArray[index] = airportIDsList.get(index);
			index++;
		}
		return airportIDsArray;
	 }

	/**
	 * The following code is used to retrieve the country code for the country name given as this is needed for the Skyscanner API request
	 * 
	 * @param countryName
	 * @return countryCode
	 */

	public static String getListMarkets(String countryName) { 
		
		String countryCode = "";

		try {
			HttpRequest requestCode = HttpRequest.newBuilder()
				.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/reference/v1.0/countries/en-GB"))
				.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
				.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(requestCode, HttpResponse.BodyHandlers.ofString());

			/**
			 * TODO may need to delete this response if examples.json isn't needed going forward
			 */
			// HttpResponse<Path> response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("countryCodes.json")));

			String countryCodes = response.body();
			JSONObject countryCodesJson = parseJSONObject(countryCodes);

			JSONArray countryCodesArray = new JSONArray();
	    		countryCodesArray = (JSONArray) countryCodesJson.get("Countries");
			
			//loop through array to find the country code 
			int index = 0;
			while (index < countryCodesArray.size()) {

				JSONObject jsonObject = (JSONObject) countryCodesArray.get(index);
				String name = (String) jsonObject.get("Name");
				
				if (name.equals(countryName)){
					countryCode = (String) jsonObject.get("Code");
				}
				index++;
			}	

		} catch(IOException e) {
                  e.printStackTrace();
		}
		catch(InterruptedException e) {
                  e.printStackTrace();
		}  
		return countryCode;
	}

	/**
	 * The following method will retrieve the airport IDs 
	 * 
	 * @param cityOfDestination
	 * @param countryOfDestination
	 * @param countryOfOriginCode
	 * @param currency
	 * @return
	 */

	// GET ListPlaces (Skyscanner API)
	public static ArrayList<String> getListPlaces(String cityOfDestination, String countryOfDestination, String countryOfOriginCode, String currency) { 
		
		ArrayList<String> airportIDs = new ArrayList();

		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/"+
						countryOfOriginCode+"/"+currency+"/"+locale+"/?query="+cityOfDestination+"%20"+countryOfDestination))
					.header("x-rapidapi-key", "91b7d3fc53mshf8b9bac5b6fd091p118e46jsn22debfe2cd83")
					.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			/**
			 * TODO may need to delete this response if airports.json isn't needed going forward
			 */
			// HttpResponse<Path> response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("airports.json")));
			
			System.out.println("Get ListPlaces: "+response.body());

			String places = response.body();
			JSONObject placesJson = parseJSONObject(places);

			JSONArray placesArray = new JSONArray();
		      placesArray = (JSONArray) placesJson.get("Places");
			System.out.println("Places array: "+placesArray);
			
			int index = 0;
			while (index < placesArray.size()) {
				JSONObject jsonObject = (JSONObject) placesArray.get(index);
				airportIDs.add((String) jsonObject.get("PlaceId"));
				index++;
			}
		} catch(IOException e) {
                  e.printStackTrace();
		}
		catch(InterruptedException e) {
                  e.printStackTrace();
		}  
		return airportIDs;
	}

	/**
	 * The following code converts a given string to a JSON object
	 * 
	 * @param response
	 * @return jsonObject
	 */

	public static JSONObject parseJSONObject(String response){

		JSONObject jsonObject = new JSONObject();
		try{
			JSONParser parser = new JSONParser();
			jsonObject = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}





