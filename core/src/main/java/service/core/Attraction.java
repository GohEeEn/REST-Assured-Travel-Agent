package service.core;

public class Attraction {
    String name;
    String category;
    String type;
    String subtype;
    int rank;

    public Attraction() {}

    public Attraction(String name, String category, String type, String subtype, int rank){
        this.name = name;
        this.category = category;
        this.type = type;
        this.subtype = subtype;
        this.rank = rank;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSubtype() { return subtype; }
    public void setSubtype(String subtype) { this.subtype = subtype; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public String toString(){
        return "Name: " + getName() + "\nCategory: " + getCategory() + "\nType: " + getType() + "\nSubType: " + getSubtype() + "\nRank: " + getRank();
    }
}
