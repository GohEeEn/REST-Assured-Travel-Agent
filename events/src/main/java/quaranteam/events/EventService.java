package quaranteam.events;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.core.Event;

import java.io.FileReader;
import java.io.IOException;

@RestController
public class EventService {


	// POST request, handles all booking requests from travel agent
	@RequestMapping(value="/events",method=RequestMethod.POST)
	public String createBooking(String description) {

		Event[] events = new Event[12];

		try (FileReader reader = new FileReader("events.json"))
		{

			JSONParser jsonParser = new JSONParser();
			//Read JSON file
			Object obj = jsonParser.parse(reader);
			System.out.println(obj);


			events = parseEventObject( (JSONObject) obj);     // add event info to list

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		for(Event ev : events){
			if(description.equals(ev.getDescription())){

				//code the actual booking

				return "Event booked.";
			}
		}
	return "No such event exists. Please try again.";
	}

	public Event createEvent(EventQuote eventQuote){
		Event s = new Event();
		s.setOrganiser(eventQuote.getOrganiser());
		s.setDescription(eventQuote.getDescription());
		s.setLocation(eventQuote.getLocation());
		s.setPrice(Integer.parseInt(eventQuote.getPrice()));
		s.setTargetGroup(eventQuote.getTargetGroup());
		s.setStartTime(eventQuote.getStartTime());
		s.setEndTime(eventQuote.getEndTime());
		
		return s;
	}

	private static Event[] parseEventObject(JSONObject event)
	{
	    Event[] eventList = new Event[12];

	    //Get places array within outer array
	    JSONArray jsonArray;
	    jsonArray = (JSONArray) event.get("Events");

	    for(int i = 0;i<jsonArray.size();i++){
			JSONObject ob = (JSONObject) jsonArray.get(i);    // testing
			System.out.println("Object: " + ob);
			Event ev = new Event(ob.get("Organiser").toString(),ob.get("Name").toString(),ob.get("Location").toString(),
					Integer.parseInt(ob.get("Price").toString()),ob.get("Group").toString(),ob.get("Start time").toString(),ob.get("End time").toString());
			eventList[i] = ev;
		}
	    return eventList;
	}
	
	// // GET request, returns the quotation with the reference passed as argument
	// @RequestMapping(value="/quotations/{reference}",method=RequestMethod.GET)
	// 	public Quotation getResource(@PathVariable("reference") String reference) {
	// 	Quotation quotation = quotations.get(reference);
	// 	if (quotation == null) throw new NoSuchQuotationException();
	// 	return quotation;
	// }

	// // If there is no quotation listed with the given reference after calling GET method then throw this exception
	// @ResponseStatus(value = HttpStatus.NOT_FOUND)
	// public class NoSuchQuotationException extends RuntimeException {
	// 	static final long serialVersionUID = -6516152229878843037L;
	// }
}