package travelapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TravelApp {
    private List<Flight> flightInventory = new ArrayList<>();
    private List<Hotel> hotelInventory = new ArrayList<>();
    private List<Reservation> activeReservations = new ArrayList<>();
    private Random random = new Random();

    public TravelApp() {
        initMockData();
    }

    // Data awal aplikasi (dummy data)
    private void initMockData() {
        flightInventory.add(new Flight("GA123", "Jakarta", "Bali", "2026-07-01", 50, 1200000));
        flightInventory.add(new Flight("GA124", "Jakarta", "Bali", "2026-07-01", 10, 1500000));
        flightInventory.add(new Flight("AK456", "Jakarta", "Surabaya", "2026-07-02", 30, 700000));
        
        hotelInventory.add(new Hotel("H01", "Grand Hyatt", "Bali", 2000000));
        hotelInventory.add(new Hotel("H02", "Aston Hotel", "Bali", 850000));
        hotelInventory.add(new Hotel("H03", "Luminor", "Surabaya", 500000));
    }

    // Menggunakan Stream dan Lambda untuk penyaringan koleksi
    public List<Flight> searchFlights(String from, String to, String date) {
        return flightInventory.stream()
                .filter(f -> f.getOrigin().equalsIgnoreCase(from) && 
                             f.getDestination().equalsIgnoreCase(to) && 
                             f.getDate().equals(date))
                .toList();
    }

    public List<Hotel> searchHotels(String location) {
        return hotelInventory.stream()
                .filter(h -> h.getLocation().equalsIgnoreCase(location))
                .toList();
    }

    public int generateConfirmationNumber() {
        // Generate acak 6 digit nomor konfirmasi
        return 100000 + random.nextInt(900000);
    }

    public void processFlightBooking(Flight flight, String name, int pax) {
        int confNum = generateConfirmationNumber();
        FlightReservation fr = new FlightReservation(confNum, flight, name, pax);
        fr.book(); // Update sisa kursi
        activeReservations.add(fr);
        System.out.println("\nBooking Berhasil! Nomor Konfirmasi Anda: " + confNum);
    }

    public void processHotelBooking(Hotel hotel, String name, int days) {
        int confNum = generateConfirmationNumber();
        HotelReservation hr = new HotelReservation(confNum, hotel, name, days);
        hr.book();
        activeReservations.add(hr);
        System.out.println("\nBooking Berhasil! Nomor Konfirmasi Anda: " + confNum);
    }

    // Implementasi Pattern Matching (Java 14/17 instanceof) untuk pembatalan
    public void cancelReservation(int confNumber) throws ReservationNotFoundException {
        Reservation match = null;
        for (Reservation r : activeReservations) {
            if (r.getConfirmationNumber() == confNumber) {
                match = r;
                break;
            }
        }

        if (match == null) {
            throw new ReservationNotFoundException("Error: Nomor konfirmasi " + confNumber + " tidak ditemukan!");
        }

        // Pattern matching: langsung binding objek tanpa casting manual
        if (match instanceof FlightReservation fr) {
            fr.cancel(); // Mengembalikan kapasitas kursi
            System.out.println("Reservasi Penerbangan Berhasil Dibatalkan.");
        } else if (match instanceof HotelReservation hr) {
            hr.cancel();
            System.out.println("Reservasi Hotel Berhasil Dibatalkan.");
        }

        activeReservations.remove(match);
    }

    public void showAllReservations() {
        if (activeReservations.isEmpty()) {
            System.out.println("Belum ada reservasi aktif.");
            return;
        }
        System.out.println("\n=== DAFTAR SEMUA RESERVASI AKTIF ===");
        // Menggunakan lambda expression untuk iterasi koleksi
        activeReservations.forEach(Reservation::displayDetails);
    }
}