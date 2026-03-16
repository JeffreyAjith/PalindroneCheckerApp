import java.util.*;

/* Reservation Entity */
class Reservation {

    private int reservationId;
    private String guestName;
    private String roomType;
    private boolean active;

    public Reservation(int reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.active = true;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        active = false;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room: " + roomType +
               ", Status: " + (active ? "CONFIRMED" : "CANCELLED");
    }
}


/* Inventory Manager */
class RoomInventory {

    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public void allocateRoom(String type) {

        int available = rooms.get(type);
        rooms.put(type, available - 1);
    }

    public void restoreRoom(String type) {

        int available = rooms.get(type);
        rooms.put(type, available + 1);
    }

    public void showInventory() {

        System.out.println("\nCurrent Inventory:");

        for (String key : rooms.keySet()) {
            System.out.println(key + " Rooms: " + rooms.get(key));
        }
    }
}


/* Booking History */
class BookingHistory {

    private Map<Integer, Reservation> bookings;

    public BookingHistory() {
        bookings = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(int id) {
        return bookings.get(id);
    }

    public void showBookings() {

        System.out.println("\nBooking History:");

        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}


/* Cancellation Service with Stack Rollback */
class CancellationService {

    private Stack<String> rollbackStack;

    public CancellationService() {
        rollbackStack = new Stack<>();
    }

    public void cancelReservation(int id,
                                  BookingHistory history,
                                  RoomInventory inventory) {

        Reservation r = history.getReservation(id);

        if (r == null) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        if (!r.isActive()) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        String roomType = r.getRoomType();

        /* Push room type to stack for rollback tracking */
        rollbackStack.push(roomType);

        /* Restore inventory */
        inventory.restoreRoom(roomType);

        /* Mark reservation cancelled */
        r.cancel();

        System.out.println("Cancellation successful for reservation ID: " + id);
    }

    public void showRollbackStack() {

        System.out.println("\nRollback Stack (recent releases):");

        for (String r : rollbackStack) {
            System.out.println(r);
        }
    }
}


/* Main Application */
public class UC10PalindroneCheckerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        System.out.println("=== Hotel Booking System ===");

        /* Booking Phase */
        System.out.print("Enter Reservation ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Guest Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Room Type (Single/Double/Suite): ");
        String room = sc.nextLine();

        Reservation reservation = new Reservation(id, name, room);

        history.addReservation(reservation);
        inventory.allocateRoom(room);

        System.out.println("\nBooking Confirmed!");

        history.showBookings();
        inventory.showInventory();

        /* Cancellation Phase */
        System.out.print("\nEnter Reservation ID to Cancel: ");
        int cancelId = sc.nextInt();

        cancelService.cancelReservation(cancelId, history, inventory);

        history.showBookings();
        inventory.showInventory();

        cancelService.showRollbackStack();

        sc.close();
    }
}