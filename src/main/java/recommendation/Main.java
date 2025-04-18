package recommendation;

import recommendation.engine.RecommendationSystem;
import recommendation.model.Movie;
import recommendation.model.SingleRecommendation;
import recommendation.model.User;
import recommendation.parser.MovieParser;
import recommendation.parser.UserParser;
import recommendation.writer.Writer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Writer writer = new Writer();
        System.out.println("Movie Recommendation System starting...");
        String movieFile = "samples/movies.txt";
        MovieParser movieParser = new MovieParser(movieFile);
        List<Movie> movies = movieParser.getMovies();

        // Check for movie validation errors
        if(!movieParser.success()){
            String error = movieParser.getError();
            System.out.println("Parsing error: " + error);
            writer.writeError(error);
            System.exit(0);
        }

        System.out.println("Successfully loaded " + movies.size() + " movies");

        String userFile = "samples/users.txt";
        UserParser userParser = new UserParser(userFile, movies);
        List<User> users = userParser.getUsers();

        // Check for user validation errors
        if(!userParser.success()){
            String error = userParser.getError();
            System.out.println("Parsing Error: " + error);
            writer.writeError(error);
            System.exit(0);
        }
        System.out.println("Successfully loaded " + users.size() + " users");

        RecommendationSystem recommender = new RecommendationSystem(users, movies);
        List<SingleRecommendation> recommendations = recommender.getSingleRecommendations();
        System.out.println("Generated " + recommendations.size() + " recommendations");

        writer.writeRecommendations(recommendations);
        System.out.println("Recommendations written to file successfully");

    }
}