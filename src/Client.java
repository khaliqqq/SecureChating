import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  {

    // for I/O
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;

    // if I use a GUI or not
    private ClientGUI cg;

    // the server, the port and the username
    private String server, username, password;
    private int port;


    /*
     * Constructor call when used from a GUI
     * in console mode the ClienGUI parameter is null
     */
    Client(String server, int port, String username, String password, ClientGUI cg) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        // save if we are in GUI mode or not
        this.cg = cg;
    }

    /*
     * To start the dialog
     */
    public boolean start() {
        ChatMessage cm;
        // try to connect to the server
        try {
            socket = new Socket(server, port);

        }
        // if it failed not much I can so
        catch(Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);
	
		/* Creating both Data Stream */
        try
        {
            sInput  = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }


        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try
        {
            //encrypt username password------------------------------------------------------------------------------------------

            /*String tempUsername = ClientEncryption.createHash256(username);
            String tempPassword = ClientEncryption.createHash256(username);*/


            //send authenticate request to server
            cm = new ChatMessage(ChatMessage.AUTH, username,password);
            sOutput.writeObject(cm);



            cm = (ChatMessage)sInput.readObject();
            String temp = cm.getMessage();
            System.out.println("------>" + temp);
            if(cm.getType() == ChatMessage.AUTHED){
                if (temp.equals("1")){
                    System.out.println("masukkkk");
                }else {
                    System.out.println("x masuk");
                    return false;
                }
            }else {
                System.out.println("x masuk x masuk");
                return false;
            }
            System.out.println("hahahahhaha");
        }
        catch (IOException eIO) {
            display("Exception during login : " + eIO);
            disconnect();
            return false;
        } catch (ClassNotFoundException e) {
            display("Exception during login : " + e);
            disconnect();
            return false;
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();

        // success we inform the caller that it worked
        return true;
    }

    /*
     * To send a message to the console or the GUI
     */
    private void display(String msg) {
        cg.append(msg + "\n");		// append to the ClientGUI JTextArea (or whatever)
    }

    /*
     * To send a message to the server
     */
    void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        }
        catch(IOException e) {
            display("Exception writing to server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception e) {} // not much else I can do
        try {
            if(sOutput != null) sOutput.close();
        }
        catch(Exception e) {} // not much else I can do
        try{
            if(socket != null) socket.close();
        }
        catch(Exception e) {} // not much else I can do

        // inform the GUI
        if(cg != null)
            cg.connectionFailed();

    }

    /*
     * a class that waits for the message from the server and append them to the JTextArea
     * if we have a GUI or simply System.out.println() it in console mode
     */
    class ListenFromServer extends Thread {

        public void run() {
            while(true) {
                try {
                    String msg = (String) sInput.readObject();
                    //print the message and add back the prompt
                    cg.append(msg);

                }
                catch(IOException e) {
                    display("Server has close the connection: " + e);
                    if(cg != null)
                        cg.connectionFailed();
                    break;
                }
                // can't happen with a String object but need the catch anyhow
                catch(ClassNotFoundException e2) {
                }
            }
        }
    }

    public ObjectOutputStream sOut(){
        return sOutput;
    }

    public ObjectInputStream sIn(){
        return sInput;
    }

}

