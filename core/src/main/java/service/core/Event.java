package service.core;

public class Event {
    public Event(String organiser, String description, String location, int price, String targetGroup, String startTime, String endTime){
        this.organiser = organiser;
        this.description = description;
        this.location = location;
        this.price = price;
        this.targetGroup = targetGroup;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Event(){}

    private String organiser;
    private String description;
    private String location;
    private int price;
    private String targetGroup;
    private String startTime;
    private String endTime;

    public String getOrganiser(){return organiser;}
    public String getDescription() {return description;}
    public String getLocation() {return location;}
    public int getPrice() {return price;}
    public String getTargetGroup() {return targetGroup;}
    public String getStartTime() {return startTime;}
    public String getEndTime() {return endTime;}

    public void setOrganiser(String organiser) {this.organiser = organiser;}
    public void setDescription(String description) {this.description = description;}
    public void setLocation(String location) {this.location = location;}
    public void setPrice(int price) {this.price = price;}
    public void setTargetGroup(String targetGroup) {this.targetGroup = targetGroup;}
    public void setStartTime(String startTime) {this.startTime = startTime;}
    public void setEndTime(String endTime) {this.endTime = endTime;}

}
