
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.*;
public class JabberClient {

public static String readyA="ra",readyL="rl",readyG="rg",readyD="rd";
public static String path;
public static void main(String[] args)
throws IOException {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter IP Address");
    String IP=sc.nextLine();
    InetAddress addr =  
    InetAddress.getByName(IP); // IPアドレスへの変換
    System.out.println("addr = " + addr);
    Socket socket = new Socket(addr, 8080); // ソケットの生成

    Path p1 = Paths.get("");  //カレントディレクトリの相対パスを取得
    Path current_dir = p1.toAbsolutePath();  //カレントディレクトリの絶対パスを取得
    String datas_path_String = current_dir.toString() + "/client_files/";  //絶対パスを文字列にし、datasディレクトリを追加
    Path datas_path = Paths.get(datas_path_String);  //文字列からパスへ
    path = datas_path_String + "/";   //ファイル保存時のために"/"を追加"
    if(!Files.exists(datas_path)) {
        try{
            Files.createDirectory(datas_path);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    try {
    System.out.println("socket = " + socket);
        BufferedReader in =
            new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream())); // データ受信用バッファの設定
        PrintWriter out =
            new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(
                        socket.getOutputStream())), true); // 送信バッファ設定
        
        

        while (true) {
            System.out.print("!");
            String msg=in.readLine();
            if(msg.equals("log_in_success")){
                break;
            }
            System.out.println(msg);
            String get = sc.nextLine();
            out.println(get);
        }

        while(true){
            System.out.println("\nモードを選んでください");
            System.out.println("ファイル追加:ADD    ファイル閲覧:LOOK   ファイル取得:GET  \nファイル削除:DEL  終了:END");
        
            String get = sc.nextLine();
            out.println(get); // データ送信
            String str = in.readLine();  //確認用
            
            if(str.equals(readyA)){
                send(in, out,sc);
            }else if (str.equals(readyL)){
                out.println(readyL);
                look_file(in, out, sc);
            }else if (str.equals(readyG)){
                get(in,out,sc);
            }else if (str.equals(readyD)){
                delete(in,out,sc);
            }

            if (get.equals("END")){
                break;
            }
        }
        
        sc.close();
        out.println("END");
        } finally {
            System.out.println("closing...");
            socket.close();
            
        }
    }

    public static void look_file(BufferedReader in,PrintWriter out,Scanner sc)throws IOException{
        String accept="";
        while(true){
            
            accept = in.readLine();
            if(accept.equals("|EOF|")){
                break;
            }
            System.out.println(accept);
        }
        
    }
    
    public static void delete(BufferedReader in,PrintWriter out,Scanner sc)throws IOException{
        out.println(readyD);
        System.out.println(in.readLine());
        String delete_file_name=sc.nextLine();
        out.println(delete_file_name);
    }

    public static void send(BufferedReader in,PrintWriter out,Scanner sc)throws IOException{
        String line;
        Pattern file_name_regex=Pattern.compile("\\\".*[\\\\](\\w+\\.\\w+)\\\"");
        Pattern file_name_regex_mac=Pattern.compile("\\\'.*[/](\\w+\\.\\w+)\\\'");
            System.out.println("ファイルのパスを指定してください。");
            String origin_filename=sc.nextLine();
            Matcher file_match = file_name_regex.matcher(origin_filename);
            Matcher file_match_mac=file_name_regex_mac.matcher(origin_filename);
            if (file_match.matches()){
                String filename=file_match.group(1);
                System.out.println("you send filename:"+filename+"\n");
               
                out.println(filename);
                String confirm=in.readLine();

                if(confirm.equals(readyA)){
                    String rep=origin_filename.replaceAll("\\\"", "");
                    BufferedReader file_reader=new BufferedReader(new FileReader(rep));
                    while((line=file_reader.readLine())!=null){
                        out.println(line);
                    }
                    out.println("EOF");
                    file_reader.close();
                }else{
                    System.out.println(confirm);
                }
            }else if(file_match_mac.matches()){
                String filename=file_match.group(1);
                System.out.println("you send filename:"+filename+"\n");
                out.println(filename);
                String confirm=in.readLine();
                System.out.println(confirm);
                if(confirm.equals(readyA)){
                    String rep=origin_filename.replaceAll("\\\'", "");
                    BufferedReader file_reader=new BufferedReader(new FileReader(rep));
                    while((line=file_reader.readLine())!=null){
                        out.println(line);
                    }
                    out.println("EOF");
                    file_reader.close();
                }else{
                    System.out.println(confirm);
                }
            }else{
                System.out.println("you entered undefined file");
            }
    } 

    
    
    public static void get(BufferedReader in,PrintWriter out,Scanner sc) throws IOException {
        String line;
        System.out.print("Get File Name: ");
        String get_filename = sc.nextLine();
        PrintWriter file_writer = new PrintWriter(new BufferedWriter(new FileWriter(path+get_filename)));
        out.println(get_filename);
        out.println(readyG); //ファイル作成完了の確認。ファイル作成後にファイル名送信しているので、なくていい

        while((line = in.readLine()) != null){
            if(line.equals("EOF")) break;
            file_writer.println(line);
        }
        file_writer.close(); 
    }
}