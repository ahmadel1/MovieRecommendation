package recommendation.parser;

import recommendation.model.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserParser {
    private List<User> users;
    private String error = null;

    public UserParser(String userFile) {
        users = new ArrayList<>();

        try
        {
            FileReader fileReader = new FileReader(userFile);
            BufferedReader bufread = new BufferedReader(fileReader);

            String firstLine;
            
            // reading the file, 2 lines by 2 lines
            while((firstLine = bufread.readLine()) != null) {
                var firstLineParts = firstLine.split(", ");
                var uname = firstLineParts[0];
                var uid = firstLineParts[1];

                var secondLine = bufread.readLine();
                var movieIds = List.of(secondLine.split(", "));

                var user = new User(uname, uid, movieIds);
                users.add(user);
            }
            
            bufread.close();
            System.out.println("Loaded users: " + users.toString());
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
}