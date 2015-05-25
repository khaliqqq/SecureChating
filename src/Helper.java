import java.io.*;

/**
 * Created by KINGSMAN on 25-May-15.
 */
public class Helper {
    public static Boolean authenticateLogin(String username, String password){
        if (username.equals("bob")&&password.equals("123")){
            return true;
        }else{
            return false;
        }
    }

    public static void writeToTextFile(String message){
        File log = new File("log.txt");

        try{
            if(!log.exists()){
                log.createNewFile();
                String path = log.getAbsolutePath().toString();
                System.out.println(path);
            }
            FileWriter fileWriter = new FileWriter(log, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(System.getProperty("line.separator") + message);
            bufferedWriter.close();
        } catch(IOException e) {
            System.out.println("COULD NOT LOG!!");
        }
    }
}
