package sk.itsovy.kincel.chatappworking.sample;

import sk.itsovy.kincel.chatappworking.database.Database;
import sk.itsovy.kincel.chatappworking.entity.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private TextField txt_login;
    public PasswordField txt_pwd;
    public Label lbl_error;
    public Button btn_login;
    //-----------------------------------------------------------------------------------
    public void btn_click(ActionEvent ae) {
        System.out.println("It works.");
        String login = txt_login.getText().trim();
        String password = txt_pwd.getText().trim();
        if(login.length()>0 && password.length()>0){
            Database db = new Database();
            Users users = db.loginUser(login,password);
            if(users ==null){
                lbl_error.setVisible(true);
            }else{
                System.out.println("You are logged.");
                openMainForm(users);
            }
        }
    }
    //---------------------------------------------------------------------------
    private void openMainForm(Users users) {
        try {
            FXMLLoader root = new FXMLLoader();
            root.setLocation(getClass().getResource("Chat.fxml"));
            Stage stage = new Stage();
            stage.setTitle("ChatApp");
            stage.setScene(new Scene(root.load(), 800, 600));
            stage.show();
            ChatController mc= root.getController();
            mc.setUser(users);
            mc.initLoginName();
            btn_login.getScene().getWindow().hide();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}