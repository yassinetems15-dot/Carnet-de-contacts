package ma.acme.contacts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ma.acme.contacts.ui.MainView;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainView view = new MainView();
        Scene scene = new Scene(view, 900, 600);

        stage.setTitle("Carnet de contacts");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}