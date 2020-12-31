// package client;

// import java.io.IOException;
// import java.util.*;

// import javax.servlet.http.HttpServletResponse;
// import service.core.ClientBooking;
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



// /**
//  * Implementation of the AuldFellas insurance quotation service.
//  * 
//  * @author Rem
//  *
//  */
// @Controller
// public class ClientController { 
	
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



