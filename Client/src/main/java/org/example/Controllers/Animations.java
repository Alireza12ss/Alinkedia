package org.example.Controllers;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Animations {
    public static void fadeScene(Stage stage, Scene scene) {
        FadeTransition ftOut = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
        ftOut.setFromValue(1.0);
        ftOut.setToValue(0.0);

        ftOut.play();
        ftOut.setOnFinished(event ->
        {
            stage.setScene(scene);
            scene.getRoot().setOpacity(0);
            stage.show();
            FadeTransition ftIn = new FadeTransition(Duration.millis(500), scene.getRoot());
            ftIn.setFromValue(0.0);
            ftIn.setToValue(1.0);
            ftIn.play();
        });
    }
}
