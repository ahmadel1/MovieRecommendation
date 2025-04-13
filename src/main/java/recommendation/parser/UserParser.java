package recommendation.parser;

import recommendation.model.Movie;
import recommendation.model.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserParser {
    private List<User> users;
    private String error = null;

    public UserParser(String userFile, List<Movie> movies) {
        if (userFile == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (movies == null) {
            throw new IllegalArgumentException("Movies list cannot be null");
        }

        users = new ArrayList<>();

        try
        {
            FileReader fileReader = new FileReader(userFile);
            BufferedReader bufread = new BufferedReader(fileReader);

            String firstLine;
            
            // TODO: check against missing lines, empty lines

            // reading the file, 2 lines by 2 lines
            while((firstLine = bufread.readLine()) != null) {
                var firstLineParts = firstLine.split(", ");
                var uname = firstLineParts[0];
                var uid = firstLineParts[1];

                Validation.userNameValidation(uname);

                Validation.userIdValidation(uid);

                var secondLine = bufread.readLine();
                var parsedMovieIds = List.of(secondLine.split(", "));

                if (parsedMovieIds.size() == 0) {
                    throw new ValidationException("ERROR: Movie Ids for {"+uid+"} are wrong");
                }
                // Use a HashSet to automatically remove duplicates
                var movieIds = new ArrayList<String>();
                var uniqueMovieIds = new HashSet<String>();
                // remove spaces in the beginning and end of each movie ID
                // and ensure uniqueness
                for (int i = 0; i < parsedMovieIds.size(); i++) {
                    String movieId = parsedMovieIds.get(i).trim();
                    if (uniqueMovieIds.add(movieId)) {
                        movieIds.add(movieId);
                    }
                }

                // validate movies exist
                for (String movieId : movieIds) {
                    boolean exists = movies.stream().anyMatch(movie -> movie.getMovieId().equals(movieId));
                    if (!exists) {
                        throw new ValidationException("ERROR: Movie Id {"+movieId+"} does not exist");
                    }
                }
                
                
                var user = new User(uname, uid, movieIds);
                users.add(user);
            }
            
            bufread.close();
            Validation.userIdUniquenessValidation(users);
            System.out.println("Loaded users: " + users.toString());
        } catch(ValidationException e) {
            System.out.println("Validation: " +e);
            error = e.getMessage();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception: " +e);
            error = "Users file is not formatted correctly";
        }
        catch(NullPointerException e) {
            System.out.println("Exception: " +e);
            error = "Users file is not formatted correctly";
        } 
        catch(IllegalArgumentException e) {
            System.out.println("Exception: " +e);
            error = "Users file is not formatted correctly";
        } catch(FileNotFoundException e) {
            System.out.println("Exception: " +e);
            error = "Users file not found";
        }
        catch(IOException e)
        {
            System.out.println("Exception: " +e);
            error = e.getMessage();
        }

    }

    public List<User> getUsers() {
        return users;
    }

    public Boolean success() {
        return error == null;
    }

    public String getError() {
        return error;
    }

    public void userIdUniquenessValidation() throws ValidationException{
        Set<String> uniqueUserIds = new HashSet<>();
        for(int i=0;i<users.size();i++){
            if(!uniqueUserIds.add(users.get(i).getUserId())){
                throw new ValidationException("ERROR: User Id {"+users.get(i).getUserId()+"} is wrong");
            }
        }
    }
}