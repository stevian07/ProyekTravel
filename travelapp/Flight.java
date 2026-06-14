package travelapp;

public class Flight {
    private String flightNumber;
    private String origin;
    private String destination;
    private String date;
    private int availableSeats;
    private double price;

    public Flight(String flightNumber, String origin, String destination, String date, int availableSeats, double price) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    // Getter dan Setter untuk Enkapsulasi data
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public int getAvailableSeats() { return availableSeats; }
    public double getPrice() { return price; }

    public void bookSeat(int passengers) {
        this.availableSeats -= passengers;
    }

    public void releaseSeat(int passengers) {
        this.availableSeats += passengers;
    }

    @Override
    public String toString() {
        return "[" + flightNumber + "] " + origin + " -> " + destination + " (" + date + ") | Kursi: " + availableSeats + " | Harga: Rp" + price;
    }
}