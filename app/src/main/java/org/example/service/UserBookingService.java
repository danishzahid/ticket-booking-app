package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.example.entity.Ticket;
import org.example.entity.User;
import org.example.util.PasswordUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing user bookings.
 */
public class UserBookingService {
    private static final String USER_PATH = "app/src/main/java/org/example/localDb/users.json"; // Adjusted path
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TICKET_PATH = "app/src/main/java/org/example/localDb/tickets.json";
    private static final String TRAIN_PATH = "app/src/main/java/org/example/localDb/trains.json"; // Train data path


    /**
     * Retrieves all users from the JSON file.
     * @return List of users.
     */
    public List<User> getAllUsers() {
        try {
            return objectMapper.readValue(new File(USER_PATH), new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Registers a new user.
     * @param newUser New user to be registered.
     * @return true if signup is successful, false if user already exists.
     */
    public void signup(User newUser) {
        try {
            List<User> users = objectMapper.readValue(new File(USER_PATH), new TypeReference<List<User>>() {});

            // Check if user already exists
            for (User user : users) {
                if (user.getUserName().equalsIgnoreCase(newUser.getUserName())) {
                    System.out.println("Username already taken!");
                    return;
                }
            }

            // Hash the password before storing
            String hashedPassword = PasswordUtil.hashPassword(newUser.getPasswordHash());
            newUser.setPasswordHash(hashedPassword);

            // Add user and save back to file
            users.add(newUser);
            objectMapper.writeValue(new File(USER_PATH), users);

            System.out.println("Signup successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Authenticates a user.
     * @param userName Username.
     * @param password Password.
     * @return true if login is successful, false otherwise.
     */
    public boolean login(String userName, String password) {
        try {
            List<User> users = objectMapper.readValue(new File(USER_PATH), new TypeReference<List<User>>() {});

            for (User user : users) {
                if (user.getUserName().equalsIgnoreCase(userName)) {
                    // Check if the entered password matches the stored hashed password
                    if (PasswordUtil.checkPassword(password, user.getPasswordHash())) {
                        System.out.println("Login successful!");
                        return true;
                    } else {
                        System.out.println("Incorrect password!");
                        return false;
                    }
                }
            }

            System.out.println("User not found!");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Retrieves a user by ID.
     * @param userId User ID.
     * @return User object if found, otherwise null.
     */
    public User getUserById(String userId) {
        return getAllUsers().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Saves the updated users list to the JSON file.
     * @param users List of users.
     */
    private void saveUsers(List<User> users) {
        try {
            objectMapper.writeValue(new File(USER_PATH), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTicketInfo(String ticketId) {
        try {
            List<Ticket> tickets = objectMapper.readValue(new File(TICKET_PATH), new TypeReference<List<Ticket>>() {});
            for (Ticket ticket : tickets) {
                if (ticket.getTicketId().equals(ticketId)) {
                    System.out.println("🎟 Ticket Details:");
                    System.out.println("Ticket ID: " + ticket.getTicketId());
                    System.out.println("User ID: " + ticket.getUserId());
                    System.out.println("Source: " + ticket.getSource());
                    System.out.println("Destination: " + ticket.getDestination());
                    System.out.println("Date of Journey: " + ticket.getDateOfJourney());
                    return;
                }
            }
            System.out.println("Ticket not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Fetches all bookings for a specific user.
     * @param userId User ID.
     */
    public void fetchBookings(String userId) {
        try {
            List<Ticket> tickets = objectMapper.readValue(new File(TICKET_PATH), new TypeReference<List<Ticket>>() {});
            List<Ticket> userTickets = new ArrayList<>();

            for (Ticket ticket : tickets) {
                if (ticket.getUserId().equals(userId)) {
                    userTickets.add(ticket);
                }
            }

            if (userTickets.isEmpty()) {
                System.out.println("No bookings found for user ID: " + userId);
                return;
            }

            System.out.println("🎟 User Bookings:");
            for (Ticket ticket : userTickets) {
                System.out.println("Ticket ID: " + ticket.getTicketId() +
                        " | Source: " + ticket.getSource() +
                        " | Destination: " + ticket.getDestination() +
                        " | Date: " + ticket.getDateOfJourney());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels a ticket booking for a user.
     * @param ticketId Ticket ID.
     */
    public void cancelBooking(String ticketId) {
        try {
            List<Ticket> tickets = objectMapper.readValue(new File(TICKET_PATH), new TypeReference<List<Ticket>>() {});
            Optional<Ticket> ticketToRemove = tickets.stream().filter(t -> t.getTicketId().equals(ticketId)).findFirst();

            if (ticketToRemove.isPresent()) {
                tickets.remove(ticketToRemove.get());
                saveTickets(tickets); // Save the updated ticket list
                System.out.println("Ticket " + ticketId + " has been canceled.");
            } else {
                System.out.println("Ticket not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches train details from local database.
     * @param trainId Train ID.
     */
    public void getTrainInfo(String trainId) {
        try {
            List<Ticket> trains = objectMapper.readValue(new File(TRAIN_PATH), new TypeReference<List<Ticket>>() {});
            for (Ticket train : trains) {
                if (train.getTicketId().equals(trainId)) { // Assuming Train ID is stored as Ticket ID (Adjust if needed)
                    System.out.println("🚆 Train Details:");
                    System.out.println("Train ID: " + train.getTicketId());
                    System.out.println("Source: " + train.getSource());
                    System.out.println("Destination: " + train.getDestination());
                    System.out.println("Date of Journey: " + train.getDateOfJourney());
                    return;
                }
            }
            System.out.println("Train not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the updated ticket list after modifications (e.g., cancellations).
     * @param tickets List of tickets.
     */
    private void saveTickets(List<Ticket> tickets) {
        try {
            objectMapper.writeValue(new File(TICKET_PATH), tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
