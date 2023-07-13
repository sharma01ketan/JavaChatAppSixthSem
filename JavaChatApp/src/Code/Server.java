package Code;

import org.w3c.dom.Text;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.text.*;

public class Server implements ActionListener {

    JTextField tf;
    static JPanel chatPanel;
    static Box vertical = Box.createVerticalBox();
    static JFrame frame = new JFrame();
    static JScrollPane scroll;
    static DataOutputStream dataoutput;

    Server(){
        frame.setLayout(null);

        //Header Panel
        JPanel header = new JPanel();
        header.setBackground(new Color(211, 122, 121));
        header.setBounds(0, 0, 450, 70);
        header.setLayout(null);
        frame.add(header);

        //Profile Image
        ImageIcon profile = new ImageIcon(ClassLoader.getSystemResource("Images/female.png"));
        Image scaledProfile = profile.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        ImageIcon profilePic = new ImageIcon(scaledProfile);
        JLabel profileLabel = new JLabel(profilePic);
        profileLabel.setBounds(0, 6, 65, 65);
        header.add(profileLabel);

        //Name
        JLabel name = new JLabel("Ishu");
        name.setBounds(70, 23, 100, 25);
        name.setForeground(Color.BLACK);
        name.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        header.add(name);

        //Exit Button
        ImageIcon cross = new ImageIcon(ClassLoader.getSystemResource("Images/cross_icon.png"));
        Image scaledCross = cross.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon crossImg = new ImageIcon(scaledCross);
        JLabel crossLabel = new JLabel(crossImg);
        crossLabel.setBounds(425, 6, 20, 20);
        header.add(crossLabel);

        crossLabel.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                System.exit(0);
            }
        });



        //Chat Panel
        chatPanel = new JPanel();
        chatPanel.setBounds(5, 75, 440, 520);

        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(chatPanel);
        scroll.setBounds(5, 75, 440, 520);

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

        //Send Button
        JButton send = new JButton("Send");
        send.setBounds(340, 600, 100, 40);
        send.setBackground(new Color(211, 122, 121));
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        frame.add(send);

//        setLocation(100, 20);
        frame.setLocation(0,0);
        frame.setUndecorated(true);
        frame.setSize(450, 645);
//        frame.getContentPane().add(scroll);
//        frame.getContentPane().add(scroll);
        frame.getContentPane().setBackground(new Color(65, 64, 64));
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent ae){
        try {
            String msg = tf.getText();

            JPanel out = boxForText(msg);
            chatPanel.add(out);
            chatPanel.setLayout(new BorderLayout());
            JPanel right = new JPanel(new BorderLayout());
            right.add(out, BorderLayout.LINE_END);

            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            chatPanel.add(vertical, BorderLayout.PAGE_START);

            dataoutput.writeUTF(msg);
            tf.setText("");

            frame.repaint();
            frame.invalidate();
            frame.validate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static JPanel boxForText(String msg){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style = \"width: 150px\">" + msg + "</p></html>");
        output.setForeground(Color.BLACK);
        output.setFont(new Font("Tahoma", Font.PLAIN, 15));
        output.setBackground(new Color(211, 122, 121));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(10, 10, 10, 40));

        if(!msg.equals(""))
            panel.add(output);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(calendar.getTime()));
        if(!msg.equals("")){
            panel.add(time);
        }

        return panel;
    }

    public static void main(String[] args) {
        new Server();

        try{
            ServerSocket serverSocket = new ServerSocket(6020);
            while(true){
                Socket s = serverSocket.accept();
                DataInputStream input = new DataInputStream(s.getInputStream());
                dataoutput = new DataOutputStream(s.getOutputStream());

                while(true){
                    String msg = input.readUTF();
                    JPanel panel = boxForText(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    frame.validate();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
