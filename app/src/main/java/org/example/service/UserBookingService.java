package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.User;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserBookingService {

private User user;

private List<User> userList;

private ObjectMapper OBJECT_MAPPER = new ObjectMapper();

private  static final String USER_PATH = "../localDb/users.json";

public UserBookingService(User user){
    this.user = user;
}
    public List<User> getAllUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read JSON file and convert it to List<User>
            return Arrays.asList(objectMapper.readValue(new File(USER_PATH), User[].class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
