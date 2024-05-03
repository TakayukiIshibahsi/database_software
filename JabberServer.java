
import java.io.*;
import java.net.*;

public class JabberServer {
    public static String readyA="ra",readyL="rl",readyG="rg";
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
                        else if (str.equals("ADD")){
                            get_file(in, out);
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

    public  static void get_file(BufferedReader in,PrintWriter out) throws IOException {
        out.println(readyA);
        String line;
        String out_file = in.readLine();
        PrintWriter file_writer = new PrintWriter(new BufferedWriter(new FileWriter(out_file)));
        out.println(readyA);
        while((line = in.readLine()) != null){
        if(line.equals("EOF")) break;
        file_writer.println(line);
        }
        file_writer.close();   
    }
}
    

