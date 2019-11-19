package ca.ubc.cs304.model;

public class Branch {
    String location;
    String city;

    public Branch(String location, String city) {
        this.location = location;
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
