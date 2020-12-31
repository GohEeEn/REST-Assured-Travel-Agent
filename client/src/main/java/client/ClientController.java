package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import service.core.ClientBooking;
import service.core.HotelRequest;
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
			clientBooking.setName(name);
			clientBooking.setCityOfOrigin(cityOfOrigin);
			clientBooking.setCountryOfOrigin(countryOfOrigin);
			clientBooking.setCityOfDestination(cityOfDestination);
			clientBooking.setCountryOfDestination(countryOfDestination);
			clientBooking.setOneWayTrip(oneWayTrip);
			clientBooking.setReturnDate(returnDate);
			clientBooking.setOutboundDate(outboundDate);
			clientBooking.setCurrency(currency);
            // clientArray[0] = clientBooking;
            cityCodeGenerator();
			response.sendRedirect("/hotels");
    }
    
    private void cityCodeGenerator(){
        Scanner sc = new Scanner("city_codes.txt");
        while(sc.hasNextLine()) 
        {   
            String line = sc.nextLine();
            int index = line.lastIndexOf(" ")+1;
            String cityCode = line.substring(index);
            String cityName = line.substring(0,index);
	        cityCodes.put(cityName.toLowerCase() , cityCode );
        }
    }

    @RequestMapping(value="/processHotelsForm",method=RequestMethod.POST)
    public void processHotelsForm(String location, String rooms, String checkIn, String checkOut, String minRating, HttpServletResponse response) throws IOException
    {
        if(cityCodes.get(location.toLowerCase())==null){
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('" + "invalid city entered, enter again" + "');");
            out.println("window.location.replace('" + "/hotels" + "');");
            out.println("</script>");
        }else {
			response.sendRedirect("/");
        }
        
    }  
}




