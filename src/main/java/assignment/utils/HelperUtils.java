package assignment.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;

import java.net.URL;

public class HelperUtils {
    public static URL getResourceLocation(String relativePath) {
        return HelperUtils.class.getClassLoader().getResource(relativePath);
    }

    public static void createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseExited(event -> popup.hide());
        label.getStylesheets().add(HelperUtils.getResourceLocation("styles/popup.css").toString());
        label.getStyleClass().add("popup");
        popup.getContent().add(label);


    }
}
