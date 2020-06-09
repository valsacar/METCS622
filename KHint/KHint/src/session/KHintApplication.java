package session;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class KHintApplication extends Application {


    @Override
    public void start(Stage stage) throws Exception {
    	KHintProblem problem = new KHintProblem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("KHintApplication.fxml"));
        
        // Create a controller instance
        KHintController controller = new KHintController(problem);
        // Set it in the FXMLLoader
        loader.setController(controller);
        GridPane flowPane = loader.load();
        Scene scene = new Scene(flowPane, 800, 500);
        
        stage.setTitle("Knowla-Hint");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(KHintApplication.class);
    }

    

}