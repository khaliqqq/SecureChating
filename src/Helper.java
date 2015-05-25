import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by KINGSMAN on 25-May-15.
 */
public class Helper {
    public static boolean authenticateLogin(String username, String password){
        if (username.equals("bob")&&password.equals("123")){
            return true;
        }else if(username.equals("alice")&&password.equals("123")){
            return true;
        } else{
            return false;
        }
    }

    public static boolean isLogin(String username){
        //flag kat sini
        return true;
    }

    public static void writeToTextFile(String username, String message, String dateTime){
        String allInOneString = dateTime + "--" + username + " : " + message;
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

    //fetch history
    public static String fetchFromFile(){
        File log = new File("log.txt");
        String fetchedString = null;
        StringBuilder stringBuilder = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(log))) {
            for(String line; (line = br.readLine()) != null; ) {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            // line is not visible here.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fetchedString = stringBuilder.toString();
        return fetchedString;
    }

    public static ArrayList fetchUser(){
        File listOfUserFile = new File("users.txt");
        ArrayList abc = new ArrayList();

        try(BufferedReader br = new BufferedReader(new FileReader(listOfUserFile))) {
            for(String line; (line = br.readLine()) != null; ) {
                abc.add(line);
            }
            // line is not visible here.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return abc;
    }

    public static boolean deleteUser(String username){

        File inputFile = new File("users.txt");
        File tempFile = new File("tmp.txt");

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if(trimmedLine.equals(username)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            copyFileUsingFileStreams(tempFile, inputFile);
            System.out.println("success delete");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("failed delete");
        return false;
    }

    public static void copyFileUsingFileStreams(File source, File dest) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //register user. true=success, false=failed
    public static boolean registerDialog(){
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        Object[] message = {
                "Username:", username,
                "Password:", password
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (authenticateLogin(username.getText(),password.getText())) {
                //validation ni kne tukar, compare username sahaja
                JOptionPane.showMessageDialog(null, "User already exist", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }if (username.getText().length() == 0 || password.getText().length() == 0){
                JOptionPane.showMessageDialog(null, "Username and Password cant be empty", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }else {
                if (addUserToDb(username.getText(), password.getText())){
                    JOptionPane.showMessageDialog(null, "Registration success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "Registration failed!", "Failed", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } else {
            System.out.println("Login canceled");
            return false;
        }
    }

    public static boolean addUserToDb(String username, String password){
        File inputFile = new File("users.txt");
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(inputFile, true));
            bw.write(System.getProperty("line.separator") + username);
            bw.newLine();
            bw.flush();
            bw.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    public static boolean authenticateFromServer(String username, String password){
        if (checkUSerInFile(username)){
            return true;
        }else {
            return false;
        }
    }

    public static boolean checkUSerInFile(String username){

        File inputFile = new File("users.txt");
        try {
            Scanner scanner=new Scanner(inputFile);
            while(scanner.hasNextLine()){
                if(username.equals(scanner.nextLine().trim())){
                    break;
                }else{
                    return false;
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
