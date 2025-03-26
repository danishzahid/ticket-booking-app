package org.example;

import org.example.service.UserBookingService;
import org.example.service.TrainService;
import org.example.service.TrainTicketService;
import org.example.entity.Train;

import java.util.List;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static UserBookingService userBookingService;
    private static TrainService trainService;
    private static TrainTicketService trainTicketService;

    public static void main(String[] args) {

        System.out.println(" Running Ticket Booking App...");

        // Initialize services
        userBookingService = new UserBookingService();
        trainService = new TrainService();
        trainTicketService = new TrainTicketService(trainService);

        // User login
        boolean loggedIn = login();

        if (!loggedIn) {
            System.out.println(" Exiting... Login required!");
            return;
        }

        // Menu-driven system
        int option;
        do {
            showMenu();
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    System.out.println(" Exiting... Thank you for using Ticket Booking App!");
                    break;
                default:
                    System.out.println(" Invalid option! Please choose again.");
            }
        } while (option != 4);

        scanner.close();
    }

    // Login method
    private static boolean login() {
        System.out.println("\n Login Required");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        return userBookingService.login(username, password);
    }

    // Show menu options
    private static void showMenu() {
        System.out.println("\n Menu:");
        System.out.println("1 View All Trains");
        System.out.println("2 Book a Ticket");
        System.out.println("3 Check Available Seats");
        System.out.println("4 Exit");
        System.out.print("Choose an option: ");
    }

    // View available trains
    private static void viewTrains() {
        List<Train> trains = trainService.getAllTrains();
        if (trains.isEmpty()) {
            System.out.println(" No trains available!");
        } else {
            System.out.println("🚆 Available Trains:");
            for (Train train : trains) {
                System.out.println("Train ID: " + train.getTrainId() + " | Train No: " + train.getTrainNo());
            }
        }
    }

    // Book a ticket (Now calls TrainTicketService)
    private static void bookTicket() {
        System.out.print("Enter Train ID: ");
        String trainId = scanner.nextLine();
        System.out.print("Enter Row: ");
        int row = scanner.nextInt();
        System.out.print("Enter Column: ");
        int col = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        boolean success = trainTicketService.bookTicket(trainId, row, col);
        if (success) {
            System.out.println("Ticket booked successfully!");
        } else {
            System.out.println("Ticket booking failed! Either seat is taken or train ID is invalid.");
        }
    }

    // Check available seats
    private static void checkSeats() {
        System.out.print("Enter Train ID: ");
        String trainId = scanner.nextLine();
        int seatsAvailable = trainService.getAvailableSeats(trainId);
        System.out.println("💺 Available Seats: " + seatsAvailable);
    }
}
