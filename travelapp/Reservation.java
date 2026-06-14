package travelapp;

// Menggunakan sealed class sesuai spesifikasi Java 17 untuk membatasi subclass
public sealed abstract class Reservation permits FlightReservation, HotelReservation {
    private int confirmationNumber;

    public Reservation(int confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    // Enkapsulasi: Getter untuk mengakses data private
    public int getConfirmationNumber() {
        return confirmationNumber;
    }

    // Metode abstrak yang wajib diimplementasikan oleh subclass concret
    public abstract void displayDetails();
}