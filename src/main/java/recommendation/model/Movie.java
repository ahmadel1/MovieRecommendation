package recommendation.model;

import java.util.List;

public class Movie {
    private String movieTitle;
    private String movieId;
    private List<String> movieGenres;

    public Movie(String movieTitle, String movieId, List<String> movieGenres) {
        this.movieTitle = movieTitle;
        this.movieId = movieId;
        this.movieGenres = movieGenres;
        System.out.println("Created Movie: " + movieTitle);
    }

    @Override
    public String toString() {
        return "Movie: " + movieTitle + ", ID: " + movieId + ", genres: " + movieGenres + "\n";
    }
}