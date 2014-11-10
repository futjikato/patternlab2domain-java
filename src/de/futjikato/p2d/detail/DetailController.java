package de.futjikato.p2d.detail;

import de.futjikato.p2d.Main;
import de.futjikato.p2d.detail.htmlview.HtmlViewController;
import de.futjikato.p2d.parser.DomainNode;
import de.futjikato.p2d.parser.ProjectConductor;
import de.futjikato.p2d.projects.ProjectEntity;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailController implements Initializable {

    @FXML
    private AnchorPane loadingPane;

    @FXML
    private GridPane detailPane;

    @FXML
    private TreeView<String> targetTree;

    @FXML
    private CheckBox isModelNode;

    @FXML
    private TextField modelId;

    private ProjectConductor conductor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conductor = new ProjectConductor();

        conductor.onLoadingComplete(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        DetailController.this.targetTree.setRoot(conductor.getTargetTree());

                        DetailController.this.loadingPane.setVisible(false);
                        DetailController.this.detailPane.setVisible(true);
                    }
                });
            }
        });

        targetTree.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<String>> change) {
                TreeItem<String> item = targetTree.getSelectionModel().getSelectedItem();
                if(item instanceof DomainNode) {
                    DomainNode node = (DomainNode) item;
                    if(node.getType().equals(DomainNode.AtomicType.PAGE)) {
                        isModelNode.setSelected(false);
                        isModelNode.setDisable(true);

                        modelId.setText("");
                        modelId.setDisable(true);
                    } else {
                        isModelNode.setSelected(node.isModelNode());
                        isModelNode.setDisable(false);

                        modelId.setText(node.getModelId());
                        modelId.setDisable(false);
                    }
                }
            }
        });
    }

    @FXML
    public void updateNode() {
        TreeItem<String> item = targetTree.getSelectionModel().getSelectedItem();
        if(item instanceof DomainNode) {
            DomainNode node = (DomainNode) item;
            node.setModelNode(isModelNode.isSelected());
            node.setModelId(modelId.getText());
        }
    }

    public void onViewHtml() {
        TreeItem<String> item = targetTree.getSelectionModel().getSelectedItem();
        if(item instanceof DomainNode) {
            DomainNode node = (DomainNode) item;

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("htmlview.fxml"));
                Parent root = fxmlLoader.load();

                HtmlViewController controller = fxmlLoader.getController();
                if (controller != null) {
                    controller.injectTemplate(node.getTemplatePath());

                    Stage stage = new Stage();
                    stage.setTitle("Node HTML view");
                    stage.setScene(new Scene(root, 600, 400));
                    stage.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void injectProject(ProjectEntity project) {
        conductor.runProject(project);
    }
}
