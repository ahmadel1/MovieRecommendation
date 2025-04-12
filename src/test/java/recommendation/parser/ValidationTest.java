package recommendation.parser;

import org.junit.jupiter.api.*;
import recommendation.model.Movie;
import recommendation.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class ValidationTest {

    //correct movie title
    @Tag("unit-test")
    @Test
    public void validateCorrectMovieTitle() {

        String movieTitle = "The Shawshank Redemption";
        try{
            Validation.movieTitleValidation(movieTitle);
        }
        catch (ValidationException e){
            fail("ValidationException should not be thrown for movie title:"+movieTitle);
        }
    }

    //movie title has word starts with small letter
    @Tag("unit-test")
    @Test
    public void validateMovieTitleWithWordStartsWithSmallLetter() {

        String movieTitle = "The shawshank Redemption";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.movieTitleValidation(movieTitle);
        });
        assertEquals("ERROR: Movie Title {"+movieTitle+"} is wrong",validationException.getMessage());
    }

    //movie title has special characters
    @Tag("unit-test")
    @Test
    public void validateMovieTitleWithSpecialCharacters() {

        String movieTitle = "The shawshank @Redemption";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.movieTitleValidation(movieTitle);
        });
        assertEquals("ERROR: Movie Title {"+movieTitle+"} is wrong",validationException.getMessage());
    }

    //correct movie id
    @Tag("unit-test")
    @Test
    public void validateCorrectMovieId() {

        String movieTitle = "The Shawshank Redemption";
        String movieId = "TSR001";
        try{
            Validation.movieIdValidation(movieTitle,movieId);
        }
        catch (ValidationException e){
            fail("ValidationException should not be thrown for movie id:"+movieId);
        }
    }

    //movie id not match corresponding movie title
    @Tag("unit-test")
    @Test
    public void validateMovieIdWithNotMatchMovieTitle() {
        String movieTitle = "The Shawshank Redemption";
        String movieId = "TWR001";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.movieIdValidation(movieTitle,movieId);
        });
        assertEquals("ERROR: Movie Id letters {"+movieId+"} are wrong",validationException.getMessage());
    }

    //movie id with not 3 numbers
    @Tag("unit-test")
    @Test
    public void validateMovieIdWithNotThreeNumbers() {
        String movieTitle = "The God Father";
        String movieId = "TGF02";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.movieIdValidation(movieTitle,movieId);
        });
        assertEquals("ERROR: Movie Id letters {"+movieId+"} are wrong",validationException.getMessage());
    }

    //movie id with special characters
    @Tag("unit-test")
    @Test
    public void validateMovieIdWithSpecialCharacters() {
        String movieTitle = "The God Father";
        String movieId = "TGF#002";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.movieIdValidation(movieTitle,movieId);
        });
        assertEquals("ERROR: Movie Id letters {"+movieId+"} are wrong",validationException.getMessage());
    }

    //correct username
    @Tag("unit-test")
    @Test
    public void validateCorrectUserName() {

        String userName = "Hassan Ali";
        try{
            Validation.userNameValidation(userName);
        }
        catch (ValidationException e){
            fail("ValidationException should not be thrown for username:"+userName);
        }
    }

    //username starts with space character
    @Tag("unit-test")
    @Test
    public void validateUserNameStartsWithSpaceCharacter() {
        String userName = " Ali Mohamed";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.userNameValidation(userName);
        });
        assertEquals("ERROR: User Name {"+userName+"} is wrong",validationException.getMessage());
    }

    //username with invalid characters
    @Tag("unit-test")
    @Test
    public void validateUserNameWithInvalidCharacters() {
        String userName = "Fady Ashraf3";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.userNameValidation(userName);
        });
        assertEquals("ERROR: User Name {"+userName+"} is wrong",validationException.getMessage());
    }

    //correct userId
    @Tag("unit-test")
    @Test
    public void validateCorrectUserId() {

        String userId = "12345678X";
        try{
            Validation.userIdValidation(userId);
        }
        catch (ValidationException e){
            fail("ValidationException should not be thrown for username:"+userId);
        }
    }

    //user id with invalid length
    @Tag("unit-test")
    @Test
    public void validateUserIdWithInvalidLength() {
        String userId = "123456789X";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.userIdValidation(userId);
        });
        assertEquals("ERROR: User Id {"+userId+"} is wrong",validationException.getMessage());
    }

    //user id with special characters
    @Tag("unit-test")
    @Test
    public void validateUserIdWithSpecialCharacters() {
        String userId = "1234567^X";
        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.userIdValidation(userId);
        });
        assertEquals("ERROR: User Id {"+userId+"} is wrong",validationException.getMessage());
    }

    //correct userId uniqueness
    @Tag("unit-test")
    @Test
    public void validateCorrectUserIdUniqueness() {
        List<User> users= new ArrayList<>();
        users.add(0,new User("Fady","001",new ArrayList<String>(){{add("drama");add("action");}}));
        users.add(1,new User("Ahmed","002",new ArrayList<String>(){{add("drama");add("action");}}));
        try{
            Validation.userIdUniquenessValidation(users);
        }
        catch (ValidationException e){
            fail("ValidationException should not be thrown for these users:"+users);
        }
    }

    //Incorrect userId uniqueness
    @Tag("unit-test")
    @Test
    public void validateInCorrectUserIdUniqueness() {
        List<User> users= new ArrayList<>();
        users.add(0,new User("Fady","001",new ArrayList<String>(){{add("drama");add("action");}}));
        users.add(1,new User("Ahmed","001",new ArrayList<String>(){{add("drama");add("action");}}));

        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.userIdUniquenessValidation(users);
        });
        assertEquals("ERROR: User Id {001} is wrong",validationException.getMessage());
    }

    //correct movieId uniqueness
    @Tag("unit-test")
    @Test
    public void validateCorrectMovieIdUniqueness() {
        List<Movie> movies= new ArrayList<>();
        movies.add(0,new Movie("Title Of Xe","TOX121",new ArrayList<String>(){{add("drama");add("action");}}));
        movies.add(1,new Movie("Title Of Xe","TOX122",new ArrayList<String>(){{add("drama");add("action");}}));
        movies.add(2,new Movie("Title Of Xe","TOX123",new ArrayList<String>(){{add("drama");add("action");}}));
        try{
            Validation.movieIdUniquenessValidation(movies);
        }
        catch (ValidationException e){
            fail("ValidationException should not be thrown for these movies:"+movies);
        }
    }

    //Incorrect movieId uniqueness
    @Tag("unit-test")
    @Test
    public void validateInCorrectMovieIdUniqueness() {
        List<Movie> movies= new ArrayList<>();
        movies.add(0,new Movie("Title Of Xe","TOX121",new ArrayList<String>(){{add("drama");add("action");}}));
        movies.add(1,new Movie("Title Of Xe","TOX121",new ArrayList<String>(){{add("drama");add("action");}}));
        movies.add(2,new Movie("Title Of Xe","TOX123",new ArrayList<String>(){{add("drama");add("action");}}));


        ValidationException validationException = assertThrows(ValidationException.class,() -> {
            Validation.movieIdUniquenessValidation(movies);
        });
        assertEquals("ERROR: Movie Id numbers {121} aren’t unique",validationException.getMessage());
    }
}
