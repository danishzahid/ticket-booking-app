package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Train;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TrainService {
    private static final Logger logger = Logger.getLogger(TrainService.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_PATH = Paths.get("app", "src", "main", "java", "org", "example", "localDb", "trains.json").toString();

    /**
     * Fetches train details by ID.
     */
    public Optional<Train> getTrainById(String trainId) {
        return getAllTrains().stream()
                .filter(train -> train.getTrainId().equals(trainId))
                .findFirst();
    }

    /**
     * Retrieves a list of all trains.
     */
    public List<Train> getAllTrains() {
        try {
            return objectMapper.readValue(new File(TRAIN_PATH), new TypeReference<List<Train>>() {});
        } catch (IOException e) {
            logger.severe("Error reading train data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Searches for trains passing through a given station.
     */
    public List<Train> searchTrainByStation(String station) {
        return getAllTrains().stream()
                .filter(train -> train.getStations().contains(station))
                .collect(Collectors.toList());
    }

    /**
     * Adds a new train to the database.
     */
    public void addTrain(Train newTrain) {
        List<Train> trains = getAllTrains();
        trains.add(newTrain);
        saveTrains(trains);
        logger.info("Train added successfully: " + newTrain.getTrainId());
    }

    /**
     * Updates an existing train.
     */
    public boolean updateTrain(Train updatedTrain) {
        List<Train> trains = getAllTrains();
        for (int i = 0; i < trains.size(); i++) {
            if (trains.get(i).getTrainId().equals(updatedTrain.getTrainId())) {
                trains.set(i, updatedTrain);
                saveTrains(trains);
                logger.info("Train updated successfully: " + updatedTrain.getTrainId());
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a train by ID.
     */
    public boolean deleteTrain(String trainId) {
        List<Train> trains = getAllTrains();
        boolean removed = trains.removeIf(train -> train.getTrainId().equals(trainId));
        if (removed) {
            saveTrains(trains);
            logger.info("Train deleted: " + trainId);
            return true;
        }
        return false;
    }

    /**
     * Saves the train list back to the JSON file.
     */
    private void saveTrains(List<Train> trains) {
        try {
            objectMapper.writeValue(new File(TRAIN_PATH), trains);
        } catch (IOException e) {
            logger.severe("Error saving train data: " + e.getMessage());
        }
    }

    /**
     * Gets available seats for a train.
     */
    public int getAvailableSeats(String trainId) {
        return getTrainById(trainId)
                .map(this::countAvailableSeats)
                .orElse(0);
    }

    /**
     * Counts available seats in a train.
     */
    private int countAvailableSeats(Train train) {
        return (int) train.getSeats().stream()
                .flatMap(List::stream)
                .filter(seat -> seat == 0)
                .count();
    }
}
