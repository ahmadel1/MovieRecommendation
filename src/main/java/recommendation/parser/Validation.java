package recommendation.parser;

import recommendation.model.Movie;
import recommendation.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Validation {
    static public void movieTitleValidation(String movieTitle) throws ValidationException{
        if(movieTitle.matches("^[A-Za-z][A-Za-z\\s]+$")){
            String[] wordsInMovieTitle=movieTitle.split(" ");
            for(int i=0; i<wordsInMovieTitle.length; i++){
                if(!((wordsInMovieTitle[i].charAt(0)>=65) && (wordsInMovieTitle[i].charAt(0)<=90))) {
                    throw new ValidationException("ERROR: Movie Title {"+movieTitle+"} is wrong");
                }
            }
            return;
        }
        throw new ValidationException("ERROR: Movie Title {"+movieTitle+"} is wrong");
    }
    static public void movieIdValidation(String movieTitle,String movieId) throws ValidationException{
        String[] wordsInMovieTitle=movieTitle.split(" ");
        String firstLetterInEachMovieTitleWord="";
        for(int i=0; i<wordsInMovieTitle.length; i++){
            firstLetterInEachMovieTitleWord+= wordsInMovieTitle[i].charAt(0);
        }
        if(movieId.matches("^[A-Z0-9]+$")){
            if(!(firstLetterInEachMovieTitleWord.equals(movieId.substring(0,movieId.length()-3)))){
                throw new ValidationException("ERROR: Movie Id letters {"+movieId+"} are wrong");
            } else {
                String lastThreeCharsInMovieId = movieId.substring(movieId.length()-3);
                try{
                    Integer.parseInt(lastThreeCharsInMovieId);
                }
                catch (NumberFormatException e){
                    throw new ValidationException("ERROR: Movie Id letters {"+movieId+"} are wrong");
                }
            }
            return;
        }
        throw new ValidationException("ERROR: Movie Id letters {"+movieId+"} are wrong");
    }
    static public void userNameValidation(String userName) throws ValidationException{
        if(userName.matches("^[A-Za-z][A-Za-z\\s]+$")){
            return;
        }
        throw new ValidationException("ERROR: User Name {"+userName+"} is wrong");
    }
    static public void userIdValidation(String userId) throws ValidationException{
        if((userId.length() == 9) &&userId.matches("^[A-Za-z0-9]+$")){
            String firstEigthCharsInUserId = userId.substring(0,userId.length()-1);
            try{
                Integer.parseInt(firstEigthCharsInUserId);
            }
            catch (NumberFormatException e){
                throw new ValidationException("ERROR: User Id {"+userId+"} is wrong");
            }
            return;
        }
        throw new ValidationException("ERROR: User Id {"+userId+"} is wrong");
    }

    static public void userIdUniquenessValidation(List<User> users) throws ValidationException{
        Set<String> uniqueUserIds = new HashSet<>();
        for(int i=0;i<users.size();i++){
            if(!uniqueUserIds.add(users.get(i).getUserId())){
                throw new ValidationException("ERROR: User Id {"+users.get(i).getUserId()+"} is wrong");
            }
        }
    }
    static public void movieIdUniquenessValidation(List<Movie> movies) throws ValidationException{
        Set<String> uniqueMovieIds = new HashSet<>();
        for(int i=0;i<movies.size();i++){
            String numberInMovieId = movies.get(i).getMovieId().substring(movies.get(i).getMovieId().length()-3);
            if(!uniqueMovieIds.add(numberInMovieId)){
                throw new ValidationException("ERROR: Movie Id numbers {"+numberInMovieId+"} aren’t unique");
            }
        }
    }
}
