import java.io.*;
import java.net.*;

public class Server extends Thread {
    Socket          socket;
    BufferedReader  socket_in;
    PrintWriter     socket_out;
    String          out_dir;
    byte[]          buffer;
    Status          status;  



    Server(Socket socket) throws IOException {
        this.socket     = socket;
        this.out_dir    = "./datas/";   //管理ファイル名
        this.status     = new.Status();
        this.buffer     = new byte[1024];
    }

    public void create_stream() throws IOException {
        BufferedReader socket_in =
        new BufferedReader(
            new InputStreamReader(
                socket.getInputStream())); // データ受信用バッファの設定
        PrintWriter socket_out =
            new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(
                        socket.getOutputStream())), true); // 送信バッファ設定
    }

    public synchronized String get_text() throws IOException { //テキストファイルの受信 
        String line;
        String out_file = out_dir + socket_in.readLine();   
        PrintWriter file_writer = new PrintWriter(new BufferedWriter(new FileWriter(out_file)));
        while((line = socket_in.readLine()) != null){
            if(line.equals("|EOF|")) break;
            file_writer.println(line);  
        }

        file_writer.close();
        return out_file;
    }

    public synchronized void get_file() throws IOException {    // Wordファイルの受信
        String file_name = socket_in.readLine();
        FileOutputStream fileOutputStream = new FileOutputStream(out_dir + file_name);
        InputStream inputStream = socket.getInputStream();
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        fileOutputStream.close();
    }


    public synchronized void send_text() throws IOException {    //テキストファイルの送信
        String line;
        String file_name = socket_in.readLine();
        BufferedReader file_reader = new BufferedReader(new FileReader(out_dir+file_name));
        
        socket_output.println(file_name);

        while((line = file_reader.readLine()) != null) {
            socket_out.println(line);
        }
        socket_out.println("|EOF|");
        file_reader.close();
    }

    public synchronized void send_file() throws IOException {    // その他ファイルの送信
        String file_name = socket_in.readLine();
        File file = new File(out_dir + file_name);
        if(!file.exists()) {
            socket_out.println(status.file_not_found);
            return;
        }
        socket_out.println(file_name);
        
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = socket.getOutputStream();
        int bytesRead;
        while ((int bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        fileInputStream.close();
    }

    public synchronized String delete() throws IOException {    //ファイルの削除
        String filename = socket_in.readLine();
        File file = new File(String.format(out_dir+filename));

        if(!file.exists()) return status.file_not_found;
        if(file.delete()) return status.success;
        message.print_err("failed to delete file in 'delete'");
        return status.fail;
    }

    public String recv_command() {      //受け取ったコマンドに応じて行う動作を決める
        String cmd="none";
        try {
            cmd = socket_input.readLine();

            if(cmd.equals("get_text")) {
                System.out.println("recieved get_text");
                send_text();
            }
            if(cmd.equals("get_file")) {
                System.out.println("recieved get_file");
                send_file();
            }
            if(cmd.equals("put_text")) {
                System.out.println("recieved put_text");
                get_text();
            }
            if(cmd.equals("put_file")) {
                System.out.println("recieved put_file");
                get_file();
            }
            if(cmd.equals("delete")) {
                System.out.println("recieved delete");
                String stats = delete();
                socket_out.println(status);
            }

            if(cmd.equals("delete")) {
                System.out.println("closing... ");
                return status.done;
            }
            return status.not_done;
        }
        catch (IOException e){message.print_err("IOException in 'recv_command'");}
        return status.not_done;
    }
}