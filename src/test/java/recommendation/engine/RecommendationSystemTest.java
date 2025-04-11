package recommendation.engine;

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

    @Test
    public void testRecommendationSystemInitialization() {
        setUp();
        assertNotNull(recommendationSystem);
    }

    @Test
    public void testEmptyInput(){
        recommendationSystem = new RecommendationSystem(Arrays.asList(), Arrays.asList());
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();
        assertTrue(recs.isEmpty(), "Recommendations should be empty when there are no users or movies");
    }

    @Test
    public void testGetSingleRecommendations_NotEmpty() {
        setUp();
        List<SingleRecommendation> recs = recommendationSystem.getSingleRecommendations();
        assertEquals(2, recs.size(), "Should return one recommendation per user");
    }

    @Test
    public void testRecommendationsForUser1() {
        setUp();
        SingleRecommendation rec = recommendationSystem.getSingleRecommendations().get(0);
        assertEquals("12345678X", rec.getUser().getUserId());

        List<Movie> recommended = rec.getMovies();
        assertTrue(recommended.stream().anyMatch(m -> m.getMovieId().equals("TG002")));
    }

    @Test
    public void testRecommendationsForUser2() {
        setUp();
        SingleRecommendation rec = recommendationSystem.getSingleRecommendations().get(1);
        assertEquals("87654321W", rec.getUser().getUserId());

        List<Movie> recommended = rec.getMovies();
        assertTrue(recommended.stream().anyMatch(m -> m.getMovieId().equals("TDK003")));
        assertTrue(recommended.stream().anyMatch(m -> m.getMovieId().equals("TSR001")));
    }

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
}
