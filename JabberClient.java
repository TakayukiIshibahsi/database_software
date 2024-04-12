package database_software;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class JabberClient {
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
        for(int i = 0; i < 10; i++) {
            String get = sc.nextLine();
            out.println(get); // データ送信
            String str = in.readLine(); // データ受信
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
}