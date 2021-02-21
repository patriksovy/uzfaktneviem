package sk.itsovy.kincel.chatappworking.database;

import sk.itsovy.kincel.chatappworking.entity.Message;
import sk.itsovy.kincel.chatappworking.entity.Users;
import sk.itsovy.kincel.chatappworking.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final String insertNewUser = "INSERT INTO user (login, password) VALUES (?,?)";
    private final String loginUser = "Select * FROM user Where login LIKE ? and password LIKE ?";
    private final String newMessage = "INSERT INTO message( fromUser, toUser, text) VALUES (?,?,?)";
    private final String deleteMsg="DELETE FROM message WHERE toUser = (?) ; ";
    private final String getID = "SELECT id FROM user WHERE login LIKE ?";
    private final String newPassword="";
    private final String myOldMesages="SELECT * FROM message  " +
            "INNER JOIN user ON user.id=message.fromUser " +
            "WHERE toUser = ?";
    //----------------------------------------------------------------------------------------------
    private String url = "jdbc:mysql://itsovy.sk:3306/chat2021";
    private String username = "mysqluser";
    private String password = "Kosice2021!";
    //----------------------------------------------------------------------------------------------
    private Connection getConnection() throws ClassNotFoundException,SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,username,password);
        return conn;
    }
    //----------------------------------------------------------------------------------------
    public boolean insertNewUser(String login,String password) {
        if (login == null || login.equals("") || password == null || password.length() < 6)
            return false;
        String hashPassword = new Util().getMd5(password);
        try {
            Connection conn=getConnection();
            if (conn==null) {
                System.out.println("Connection error.");
                return false;
            }
            PreparedStatement ps=conn.prepareStatement(insertNewUser);
            ps.setString(1, login);
            ps.setString(2, hashPassword);
            int result = ps.executeUpdate();
            conn.close();
            if(result==0)
                return false;
            else{
                System.out.println("User has been registered.");
                return true;
            }
        }catch(Exception ex){
            System.out.println("User already exist.");
        }
        return true;
    }
    //--------------------------------------------------------------------------------------------
    public Users loginUser(String login, String password) {
        if (login==null || login.equals("") || password==null || password.length() < 6)
            return null;
        String hashPassword=new Util().getMd5(password);
        try {
            Connection conn=getConnection();
            if (conn==null) {
                System.out.println("Connection error.");
                return null;
            }
            PreparedStatement ps=conn.prepareStatement(loginUser);                                                         ;
            ps.setString(1, login);
            ps.setString(2, hashPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Success.");
                int id = rs.getInt("id");
                Users users = new Users(id, login, hashPassword);
                conn.close();
                return users;
            } else {
                conn.close();
                System.out.println("Incorect credentials.");
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    //----------------------------------------------------------------------------------------------
    public boolean changePwd(String login, String oldPwd, String newPwd){
        if (oldPwd==null || oldPwd.equals("") || newPwd==null || newPwd.equals(""))
            return  false;
        try {
            Connection conn=getConnection();
            if (conn==null){
                System.out.println("error");
                return false;
            }
            PreparedStatement ps= conn.prepareStatement(newPassword);
            ps.setString(1, login);
            ps.setString(2, oldPwd);
            ps.setString(3, newPwd);
            int rs= ps.executeUpdate();
            if (rs<1){
                System.out.println("Error.");
                conn.close();
                return false;
            }else{
                System.out.println("password changed.");
                conn.close();
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
    //--------------------------------------------------------------------------------
    public boolean sendMessage(int from, String toUser, String text){
        if(text==null || text.equals(""))
            return false;
        int to = getUserId(toUser);
        if(to==-1)
            return false;
        try {
            Connection conn=getConnection();
            if(conn==null){
                System.out.println("Error.");
                return false;
            }
            PreparedStatement ps=conn.prepareStatement(newMessage);
            ps.setInt(1,from);
            ps.setInt(2, to);
            ps.setString(3, text);
            int result=ps.executeUpdate();
            conn.close();
            if(result<1){
                System.out.println("Message not sent.");
                return false;
            }
            else{
                System.out.println("Message sent.");
                return true;
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return true;
    }
    //--------------------------------------------------------------------------------------
    public int getUserId(String login){
        if(login==null || login.equals(""))
            return -1;
        int id=-1;
        try{
            Connection conn=getConnection();
            if(conn!=null) {
                PreparedStatement ps = conn.prepareStatement(getID);
                ps.setString(1, login);
                ResultSet rs = ps.executeQuery();
                System.out.println(ps);
                if (rs.next()) {
                    id = rs.getInt("id");
                }
                conn.close();
                return id;
                 /*else {
                    conn.close();
                    return -1;
             }*/
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //-----------------------------------------------------------------------------------------
    public List<Message> getMyMessages(String login){
        if(login==null || login.equals(""))
            return null;
        int id=getUserId(login);
        if (id!=-1) {
            try {
                Connection conn = getConnection();
                if (conn == null) {
                    System.out.println("error");
                    return null;
                }
                List<Message> messagesList = new ArrayList<>();
                PreparedStatement ps = conn.prepareStatement(myOldMesages);
                ps.setInt(1,id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String from = rs.getString("user.login");
                    String msg = rs.getString("text");
                    int idMsg = rs.getInt("message.id");
                    Date date = rs.getDate("dt");
                    Message m = new Message(idMsg,from,login,date,msg);
                    messagesList.add(m);

                }
                conn.close();
                //  deleteMessages(login);
                return messagesList;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //------------------------------------------------------------------------------------------
    public boolean deleteMessages(String login){
        if(login==null || login.equals(""))
            return false;
        try{
            Connection conn=getConnection();

            PreparedStatement ps= conn.prepareStatement(deleteMsg);
            ps.setString(1, login);
            int rs= ps.executeUpdate();
            if (rs<1){
                System.out.println("Messages are not deleted.");
                conn.close();
                return false;
            }else{
                System.out.println("Messages are deleted.");
                conn.close();
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }



}