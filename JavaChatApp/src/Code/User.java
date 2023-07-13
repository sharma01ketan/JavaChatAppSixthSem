//User class, multiple instances of this can be created to generate a new user

package Code;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.text.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class User implements Runnable{

    JTextField tf;
    static JPanel chatPanel;
    static JFrame frame;
    static JScrollPane scroll;
    BufferedReader reader;
    BufferedWriter writer;
    Socket socket;
    JLabel profilelabel;
    String username;
    JPanel header;
    GroupFrame membersFrame;
    boolean authentic = false;

    User(String name, GroupFrame membersFrame) {
        this.username = name;
        this.membersFrame = membersFrame;
        this.membersFrame.addUser(this);
        frame = new JFrame();
        frame.setLayout(null);

        //Header Panel
        header = new JPanel();
        header.setBackground(new Color(211, 122, 121));
        header.setBounds(0, 0, 450, 80);
        header.setLayout(null);

        //Profile Image
        ImageIcon profile = new ImageIcon(ClassLoader.getSystemResource("Images/female.png"));
        Image scaledProfile = profile.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        ImageIcon profilePic = new ImageIcon(scaledProfile);
        profilelabel = new JLabel(profilePic);
        profilelabel.setBounds(0, 6, 65, 65);
        header.add(profilelabel);

        //Connect Button
        ActionListener connectAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JTextField usernameField = new JTextField(20);
                panel.add(new JLabel("Username:"));
                panel.add(usernameField);

                JCheckBox checkbox = new JCheckBox("Agree to terms");
                panel.add(checkbox);

                int result = JOptionPane.showConfirmDialog(frame, panel, "Connect group", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION){
                    username = usernameField.getText();
                    authentic = checkbox.isSelected();

                    if (authentic) {
                        try {
                            connectToGroup(username);
                        } catch (inauthenticUserException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "User is not authentic!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        try {
                            throw new inauthenticUserException("User is not authentic!");
                        } catch (inauthenticUserException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "User is not authentic!", "Error", JOptionPane.ERROR_MESSAGE);
                        }                    }
                    updateHeader(username);
                }
            }
        };

        //Connect Button
        JButton connect = new JButton("Connect group");
        connect.setBounds(280, 10, 110, 30);
        connect.setBackground(new Color(211, 122, 121));
        connect.addActionListener(connectAction);
        connect.setFont(new Font("SAN_SERIF", Font.BOLD, 9));
        header.add(connect);

        //Name
        JLabel nameLabel = new JLabel(name);
        nameLabel.setBounds(70, 30, 100, 25);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        header.add(nameLabel);

        //Private Msg Button
        JButton pvtmsg = new JButton("Private Msg");
        pvtmsg.setBounds(280, 45, 110, 30);
        pvtmsg.setBackground(new Color(211, 122, 121));
        pvtmsg.setFont(new Font("SAN_SERIF", Font.BOLD, 9));
        header.add(pvtmsg);

        //Exit Button
        ImageIcon cross = new ImageIcon(ClassLoader.getSystemResource("Images/cross_icon.png"));
        Image scaledCross = cross.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon crossImg = new ImageIcon(scaledCross);
        JLabel crossLabel = new JLabel(crossImg);
        crossLabel.setBounds(425, 6, 20, 20);
        header.add(crossLabel);

        crossLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        frame.add(header);

        //Chat Panel
        chatPanel = new JPanel();
        chatPanel.setBounds(5, 85, 440, 510);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(chatPanel);
        scroll.setBounds(5, 85, 440, 510);

        JScrollBar scrollBar = scroll.getVerticalScrollBar();
        scrollBar.setUnitIncrement(10);
        scrollBar.setBlockIncrement(50);
        frame.add(scroll);

        //Text Input Panel
        tf = new JTextField();
        tf.setBounds(10, 600, 325, 40);
        tf.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        tf.setBackground(new Color(211, 122, 121));
        tf.setForeground(Color.BLACK);
        frame.add(tf);

        //Send Batan
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {

                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:/Users/acer/IdeaProjects/JavaChatApp/src/Images/pristine-609.wav"));
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);

                    JScrollBar verticalScrollBar = scroll.getVerticalScrollBar();
                    verticalScrollBar.setValue(verticalScrollBar.getMaximum());

                    membersFrame.addChatMessage(username, tf.getText());
                    tf.setText("");
                    clip.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //Send Button
        JButton send = new JButton("Send");
        send.setBounds(340, 600, 100, 40);
        send.setBackground(new Color(211, 122, 121));
        send.addActionListener(sendAction);
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        frame.add(send);

        //Frame properties
        frame.setLocation(0, 0);
        frame.setSize(450, 645);
        frame.getContentPane().setBackground(new Color(65, 64, 64));
        frame.setUndecorated(true);
        try {
            Socket socket = new Socket("localhost", 2003);  //connect to the server hosted on port 2003 in the GroupServer class
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
        updateHeader(username);
    }

    //dynamically inserts the profile, and name of the user
    public void updateHeader(String username){
//        for (Component component : header.getComponents()) {
//            if (component instanceof JLabel) {
//                JLabel label = (JLabel) component;
//                if (label.getText() != null && label.getText().equals(username)) {
//                    header.remove(label);
//                    break;
//                }
//            }
//        }
//        header.remove(profilelabel);

        if (profilelabel != null) {
            header.remove(profilelabel);
        }

        if(username != null){
            //update the profile image
            ImageIcon profile = new ImageIcon(ClassLoader.getSystemResource("Images/" + (username.equalsIgnoreCase("Ishu") || username.equalsIgnoreCase("Niyati") ? "female.png" : "man.png")));
            Image scaledProfile = profile.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
            ImageIcon profilePic = new ImageIcon(scaledProfile);
            profilelabel = new JLabel(profilePic);
            profilelabel.setBounds(0, 6, 65, 65);
            header.add(profilelabel);

            //updates the name
            for (Component component : header.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    if (label.getText() != null) {
                        label.setText(username);
                        break;
                    }
                }
            }
        }
        header.revalidate();
        header.repaint();
    }

    //connects the user to the group chat frame
    public void connectToGroup(String userName) throws inauthenticUserException{
        membersFrame = GroupFrame.getInstance();
        membersFrame.addGroupMember(userName);
        membersFrame.showFrame();

        try{
            String serverHost = "localhost";
            int serverPort = 2003;

            socket = new Socket(serverHost, serverPort);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

//    //displays the message
//    public void displayMessage(String message) {
//        User user = this;
//
//        JPanel out = boxForText(message);
//        user.chatPanel.add(out);
//        this.frame.repaint();
//        this.frame.invalidate();
//        this.frame.validate();
//    }
//    public void createNewUser(String userName) {
//        username = userName;
//
//        Thread thread = new Thread(this);
//        thread.start();
//    }

//    public static JPanel boxForText(String msg) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        JLabel output = new JLabel("<html><p style = \"width: 150px\">" + msg + "</p></html>");
//        output.setForeground(Color.BLACK);
//        output.setFont(new Font("Tahoma", Font.PLAIN, 15));
//        output.setBackground(new Color(211, 122, 121));
//        output.setOpaque(true);
//        output.setBorder(new EmptyBorder(0, 10, 1, 40));
//
//        if (!msg.equals(""))
//            panel.add(output);
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//
//        JLabel time = new JLabel();
//        time.setText(sdf.format(calendar.getTime()));
//        if (!msg.equals("")) {
//            panel.add(time);
//        }
//
//        return panel;
//    }

    @Override
    public void run() {
        try {
            updateHeader(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        //create 3 users (because that is all I can fit on my screen at once), and allot a separate thread to each
        SwingUtilities.invokeLater(() -> {
            GroupFrame groupFrame = GroupFrame.getInstance();
            User user1 = new User("Ishu", groupFrame);
            User user2 = new User("Motu", groupFrame);
            user2.frame.setLocation(820, 0);
            User user3 = new User("Ketan", groupFrame);
            user3.frame.setLocation(455, 0);
            Thread thread1 = new Thread(user1);
            Thread thread2 = new Thread(user2);
            Thread thread3 = new Thread(user3);
            thread1.start();
            thread2.start();
            thread3.start();
        });
    }
}

class inauthenticUserException extends Exception{
    public inauthenticUserException(String message) {
        super(message);
    }
}


