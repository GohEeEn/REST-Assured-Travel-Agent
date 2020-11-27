package service.core;

public class ClientBooking {

	
	public ClientBooking(String name, String cityOfOrigin, String countryOfOrigin, String cityOfDestination,
	String countryOfDestination, boolean oneWayTrip, String returnDate, String outboundDate, String currency) {
		this.name = name;
		this.cityOfOrigin = cityOfOrigin; 
		this.countryOfOrigin = countryOfOrigin;
		this.cityOfDestinaton = cityOfDestination;
		this.countryOfDestination = countryOfDestination;
		this.oneWayTrip = oneWayTrip;
		this.outboundDate = outboundDate;
		this.returnDate = returnDate;
		this.currency = currency;
	}
	
	public ClientBooking() {}

	private String name;
	private String cityOfOrigin;
	private String countryOfOrigin;
	private String cityOfDestinaton;
	private String countryOfDestination;
	private boolean oneWayTrip;
	private String returnDate;  //format: yyyy-mm-dd
	private String outboundDate; //format: yyyy-mm-dd or null
	private String currency;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getCityOfOrigin(){
		return cityOfOrigin;
	}

	public void setCityOfOrigin(String city){
		this.cityOfOrigin = city;
	}

	public String getCountryOfOrigin(){
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(String country){
		this.countryOfOrigin = country;
	}


	public String getCityOfDestination(){
		return cityOfDestinaton;
	}

	public void setCityOfDestination(String city){
		this.cityOfDestinaton = city;
	}

	public String getCountryOfDestination(){
		return countryOfDestination;
	}

	public void setCountryOfDestination(String country){
		this.countryOfDestination = country;
	}

	public boolean getOneWayTrip(){
		return oneWayTrip;
	}

	public void setOneWayTrip(boolean oneWayTrip){
		this.oneWayTrip = oneWayTrip;
	}

	public String getReturnDate(){
		return returnDate;
	}

	public void setReturnDate(String returnDate){
		this.returnDate = returnDate;
	}

	public String getOutboundDate(){
		return outboundDate;
	}

	public void setOutboundDate(String outboundDate){
		this.outboundDate = outboundDate;
	}

	public String getCurrency(){
		return currency;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}
}