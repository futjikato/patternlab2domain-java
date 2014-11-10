package de.futjikato.p2d.parser;

import javafx.scene.control.TreeItem;

public class DomainNode extends TreeItem<String> {

    public enum AtomicType {
        PAGE,
        ATOM,
        MOLECULE,
        ORGANSIM
    }

    private AtomicType type;

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

    public AtomicType getType() {
        return type;
    }

    public void setType(AtomicType type) {
        this.type = type;
    }
}
