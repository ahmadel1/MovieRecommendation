package recommendation.model;

import java.util.List;

public class User {
    private String userName;
    private String userId;
    private List<String> movieIds;

    public User(String userName, String userId, List<String> movieIds) {
        this.userName = userName;
        this.userId = userId;
        this.movieIds = movieIds;
        System.out.println("Created User: " + userName);
    }

    public String getUserName(){
        return this.userName;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public List<String> getMovieIds() {
        return movieIds;
    }

    @Override
    public String toString() {
        return "User: " + userName + ", ID: " + userId + ", movieIds: " + movieIds + "\n";
    }
}