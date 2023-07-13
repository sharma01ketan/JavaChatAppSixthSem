package Code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class GroupServer implements Runnable{
    Socket socket;
    public static Vector client = new Vector();
    public GroupServer(Socket socket){
        try{
            this.socket = socket;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            client.add(writer);

            while(true){
                String msg = reader.readLine().trim();
                System.out.println("Received: " + msg);

                for (int i = 0; i < client.size(); i++) {
                    try{
                        BufferedWriter bw = (BufferedWriter) client.get(i);
                        bw.write(msg);
                        bw.write("\r\n");
                        bw.flush();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        ServerSocket s = new ServerSocket(2003);
        while(true){
            Socket socket = s.accept();
            GroupServer groupServer = new GroupServer(socket);
            Thread thread = new Thread(groupServer);
            thread.start();
        }
    }
}