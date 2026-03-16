import java.util.*;

/* Custom Exception for Invalid Booking */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}


/* Reservation Entity */
class Reservation {

    private int reservationId;
    private String guestName;
    private String roomType;

    public Reservation(int reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room Type: " + roomType;
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

    public boolean isValidRoomType(String type) {
        return rooms.containsKey(type);
    }

    public void allocateRoom(String type) throws InvalidBookingException {

        if (!rooms.containsKey(type)) {
            throw new InvalidBookingException("Invalid Room Type Provided.");
        }

        int available = rooms.get(type);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        rooms.put(type, available - 1);
    }

    public void showInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (String key : rooms.keySet()) {
            System.out.println(key + " Rooms Available: " + rooms.get(key));
        }
    }
}


/* Booking Validator */
class InvalidBookingValidator {

    public static void validate(int id, String guest, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (id <= 0) {
            throw new InvalidBookingException("Reservation ID must be positive.");
        }

        if (guest == null || guest.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Room type does not exist.");
        }
    }
}


/* Main Application */
public class UC9PalindroneCheckerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();

        System.out.println("=== Hotel Booking System (Error Handling & Validation) ===");

        try {

            System.out.print("Enter Reservation ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Guest Name: ");
            String guest = sc.nextLine();

            System.out.print("Enter Room Type (Single / Double / Suite): ");
            String roomType = sc.nextLine();

            /* Validation Step */
            InvalidBookingValidator.validate(id, guest, roomType, inventory);

            /* Allocate Room */
            inventory.allocateRoom(roomType);

            Reservation reservation = new Reservation(id, guest, roomType);

            System.out.println("\nBooking Successful!");
            System.out.println(reservation);

        }
        catch (InvalidBookingException e) {

            System.out.println("\nBooking Failed: " + e.getMessage());

        }
        catch (Exception e) {

            System.out.println("\nUnexpected Error: " + e.getMessage());

        }

        inventory.showInventory();

        sc.close();
    }
}