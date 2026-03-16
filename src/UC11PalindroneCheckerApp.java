import java.util.*;

/* Booking Request */
class BookingRequest {

    int requestId;
    String guestName;
    String roomType;

    public BookingRequest(int requestId, String guestName, String roomType) {
        this.requestId = requestId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}


/* Shared Room Inventory */
class RoomInventory {

    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    /* Critical Section */
    public synchronized boolean allocateRoom(String type, String guest) {

        int available = rooms.getOrDefault(type, 0);

        if (available > 0) {
            rooms.put(type, available - 1);

            System.out.println(
                guest + " successfully booked a " + type + " room. Remaining: " + (available - 1)
            );

            return true;
        }

        System.out.println(guest + " booking failed. No " + type + " rooms available.");
        return false;
    }

    public void showInventory() {

        System.out.println("\nFinal Inventory State:");

        for (String key : rooms.keySet()) {
            System.out.println(key + " Rooms: " + rooms.get(key));
        }
    }
}


/* Shared Booking Queue */
class BookingQueue {

    private Queue<BookingRequest> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void addRequest(BookingRequest req) {
        queue.add(req);
    }

    public synchronized BookingRequest getRequest() {

        if (queue.isEmpty()) {
            return null;
        }

        return queue.poll();
    }
}


/* Concurrent Booking Processor (Thread) */
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            BookingRequest request = queue.getRequest();

            if (request == null) {
                break;
            }

            inventory.allocateRoom(request.roomType, request.guestName);

            try {
                Thread.sleep(100); // simulate processing delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


/* Main Application */
public class UC11PalindroneCheckerApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        /* Simulate booking requests */
        queue.addRequest(new BookingRequest(1, "Alice", "Single"));
        queue.addRequest(new BookingRequest(2, "Bob", "Single"));
        queue.addRequest(new BookingRequest(3, "Charlie", "Single"));
        queue.addRequest(new BookingRequest(4, "David", "Double"));
        queue.addRequest(new BookingRequest(5, "Emma", "Suite"));

        /* Multiple threads simulating concurrent guests */
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.showInventory();
    }
}