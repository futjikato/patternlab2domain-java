package de.futjikato.p2d.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NodeBuilder {

    public static final String ATOM_PREFIX = "atoms-";
    public static final String ORGANISM_PREFIX = "organisms-";
    public static final String MOLECULE_PREFIX = "molecules-";

    private List<DomainNode> nodes;

    private List<DomainNode> children;

    private Map<String, Object> information;

    private String id;

    private DomainNode.AtomicType type;

    public NodeBuilder(String id) {
        this.id = id;
        this.nodes = new LinkedList<DomainNode>();
        this.children = new LinkedList<DomainNode>();
        this.information = new HashMap<String, Object>();

        if(id != null) {
            if (id.substring(0, ATOM_PREFIX.length()).equals(ATOM_PREFIX)) {
                type = DomainNode.AtomicType.ATOM;
            } else if (id.substring(0, ORGANISM_PREFIX.length()).equals(ORGANISM_PREFIX)) {
                type = DomainNode.AtomicType.ORGANSIM;
            } else if (id.substring(0, MOLECULE_PREFIX.length()).equals(MOLECULE_PREFIX)) {
                type = DomainNode.AtomicType.MOLECULE;
            }
        }
    }

    public DomainNode createTreeItem() {
        DomainNode treeItem = new DomainNode(id, this);
        treeItem.setType(type);
        nodes.add(treeItem);
        return treeItem;
    }

    public void finalizeBuilder() {
        for(DomainNode node : nodes) {
            node.getChildren().addAll(children);
        }
    }

    public void addChild(DomainNode node) {
        children.add(node);
    }

    public Object getInformation(String info) {
        return information.get(info);
    }

    public void setInformation(String info, Object value) {
        information.put(info, value);
    }
}
