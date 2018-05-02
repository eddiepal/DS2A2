package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Parent root =
		// FXMLLoader.load(getClass().getResource("/src/application/Main.fxml"));
		Parent root1 = (Parent) FXMLLoader.load(Main.class.getResource("/application/Main.fxml"));
		primaryStage.setTitle("Map Finder 2018 - Pro Edition");
		primaryStage.setScene(new Scene(root1, 1000, 800));
		GraphNodeAL2 rh = new GraphNodeAL2();
		primaryStage.show();

	}
}
