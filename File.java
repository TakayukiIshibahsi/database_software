public class File {
    int file_id;
    String file_name;
    int user_id;
    String create_time;
    String update_time;
    
    File(int file_id,String file_name,int user_id,String create_time,String update_time){
        this.file_id=file_id;
        this.file_name=file_name;
        this.user_id=user_id;
        this.create_time=create_time;
        this.update_time=update_time;
    }
}
