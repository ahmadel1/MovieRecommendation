package recommendation.engine;

import recommendation.model.Movie;
import recommendation.model.SingleRecommendation;
import recommendation.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RecommendationSystem {
    private List<SingleRecommendation> singleRecommendations;
    private List<Movie> movies;
    private List<User> users;

    public RecommendationSystem(List<User> users, List<Movie> movies) {
        singleRecommendations = new ArrayList<>();
        this.movies = movies;
        this.users = users;
        generateRecommendations();
        System.out.println("RecommendationSystem initialized");
    }

    private void generateRecommendations() {
        this.users.forEach(user->{
            List<String> userGenres = new ArrayList<>();
            List<String> userMovies = user.getMovieIds();
            userMovies.forEach(movieId->{
                this.movies.forEach(movie->{
                    if(movieId.equals(movie.getMovieId())){
                        List<String> movieGenres = movie.getMovieGenres();
                        userGenres.addAll(movieGenres);
                    }
                });
            });
            this.singleRecommendations.add(generateSingleRecommendation(user, userGenres));
        });
        System.out.println("Generating recommendations...");
    }

    private SingleRecommendation generateSingleRecommendation(User user, List<String> userGenres){
        List<Movie> userMovies = new ArrayList<>();
        userGenres.forEach(genre->{
            this.movies.forEach(movie->{
                if(movie.getMovieGenres().contains(genre) && !userMovies.contains(movie) && !user.getMovieIds().contains(movie.getMovieId())){
                    userMovies.add(movie);
                }
            });
        });
        SingleRecommendation recommendation = new SingleRecommendation(user, userMovies);
        return recommendation;
    }

    public List<SingleRecommendation> getSingleRecommendations() {
        this.singleRecommendations.forEach(singleRecommendation -> {
            System.out.print("User: " + singleRecommendation.getUser());
            System.out.println("Recommended movies: " + singleRecommendation.getMovies());
        });
        return singleRecommendations;
    }
}