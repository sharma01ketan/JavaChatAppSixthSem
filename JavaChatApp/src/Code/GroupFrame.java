package Code;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class GroupFrame {
    private static GroupFrame instance;
    static JFrame frame;
    List<String> groupMembers;
    Box vertical;
    static JPanel titleBar;
    static JPanel panel;
    private static Map<String, User> connectedUsers;
    static JPanel chatPanel;
    JScrollPane scroll;

    public static void main(String[] args) {
        System.out.println(connectedUsers);
    }
    public GroupFrame() {
        frame = new JFrame();

        frame.setUndecorated(true);
        frame.getContentPane().setLayout(null);

        frame.setTitle("Group Members");
        frame.setSize(450, 645);
        frame.setLocation(470, 10);
        frame.getContentPane().setBackground(new Color(65, 64, 64));

        groupMembers = new ArrayList<>();
        connectedUsers = new HashMap<>();

        titleBar = new JPanel();
        titleBar.setBounds(0, 0, 450, 10);
        titleBar.setBackground(new Color(211, 122, 121));
        titleBar.setLayout(null);
        titleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                frameMousePressed(evt);
            }
        });
        titleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                frameMouseDragged(evt);
            }
        });

        frame.getContentPane().add(titleBar);

        //Header
        JPanel header = new JPanel();
        header.setBackground(new Color(211, 122, 121));
        header.setBounds(0, 0, 450, 80);
        header.setLayout(null);
        frame.add(header);

        //Profile Image
        ImageIcon profile = new ImageIcon(ClassLoader.getSystemResource("Images/group.png"));
        Image scaledProfile = profile.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        ImageIcon profilePic = new ImageIcon(scaledProfile);
        JLabel profileLabel = new JLabel(profilePic);
        profileLabel.setBounds(0, 6, 65, 65);
        header.add(profileLabel);

        //Name
        JLabel nameLabel = new JLabel("Group");
        nameLabel.setBounds(70, 30, 100, 25);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        header.add(nameLabel);


        //exit button
        ImageIcon cross = new ImageIcon(ClassLoader.getSystemResource("Images/cross_icon.png"));
        Image scaledCross = cross.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon crossImg = new ImageIcon(scaledCross);
        JLabel crossLabel = new JLabel(crossImg);
        crossLabel.setBounds(420, 12, 20, 20);
        header.add(crossLabel);

        crossLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });



        //Chat panel
        chatPanel = new JPanel();
        chatPanel.setBounds(5, 85, 440, 550);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(chatPanel);
        scroll.setBounds(5, 85, 440, 550);

        JScrollBar scrollBar = scroll.getVerticalScrollBar();
        scrollBar.setUnitIncrement(10);
        scrollBar.setBlockIncrement(50);
        frame.getContentPane().add(scroll);

        // Create a panel to hold the group members
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a scroll pane for the panel
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.getContentPane().add(scrollPane);
        vertical = Box.createVerticalBox();

        frame.setVisible(true);
    }

    public static GroupFrame getInstance(){
        if(instance == null){
            instance = new GroupFrame();
        }
        return instance;
    }
    public void addGroupMember(String memberName) {
        groupMembers.add(memberName);
        JLabel label = new JLabel(memberName);
        panel.add(label);
        panel.revalidate();
    }

    //for relocating the group chat frame
    private int xMouse, yMouse;
    private void frameMousePressed(java.awt.event.MouseEvent evt) {
        xMouse = evt.getX();
        yMouse = evt.getY();
    }
    private void frameMouseDragged(java.awt.event.MouseEvent evt) {
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        frame.setLocation(x - xMouse, y - yMouse);
    }

    //creates an outline for the displayed text message
    public static JPanel boxForText(String msg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style = \"width: 100px\">" + msg + "</p></html>");
        output.setForeground(Color.BLACK);
        output.setFont(new Font("Tahoma", Font.PLAIN, 15));
        output.setBackground(new Color(211, 122, 121));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(0, 0, 1, 40));

        if (!msg.equals(""))
            panel.add(output);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(calendar.getTime()));
        if (!msg.equals("")) {
            panel.add(time);
        }
        return panel;
    }

    public void addChatMessage(String username, String message) {
        JLabel output = new JLabel("<html><p style=\"width: 150px\"><b>" + username + "</b> : " + message + "</p></html>");
        output.setForeground(Color.BLACK);
        output.setFont(new Font("Tahoma", Font.PLAIN, 15));
        output.setBackground(new Color(211, 122, 121));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(0, 10, 1, 40));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(output);

        vertical.add(panel);
        vertical.add(Box.createVerticalStrut(15));
        chatPanel.add(vertical);

        JScrollBar verticalScrollBar = scroll.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        frame.repaint();
        frame.invalidate();
        frame.validate();
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    public void addUser(User user) {
        connectedUsers.put(user.getUsername(), user);
    }
    public void removeUser(User user) {
        connectedUsers.remove(user.getUsername());
    }
}