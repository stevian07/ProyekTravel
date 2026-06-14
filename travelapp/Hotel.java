package travelapp;

public class Hotel {
    private String hotelId;
    private String name;
    private String location;
    private double pricePerNight;

    public Hotel(String hotelId, String name, String location, double pricePerNight) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.pricePerNight = pricePerNight;
    }

    // Getter dan Setter Enkapsulasi
    public String getHotelId() { return hotelId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getPricePerNight() { return pricePerNight; }

    @Override
    public String toString() {
        return "[" + hotelId + "] " + name + " - " + location + " | Per malam: Rp" + pricePerNight;
    }
}