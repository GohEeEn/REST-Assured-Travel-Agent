package service.core;

public class ClientBookingInfo {

	
	public ClientBookingInfo(String name, String cityOfOrigin, String countryOfOrigin, String cityOfDestination,
	String countryOfDestination, boolean oneWayTrip, String inboundDate, String outboundDate, String currency) {
		this.name = name;
		this.cityOfOrigin = cityOfOrigin; 
		this.countryOfOrigin = countryOfOrigin;
		this.cityOfDestinaton = cityOfDestination;
		this.countryOfDestination = countryOfDestination;
		this.oneWayTrip = oneWayTrip;
		this.inboundDate = inboundDate;
		this.outboundDate = outboundDate;
		this.currency = currency;
	}
	
	public ClientBookingInfo() {}

	private String name;
	private String cityOfOrigin;
	private String countryOfOrigin;
	private String cityOfDestinaton;
	private String countryOfDestination;
	private boolean oneWayTrip;
	private String inboundDate;  //format: yyyy-mm-dd
	private String outboundDate; //format: yyyy-mm-dd or null
	private String currency;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getcityOfOrigin(){
		return cityOfOrigin;
	}

	public void setcityOfOrigin(String city){
		this.cityOfOrigin = city;
	}

	public String getcountryOfOrigin(){
		return countryOfOrigin;
	}

	public void setcountryOfOrigin(String country){
		this.countryOfOrigin = country;
	}


	public String getcityOfDestination(){
		return cityOfDestinaton;
	}

	public void setcityOfDestination(String city){
		this.cityOfDestinaton = city;
	}

	public String getcountryOfDestination(){
		return countryOfDestination;
	}

	public void setcountryOfDestination(String country){
		this.countryOfDestination = country;
	}

	public boolean getOneWayTrip(){
		return oneWayTrip;
	}

	public void setOneWayTrip(boolean oneWayTrip){
		this.oneWayTrip = oneWayTrip;
	}

	public String getInboundDate(){
		return inboundDate;
	}

	public void setInboundDate(String inboundDate){
		this.inboundDate = inboundDate;
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
