package assignment;

import assignment.utils.HelperUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Launcher extends Application {
    private LauncherController launcherController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(HelperUtils.getResourceLocation("fxml/launcher.fxml"));
        Parent root = loader.load();
        launcherController = loader.getController();
        primaryStage.setTitle("JavaFX Clock");
        primaryStage.setMinHeight(640);
        primaryStage.setMinWidth(420);
        primaryStage.setResizable(false);
        primaryStage.setOnShown((event -> launcherController.resetAllColors()));

        initClocks();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(HelperUtils.getResourceLocation("styles/root.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initClocks() {
        launcherController.registerClock("fxml/clocks/analog.fxml", "Analog Clock").showClock();
        launcherController.registerClock("fxml/clocks/digital.fxml", "Digital Clock");
        launcherController.registerClock("fxml/clocks/mirrored.fxml", "Mirrored Analog Clock");
        launcherController.registerClock("fxml/clocks/roman-analog.fxml", "Roman Analog Clock");
        launcherController.registerClock("fxml/clocks/roman-digital.fxml", "Roman Digital Clock");
        launcherController.registerClock("fxml/clocks/futuristic.fxml", "Futuristic Clock");
    }

    public static void main(String[] args) {
        Launcher.launch();
    }
}
