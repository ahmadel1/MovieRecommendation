package recommendation.writer;

import recommendation.model.Movie;
import recommendation.model.SingleRecommendation;
import recommendation.model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Writer {
    private String error;

    public Writer() {
        System.out.println("Writer initialized");
    }
    public void writeRecommendations(List<SingleRecommendation> recommendations){
        System.out.println("Writing " + recommendations.size() + " recommendations to file");
        try {
            FileWriter writer = new FileWriter("samples/recommendations.txt");
            System.out.println("writing output file");
            recommendations.forEach(recommendation -> {
                User user = recommendation.getUser();
                List<Movie> movies = recommendation.getMovies();
                String firstLine = user.getUserName() + ", " + user.getUserId() + "\n";
                AtomicReference<String> secondLine = new AtomicReference<>("");
                movies.forEach(movie -> {
                    if(secondLine.toString().equals(""))
                        secondLine.set(secondLine + movie.getMovieTitle());
                    else
                        secondLine.set(secondLine + ", " + movie.getMovieTitle());
                });
                secondLine.set(secondLine + "\n");
                try {
                    writer.write(firstLine);
                    writer.write(String.valueOf(secondLine));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        recommendations.forEach(recommendation -> {

        });
    }
}