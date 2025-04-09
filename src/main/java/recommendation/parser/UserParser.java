package recommendation.parser;

import recommendation.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserParser {
    private List<User> users;
    private String error;

    public UserParser(String userFile){
        users = new ArrayList<>();
        parseUserFile(userFile);
        System.out.println("UserParser initialized with file: " + userFile);
    }

    private void parseUserFile(String userFile){
        System.out.println("Parsing user file...");
    }

    public List<User> getUsers() {
        return users;
    }
}