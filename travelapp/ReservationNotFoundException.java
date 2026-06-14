package travelapp;

// Custom exception untuk menangani error pencarian kode booking
public class ReservationNotFoundException extends Exception {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}