import java.sql.*;
public class JDBCtest {
    public static void main(String[] args){

        Connection con = null;
        Statement st = null;
        String URL = "jdbc:sqlite:Database.db";

        try{
            Class.forName("org.sqlite.JDBC");

            con=DriverManager.getConnection(URL);
            st=con.createStatement();

            String sql = "INSERT INTO user VALUES(2,'master','pass')";
            st.executeUpdate(sql);
                       

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
        

    }    
}

