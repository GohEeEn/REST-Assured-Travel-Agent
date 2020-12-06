package quaranteam.events;

public class EventQuote {

      public EventQuote() {}

      private String organiser;
      private String description;
      private String location;
      private String price;
      private String targetGroup;
      private String startTime;
      private String endTime;

      public String getOrganiser(){ return organiser; }
      public void setOrganiser(String organiser){ this.organiser = organiser; }

      public String getDescription(){ return description; }
      public void setDescription(String description){ this.description = description; }

      public String getLocation(){ return location; }
      public void setLocation(String location){ this.location = location; }

      public String getPrice(){ return price; }
      public void setPrice(String price){ this.price = price; }

      public String getTargetGroup(){ return targetGroup; }
      public void setTargetGroup(String targetGroup){ this.targetGroup = targetGroup; }

      public String getStartTime(){ return startTime;}
      public void setStartTime(String startTime){ this.startTime = startTime; }

      public String getEndTime(){ return endTime; }
      public void setEndTime(String endTime){ this.endTime = endTime; }

}
