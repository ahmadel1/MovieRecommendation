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

        if (movieFile == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }

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

                Validation.movieTitleValidation(mtitle);

                Validation.movieIdValidation(mid, mtitle);

                var secondLine = bufread.readLine();
                var parsedGenres = List.of(secondLine.split(", "));

                // Use a HashSet to automatically remove duplicates
                var movieGenres = new ArrayList<String>();
                var uniqueGenres = new HashSet<String>();
                
                // remove spaces in the beginning and end of each genre
                // and ensure uniqueness
                for (int i = 0; i < parsedGenres.size(); i++) {
                    String genre = parsedGenres.get(i).trim();
                    if (uniqueGenres.add(genre)) {
                        movieGenres.add(genre);
                    }
                }
                
                Validation.movieGenresValidation(movieGenres, mid);
                
                
                var movie = new Movie(mtitle, mid, movieGenres);
                movies.add(movie);
            }
            
            Validation.movieIdUniquenessValidation(movies);
            
            bufread.close();
            System.out.println("Loaded movies: " + movies.toString());
        
        } catch(ValidationException e) {
            System.out.println("Validation: " +e);
            error = e.getMessage();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception: " +e);
            error = "Movies file is not formatted correctly";
        }
        catch(NullPointerException e) {
            System.out.println("Exception: " +e);
            error = "Movies file is not formatted correctly";
        } 
        catch(IllegalArgumentException e) {
            System.out.println("Exception: " +e);
            error = "Movies file is not formatted correctly";
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