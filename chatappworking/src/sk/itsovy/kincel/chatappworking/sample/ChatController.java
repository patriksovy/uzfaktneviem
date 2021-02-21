package sk.itsovy.kincel.chatappworking.sample;

import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sk.itsovy.kincel.chatappworking.database.Database;
import sk.itsovy.kincel.chatappworking.entity.Message;
import sk.itsovy.kincel.chatappworking.entity.Users;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;



public class ChatController {
    public Label lbl_login;
    private Users users;
    public TextField txt_message;
    public TextField to_user;
    public ListView chat_window;
    public Button close_btn;
    // DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    //-------------------------------------------------------------------------------------
    public ChatController(){
        //    System.out.println("Success.");
    }
    //--------------------------------------------------------------------------------
    public void logout(javafx.event.ActionEvent ae) {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
        System.out.println("Logged Out.");
        System.out.println("Chat closed.");
    }
    //--------------------------------------------------------------------------------
    public void setUser(Users users){
        this.users = users;
    }
    //-----------------------------------------------------------------------------------
    public void initLoginName() {
        lbl_login.setText(users.getLogin());
    }
    //---------------------------------------------------------------------------------------
    public void click_refresh(javafx.event.ActionEvent actionEvent) {
        List<Message> list=new Database().getMyMessages(users.getLogin());
        if ( list.isEmpty()){
            return;
        }
        for (Message m:list){
            String from= m.getFrom();
            Date date=m.getDt();
            String msg=m.getText();
            chat_window.getItems().add(0,from+" "+date+" - "+msg);

        }
        System.out.println("Refreshed");
    }
    //----------------------------------------------------------------------------------------------
    public void sent_btn(javafx.event.ActionEvent actionEvent) {
        String reciever=to_user.getText().trim();
        String newMessage=txt_message.getText().trim();
        if(reciever.length()<1 || newMessage.length()<1) {
            return;
        }
        boolean result=new Database().sendMessage(users.getId(),reciever,newMessage);
        to_user.setText("");
        txt_message.setText("");
        System.out.println("Message sent");
    }

}