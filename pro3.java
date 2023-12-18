import java.io.*;
import java.util.*;

class User implements Serializable {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class Flight {
    private int totalEconomySeats;
    private int totalBusinessSeats;
    private List<Boolean> economySeats;
    private List<Boolean> businessSeats;

    public Flight(int totalEconomySeats, int totalBusinessSeats) {
        this.totalEconomySeats = totalEconomySeats;
        this.totalBusinessSeats = totalBusinessSeats;
        this.economySeats = new ArrayList<>(Collections.nCopies(totalEconomySeats, false));
        this.businessSeats = new ArrayList<>(Collections.nCopies(totalBusinessSeats, false));
    }

    public void displayAvailableSeats() {
        System.out.println("Available Economy Class Seats: " + Collections.frequency(economySeats, false));
        System.out.println("Available Business Class Seats: " + Collections.frequency(businessSeats, false));
    }

    public boolean bookSeat(int seatNumber, String seatClass) {
        List<Boolean> seats = (seatClass.equalsIgnoreCase("economy")) ? economySeats : businessSeats;

        if (seatNumber >= 1 && seatNumber <= seats.size() && !seats.get(seatNumber - 1)) {
            seats.set(seatNumber - 1, true);
            return true;
        } else {
            System.out.println("Invalid seat selection or seat already booked.");
            return false;
        }
    }

    public boolean cancelSeat(int seatNumber, String seatClass) {
        List<Boolean> seats = (seatClass.equalsIgnoreCase("economy")) ? economySeats : businessSeats;

        if (seatNumber >= 1 && seatNumber <= seats.size() && seats.get(seatNumber - 1)) {
            seats.set(seatNumber - 1, false);
            return true;
        } else {
            System.out.println("Invalid seat selection or seat not booked.");
            return false;
        }
    }
}

public class pro3 {
    private static final String USER_FILE = "users.ser";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load existing users from file
        List<User> users = loadUsers();

        // Dummy Flight with 10 Economy and 5 Business Seats
        Flight flight = new Flight(10, 5);
        System.out.println("Welcome to fly with me airline");
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Book a Seat");
            System.out.println("4. View Available Seats");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    loginUser(users, scanner);
                    break;
                case 2:
                    registerUser(users, scanner);
                    break;
                case 3:
                    bookSeat(users, flight, scanner);
                    break;
                case 4:
                    flight.displayAvailableSeats();
                    break;
                case 5:
                    cancelBooking(users, flight, scanner);
                    break;
                case 6:
                    saveUsers(users);
                    System.out.println("Exiting... Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If file not found or empty, return an empty list
            return new ArrayList<>();
        }
    }

    private static void saveUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loginUser(List<User> users, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User foundUser = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (foundUser != null) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private static void registerUser(List<User> users, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Check if the username already exists
        if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        users.add(newUser);

        System.out.println("Registration successful!");
    }

    private static void bookSeat(List<User> users, Flight flight, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Check if the user is registered
        if (users.stream().noneMatch(user -> user.getUsername().equals(username))) {
            System.out.println("User not found. Please register first.");
            return;
        }

        System.out.print("Enter seat class (economy/business): ");
        String seatClass = scanner.nextLine();

        flight.displayAvailableSeats();

        System.out.print("Enter seat number to book: ");
        int seatNumber = scanner.nextInt();

        boolean success = flight.bookSeat(seatNumber, seatClass);
        if (success) {
            System.out.println("Seat booked successfully!");
        }
    }

    private static void cancelBooking(List<User> users, Flight flight, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Check if the user is registered
        if (users.stream().noneMatch(user -> user.getUsername().equals(username))) {
            System.out.println("User not found. Please register first.");
            return;
        }

        System.out.print("Enter seat class (economy/business): ");
        String seatClass = scanner.nextLine();

        flight.displayAvailableSeats();

        System.out.print("Enter seat number to cancel: ");
        int seatNumber = scanner.nextInt();

        boolean success = flight.cancelSeat(seatNumber, seatClass);
        if (success) {
            System.out.println("Booking canceled successfully!");
        }
    }
}
