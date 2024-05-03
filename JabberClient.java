
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.*;
public class JabberClient {

public static String readyA="ra",readyL="rl",readyG="rg";
public static void main(String[] args)
throws IOException {
    
    InetAddress addr =  
    InetAddress.getByName("localhost"); // IPアドレスへの変換
    System.out.println("addr = " + addr);
    Socket socket = new Socket(addr, JabberServer.PORT); // ソケットの生成
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

        Scanner sc = new Scanner(System.in);
        System.out.println("モードを選んでください");
        System.out.println("ファイル追加:ADD    ファイル閲覧:LOOK   ファイル取得:GET");
        while(true){
            String get = sc.nextLine();
            out.println(get); // データ送信
            String str = in.readLine(); 
            
            if(str.equals(readyA)){
                send(in, out,sc);
            }else if (str.equals(readyL)){
                out.println(readyL);
            }else if (str.equals(readyG)){
                out.println(readyG);
            }
            System.out.println("確認"+str);
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



    public static void send(BufferedReader in,PrintWriter out,Scanner sc)throws IOException{
        String line;
        Pattern file_name_regex=Pattern.compile("\\\".*[\\\\](\\w+\\.\\w+)\\\"");
            System.out.println("ファイルのパスを指定してください。");
            String origin_filename=sc.nextLine();
            Matcher file_match = file_name_regex.matcher(origin_filename);
            if (file_match.matches()){
                String filename=file_match.group(1);
                System.out.println("you send:"+filename+"\n");
                out.println(filename);
                if(in.readLine().equals(readyA)){
                
                    String rep=origin_filename.replaceAll("\\\"", "");
                    System.out.println("kakuninn"+rep+"\n");
                    BufferedReader file_reader=new BufferedReader(new FileReader(rep));
                    while((line=file_reader.readLine())!=null){
                        out.println(line);
                    }
                    out.println("EOF");
                    file_reader.close();
                }
            }else{
                System.out.println("you entered undefined file");
            }
    } 
}