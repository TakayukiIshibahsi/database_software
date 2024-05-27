package SQLite;
import java.sql.*;
import java.time.*;
import java.util.regex.*;

public class JDBC {

    public static void user_insert(int id,String name,String pass){
        String sql = "INSERT INTO user VALUES("+String.valueOf(id)+",'"+name+"','"+pass+"');";
        sql(sql,1,"");
    }

    public static String user_search(String name,String search){
        String sql = "SELECT " +search+ " FROM user WHERE name="+"'"+name+"';";
        String result=sql(sql,-1,search);
        return result;
    }
    
    public static String file_update(String filename){
        String sql = "SELECT user_id FROM file WHERE file_name='"+filename+"';" ;
        String result=sql(sql,-1,"user_id");
        if (result.equals("failed")){
            return "NE";
        }else{
            String update_time = String.valueOf(LocalDateTime.now());
            sql="UPDATE file SET update_time='"+update_time+"' WHERE file_name='"+filename+"';";
            sql(sql,1,"");
            System.out.println("file_update id:"+result);
            return result;
        }

    }

    public static void file_insert(String filename,int user_id){
        int file_id=Integer.parseInt(file_count())+1;
        String made_time=String.valueOf(LocalDateTime.now());
        System.out.println(String.valueOf(file_id)+",'"+filename+"',"+String.valueOf(user_id)+",'"+made_time+"','"+made_time);
        String sql = "INSERT INTO file VALUES("+String.valueOf(file_id)+",'"+filename+"',"+String.valueOf(user_id)+",'"+made_time+"','"+made_time+"');";
        sql(sql,1,"");
    }

    public static String file_count(){
        String sql="SELECT * FROM file;";
        return sql(sql, 3,""); 
    }

    public static String file_user_id_search(String name){
        String sql = "select * from file where file_name = '"+name+"';";
        return  sql(sql,-1,"user_id");
    }

    public static String file_delete(String name,int id){
        if (id!=Integer.parseInt(file_user_id_search(name))){
            return "you don't have permit";
        }
        String sql="DELETE FROM file where file_name = '"+name+"';";
        return sql(sql,1,"");
    }

    public static String file_look(){
        String sql="SELECT * FROM file;";
        return sql(sql, 0,""); 
    }

    public static String number_of_member(){
        String sql="SELECT * FROM user;";
        return sql(sql, 3,"");
    }

    public static String sql(String sql,int mode,String search){
        Connection con = null;
        Statement st = null;
        String URL = "jdbc:sqlite:Database.db";
        try{
            Class.forName("org.sqlite.JDBC");
            con=DriverManager.getConnection(URL);
            st=con.createStatement();
            
            if (mode==1){
                st.executeUpdate(sql);
                return "done"; 
            }else if (mode==0){
                String regex="(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+).*";
                ResultSet rs =st.executeQuery(sql);
                StringBuilder s = new StringBuilder();
                int count=Integer.parseInt(file_count());
                File2[] files=new File2[count] ;
                int p=0;
                //file_array
                while(rs.next()){
                    File2 present_file = new File2(rs.getInt("file_id"),rs.getString("file_name"),rs.getInt("user_id"),rs.getString("create_time"),rs.getString("update_time"));
                    files[p]=present_file;
                    p++;
                }
                //sort
                Pattern timep = Pattern.compile(regex);
                for(int i=0;i<count-1;i++){
                    boolean path=false;
                    for(int j=0;j<count-1-i;j++){
                        Matcher b = timep.matcher(files[j].update_time);
                        Matcher a = timep.matcher(files[j+1].update_time);
                        path=true;
                        for(int k=0;k<6;k++){
                            b.matches();
                            a.matches();
                            int bi =Integer.parseInt(b.group(k+1));
                            int ai =Integer.parseInt(a.group(k+1));
                            if(bi<ai){
                                File2 temp=files[j];
                                files[j]=files[j+1];
                                files[j+1]=temp;
                                path=false;
                                break;
                            }else if(bi>ai){
                                break;
                            }
                        }
                    }
                }
                for(int i=0;i<count;i++){
                    s.append(files[i].file_name+"\n");
                }
                return ""+s;
            }else if(mode==-1){
                ResultSet rs =st.executeQuery(sql);
                StringBuilder s = new StringBuilder();
                while(rs.next()){
                    if (search.contains("id")){                     
                        s.append(String.valueOf(rs.getInt(search)));
                    }else{
                        s.append(rs.getString(search));
                    }
                return s+"";
                }
            }else if(mode==3){
                ResultSet rs =st.executeQuery(sql);
                int count=0;
                while(rs.next()){
                    count++;
                }
                return String.valueOf(count);
            }
            
                       
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                st.close();
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return "failed";
    }    


    public static class User {
        public int id;
        public String name;
        public String password;
    
        public User(int id,String name,String password){
            this.id=id;
            this.name=name;
            this.password=password;
        }
        
        public void show(){
            System.out.println("id:"+String.valueOf(this.id));
            System.out.println("name:"+name);
            System.out.println("password:"+password);
        }
    
        public void insert(){
            JDBC.user_insert(this.id, this.name, this.password);
        } 
    
    }

    public static class File2{
        public int file_id;
        public String file_name;
        public int user_id;
        public String create_time;
        public String update_time;

        public File2(int file_id,String file_name,int user_id,String create_time,String update_time){
            this.file_id=file_id;
            this.file_name=file_name;
            this.user_id=user_id;
            this.create_time=create_time;
            this.update_time=update_time;
        }


    }

}

