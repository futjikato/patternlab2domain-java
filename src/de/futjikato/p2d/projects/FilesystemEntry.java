package de.futjikato.p2d.projects;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

import java.io.File;

public class FilesystemEntry extends TreeItem<String> {

    private File file;

    private boolean loadedChildren = false;

    private FilesystemEntry(String name) {
        super(name);
    }

    private FilesystemEntry(File file) {
        super();
        this.file = file;
        String itemName = file.getName();
        if(itemName.length() == 0) {
            itemName = file.getAbsolutePath();
        }
        setValue(itemName);
    }

    public static FilesystemEntry createRootNode(int depth){
        FilesystemEntry root = new FilesystemEntry("Filesystem");
        root.setExpanded(true);
        root.loadedChildren = true;
        for(File fsRoot : File.listRoots()) {
            FilesystemEntry fsRootItem = createFilesystemEntry(fsRoot, depth);
            root.getChildren().add(fsRootItem);
        }

        return root;
    }

    public static FilesystemEntry createRootNode(){
        return createRootNode(3);
    }

    private static FilesystemEntry createFilesystemEntry(File file, int depth) {
        FilesystemEntry fsItem = new FilesystemEntry(file);

        if(depth > 0) {
            fsItem.loadChildren(depth);
        }

        fsItem.addEventHandler(FilesystemEntry.branchExpandedEvent(), new EventHandler<TreeModificationEvent<Object>>() {
            @Override
            public void handle(TreeModificationEvent<Object> objectTreeModificationEvent) {
                TreeItem<Object> source = objectTreeModificationEvent.getSource();
                try {
                    FilesystemEntry fsItem = FilesystemEntry.class.cast(source);
                    for(TreeItem<String> childItem : fsItem.getChildren()) {
                        if(childItem instanceof FilesystemEntry) {
                            FilesystemEntry fsChildItem = (FilesystemEntry) childItem;
                            if(!fsChildItem.loadedChildren) {
                                fsChildItem.loadChildren(1);
                            }
                        }
                    }
                } catch(ClassCastException e) {
                    /* Do nothing */
                }
            }
        });

        return fsItem;
    }

    private void loadChildren(int depth) {
        loadedChildren = true;
        if(file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if(subFiles != null) {
                for (File subFile : subFiles) {
                    FilesystemEntry fsSubItem = createFilesystemEntry(subFile, depth - 1);
                    getChildren().add(fsSubItem);
                }
            }
        }
    }

    public File getFile() {
        return file;
    }
}
