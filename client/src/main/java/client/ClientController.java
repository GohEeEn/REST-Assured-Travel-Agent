package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletResponse;
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
    private TravelPackage tp = new TravelPackage();
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

			// ClientBooking[] clientArray = new ClientBooking[1] ;
			// ClientBooking clientBooking = new ClientBooking();
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
            cityCodeGenerator();
			response.sendRedirect("/hotels");
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
            tp = Client.sendBookingToTravelAgent(flightRequest, hotelRequest);
			response.sendRedirect("/displayFlights");
        } 
    }

    @GetMapping("/displayFlights")
    public String displayflights(Model model){
        model.addAttribute("flightDetails", tp.getFlights());
        return "displayFlights.html";
    }
}

// //	@RequestMapping(value="/",method=RequestMethod.GET)
// //	@ResponseBody 
// 	@GetMapping("/")
// 	public String greeting(){
// 		return "index.html";
// 	}

// 	@RequestMapping(value="/processForm",method=RequestMethod.POST)  
// 	public void processForm(String name, String cityOfOrigin, String countryOfOrigin, String cityOfDestination, String countryOfDestination, boolean oneWayTrip, String returnDate, String outboundDate, String currency, HttpServletResponse response) throws IOException {

// 			ClientBooking[] clientArray = new ClientBooking[1] ;
// 			ClientBooking clientBooking = new ClientBooking();
// 			clientBooking.setName(name);
// 			clientBooking.setCityOfOrigin(cityOfOrigin);
// 			clientBooking.setCountryOfOrigin(countryOfOrigin);
// 			clientBooking.setCityOfDestination(cityOfDestination);
// 			clientBooking.setCountryOfDestination(countryOfDestination);
// 			clientBooking.setOneWayTrip(oneWayTrip);
// 			clientBooking.setReturnDate(returnDate);
// 			clientBooking.setOutboundDate(outboundDate);
// 			clientBooking.setCurrency(currency);
// 			clientArray[0] = clientBooking;
// 			Client.bookingAdventure(clientArray);
// 			response.sendRedirect("/");
// 	}
// }
// 



