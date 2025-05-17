package recommendation.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import recommendation.engine.RecommendationSystem;
import recommendation.parser.MovieParser;
import recommendation.parser.UserParser;
import recommendation.model.Movie;
import recommendation.model.User;
import recommendation.writer.Writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;


public class RecommendationIntegrationTest {

    private static MovieParser movieParser;
    private static UserParser userParser;
    private static RecommendationSystem recommendationSystem;

    @BeforeAll
    public static void init() {
        String moviePath = "src/test/resources/movies/valid_movies.txt";
        movieParser = new MovieParser(moviePath);
        String userPath = "src/test/resources/users/valid_users.txt";
        userParser = new UserParser(userPath, movieParser.getMovies());
        recommendationSystem = new RecommendationSystem(userParser.getUsers(), movieParser.getMovies());
    }

    @AfterEach
    public void cleanup() {
        // Clean up generated files after each test
        deleteFileIfExists("samples/recommendations.txt");
        deleteFileIfExists("samples/errors.txt");
    }

    private void deleteFileIfExists(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            // Ignore exceptions during cleanup
        }
    }


    @Test
    public void testMovieParserWithInvalidGenres() {
        String path = "src/test/resources/movies/invalid_movie_genres.txt";
        MovieParser parser = new MovieParser(path);
        assertFalse(parser.success());
        assertTrue(parser.getError().contains("Movie Genre"));
    }


    @Test
    public void testMalformedMovieFile() {
        String path = "src/test/resources/movies/malformed_commas.txt";
        MovieParser parser = new MovieParser(path);
        assertFalse(parser.success());
        assertTrue(parser.getError().contains("not formatted correctly"));
    }


    @Test
    public void testUserParserWithInvalidMovieReference() {
        MovieParser mp = new MovieParser("src/test/resources/movies/valid_movies.txt");
        UserParser up = new UserParser(
                "src/test/resources/users/non_existent_movie_ids.txt",
                mp.getMovies()
        );
        assertFalse(up.success());
        assertTrue(up.getError().contains("does not exist"));
    }


    @Test
    public void testUserParserDataDeduplication() {
        MovieParser mp = new MovieParser("src/test/resources/movies/valid_movies.txt");
        UserParser up = new UserParser(
                "src/test/resources/users/repeated_movie_ids.txt",
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
        MovieParser mp = new MovieParser("src/test/resources/movies/valid_movies.txt");
        UserParser up = new UserParser(
                "src/test/resources/users/malformed_commas.txt",
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


    @Test
    public void testWriteRecommendationsToFile() {
        Writer writer = new Writer();
        writer.writeRecommendations(recommendationSystem.getSingleRecommendations());

        Path path = Paths.get("samples/recommendations.txt");
        assertTrue(Files.exists(path), "Recommendations file should be created");

        try {
            List<String> lines = Files.readAllLines(path);
            assertFalse(lines.isEmpty(), "File should not be empty");
            // Check if the first line contains a comma-separated user info
            assertTrue(lines.get(0).contains(", "), "First line should contain user name and ID separated by a comma");
            assertTrue(lines.size() >= 2, "There should be movie recommendations");
        } catch (IOException e) {
            fail("IOException reading recommendations file: " + e.getMessage());
        }
    }


    @Test
    public void testWriteErrorToFile() {
        String testError = "Test error message";
        Writer writer = new Writer();
        try {
            writer.writeError(testError);
            Path errorPath = Paths.get("samples/errors.txt");
            assertTrue(Files.exists(errorPath), "Error file should be created");

            List<String> lines = Files.readAllLines(errorPath);
            assertEquals(1, lines.size(), "Error file should have one line");
            assertEquals(testError, lines.get(0), "Error message should match");
        } catch (IOException e) {
            fail("IOException handling error file: " + e.getMessage());
        }
    }

    @Test
    public void testWriteEmptyRecommendations() {
        User user = new User("EmptyUser", "EMPTY001", Collections.emptyList());
        List<Movie> movies = Collections.emptyList();
        RecommendationSystem rs = new RecommendationSystem(List.of(user), movies);
        Writer writer = new Writer();
        writer.writeRecommendations(rs.getSingleRecommendations());

        Path path = Paths.get("samples/recommendations.txt");
        try {
            List<String> lines = Files.readAllLines(path);
            assertEquals(2, lines.size(), "User line and empty recommendation line");
            assertEquals("EmptyUser, EMPTY001", lines.get(0));
            assertEquals("", lines.get(1), "Recommendation line should be empty");
        } catch (IOException e) {
            fail("IOException reading recommendations file: " + e.getMessage());
        }
    }
}
