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

public class MovieParser {
    private List<Movie> movies;
    private String error;

    public MovieParser(String movieFile) {
        movies = new ArrayList<>();

        try
        {
            FileReader fileReader = new FileReader(movieFile);
            BufferedReader bufread = new BufferedReader(fileReader);

            String firstLine;
            
            // TODO: check against missing lines, empty lines

            // reading the file, 2 lines by 2 lines
            while((firstLine = bufread.readLine()) != null) {
                var firstLineParts = firstLine.split(", ");
                var mtitle = firstLineParts[0];
                var mid = firstLineParts[1];

                // TODO: check if both exist

                // TODO: validate movie name
                // TODO: validate movie ID

                var secondLine = bufread.readLine();
                var movieGenres = List.of(secondLine.split(", "));

                // TODO: validate movie genres
                // TODO: validate movie IDs are not repeated

                var movie = new Movie(mtitle, mid, movieGenres);
                movies.add(movie);
            }
            
            bufread.close();
            System.out.println("Loaded movies: " + movies.toString());
        } catch(FileNotFoundException e) {
            System.out.println("Exception: " +e);
            error = "Movies file not found";
        }
        catch(IOException e)
        {
            System.out.println("Exception: " +e);
            error = e.getMessage();
        }

    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Boolean success() {
        return error == null;
    }

    public String getError() {
        return error;
    }
    public void movieIdUniquenessValidation() throws ValidationException{
        Set<String> uniqueMovieIds = new HashSet<>();
        for(int i=0;i<movies.size();i++){
            String numberInMovieId = movies.get(i).getMovieId().substring(movies.get(i).getMovieId().length()-3);
            System.out.println("id: "+numberInMovieId);
            if(!uniqueMovieIds.add(numberInMovieId)){
                throw new ValidationException("ERROR: Movie Id numbers {"+numberInMovieId+"} aren’t unique");
            }
        }
    }
}