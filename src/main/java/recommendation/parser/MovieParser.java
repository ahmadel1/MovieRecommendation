package recommendation.parser;

import recommendation.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieParser {
    private List<Movie> movies;
    private String error;

    public MovieParser(String movieFile){
        movies = new ArrayList<>();
        parseMovieFile(movieFile);
        System.out.println("MovieParser initialized with file: " + movieFile);
    }

    private void parseMovieFile(String movieFile){
        System.out.println("Parsing movie file...");
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Boolean success() {
        return error == null;
    }

    public String getError() {
        return error;
    }
}