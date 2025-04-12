package recommendation.engine;

import org.junit.jupiter.api.Tag;
import recommendation.model.Movie;
import recommendation.model.User;
import recommendation.model.SingleRecommendation;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RecommendationSystemTest {

    private List<Movie> movies;
    private List<User> users;
    private RecommendationSystem recommendationSystem;

    public void setUp() {
        movies = Arrays.asList(
                new Movie("The Shawshank Redemption", "TSR001", Arrays.asList("Drama")),
                new Movie("The Godfather", "TG002", Arrays.asList("Crime", "Drama")),
                new Movie("The Dark Knight", "TDK003", Arrays.asList("Action", "Crime", "Drama"))
        );

        users = Arrays.asList(
                new User("Hassan Ali", "12345678X",Arrays.asList("TSR001", "TDK003")),
                new User("Ali Mohamed", "87654321W",Arrays.asList("TG002"))
        );

        recommendationSystem = new RecommendationSystem(users, movies);
    }

    @Tag("unit-test")
    @Test
    public void testRecommendationSystemInitialization() {
        setUp();
        assertNotNull(recommendationSystem);
    }

    @Tag("unit-test")
    @Test
    public void testEmptyInput(){
        recommendationSystem = new RecommendationSystem(Arrays.asList(), Arrays.asList());
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();
        assertTrue(recs.isEmpty(), "Recommendations should be empty when there are no users or movies");
    }

    @Tag("unit-test")
    @Test
    public void testGetSingleRecommendations_NotEmpty() {
        setUp();
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();
        assertEquals(2, recs.size(), "Should return one recommendation per user");
    }

    @Tag("unit-test")
    @Test
    public void testRecommendationsForUser1() {
        setUp();
        SingleRecommendation rec = recommendationSystem.getSingleRecommendations().get(0);
        assertEquals("12345678X", rec.getUser().getUserId());

        List<Movie> recommended = rec.getMovies();
        assertTrue(recommended.stream().anyMatch(m -> m.getMovieId().equals("TG002")));
    }

    @Tag("unit-test")
    @Test
    public void testRecommendationsForUser2() {
        setUp();
        SingleRecommendation rec = recommendationSystem.getSingleRecommendations().get(1);
        assertEquals("87654321W", rec.getUser().getUserId());

        List<Movie> recommended = rec.getMovies();
        assertTrue(recommended.stream().anyMatch(m -> m.getMovieId().equals("TDK003")));
        assertTrue(recommended.stream().anyMatch(m -> m.getMovieId().equals("TSR001")));
    }

    @Tag("unit-test")
    @Test
    public void testRecsNotIncludeMovieUserWatched() {
        setUp();
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();

        for (SingleRecommendation recommendation : recs) {
            User user = recommendation.getUser();
            List<String> watchedMovieIds = user.getMovieIds();

            for (Movie recommendedMovie : recommendation.getMovies()) {
                assertFalse(watchedMovieIds.contains(recommendedMovie.getMovieId()),
                        "Recommended movie should not be one the user has already watched");
            }
        }
    }

    @Tag("unit-test")
    @Test
    public void testUserWithNoWatchedMovies() {
        movies = Arrays.asList(
                new Movie("The Shawshank Redemption", "TSR001", Arrays.asList("Drama")),
                new Movie("The Godfather", "TG002", Arrays.asList("Crime", "Drama"))
        );

        users = Arrays.asList(
                new User("New User", "00000000X", Arrays.asList()) // no watched movies
        );

        recommendationSystem = new RecommendationSystem(users, movies);
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();

        assertEquals(1, recs.size());
        assertEquals(0, recs.get(0).getMovies().size());
    }

    @Tag("unit-test")
    @Test
    public void testAllMoviesWatchedByUser() {
        movies = Arrays.asList(
                new Movie("Movie1", "M001", Arrays.asList("Genre1")),
                new Movie("Movie2", "M002", Arrays.asList("Genre2"))
        );

        users = Arrays.asList(
                new User("Test User", "99999999Z", Arrays.asList("M001", "M002")) // watched all
        );

        recommendationSystem = new RecommendationSystem(users, movies);
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();

        assertTrue(recs.get(0).getMovies().isEmpty(), "Should return no recommendations if user has seen all movies");
    }

    @Tag("unit-test")
    @Test
    public void testDuplicateGenresHandling() {
        movies = Arrays.asList(
                new Movie("Action Movie 1", "A001", Arrays.asList("Action")),
                new Movie("Action Movie 2", "A002", Arrays.asList("Action")),
                new Movie("Drama Movie", "D001", Arrays.asList("Drama"))
        );

        users = Arrays.asList(
                new User("Genre Lover", "G123", Arrays.asList("A001"))
        );

        recommendationSystem = new RecommendationSystem(users, movies);
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();
        List<Movie> recommended = recs.get(0).getMovies();

        assertTrue(recommended.stream().anyMatch(m -> m.getMovieId().equals("A002")), "Should recommend movies of same genre");
        assertFalse(recommended.stream().anyMatch(m -> m.getMovieId().equals("A001")), "Should not recommend already watched movie");
    }

    @Tag("unit-test")
    @Test
    public void testNullUserList() {
        recommendationSystem = new RecommendationSystem(null, movies);
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();
        assertTrue(recs.isEmpty(), "Should handle null users list gracefully");
    }

    @Tag("unit-test")
    @Test
    public void testNullMovieList() {
        recommendationSystem = new RecommendationSystem(users, null);
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();
        for (SingleRecommendation rec : recs) {
            assertTrue(rec.getMovies().isEmpty(), "Should handle null movies list gracefully");
        }
    }

    @Tag("unit-test")
    @Test
    public void testRecommendationsAreUnique() {
        setUp();
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();

        for (SingleRecommendation rec : recs) {
            List<String> movieIds = rec.getMovies().stream().map(Movie::getMovieId).toList();
            long uniqueCount = movieIds.stream().distinct().count();
            assertEquals(movieIds.size(), uniqueCount, "Recommended movies should be unique");
        }
    }

}
