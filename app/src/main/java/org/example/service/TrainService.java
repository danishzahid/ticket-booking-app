package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TrainService {

    private static final String TRAIN_PATH = "app/src/main/java/org/example/localDb/trains.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void getTrainInfo(String trainId) {
        try {
            List<Train> trains = objectMapper.readValue(new File(TRAIN_PATH), new TypeReference<List<Train>>() {});
            for (Train train : trains) {
                if (train.getTrainId().equals(trainId)) {
                    System.out.println("🚆 Train Details:");
                    System.out.println("Train ID: " + train.getTrainId());
                    System.out.println("Train No: " + train.getTrainNo());
                    System.out.println("Stations: " + train.getStations());
                    System.out.println("Timings: " + train.getStationTimes());
                    return;
                }
            }
            System.out.println("❌ Train not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
