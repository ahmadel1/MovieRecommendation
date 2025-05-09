package recommendation.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import recommendation.engine.RecommendationSystem;
import recommendation.parser.MovieParser;
import recommendation.parser.UserParser;
import recommendation.model.Movie;
import recommendation.model.User;

import java.util.Collections;
import java.util.List;


public class RecommendationIntegrationTest {

    private static MovieParser movieParser;
    private static UserParser userParser;
    private static RecommendationSystem recommendationSystem;

    @BeforeAll
    public static void init() {
        // Initialize with valid data files for default setup
        String moviePath = RecommendationIntegrationTest.class.getResource("/movies/valid_movies.txt").getFile();
        movieParser = new MovieParser(moviePath);
        String userPath = RecommendationIntegrationTest.class.getResource("/users/valid_users.txt").getFile();
        userParser = new UserParser(userPath, movieParser.getMovies());
        recommendationSystem = new RecommendationSystem(userParser.getUsers(), movieParser.getMovies());
    }


    @Test
    public void testMovieParserWithInvalidGenres() {
        String path = getClass().getResource("/movies/invalid_movie_genres.txt").getFile();
        MovieParser parser = new MovieParser(path);
        assertFalse(parser.success());
        assertTrue(parser.getError().contains("Movie Genre"));
    }


    @Test
    public void testMalformedMovieFile() {
        String path = getClass().getResource("/movies/malformed_commas.txt").getFile();
        MovieParser parser = new MovieParser(path);
        assertFalse(parser.success());
        assertTrue(parser.getError().contains("not formatted correctly"));
    }


    @Test
    public void testUserParserWithInvalidMovieReference() {
        MovieParser mp = new MovieParser(getClass().getResource("/movies/valid_movies.txt").getFile());
        UserParser up = new UserParser(
                getClass().getResource("/users/non_existent_movie_ids.txt").getFile(),
                mp.getMovies()
        );
        assertFalse(up.success());
        assertTrue(up.getError().contains("does not exist"));
    }


    @Test
    public void testUserParserDataDeduplication() {
        MovieParser mp = new MovieParser(getClass().getResource("/movies/valid_movies.txt").getFile());
        UserParser up = new UserParser(
                getClass().getResource("/users/repeated_movie_ids.txt").getFile(),
                mp.getMovies()
        );
        assertTrue(up.success());
        List<User> users = up.getUsers();
        users.forEach(user -> {
            List<String> watched = user.getMovieIds();
            assertEquals(watched.size(), watched.stream().distinct().count());
        });
    }

    @Test
    public void testBasicRecommendations() {
        List<Movie> movies = List.of(
                new Movie("Inception", "INCP01", List.of("Sci-Fi")),
                new Movie("Interstellar", "INST02", List.of("Sci-Fi"))
        );

        User user = new User("SciFiFan", "USR123", List.of("INCP01"));
        RecommendationSystem rs = new RecommendationSystem(List.of(user), movies);

        List<Movie> recommendations = rs.getSingleRecommendations().get(0).getMovies();
        assertTrue(recommendations.stream().anyMatch(m -> m.getMovieId().equals("INST02")));
    }


    @Test
    public void testEndToEndRecommendations() {
        assertTrue(movieParser.success());
        assertTrue(userParser.success());

        RecommendationSystem rs = recommendationSystem;
        assertFalse(rs.getSingleRecommendations().isEmpty());
        rs.getSingleRecommendations().forEach(recommendation -> {
            assertNotNull(recommendation.getUser());
            assertFalse(recommendation.getMovies().isEmpty());
        });
    }

    @Test
    public void testEmptyDataHandling() {
        RecommendationSystem rs = new RecommendationSystem(List.of(), List.of());
        assertTrue(rs.getSingleRecommendations().isEmpty());
    }

    @Test
    public void testMalformedUserFile() {
        MovieParser mp = new MovieParser(getClass().getResource("/movies/valid_movies.txt").getFile());
        UserParser up = new UserParser(
                getClass().getResource("/users/malformed_commas.txt").getFile(),
                mp.getMovies()
        );
        assertFalse(up.success());
        assertTrue(up.getError().contains("not formatted correctly"));
    }


    @Test
    public void testGenreBasedRecommendations() {
        List<Movie> movies = List.of(
                new Movie("Action Movie", "ACT001", List.of("Action")),
                new Movie("Comedy Movie", "COM002", List.of("Comedy")),
                new Movie("Action Comedy", "ACD003", List.of("Action", "Comedy"))
        );

        User user = new User("TestUser", "USR123", List.of("ACT001", "COM002"));
        RecommendationSystem rs = new RecommendationSystem(List.of(user), movies);

        List<Movie> recommendations = rs.getSingleRecommendations().get(0).getMovies();
        assertEquals(1, recommendations.size());
        assertEquals("ACD003", recommendations.get(0).getMovieId());
    }


    @Test
    public void testNoRecommendationsWhenAllWatched() {
        Movie onlyMovie = new Movie("Lone Movie", "LONE001", List.of("Drama"));
        User user = new User("MovieBuff", "MB123", List.of("LONE001"));

        RecommendationSystem rs = new RecommendationSystem(List.of(user), List.of(onlyMovie));
        assertTrue(rs.getSingleRecommendations().get(0).getMovies().isEmpty());
    }


    @Test
    public void testNewUserRecommendations() {
        List<Movie> movies = List.of(
                new Movie("Horror Movie", "HOR001", List.of("Horror"))
        );

        User user = new User("NewUser", "NEW123", Collections.emptyList());
        RecommendationSystem rs = new RecommendationSystem(List.of(user), movies);

        assertTrue(rs.getSingleRecommendations().get(0).getMovies().isEmpty());
    }


    @Test
    public void testNoMatchingGenreRecommendations() {
        List<Movie> movies = List.of(
                new Movie("Horror Movie", "HOR001", List.of("Horror")),
                new Movie("Comedy Film", "COM002", List.of("Comedy"))
        );

        User user = new User("HorrorFan", "HOR123", List.of("HOR001"));
        RecommendationSystem rs = new RecommendationSystem(List.of(user), movies);

        assertTrue(rs.getSingleRecommendations().get(0).getMovies().isEmpty());
    }
}
