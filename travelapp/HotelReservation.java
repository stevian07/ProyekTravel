package travelapp;

public final class HotelReservation extends Reservation implements Bookable {
    private Hotel hotel;
    private String guestName;
    private int durationDays;

    public HotelReservation(int confirmationNumber, Hotel hotel, String guestName, int durationDays) {
        super(confirmationNumber);
        this.hotel = hotel;
        this.guestName = guestName;
        this.durationDays = durationDays;
    }

    @Override
    public void book() {
        // Logika internal booking kamar hotel (bisa dikembangkan)
        System.out.println("Kamar di " + hotel.getName() + " berhasil dialokasikan.");
    }

    @Override
    public void cancel() {
        System.out.println("Kamar di " + hotel.getName() + " telah dilepas kembali.");
    }

    @Override
    public void displayDetails() {
        System.out.println("=== VOUCHER HOTEL ===");
        System.out.println("No Konfirmasi : " + getConfirmationNumber());
        System.out.println("Nama Tamu     : " + guestName);
        System.out.println("Nama Hotel    : " + hotel.getName() + " (" + hotel.getLocation() + ")");
        System.out.println("Durasi Menginap: " + durationDays + " malam");
        System.out.println("Total Bayar   : Rp" + (hotel.getPricePerNight() * durationDays));
        System.out.println("-------------------------");
    }
}