package client;

import java.io.IOException;
<<<<<<< HEAD
=======
import java.io.PrintWriter;
>>>>>>> 8a3675545a4fb007e34a27c0e0019b23399c444f
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletResponse;
import service.core.ClientBooking;
<<<<<<< HEAD
=======
import service.core.HotelRequest;
>>>>>>> 8a3675545a4fb007e34a27c0e0019b23399c444f
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
<<<<<<< HEAD
	
//	@RequestMapping(value="/",method=RequestMethod.GET)
//	@ResponseBody 
	@GetMapping("/")
	public String greeting(){
		return "index.html";
	}

	@RequestMapping(value="/processForm",method=RequestMethod.POST)  
	public void processForm(String name, String cityOfOrigin, String countryOfOrigin, String cityOfDestination, String countryOfDestination, boolean oneWayTrip, String returnDate, String outboundDate, String currency, HttpServletResponse response) throws IOException {

			ClientBooking[] clientArray = new ClientBooking[1] ;
			ClientBooking clientBooking = new ClientBooking();
=======
    private HashMap<String, String> cityCodes = new HashMap<String, String>();
    private ClientBooking clientBooking = new ClientBooking();
    private HotelRequest hotelRequest = new HotelRequest();
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
>>>>>>> 8a3675545a4fb007e34a27c0e0019b23399c444f
			clientBooking.setName(name);
			clientBooking.setCityOfOrigin(cityOfOrigin);
			clientBooking.setCountryOfOrigin(countryOfOrigin);
			clientBooking.setCityOfDestination(cityOfDestination);
			clientBooking.setCountryOfDestination(countryOfDestination);
			clientBooking.setOneWayTrip(oneWayTrip);
			clientBooking.setReturnDate(returnDate);
			clientBooking.setOutboundDate(outboundDate);
			clientBooking.setCurrency(currency);
<<<<<<< HEAD
			clientArray[0] = clientBooking;
			Client.bookingAdventure(clientArray);
			response.sendRedirect("/");
	}
}

=======
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
            
            int minNumOfStars;
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
            
			response.sendRedirect("/");
        }
        
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
>>>>>>> 8a3675545a4fb007e34a27c0e0019b23399c444f





