package de.futjikato.p2d.parser;

import javafx.scene.control.TreeItem;

import java.util.LinkedList;
import java.util.List;

public class NodeBuilder {

    private TreeItem<String> treeItem;

    private List<NodeBuilder> parents;

    public NodeBuilder(String id, NodeBuilder parent) {
        this.treeItem = new DomainNode(id);
        this.parents = new LinkedList<NodeBuilder>();

        if(parent != null) {
            addParent(parent);
        }
    }

    public NodeBuilder(String id) {
        this(id, null);
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }

    public void addParent(NodeBuilder parent) {
        parents.add(parent);
        parent.getTreeItem().getChildren().add(getTreeItem());
    }
}
