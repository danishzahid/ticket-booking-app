package org.example.service;

import org.example.entity.Train;
import java.util.Optional;
import java.util.logging.Logger;

public class TrainTicketService {
    private static final Logger logger = Logger.getLogger(TrainTicketService.class.getName());
    private final TrainService trainService;

    // Constructor-based Dependency Injection
    public TrainTicketService(TrainService trainService) {
        this.trainService = trainService;
    }

    /**
     * Books a seat in the given train.
     */
    public boolean bookTicket(String trainId, int row, int col) {
        Optional<Train> optionalTrain = trainService.getTrainById(trainId);

        if (optionalTrain.isEmpty()) {
            logger.warning("Train not found: " + trainId);
            return false;
        }

        Train train = optionalTrain.get();
        if (row < 0 || row >= train.getSeats().size() || col < 0 || col >= train.getSeats().get(row).size()) {
            logger.warning("Invalid seat position.");
            return false;
        }

        if (train.getSeats().get(row).get(col) == 0) {
            train.getSeats().get(row).set(col, 1); // Mark seat as booked
            trainService.updateTrain(train);
            logger.info("Ticket booked successfully for train " + trainId + " at (" + row + "," + col + ")");
            return true;
        } else {
            logger.warning("Seat already booked!");
            return false;
        }
    }

    /**
     * Cancels a booked ticket.
     */
    public boolean cancelTicket(String trainId, int row, int col) {
        Optional<Train> optionalTrain = trainService.getTrainById(trainId);

        if (optionalTrain.isEmpty()) {
            logger.warning("Train not found: " + trainId);
            return false;
        }

        Train train = optionalTrain.get();
        if (row < 0 || row >= train.getSeats().size() || col < 0 || col >= train.getSeats().get(row).size()) {
            logger.warning("Invalid seat position.");
            return false;
        }

        if (train.getSeats().get(row).get(col) == 1) {
            train.getSeats().get(row).set(col, 0); // Mark seat as available
            trainService.updateTrain(train);
            logger.info("Ticket cancelled for train " + trainId + " at (" + row + "," + col + ")");
            return true;
        } else {
            logger.warning("Seat is already available!");
            return false;
        }
    }
}
