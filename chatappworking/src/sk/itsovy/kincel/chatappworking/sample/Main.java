package sk.itsovy.kincel.chatappworking.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sk.itsovy.kincel.chatappworking.database.Database;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ChatApp");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);
        //new Database().test();
        Database db=new Database();
        db.insertNewUser("Kincel","password123");
        db.loginUser("Kincel","password123");
    }
}