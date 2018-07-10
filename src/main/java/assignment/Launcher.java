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
        primaryStage.setTitle("Clock");
        primaryStage.setMinHeight(640);
        primaryStage.setMinWidth(420);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(HelperUtils.getResourceLocation("styles/root.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        initClocks();
        primaryStage.show();
    }

    public void initClocks() {
        launcherController.registerClock(new FXMLLoader(HelperUtils.getResourceLocation("fxml/clocks/analog.fxml")), "Analog Clock");
        launcherController.registerClock(new FXMLLoader(HelperUtils.getResourceLocation("fxml/clocks/digital.fxml")), "Digital Clock");
        launcherController.registerClock(new FXMLLoader(HelperUtils.getResourceLocation("fxml/clocks/mirrored.fxml")), "Mirrored Analog Clock");
        launcherController.registerClock(new FXMLLoader(HelperUtils.getResourceLocation("fxml/clocks/roman-analog.fxml")), "Roman Analog Clock");
        launcherController.registerClock(new FXMLLoader(HelperUtils.getResourceLocation("fxml/clocks/roman-digital.fxml")), "Roman Digital Clock");
        launcherController.registerClock(new FXMLLoader(HelperUtils.getResourceLocation("fxml/clocks/futuristic.fxml")), "Futuristic Clock").showClock();
    }

    public static void main(String[] args) {
        Launcher.launch();
    }
}
