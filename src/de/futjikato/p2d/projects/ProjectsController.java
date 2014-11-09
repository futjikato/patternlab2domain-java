package de.futjikato.p2d.projects;

import com.sun.javafx.collections.ObservableListWrapper;
import de.futjikato.p2d.Main;
import de.futjikato.p2d.detail.DetailController;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ProjectsController implements Initializable {

    private ProjectStorage storage;

    @FXML
    private TextField projectName;

    @FXML
    private TreeView<String> projectDir;

    @FXML
    private ListView<ProjectEntity> projectList;

    @FXML
    private ComboBox<String> projectPagesDir;

    private boolean projectSelectionValid = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        storage = new ProjectStorage();
        // todo load previously saved projects
        projectList.setItems(storage);

        projectDir.setRoot(FilesystemEntry.createRootNode());
        projectDir.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<String>> change) {
                TreeItem<String> selectedItem = projectDir.getSelectionModel().getSelectedItems().get(0);
                if(selectedItem instanceof FilesystemEntry) {
                    FilesystemEntry selectedFsItem = (FilesystemEntry) selectedItem;
                    File selectedFile = selectedFsItem.getFile();
                    File patternDir = new File(String.format("%s/%s", selectedFile.getAbsolutePath(), "source/_patterns"));
                    if(patternDir.exists() && patternDir.isDirectory()) {
                        projectPagesDir.setItems(new ObservableListWrapper<String>(Arrays.asList(patternDir.list())));
                        projectPagesDir.setDisable(false);
                        projectSelectionValid = true;
                    } else {
                        projectSelectionValid = false;
                        projectPagesDir.getItems().clear();
                        projectPagesDir.setDisable(true);
                    }
                }
            }
        });
    }

    @FXML
    public void onCreateProject() {
        // todo proper validation
        if(!projectSelectionValid) {
            return;
        }

        TreeItem<String> selection = projectDir.getSelectionModel().getSelectedItems().get(0);
        if(selection instanceof FilesystemEntry) {
            FilesystemEntry fsItem = (FilesystemEntry) selection;
            String resourceRoot = fsItem.getFile().getAbsolutePath();

            String pagesDir = projectPagesDir.getSelectionModel().getSelectedItem();
            String pagesAbsolutePath = String.format("%s/%s/%s", resourceRoot, "source/_patterns", pagesDir);

            storage.save(projectName.getText(), resourceRoot, pagesAbsolutePath);

            // todo save storage to disc
        }
    }

    @FXML
    public void onStartProject() {
        ProjectEntity selectedEntity = projectList.getSelectionModel().getSelectedItem();
        if(selectedEntity == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("detail.fxml"));
            Parent root = fxmlLoader.load();

            DetailController controller = fxmlLoader.getController();
            if(controller != null) {
                controller.injectProject(selectedEntity);

                Stage stage = new Stage();
                stage.setTitle("Project details");
                stage.setScene(new Scene(root, 600, 400));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
