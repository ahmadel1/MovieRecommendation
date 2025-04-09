package recommendation.engine;

import recommendation.model.Movie;
import recommendation.model.SingleRecommendation;
import recommendation.model.User;

import java.util.ArrayList;
import java.util.List;

public class RecommendationSystem {
    private List<SingleRecommendation> singleRecommendations;

    public RecommendationSystem(List<User> users, List<Movie> movies) {
        singleRecommendations = new ArrayList<>();
        generateRecommendations(users, movies);
        System.out.println("RecommendationSystem initialized");
    }

    private void generateRecommendations(List<User> users, List<Movie> movies) {
        System.out.println("Generating recommendations...");
    }

    public List<SingleRecommendation> getSingleRecommendations() {
        return singleRecommendations;
    }
}