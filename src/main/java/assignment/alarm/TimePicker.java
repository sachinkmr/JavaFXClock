package assignment.alarm;

import assignment.component.TimeSpinner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TimePicker {
    public static String getSelected() {
        TimeSpinner timeSpinner = new TimeSpinner();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Alarm Time");
        window.setMinWidth(200);
        window.setMinHeight(100);
        window.setResizable(false);

        Label label = new Label("");
        label.setVisible(false);

        Button okButton = new Button("Okay");
        Button cancelButton = new Button("Cancel");
        okButton.setOnAction(event -> {
            window.close();
            label.setText(timeSpinner.getEditor().getText());
        });
        cancelButton.setOnAction(event -> window.close());

        HBox hbox = new HBox();
        hbox.setSpacing(25);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(okButton, cancelButton);

        VBox pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.setSpacing(25);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(timeSpinner, hbox);

        Scene scene = new Scene(pane, 200, 100);
        window.setScene(scene);
        window.showAndWait();
        return label.getText();
    }
}
