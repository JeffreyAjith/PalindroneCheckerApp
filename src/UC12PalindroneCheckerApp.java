import java.io.*;
import java.util.*;

/* Reservation Entity */
class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    int reservationId;
    String guestName;
    String roomType;

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


/* System State (Inventory + Booking History) */
class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState() {

        inventory = new HashMap<>();
        bookings = new ArrayList<>();

        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }
}


/* Persistence Service */
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.dat";

    /* Save system state */
    public static void save(SystemState state) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(FILE_NAME));

            out.writeObject(state);
            out.close();

            System.out.println("System state saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    /* Load system state */
    public static SystemState load() {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(FILE_NAME));

            SystemState state = (SystemState) in.readObject();

            in.close();

            System.out.println("System state restored from file.");

            return state;

        } catch (Exception e) {

            System.out.println("No previous state found. Starting fresh system.");

            return new SystemState();
        }
    }
}


/* Main Application */
public class UC12PalindroneCheckerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        /* Load persisted state */
        SystemState state = PersistenceService.load();

        System.out.println("\n=== Hotel Booking System ===");

        System.out.print("Enter Reservation ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Guest Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Room Type (Single/Double/Suite): ");
        String room = sc.nextLine();

        int available = state.inventory.getOrDefault(room, 0);

        if (available > 0) {

            state.inventory.put(room, available - 1);

            Reservation reservation =
                    new Reservation(id, name, room);

            state.bookings.add(reservation);

            System.out.println("\nBooking Confirmed!");
            System.out.println(reservation);

        } else {

            System.out.println("\nBooking Failed. No rooms available.");
        }

        /* Display current inventory */
        System.out.println("\nCurrent Inventory:");

        for (String key : state.inventory.keySet()) {

            System.out.println(key + " Rooms: " + state.inventory.get(key));
        }

        /* Display booking history */
        System.out.println("\nBooking History:");

        for (Reservation r : state.bookings) {
            System.out.println(r);
        }

        /* Save state before shutdown */
        PersistenceService.save(state);

        sc.close();
    }
}