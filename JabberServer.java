package database_software;
import java.io.*;
import java.net.*;

public class JabberServer {
    public static final int PORT = 8080;
    public static void main (String[] args)

    throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Started:"+s);
        try{
            Socket socket = s.accept();
            try{
                System.out.println(
                    "Connection accepted:"+socket);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream()),true);
                    
                    while (true) {
                        String str = in.readLine();
                        if (str.equals("END")){
                            break;
                        }
                        System.out.println("Catch : "+str);
                        out.println(str);
                    }
                }finally{
                    System.out.println("closing...");
                    socket.close();
                }
            
            }finally{
                s.close();
            }
        }
    }
    

