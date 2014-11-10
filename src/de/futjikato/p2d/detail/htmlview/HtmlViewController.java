package de.futjikato.p2d.detail.htmlview;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.*;

public class HtmlViewController {

    @FXML
    private VBox contentBox;

    public void injectTemplate(String path) {
        File templateFile = new File(path);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(templateFile));
            String line;
            while ((line = reader.readLine()) != null) {
                contentBox.getChildren().add(new Label(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
