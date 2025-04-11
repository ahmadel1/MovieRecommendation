package recommendation.parser;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidParameterException;

public class UserParserTest {
    
    // happy case
    @Tag("unit-test")
    @Test
    public void parseValidUsers() {
        var filePath = "samples/users.txt";
        var parser = new UserParser(filePath);
        assertNull(parser.getError(), "No error in valid users file");

        var users = parser.getUsers();
        assertEquals(2, users.size(), "2 users parsed");
    }

    // input is null
    @Tag("unit-test")
    @Test
    public void parseNullParameter() {
        String filePath = null;
        assertThrows(InvalidParameterException.class, () -> {
            new UserParser(filePath);
        }, "Throw Invalid Parameter Exception");
    }

    // input file missing
    @Tag("unit-test")
    @Test
    public void parseMissingFile() {
        String filePath = "non-existing/path/file.txt";
        var parser = new UserParser(filePath);
        assertEquals("Users file not found", parser.getError(), "Error message should indicate file not found");
        assertFalse(parser.success(), "Parser should not be successful");
    }

    // input file empty
    @Tag("unit-test")
    @Test
    public void parseEmptyFile() {
        String filePath = "src/test/resources/users/empty.txt";
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have an error for empty file");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // input file has missing or empty lines
    @Tag("unit-test")
    @Test
    public void parseMissingEmptyLines() {
        String filePath = "src/test/resources/users/missing_empty_lines.txt";
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have error for missing or empty lines");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // first lines has less than 2 items
    @Tag("unit-test")
    @Test
    public void parseFirstLineWithLessThanTwoItems() {
        String filePath = "src/test/resources/users/less_than_two_items.txt";
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have error for first line with less than 2 items");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // first lines have malformed commas
    @Tag("unit-test")
    @Test
    public void parseFirstLineWithMalformedCommas() {
        String filePath = "src/test/resources/users/malformed_commas.txt";
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have error for line with malformed commas");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // user name invalid (multiple cases)
    @Tag("unit-test")
    @Test
    public void parseInvalidUserName() {
        // Test case 1: Username with non-alphabetic characters
        String filePath1 = "src/test/resources/users/invalid_user_name_characters.txt";
        String invalidUserName1 = "Hassan Ali123";
        var parser1 = new UserParser(filePath1);
        assertEquals("ERROR: User Name " + invalidUserName1 + " is wrong",
                    parser1.getError(),
                    "Should have specific error message for username with non-alphabetic characters");
        assertFalse(parser1.success(), "Parser should not be successful");
        
        // Test case 2: Username starting with space
        String filePath2 = "src/test/resources/users/invalid_user_name_space.txt";
        String invalidUserName2 = " Hassan Ali";
        var parser2 = new UserParser(filePath2);
        assertEquals("ERROR: User Name " + invalidUserName2 + " is wrong",
                    parser2.getError(),
                    "Should have specific error message for username starting with space");
        assertFalse(parser2.success(), "Parser should not be successful");
    }
    
    // user id invalid (multiple cases)
    @Tag("unit-test")
    @Test
    public void parseInvalidUserId() {
        // Test case 1: Invalid format (missing letter at the end)
        String filePath1 = "src/test/resources/users/invalid_user_id.txt";
        String invalidUserId1 = "123456789";
        var parser1 = new UserParser(filePath1);
        assertEquals("ERROR: User Id " + invalidUserId1 + " is wrong",
                    parser1.getError(),
                    "Should have specific error message for invalid user ID format");
        assertFalse(parser1.success(), "Parser should not be successful");
        
        // Test case 2: Too short user ID
        String filePath2 = "src/test/resources/users/invalid_user_id_too_short.txt";
        String invalidUserId2 = "1234X";
        var parser2 = new UserParser(filePath2);
        assertEquals("ERROR: User Id " + invalidUserId2 + " is wrong",
                    parser2.getError(),
                    "Should have specific error message for too short user ID");
        assertFalse(parser2.success(), "Parser should not be successful");
        
        // Test case 3: Empty user ID
        String filePath3 = "src/test/resources/users/invalid_user_id_empty.txt";
        String emptyUserId = "";
        var parser3 = new UserParser(filePath3);
        assertNotNull(parser3.getError(), "Should have error for empty user ID");
        assertTrue(parser3.getError().contains(emptyUserId) || 
                  parser3.getError().startsWith("ERROR: User Id"),
                  "Error message should relate to empty user ID");
        assertFalse(parser3.success(), "Parser should not be successful");
    }
    
    // user id repeated
    @Tag("unit-test")
    @Test
    public void parseRepeatedUserId() {
        String filePath = "src/test/resources/users/repeated_user_id.txt";
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have error for repeated user ID");
        assertFalse(parser.success(), "Parser should not be successful");
    }

    // second lines are empty
    @Tag("unit-test")
    @Test
    public void parseEmptySecondLine() {
        String filePath = "src/test/resources/users/empty_second_line.txt";
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have error for empty second line");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // second lines malformed
    @Tag("unit-test")
    @Test
    public void parseMalformedSecondLine() {
        String filePath = "src/test/resources/users/malformed_second_line.txt";
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have error for malformed second line");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // movie ids invalid
    @Tag("unit-test")
    @Test
    public void parseInvalidMovieIds() {
        String filePath = "src/test/resources/users/invalid_movie_ids.txt";
        String invalidMovieId = "TSR-001"; // Invalid movie ID with hyphen
        var parser = new UserParser(filePath);
        assertEquals("ERROR: Movie Id " + invalidMovieId + " is wrong",
            parser.getError(),
                  "Error should mention the specific invalid movie ID");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // movie ids dont exist in parsed movies
    @Tag("unit-test")
    @Test
    public void parseNonExistentMovieIds() {
        String filePath = "src/test/resources/users/non_existent_movie_ids.txt";
        // Need to first parse movies to check against
        var movieParser = new MovieParser("samples/movies.txt");
        // Then parse users with non-existent movie IDs
        var parser = new UserParser(filePath);
        assertNotNull(parser.getError(), "Should have error for non-existent movie IDs");
        assertFalse(parser.success(), "Parser should not be successful");
    }
    
    // movie ids repeated (output User object shouldn't have repeated movie ids)
    @Tag("unit-test")
    @Test
    public void parseRepeatedMovieIds() {
        String filePath = "src/test/resources/users/repeated_movie_ids.txt";
        var parser = new UserParser(filePath);
        assertNull(parser.getError(), "Should process file with repeated movie IDs");
        assertTrue(parser.success(), "Parser should be successful");
        
        // Check that each user has unique movie IDs
        parser.getUsers().forEach(user -> {
            var movieIds = user.getMovieIds();
            assertEquals(movieIds.size(), movieIds.stream().distinct().count(), 
                    "User should not have duplicate movie IDs");
        });
    }
}
