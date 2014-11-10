package de.futjikato.p2d.detail.htmlview;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;

public class HtmlViewController {

    @FXML
    private VBox contentBox;

    private int lineCount = 1;

    public void injectTemplate(String path) {
        File templateFile = new File(path);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(templateFile));
            String line;
            while ((line = reader.readLine()) != null) {
                contentBox.getChildren().add(createRow(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node createRow(String line) {
        HBox row = new HBox();
        row.getStyleClass().add("row-container");
        row.getChildren().add(createLineCounterLabel(lineCount++));
        row.getChildren().add(createSeparator());
        row.getChildren().add(createLabel(line));

        return row;
    }

    private Separator createSeparator() {
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.getStyleClass().add("line-separator");

        return separator;
    }

    private Label createLineCounterLabel(int lineNumber) {
        Label label = new Label(String.valueOf(lineNumber));
        label.getStyleClass().add("line-number");

        return label;
    }

    private Label createLabel(String line) {
        Label label = new Label(line);

        MenuItem toContainerButton = new MenuItem("Use tag as container");
        MenuItem ignoreButton = new MenuItem("Ignore tag/line");

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(toContainerButton);
        contextMenu.getItems().add(ignoreButton);

        label.setContextMenu(contextMenu);

        return label;
    }
}
