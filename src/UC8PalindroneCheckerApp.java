import java.util.*;

/* Reservation Class */
class Reservation {

    private int reservationId;
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(int reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room Type: " + roomType +
               ", Nights: " + nights;
    }
}


/* Booking History Class */
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}


/* Booking Report Service */
class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        System.out.println("\n===== BOOKING HISTORY REPORT =====");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }

        System.out.println("\nTotal Reservations: " + reservations.size());
    }
}


/* Main Application */
public class UC8PalindroneCheckerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        System.out.print("Enter number of bookings: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 1; i <= n; i++) {

            System.out.println("\nEnter Booking " + i + " Details");

            System.out.print("Reservation ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Room Type (Single/Double/Suite): ");
            String room = sc.nextLine();

            System.out.print("Number of Nights: ");
            int nights = sc.nextInt();
            sc.nextLine();

            Reservation reservation = new Reservation(id, name, room, nights);

            history.addReservation(reservation);

            System.out.println("Booking Confirmed!");
        }

        /* Admin Requests Booking Report */
        reportService.generateReport(history.getReservations());

        sc.close();
    }
}