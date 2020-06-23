package session;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logging.KLogger;

/**
 * @author Joseph Monk
 *
 * Main application window, also holds the static reference to the logging thread.
 */
public class KHintApplication extends Application {
	public static KLogger LOGGER = new KLogger();
	private static Thread logThread = new Thread(LOGGER);

    @Override
    public void start(Stage stage) throws Exception {
    	KHintProblem problem = new KHintProblem(1);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("KHintApplication.fxml"));
        
        // Create a controller instance
        KHintController controller = new KHintController(problem);
        // Set it in the FXMLLoader
        loader.setController(controller);
        GridPane flowPane = loader.load();
        Scene scene = new Scene(flowPane, 1200, 500);
        
        stage.setTitle("Knowla-Hint -- " + problem.getTitle());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
    	logThread.start();
    	LOGGER.log("Application stated");
        launch(KHintApplication.class);
    }

    

}