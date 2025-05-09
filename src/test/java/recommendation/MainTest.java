package recommendation;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private static final String OUTPUT_FILE = "samples/recommendations.txt";
    private static final String ERROR_FILE = "samples/errors.txt";

    @BeforeEach
    public void cleanOutput() throws IOException {
        Files.deleteIfExists(Paths.get(OUTPUT_FILE));
        Files.deleteIfExists(Paths.get(ERROR_FILE));
    }

    @Test
    public void testValidInput() throws IOException {
        String userFile = "src/test/resources/users/valid_users.txt";
        String movieFile = "src/test/resources/movies/valid_movies.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(OUTPUT_FILE)));
        String output = Files.readString(Paths.get(OUTPUT_FILE));
        assertFalse(output.contains("ERROR"));
        assertTrue(output.contains("Hassan Ali, 12345678X"));
        assertTrue(output.contains("Ali Mohamed, 87654321W"));
        assertFalse(Files.exists(Paths.get(ERROR_FILE)));
    }

    @Test
    public void testInvalidMovieTitle() throws IOException {
        String userFile = "src/test/resources/users/valid_users.txt";
        String movieFile = "src/test/resources/movies/invalid_movie_name_lowercase.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("ERROR: Movie Title {the shawshank Redemption} is wrong", error);
    }

    @Test
    public void testInvalidMovieIdLetters() throws IOException {
        String userFile = "src/test/resources/users/valid_users.txt";
        String movieFile = "src/test/resources/movies/invalid_movie_id_missing_capitals.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("ERROR: Movie Id letters {TR001} are wrong", error);
    }

    @Test
    public void testInvalidMovieIdNumbers() throws IOException {
        String userFile = "src/test/resources/users/valid_users.txt";
        String movieFile = "src/test/resources/movies/invalid_movie_id_wrong_number_count.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("ERROR: Movie Id letters {TSR01} are wrong", error);
    }

    @Test
    public void testInvalidUserName() throws IOException {
        String userFile = "src/test/resources/users/invalid_user_name_space.txt";
        String movieFile = "src/test/resources/movies/valid_movies.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("ERROR: User Name { Hassan Ali} is wrong", error);
    }

    @Test
    public void testInvalidUserId() throws IOException {
        String userFile = "src/test/resources/users/invalid_user_id.txt";
        String movieFile = "src/test/resources/movies/valid_movies.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("ERROR: User Id {1234567XY} is wrong", error);
    }

    @Test
    public void testEmptyUserFile() throws IOException {
        String userFile = "src/test/resources/users/empty.txt";
        String movieFile = "src/test/resources/movies/valid_movies.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("Users file is not formatted correctly", error);
    }

    @Test
    public void testEmptyMovieFile() throws IOException {
        String userFile = "src/test/resources/users/valid_users.txt";
        String movieFile = "src/test/resources/movies/empty.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("Movies file is not formatted correctly", error);
    }

    @Test
    public void testUserFileWithNoLikedMovies() throws IOException {
        String userFile = "src/test/resources/users/empty_second_line.txt";
        String movieFile = "src/test/resources/movies/valid_movies.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("ERROR: Missing movie Ids for user {Hassan Ali}", error);
    }

    @Test
    public void testMovieFileWithNoGenres() throws IOException {
        String userFile = "src/test/resources/users/valid_users.txt";
        String movieFile = "src/test/resources/movies/empty_second_line.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("ERROR: A Movie Genre for {TSR001} is empty", error);
    }

    @Test
    public void testMalformedMovieFile() throws IOException {
        String userFile = "src/test/resources/users/valid_users.txt";
        String movieFile = "src/test/resources/movies/missing_empty_lines.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("Movies file is not formatted correctly", error);
    }

    @Test
    public void testMalformedUserFile() throws IOException {
        String userFile = "src/test/resources/users/missing_empty_lines.txt";
        String movieFile = "src/test/resources/movies/valid_movies.txt";
        new Main(userFile, movieFile);
        assertTrue(Files.exists(Paths.get(ERROR_FILE)));
        String error = Files.readString(Paths.get(ERROR_FILE));
        assertEquals("Users file is not formatted correctly", error);
    }
}
