package travelapp;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// =========================================================================
// 1. INTERFACE & ABSTRACT CLASS (STRUKTUR UTAMA OOP)
// =========================================================================

// Antarmuka untuk objek yang memiliki kemampuan untuk dipesan dan dibatalkan
interface Bookable {
    void book();
    void cancel();
}

// Implementasi Pengendalian Pewarisan dengan Sealed Class (Fitur Java 17)
sealed abstract class Reservation permits FlightReservation, HotelReservation {
    private int confirmationNumber; // Enkapsulasi: Menggunakan private field

    public Reservation(int confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    // Getter untuk akses enkapsulasi data
    public int getConfirmationNumber() {
        return confirmationNumber;
    }

    // Metode abstrak untuk polimorfisme tampilan detail
    public abstract void displayDetails();
}

// =========================================================================
// 2. ENTITAS LAYANAN DAN RESERVASI PENERBANGAN (FLIGHT & RESERVATION)
// =========================================================================

class Flight {
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

    // Getter untuk Enkapsulasi data field private
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public int getAvailableSeats() { return availableSeats; }
    public double getPrice() { return price; }

    // Metode untuk memanipulasi kuota kursi secara internal
    public void bookSeat(int passengers) {
        this.availableSeats -= passengers;
    }

    public void releaseSeat(int passengers) {
        this.availableSeats += passengers;
    }

    @Override
    public String toString() {
        return "[" + flightNumber + "] " + origin + " -> " + destination + " (" + date + ") | Sisa Kursi: " + availableSeats + " | Harga: Rp" + price;
    }
}

// Menggunakan Final Class gabungan dari batasan Sealed Class induk
final class FlightReservation extends Reservation implements Bookable {
    private Flight flight;
    private String passengerName;
    private int passengerCount;

    public FlightReservation(int confirmationNumber, Flight flight, String passengerName, int passengerCount) {
        super(confirmationNumber);
        this.flight = flight;
        this.passengerName = passengerName;
        this.passengerCount = passengerCount;
    }

    // Polimorfisme dari implementasi interface Bookable
    @Override
    public void book() {
        flight.bookSeat(passengerCount);
    }

    @Override
    public void cancel() {
        flight.releaseSeat(passengerCount);
    }

    // Polimorfisme penulisan ulang metode dari abstract class induk
    @Override
    public void displayDetails() {
        System.out.println("=== TIKET PENERBANGAN ===");
        System.out.println("No Konfirmasi : " + getConfirmationNumber());
        System.out.println("Nama Penumpang: " + passengerName);
        System.out.println("Detail Rute   : " + flight.getOrigin() + " ke " + flight.getDestination() + " (" + flight.getFlightNumber() + ")");
        System.out.println("Jumlah Kursi  : " + passengerCount + " pax");
        System.out.println("Total Bayar   : Rp" + (flight.getPrice() * passengerCount));
        System.out.println("-------------------------");
    }
}

// =========================================================================
// 3. ENTITAS LAYANAN DAN RESERVASI HOTEL (HOTEL & RESERVATION)
// =========================================================================

class Hotel {
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

    // Getter untuk Enkapsulasi data field private
    public String getHotelId() { return hotelId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getPricePerNight() { return pricePerNight; }

    @Override
    public String toString() {
        return "[" + hotelId + "] " + name + " - " + location + " | Per malam: Rp" + pricePerNight;
    }
}

// Menggunakan Final Class gabungan dari batasan Sealed Class induk
final class HotelReservation extends Reservation implements Bookable {
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
        // Alokasi penanda sistem internal booking kamar hotel
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
        System.out.println("Durasi Stay   : " + durationDays + " malam");
        System.out.println("Total Bayar   : Rp" + (hotel.getPricePerNight() * durationDays));
        System.out.println("-------------------------");
    }
}

// =========================================================================
// 4. CUSTOM EXCEPTION (PENANGANAN EKSEPSI BISNIS)
// =========================================================================

// Custom exception khusus untuk menangani error pencarian nomor konfirmasi
class ReservationNotFoundException extends Exception {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}

// =========================================================================
// 5. LOGIKA SISTEM UTAMA (MANAGEMENT ENGINE)
// =========================================================================

class TravelApp {
    private List<Flight> flightInventory = new ArrayList<>();
    private List<Hotel> hotelInventory = new ArrayList<>();
    private List<Reservation> activeReservations = new ArrayList<>();
    private Random random = new Random();

    public TravelApp() {
        initMockData();
    }

    // Inisialisasi basis data simulasi (Mock Data)
    private void initMockData() {
        flightInventory.add(new Flight("GA123", "Jakarta", "Bali", "2026-07-01", 50, 1200000));
        flightInventory.add(new Flight("GA124", "Jakarta", "Bali", "2026-07-01", 10, 1500000));
        flightInventory.add(new Flight("AK456", "Jakarta", "Surabaya", "2026-07-02", 30, 700000));
        
        hotelInventory.add(new Hotel("H01", "Grand Hyatt", "Bali", 2000000));
        hotelInventory.add(new Hotel("H02", "Aston Hotel", "Bali", 850000));
        hotelInventory.add(new Hotel("H03", "Luminor", "Surabaya", 500000));
    }

    // Menggunakan Stream API dan Ekspresi Lambda untuk penyaringan koleksi
    public List<Flight> searchFlights(String from, String to, String date) {
        return flightInventory.stream()
                .filter(f -> f.getOrigin().equalsIgnoreCase(from) && 
                             f.getDestination().equalsIgnoreCase(to) && 
                             f.getDate().equals(date))
                .toList();
    }

    // Menggunakan Stream API dan Ekspresi Lambda untuk penyaringan koleksi hotel
    public List<Hotel> searchHotels(String location) {
        return hotelInventory.stream()
                .filter(h -> h.getLocation().equalsIgnoreCase(location))
                .toList();
    }

    // Menghasilkan nomor konfirmasi acak 6 digit unik
    public int generateConfirmationNumber() {
        return 100000 + random.nextInt(900000);
    }

    public void processFlightBooking(Flight flight, String name, int pax) {
        int confNum = generateConfirmationNumber();
        FlightReservation fr = new FlightReservation(confNum, flight, name, pax);
        fr.book(); // Sinkronisasi pengurangan kursi penerbangan
        activeReservations.add(fr);
        System.out.println("\nBooking Sukses! Nomor Konfirmasi Anda: " + confNum);
    }

    public void processHotelBooking(Hotel hotel, String name, int days) {
        int confNum = generateConfirmationNumber();
        HotelReservation hr = new HotelReservation(confNum, hotel, name, days);
        hr.book(); // Sinkronisasi alokasi kamar hotel
        activeReservations.add(hr);
        System.out.println("\nBooking Sukses! Nomor Konfirmasi Anda: " + confNum);
    }

    // Penerapan Fitur Pattern Matching untuk instanceof (Java 14/17)
    public void cancelReservation(int confNumber) throws ReservationNotFoundException {
        Reservation match = null;
        for (Reservation r : activeReservations) {
            if (r.getConfirmationNumber() == confNumber) {
                match = r;
                break;
            }
        }

        // Jika data tidak ditemukan, lemparkan custom exception
        if (match == null) {
            throw new ReservationNotFoundException("Gagal: Nomor konfirmasi " + confNumber + " tidak ditemukan!");
        }

        // Pattern matching: binding objek langsung tanpa casting manual lagi
        if (match instanceof FlightReservation fr) {
            fr.cancel(); // Mengembalikan jumlah kursi otomatis
            System.out.println("Reservasi Penerbangan Berhasil Dibatalkan.");
        } else if (match instanceof HotelReservation hr) {
            hr.cancel(); // Mengosongkan status kamar kembali
            System.out.println("Reservasi Hotel Berhasil Dibatalkan.");
        }

        activeReservations.remove(match);
    }

    public void showAllReservations() {
        if (activeReservations.isEmpty()) {
            System.out.println("Belum ada data reservasi yang aktif saat ini.");
            return;
        }
        System.out.println("\n=== DAFTAR SEMUA RESERVASI AKTIF ===");
        // Iterasi menggunakan metode referensi ekspresi lambda
        activeReservations.forEach(Reservation::displayDetails);
    }
}

// =========================================================================
// 6. MAIN DRIVER PROGRAM (SATU-SATUNYA KELAS PUBLIC)
// =========================================================================

public class Main {
    public static void main(String[] args) {
        TravelApp app = new TravelApp();
        Scanner scanner = new Scanner(System.in);

        // Perulangan utama menu interaktif aplikasi konsol
        while (true) {
            System.out.println("\n==================================");
            System.out.println("     NUSA TRAVEL BOOKING SYSTEM   ");
            System.out.println("==================================");
            System.out.println("1. Cari & Pesan Penerbangan");
            System.out.println("2. Cari & Pesan Hotel");
            System.out.println("3. Batalkan Reservasi");
            System.out.println("4. Lihat Semua Reservasi");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu (1-5): ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Membersihkan sisa buffer enter
            } catch (InputMismatchException e) {
                System.out.println("Error: Input menu harus berupa angka numerik!");
                scanner.nextLine(); // Membersihkan input yang salah dari buffer
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Masukkan Kota Asal: ");
                    String origin = scanner.nextLine();
                    System.out.print("Masukkan Kota Tujuan: ");
                    String dest = scanner.nextLine();
                    System.out.print("Masukkan Tanggal (YYYY-MM-DD): ");
                    String date = scanner.nextLine();

                    List<Flight> results = app.searchFlights(origin, dest, date);
                    if (results.isEmpty()) {
                        System.out.println("Pemberitahuan: Tidak ada penerbangan tersedia untuk kriteria tersebut.");
                    } else {
                        System.out.println("\nHasil Penerbangan Ditemukan:");
                        for (int i = 0; i < results.size(); i++) {
                            System.out.println((i + 1) + ". " + results.get(i));
                        }
                        System.out.print("Pilih nomor penerbangan untuk dipesan: ");
                        try {
                            int idx = scanner.nextInt() - 1;
                            scanner.nextLine();
                            
                            if (idx >= 0 && idx < results.size()) {
                                Flight selectedFlight = results.get(idx);
                                System.out.print("Masukkan Nama Penumpang: ");
                                String name = scanner.nextLine();
                                System.out.print("Masukkan Jumlah Penumpang (Kursi): ");
                                int pax = scanner.nextInt();
                                
                                if (pax <= selectedFlight.getAvailableSeats()) {
                                    app.processFlightBooking(selectedFlight, name, pax);
                                } else {
                                    System.out.println("Gagal: Kapasitas kursi pesawat yang tersisa tidak mencukupi.");
                                }
                            } else {
                                System.out.println("Error: Nomor pilihan di luar rentang daftar.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Gagal: Terjadi kesalahan tipe input data proses booking.");
                            scanner.nextLine();
                        }
                    }
                }
                case 2 -> {
                    System.out.print("Masukkan Kota / Lokasi Hotel: ");
                    String loc = scanner.nextLine();
                    
                    List<Hotel> hotels = app.searchHotels(loc);
                    if (hotels.isEmpty()) {
                        System.out.println("Pemberitahuan: Tidak ada hotel yang terdaftar di lokasi tersebut.");
                    } else {
                        System.out.println("\nHasil Hotel Ditemukan:");
                        for (int i = 0; i < hotels.size(); i++) {
                            System.out.println((i + 1) + ". " + hotels.get(i));
                        }
                        System.out.print("Pilih nomor hotel untuk dipesan: ");
                        try {
                            int idx = scanner.nextInt() - 1;
                            scanner.nextLine();
                            
                            if (idx >= 0 && idx < hotels.size()) {
                                Hotel selectedHotel = hotels.get(idx);
                                System.out.print("Masukkan Nama Tamu Utama: ");
                                String name = scanner.nextLine();
                                System.out.print("Durasi Menginap (Jumlah Malam): ");
                                int days = scanner.nextInt();
                                
                                app.processHotelBooking(selectedHotel, name, days);
                            } else {
                                System.out.println("Error: Nomor pilihan di luar rentang daftar.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Gagal: Terjadi kesalahan tipe input data proses booking hotel.");
                            scanner.nextLine();
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Masukkan 6-Digit Nomor Konfirmasi Booking Anda: ");
                    try {
                        int code = scanner.nextInt();
                        app.cancelReservation(code);
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Format input nomor konfirmasi wajib berupa angka!");
                        scanner.nextLine();
                    } catch (ReservationNotFoundException e) {
                        System.out.println(e.getMessage()); // Menampilkan pesan dari custom exception
                    }
                }
                case 4 -> app.showAllReservations();
                case 5 -> {
                    System.out.println("Terima kasih telah menggunakan sistem Nusa Travel Booking System.");
                    System.exit(0);
                }
                default -> System.out.println("Peringatan: Menu pilihan salah. Silakan ketik ulang (1-5).");
            }
        }
    }
}