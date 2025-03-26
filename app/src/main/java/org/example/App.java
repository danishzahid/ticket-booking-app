package org.example;

import org.example.entity.User;
import org.example.service.UserBookingService;
import org.example.service.TrainService;
import org.example.service.TrainTicketService;
import org.example.entity.Train;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserBookingService userBookingService;
    private static TrainService trainService;
    private static TrainTicketService trainTicketService;

    private static boolean isLoggedIn = false; // Track login state

    public static void main(String[] args) {
        System.out.println("Running Ticket Booking App...");

        try {
            // Initialize services
            userBookingService = new UserBookingService();
            trainService = new TrainService();
            trainTicketService = new TrainTicketService(trainService);

            // Login or Signup
            while (!isLoggedIn) {
                showLoginSignupMenu();
                int authOption = getValidIntegerInput();
                switch (authOption) {
                    case 1:
                        isLoggedIn = login(); // Login
                        break;
                    case 2:
                        signup(); // Signup
                        break;
                    case 3:
                        System.out.println("Exiting... Authentication required!");
                        return;
                    default:
                        System.out.println("Invalid option! Please choose again.");
                }
            }

            // Menu-driven system after login
            int option;
            do {
                showMainMenu();
                option = getValidIntegerInput();
                switch (option) {
                    case 1:
                        viewTrains();
                        break;
                    case 2:
                        bookTicket();
                        break;
                    case 3:
                        checkSeats();
                        break;
                    case 4:
                        System.out.println("Exiting... Thank you for using Ticket Booking App!");
                        break;
                    default:
                        System.out.println("Invalid option! Please choose again.");
                }
            } while (option != 4);

        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void showLoginSignupMenu() {
        System.out.println("\nWelcome! Please choose an option:");
        System.out.println("1. Login");
        System.out.println("2. Signup");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private static void signup() {
        System.out.println("\nSignup New User");
        try {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();

            User newUser = new User();
            newUser.setUserName(username);
            newUser.setPasswordHash(password); // Password will be hashed by UserBookingService

            userBookingService.signup(newUser);
        } catch (Exception e) {
            System.out.println("Error during signup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean login() {
        System.out.println("\nLogin Required");
        try {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();

            boolean loginSuccess = userBookingService.login(username, password);
            if (loginSuccess) {
                System.out.println("Login Successful!");
                return true;
            } else {
                System.out.println("Invalid username or password! Try again.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void showMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. View All Trains");
        System.out.println("2. Book a Ticket");
        System.out.println("3. Check Available Seats");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private static void viewTrains() {
        if (!isLoggedIn) {
            System.out.println("Please login to access this feature!");
            return;
        }
        try {
            List<Train> trains = trainService.getAllTrains();
            if (trains.isEmpty()) {
                System.out.println("No trains available!");
            } else {
                System.out.println("Available Trains:");
                for (Train train : trains) {
                    System.out.println("Train ID: " + train.getTrainId() + " | Train No: " + train.getTrainNo());
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching train list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void bookTicket() {
        if (!isLoggedIn) {
            System.out.println("Please login to access this feature!");
            return;
        }
        try {
            System.out.print("Enter Train ID: ");
            String trainId = scanner.nextLine().trim();

            System.out.print("Enter Row: ");
            int row = getValidIntegerInput();

            System.out.print("Enter Column: ");
            int col = getValidIntegerInput();

            boolean success = trainTicketService.bookTicket(trainId, row, col);
            if (success) {
                System.out.println("Ticket booked successfully!");
            } else {
                System.out.println("Ticket booking failed! Either seat is taken or train ID is invalid.");
            }
        } catch (Exception e) {
            System.out.println("Error during ticket booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void checkSeats() {
        if (!isLoggedIn) {
            System.out.println("Please login to access this feature!");
            return;
        }
        try {
            System.out.print("Enter Train ID: ");
            String trainId = scanner.nextLine().trim();

            int seatsAvailable = trainService.getAvailableSeats(trainId);
            System.out.println("Available Seats: " + seatsAvailable);
        } catch (Exception e) {
            System.out.println("Error checking available seats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int getValidIntegerInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
}
