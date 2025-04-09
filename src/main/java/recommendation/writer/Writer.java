package recommendation.writer;

import recommendation.model.SingleRecommendation;

import java.util.List;

public class Writer {
    private String error;

    public Writer() {
        System.out.println("Writer initialized");
    }
    public void writeRecommendations(List<SingleRecommendation> recommendations){
        System.out.println("Writing " + recommendations.size() + " recommendations to file");
    }
}