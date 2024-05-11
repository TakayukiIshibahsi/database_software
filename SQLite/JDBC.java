package SQLite;
import java.sql.*;

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
    
    public static void file_insert(int file_id,String filename,int user_id,String made_time,String update_time){
        String sql = "INSERT INTO file VALUES("+String.valueOf(file_id)+",'"+filename+"'','"+String.valueOf(file_id)+"'','"+made_time+"'','"+update_time+"');";
        sql(sql,1,"");
    }

    public static String file_over(){
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
                ResultSet rs =st.executeQuery(sql);
                rs.first();
                StringBuilder s = new StringBuilder();
                while(true){
                    s.append(rs.getString("file_name"));
                    if (rs.isLast()){
                        break;
                    }
                    rs.next();
                }
                return ""+s;
            }else if(mode==-1){
                ResultSet rs =st.executeQuery(sql);
                StringBuilder s = new StringBuilder();
                while(rs.next()){
                    if (search!="id"){
                        s.append(rs.getString(search));
                    }else{
                        
                        s.append(String.valueOf(rs.getInt(search)));
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

}

