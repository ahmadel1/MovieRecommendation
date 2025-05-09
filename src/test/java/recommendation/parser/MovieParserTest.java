package recommendation.parser;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidParameterException;

public class MovieParserTest {

    // happy case
    @Tag("unit-test")
    @Test
    public void parseValidMovies() {
        var filePath = "src/test/resources/movies/valid_movies.txt";
        var parser = new MovieParser(filePath);
        assertNull(parser.getError(), "No error in valid movies file");

        var movies = parser.getMovies();
        assertEquals(3, movies.size(), "3 movies parsed");
    }

    // input is null
//    @Tag("unit-test")
//    @Test
//    public void parseNullParameter() {
//        String filePath = null;
//        assertThrows(IllegalArgumentException.class, () -> {
//            new MovieParser(filePath);
//        }, "Throw Illegal Argument Exception");
//    }

    // input file missing
    @Tag("unit-test")
    @Test
    public void parseMissingFile() {
        String filePath = "non-existing/path/file.txt";
        var parser = new MovieParser(filePath);
        assertEquals("Movies file not found", parser.getError(), "Error message should indicate file not found");
        assertFalse(parser.success(), "Parser should not be successful");
    }

    // input file empty
    @Tag("unit-test")
    @Test
    public void parseEmptyFile() {
        String filePath = "src/test/resources/movies/empty.txt";
        var parser = new MovieParser(filePath);
        assertNotNull(parser.getError(), "Should have an error for empty file");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // input file has missing or empty lines
    @Tag("unit-test")
    @Test
    public void parseMissingEmptyLines() {
        String filePath = "src/test/resources/movies/missing_empty_lines.txt";
        var parser = new MovieParser(filePath);
        assertNotNull(parser.getError(), "Should have error for missing or empty lines");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // first lines has less than 2 items
    @Tag("unit-test")
    @Test
    public void parseFirstLineWithLessThanTwoItems() {
        String filePath = "src/test/resources/movies/less_than_two_items.txt";
        var parser = new MovieParser(filePath);
        assertNotNull(parser.getError(), "Should have error for first line with less than 2 items");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // first lines have malformed commas
    @Tag("unit-test")
    @Test
    public void parseFirstLineWithMalformedCommas() {
        String filePath = "src/test/resources/movies/malformed_commas.txt";
        var parser = new MovieParser(filePath);
        assertNotNull(parser.getError(), "Should have error for line with malformed commas");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // movie name invalid (multiple cases)
    @Tag("unit-test")
    @Test
    public void parseInvalidMovieName() {
        // Test case 1: Words not starting with capital letters
        String filePath1 = "src/test/resources/movies/invalid_movie_name_lowercase.txt";
        String invalidTitle = "the shawshank Redemption";
        var parser1 = new MovieParser(filePath1);
        assertEquals("ERROR: Movie Title {" + invalidTitle + "} is wrong", 
                     parser1.getError(),
                     "Should have specific error message for lowercase words in title");
        assertFalse(parser1.success(), "Parser should not be successful");
        
        // Test case 2: Empty movie name
        String filePath2 = "src/test/resources/movies/invalid_movie_name_empty.txt";
        String emptyTitle = "";
        var parser2 = new MovieParser(filePath2);
        assertEquals("ERROR: Movie Title {" + emptyTitle + "} is wrong", 
                     parser2.getError(),
                     "Should have specific error message for empty title");
        assertFalse(parser2.success(), "Parser should not be successful");
        
        // Test case 3: Movie name with numbers at start
        String filePath3 = "src/test/resources/movies/invalid_movie_name_numbers.txt";
        String titleWithNumbers = "12 Angry Men";
        var parser3 = new MovieParser(filePath3);
        assertEquals("ERROR: Movie Title {" + titleWithNumbers + "} is wrong", 
                     parser3.getError(),
                     "Should have specific error message for title with numbers at start");
        assertFalse(parser3.success(), "Parser should not be successful");
    }
    
    // movie id invalid (multiple cases)
    @Tag("unit-test")
    @Test
    public void parseInvalidMovieId() {
        // Test case 1: ID not containing all capital letters from title
        String filePath1 = "src/test/resources/movies/invalid_movie_id_missing_capitals.txt";
        var parser1 = new MovieParser(filePath1);
        assertFalse(parser1.success(), "Parser should not be successful");
        assertTrue(parser1.getError().contains("TR001"), "Error message should include the invalid ID");
        
        // Test case 2: ID not having exactly 3 numbers
        String filePath2 = "src/test/resources/movies/invalid_movie_id_wrong_number_count.txt";
        var parser2 = new MovieParser(filePath2);
        assertFalse(parser2.success(), "Parser should not be successful");
        assertTrue(parser2.getError().contains("TSR01"), 
                   "Error message should include the invalid ID");
        
        // Test case 3: ID with non-unique numbers
        String filePath3 = "src/test/resources/movies/invalid_movie_id_non_unique_numbers.txt";
        String invalidId3 = "TSR111";  // Repeated number '1'
        var parser3 = new MovieParser(filePath3);
//        assertFalse(parser3.success(), "Parser should not be successful");
//        assertTrue(parser3.getError().contains(invalidId3),
//                   "Error message should include the invalid ID");
        
        // Test case 4: Empty ID
        String filePath4 = "src/test/resources/movies/invalid_movie_id_empty.txt";
        String emptyId = "";
        var parser4 = new MovieParser(filePath4);
        assertNotNull(parser4.getError(), "Should have error for empty movie ID");
        assertFalse(parser4.success(), "Parser should not be successful");
        assertTrue(parser4.getError().contains(emptyId), "Error message should include the empty ID");
    }
    
    // movie id repeated
    @Tag("unit-test")
    @Test
    public void parseRepeatedMovieId() {
        String filePath = "src/test/resources/movies/repeated_movie_id.txt";
        var parser = new MovieParser(filePath);
        assertNotNull(parser.getError(), "Should have error for repeated movie ID");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // second lines are empty
    @Tag("unit-test")
    @Test
    public void parseEmptySecondLine() {
        String filePath = "src/test/resources/movies/empty_second_line.txt";
        var parser = new MovieParser(filePath);
        assertNotNull(parser.getError(), "Should have error for empty second line");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // second lines malformed
    @Tag("unit-test")
    @Test
    public void parseMalformedSecondLine() {
        String filePath = "src/test/resources/movies/malformed_second_line.txt";
        var parser = new MovieParser(filePath);
        assertFalse(parser.success(), "Parser should not be successful");
        assertNotNull(parser.getError(), "Should have error for malformed second line");
    }
    
    // movie genres invalid
    @Tag("unit-test")
    @Test
    public void parseInvalidMovieGenres() {
        String filePath = "src/test/resources/movies/invalid_movie_genres.txt";
        var parser = new MovieParser(filePath);
        assertFalse(parser.success(), "Parser should not be successful");
        assertNotNull(parser.getError(), "Should have error for invalid movie genres");
    }
    
    // movie genres with empty genre in the list
    @Tag("unit-test")
    @Test
    public void parseEmptyGenresInList() {
        String filePath = "src/test/resources/movies/empty_genre_in_list.txt";
        var parser = new MovieParser(filePath);
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // movie genre repeated (output Movie object shouldn't have repeated genres)
    @Tag("unit-test")
    @Test
    public void parseRepeatedMovieGenres() {
        String filePath = "src/test/resources/movies/repeated_movie_genres.txt";
        var parser = new MovieParser(filePath);
        assertTrue(parser.success(), "Parser should be successful");
        assertNull(parser.getError(), "Should process file with repeated genres");
        // Check that each movie has unique genres
        parser.getMovies().forEach(movie -> {
            var genres = movie.getMovieGenres();
            assertEquals(genres.size(), genres.stream().distinct().count(), 
                    "Movie should not have duplicate genres");
        });
    }
    @Tag("unit-test")
    @Test
    public void parseNullFilePath() {
        String filePath = null;
        var parser = new MovieParser(filePath);
        assertFalse(parser.success(), "Movie Parser failed");
        assertNotNull(parser.getError(), "Error parsing");
    }
}
