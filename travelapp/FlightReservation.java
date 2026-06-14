package travelapp;

public final class FlightReservation extends Reservation implements Bookable {
    private Flight flight;
    private String passengerName;
    private int passengerCount;

    public FlightReservation(int confirmationNumber, Flight flight, String passengerName, int passengerCount) {
        super(confirmationNumber);
        this.flight = flight;
        this.passengerName = passengerName;
        this.passengerCount = passengerCount;
    }

    // Polimorfisme dari interface Bookable
    @Override
    public void book() {
        flight.bookSeat(passengerCount);
    }

    @Override
    public void cancel() {
        flight.releaseSeat(passengerCount);
    }

    // Polimorfisme dari abstract class Reservation
    @Override
    public void displayDetails() {
        System.out.println("=== TIKET PENERBANGAN ===");
        System.out.println("No Konfirmasi : " + getConfirmationNumber());
        System.out.println("Nama Penumpang: " + passengerName);
        System.out.println("Detail Flight : " + flight.getOrigin() + " ke " + flight.getDestination() + " (" + flight.getFlightNumber() + ")");
        System.out.println("Jumlah Kursi  : " + passengerCount + " pax");
        System.out.println("Total Bayar   : Rp" + (flight.getPrice() * passengerCount));
        System.out.println("-------------------------");
    }
}