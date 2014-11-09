package de.futjikato.p2d.parser;

import javafx.scene.control.TreeItem;

public class DomainNode extends TreeItem<String> {

    private String modelId = "";

    private boolean isModelNode = true;

    public DomainNode(String s) {
        super(s);
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public boolean isModelNode() {
        return isModelNode;
    }

    public void setModelNode(boolean isModelNode) {
        this.isModelNode = isModelNode;
    }
}
