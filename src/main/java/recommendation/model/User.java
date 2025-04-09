package recommendation.model;

import java.util.List;

public class User {
    private String userName;
    private int userId;
    private List<Integer> movieIds;

    public User(String userName, int userId, List<Integer> movieIds) {
        this.userName = userName;
        this.userId = userId;
        this.movieIds = movieIds;
        System.out.println("Created User: " + userName);
    }

    public String getUserName(){
        return this.userName;
    }
}