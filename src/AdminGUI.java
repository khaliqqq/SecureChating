import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Handler;

public class AdminGUI  extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    // will first hold "Username:", later on "Enter message"
    private JLabel label;
    // to hold the Username and later on the messages
    private JTextField tf;
    private JPasswordField pf;
    // to hold the server address an the port number
    private JTextField tfServer, tfPort;
    // to Logout and get the list of the users
    private JButton login, logout, delete, register;
    // for the chat room
    private JTextArea ta;
    // if it is for connection
    private boolean connected;
    // the Client object
    private Admin admin;
    // the default port number
    private int defaultPort;
    private String defaultHost;

    private JList jlist;

    AdminGUI(String host, int port) {

        super("Administrator");
        defaultPort = port;
        defaultHost = host;

        // The NorthPanel with:
        JPanel northPanel = new JPanel(new GridLayout(4,1));
        // the server name anmd the port number
        JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 5));
        // the two JTextField with default value for server address and port number
        tfServer = new JTextField(host);
        tfPort = new JTextField("" + port);
        tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

        serverAndPort.add(new JLabel("Server Address:  "));
        serverAndPort.add(tfServer);
        serverAndPort.add(new JLabel(""));
        serverAndPort.add(new JLabel("Port Number:  "));
        serverAndPort.add(tfPort);
        // adds the Server an port field to the GUI
        northPanel.add(serverAndPort);

        // the Label and the TextField
        label = new JLabel("Enter username and password below", SwingConstants.LEFT);
        northPanel.add(label);
        tf = new JTextField("Username");
        tf.setBackground(Color.WHITE);
        northPanel.add(tf);
        add(northPanel, BorderLayout.NORTH);

        pf = new JPasswordField("Password");
        pf.setBackground(Color.WHITE);
        northPanel.add(pf);
        add(northPanel, BorderLayout.NORTH);

        // The CenterPanel which is the chat room
        jlist = new JList(Helper.fetchUser().toArray());
        JPanel centerPanel = new JPanel(new GridLayout(1,1));
        centerPanel.add(new JScrollPane(jlist));
        add(centerPanel, BorderLayout.CENTER);
        jlist.setVisible(false);

        // the 3 buttons
        login = new JButton("Login");
        login.addActionListener(this);
        logout = new JButton("Logout");
        logout.addActionListener(this);
        delete = new JButton("Delete");
        delete.addActionListener(this);
        register = new JButton("Register");
        register.addActionListener(this);
        logout.setEnabled(false);		// you have to login before being able to logout
        delete.setEnabled(false);
        register.setEnabled(false);

        JPanel southPanel = new JPanel();
        southPanel.add(login);
        southPanel.add(logout);
        southPanel.add(delete);
        southPanel.add(register);
        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        tf.requestFocus();

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if(o == login) {
            // ok it is a connection request
            String username = tf.getText().trim();
            // empty username ignore it
            if(username.length() == 0)
                return;

            String password = pf.getText().trim();
            // empty username ignore it
            if(password.length() == 0)
                return;

            // empty serverAddress ignore it
            String server = tfServer.getText().trim();
            if(server.length() == 0)
                return;
            // empty or invalid port numer, ignore it
            String portNumber = tfPort.getText().trim();
            if(portNumber.length() == 0)
                return;
            int port = 0;
            try {
                port = Integer.parseInt(portNumber);
            }
            catch(Exception en) {
                return;   // nothing I can do if port number is not valid
            }

            if(!Helper.authenticateLogin(username, password)){
                JOptionPane.showMessageDialog(null, "Wrong username or password", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }else if (!Helper.isLogin(username)){
                JOptionPane.showMessageDialog(null, "User already login", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }else{
                jlist.setVisible(true);
                logout.setEnabled(true);
                delete.setEnabled(true);
                register.setEnabled(true);
                login.setEnabled(false);
                tf.setVisible(false);
                pf.setVisible(false);
                label.setVisible(false);
                jlist.setListData(Helper.fetchUser().toArray());
            }
        }

        if(o == delete){
            if(jlist.getSelectedIndex() != -1){
                int response = JOptionPane.showConfirmDialog(null, "Do you want to delete?", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.NO_OPTION) {
                    System.out.println("Delete cancelled");
                } else if (response == JOptionPane.YES_OPTION) {
                    if (Helper.deleteUser(jlist.getSelectedValue().toString())){
                        jlist.setListData(Helper.fetchUser().toArray());
                    }else {
                        System.out.println("delete failed - admingui");
                    }
                } else if (response == JOptionPane.CLOSED_OPTION) {
                    System.out.println("Delete dialog closed");
                }
            }
        }

        if(o == logout) {
            /*admin.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));*/
            pf.setVisible(true);
            tf.setVisible(true);
            login.setEnabled(true);
            label.setVisible(true);
            jlist.setVisible(false);
            logout.setEnabled(false);
            delete.setEnabled(false);
            register.setEnabled(false);
            JOptionPane.showMessageDialog(null, "You have logged out", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        if(o == register){
            if (Helper.registerDialog()){
                jlist.setListData(Helper.fetchUser().toArray());
            }
        }
    }

    void connectionFailed() {
        login.setEnabled(true);
        logout.setEnabled(false);
        label.setText("Enter username and password below");
        tf.setText("");
        pf.setText("");
        // reset port number and host name as a construction time
        tfPort.setText("" + defaultPort);
        tfServer.setText(defaultHost);
        // let the user change them
        tfServer.setEditable(false);
        tfPort.setEditable(false);
        // don't react to a <CR> after the username
        tf.removeActionListener(this);
        connected = false;
    }

    // to start the whole thing the server
    public static void main(String[] args) {
        new AdminGUI("localhost", 1500);
    }
}
