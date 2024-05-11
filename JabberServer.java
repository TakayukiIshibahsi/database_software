

import java.io.*;
import java.net.*;

import SQLite.JDBC;


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
                    int id = log_in(in, out);
                    if (id==-1){
                        out.println("ログインに失敗しました。");
                        return;
                    }
                    out.println("log_in_success");
                    out.println("your id:"+String.valueOf(id));
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








    public static int log_in(BufferedReader in,PrintWriter out){
        out.println("新規登録:new ログイン:in");
        try{
            String j=in.readLine();
LOOP:       while(true){
                if (j.equals("in")){
                    out.println("名前を入力してください");
                    String name=in.readLine();
                    String password=JDBC.user_search(name,"password");
                    if (password!="failed"){
                       while (true) {
                            out.println("パスワードを入力してください");
                            String pass=in.readLine();  
                            if (password.equals(pass)){
                                int id =Integer.parseInt(JDBC.user_search(name,"id"));
                                System.out.println(id);
                                return id;
                            }else{
                                out.println("パスワードが正しくありません");
                                break LOOP;
                            }
                        }
                    }else{
                        out.println("ファイルが見つかりません");
                        break LOOP;
                    }

            }else if(j.equals("new")){
                out.println("名前を入力してください");
                String name=in.readLine(); 
                out.println("パスワードを入力してください");
                String password=in.readLine();
                int id = Integer
                .parseInt(JDBC.number_of_member())+1;
                JDBC.user_insert(id,name,password);
                return id;
            }
            
        }
        }catch (IOException e){
            System.out.println("failed");
        }
        return -1;

    }
}
    

