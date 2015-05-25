import javax.swing.*;
import java.io.*;

/**
 * Created by KINGSMAN on 25-May-15.
 */
public class Helper {
    public static Boolean authenticateLogin(String username, String password){
        if (username.equals("bob")&&password.equals("123")){
            return true;
        }else if(username.equals("alice")&&password.equals("123")){
            return true;
        } else{
            return false;
        }
    }

    public static void writeToTextFile(String username, String message, String dateTime){
        String allInOneString = dateTime + " " + username + " : " + message;
        File log = new File("log.txt");

        try{
            if(!log.exists()){
                log.createNewFile();
                String path = log.getAbsolutePath().toString();
                System.out.println(path);
            }
            FileWriter fileWriter = new FileWriter(log, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(System.getProperty("line.separator") + allInOneString);
            bufferedWriter.close();
        } catch(IOException e) {
            System.out.println("COULD NOT LOG!!");
        }
    }

    public static void fetchFromFile(JTextArea textArea){
        File log = new File("log.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(log))) {
            for(String line; (line = br.readLine()) != null; ) {
                textArea.append(line + System.getProperty("line.separator"));
            }
            // line is not visible here.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
