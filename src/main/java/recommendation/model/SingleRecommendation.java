package recommendation.model;

import java.util.List;

public class SingleRecommendation {
    private User user;
    private List<Movie> movies;

    public SingleRecommendation(User user, List<Movie> movies) {
        this.user = user;
        this.movies = movies;
        System.out.println("Created recommendation for user: " + user.getUserName());
    }
}