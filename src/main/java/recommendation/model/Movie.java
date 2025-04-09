package recommendation.model;

import java.util.List;

public class Movie {
    private String movieTitle;
    private int movieId;
    private List<String> movieGenres;

    public Movie(String movieTitle, int movieId, List<String> movieGenres) {
        this.movieTitle = movieTitle;
        this.movieId = movieId;
        this.movieGenres = movieGenres;
        System.out.println("Created Movie: " + movieTitle);
    }
}