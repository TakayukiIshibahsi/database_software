

import java.io.*;
import java.net.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import SQLite.JDBC;
import SQLite.JDBC.User;

public class JabberServer {
    public static String readyA="ra",readyL="rl",readyG="rg",readyD="rd";
    public static final int PORT = 8080;
    public static String path;
    public static void main (String[] args)

    throws IOException {

        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Started:"+s);
        System.out.println("IP:"+InetAddress.getLocalHost());
        //ディレクトリの作成
        Path p1 = Paths.get("");  //カレントディレクトリの相対パスを取得
        Path current_dir = p1.toAbsolutePath();  //カレントディレクトリの絶対パスを取得
        String datas_path_String = current_dir.toString() + "/server_files/";  //絶対パスを文字列にし、datasディレクトリを追加
        Path datas_path = Paths.get(datas_path_String);  //文字列からパスへ
        path = datas_path_String + "/";   //ファイル保存時のために"/"を追加"
        if(!Files.exists(datas_path)) {
            try{
                Files.createDirectory(datas_path);
            }catch(IOException e){
                System.out.println(e);
            }
        }

        try{
            Socket socket = s.accept();
            try{
                System.out.println(
                    "Connection accepted:"+socket);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream()),true);
                    User user = log_in(in, out);
                    user.show();
                    int id = user.id; 
                    if (id==-1){
                        out.println("ログインに失敗しました。");
                        return;
                    }
                    out.println("log_in_success");
                    System.out.println("id:"+String.valueOf(id));
                    while (true) {
                        String str = in.readLine();
                        if (str.equals("END")){
                            break;
                        }
                        else if (str.equals("ADD")){
                            get_file(in, out, id);
                        }
                        else if (str.equals("LOOK")){
                            file_list(in, out);
                        }
                        else if (str.equals("GET")){
                            send_file(in, out);
                        }
                        else if (str.equals("DEL")){
                            delete_file(in, out, id);
                        }
                        else{
                            out.println(str);
                        }
                    }
                }finally{
                    System.out.println("closing...");
                    socket.close();
                }
            
            }finally{
                s.close();
                
            }
           
        }





    public static void delete_file(BufferedReader in,PrintWriter out,int id)throws IOException{
        out.println(readyD);
        String str=in.readLine();
        if(str.equals(readyD)){
            out.println("削除したいファイル名を指定してください。(アップロードしたユーザしか削除できません。)");
            String delete_file_name=in.readLine();
            File delete_file = new File(String.format("./server_files/" + delete_file_name));
            if(!delete_file.exists()) {
            System.out.println("File Not Found");
            } else if(delete_file.delete()) {
            System.out.println("File Deleted");
        }
            System.out.print(JDBC.file_delete(delete_file_name, id));
        }else{
            System.out.println("クライエント側の準備ができていません");
        }
    }


        
    public  static void get_file(BufferedReader in,PrintWriter out,int id) throws IOException {
        out.println(readyA);
        String line;
        String out_file_name = in.readLine();
        String result = JDBC.file_update(out_file_name);
        if(result=="NE"){
            JDBC.file_insert(out_file_name,id);
            System.out.println(result);
            PrintWriter file_writer = new PrintWriter(new BufferedWriter(new FileWriter("./server_files/"+out_file_name)));
            out.println(readyA);
            while((line = in.readLine()) != null){
                if(line.equals("EOF")) break;
                file_writer.println(line);                
                
            }
            file_writer.close();

        }else{
            if (id==Integer.parseInt(result)){
                System.out.println("get_file.id:"+result);
                PrintWriter file_writer = new PrintWriter(new BufferedWriter(new FileWriter("./server_files/"+out_file_name)));
                out.println(readyA);
                while((line = in.readLine()) != null){
                if(line.equals("EOF")) break;
                file_writer.println(line);
                
                }
                file_writer.close();
            }else{
                
                out.println("ファイルを更新する権限がありません\n");
            }

        }
           
    }

    public static void file_list(BufferedReader in,PrintWriter out)throws IOException{
        out.println(readyL);
        String str = in.readLine();
        if (str.equals(readyL)){
            out.println("\n--- file list ---");
            String list = JDBC.file_look();
            out.println(list);
            out.println("|EOF|");
            
        } 
    }

    public static void send_file(BufferedReader in,PrintWriter out) throws IOException {
        out.println(readyG);
        String line;
        String send_file = path + in.readLine();
        //ここに要求されたファイル名が存在するかの分岐を追加
        
        //ファイル読み込み
        if (in.readLine().equals(readyG)) {
            BufferedReader file_reader=new BufferedReader(new FileReader(send_file));
            while((line=file_reader.readLine())!=null){
                out.println(line);
            }
            out.println("EOF");
            file_reader.close();
        } else{
            System.out.println("you entered undefined file");
        }
    }




    public static User log_in(BufferedReader in,PrintWriter out){
        out.println("新規登録:new ログイン:in");
        try{
            String j=in.readLine();
LOOP:       while(true){
                if (j.equals("in")){
                    out.println("名前を入力してください");
                    String name=in.readLine();
                    String password=JDBC.user_search(name,"password");
                    System.out.println(name+" : "+password);
                    if (password!="failed"){
                       while (true) {
                            out.println("パスワードを入力してください");
                            String pass=in.readLine();  
                            if (password.equals(pass)){
                                int id =Integer.parseInt(JDBC.user_search(name,"id"));
                                User user = new User(id,name,password);
                                return user;
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
                User user = new User(id,name,password);
                user.insert();
                return user;
            }
            
        }
        }catch (IOException e){
            System.out.println("failed");
        }
        return null;

    }
}
    

