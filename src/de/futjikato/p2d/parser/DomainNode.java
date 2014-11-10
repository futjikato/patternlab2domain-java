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

    private NodeBuilder builder;

    public DomainNode(String s, NodeBuilder nodeBuilder) {
        super(s);
        this.builder = nodeBuilder;
    }

    public String getModelId() {
        Object obj = builder.getInformation("model-id");
        if(obj != null && obj instanceof String) {
            return (String) obj;
        }

        return getValue();
    }

    public void setModelId(String modelId) {
        builder.setInformation("model-id", modelId);
    }

    public boolean isModelNode() {
        Object obj = builder.getInformation("is-model-node");
        if(obj != null && obj instanceof Boolean) {
            return (Boolean)obj;
        }

        return true;
    }

    public void setModelNode(boolean isModelNode) {
        builder.setInformation("is-model-node", isModelNode);
    }

    public AtomicType getType() {
        return type;
    }

    public void setType(AtomicType type) {
        this.type = type;
    }
}
