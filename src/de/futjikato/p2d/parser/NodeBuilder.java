package de.futjikato.p2d.parser;

import java.util.LinkedList;
import java.util.List;

public class NodeBuilder {

    public static final String ATOM_PREFIX = "atoms-";
    public static final String ORGANISM_PREFIX = "organisms-";
    public static final String MOLECULE_PREFIX = "molecules-";

    private DomainNode treeItem;

    private List<NodeBuilder> parents;

    public NodeBuilder(String id, NodeBuilder parent) {
        this.treeItem = new DomainNode(id);
        this.parents = new LinkedList<NodeBuilder>();

        if(parent != null) {
            addParent(parent);
        }

        if(id != null) {
            if (id.substring(0, ATOM_PREFIX.length()).equals(ATOM_PREFIX)) {
                treeItem.setType(DomainNode.AtomicType.ATOM);
            } else if (id.substring(0, ORGANISM_PREFIX.length()).equals(ORGANISM_PREFIX)) {
                treeItem.setType(DomainNode.AtomicType.ORGANSIM);
            } else if (id.substring(0, MOLECULE_PREFIX.length()).equals(MOLECULE_PREFIX)) {
                treeItem.setType(DomainNode.AtomicType.MOLECULE);
            }
        }
    }

    public NodeBuilder(String id) {
        this(id, null);
    }

    public DomainNode getTreeItem() {
        return treeItem;
    }

    public void addParent(NodeBuilder parent) {
        parents.add(parent);
        parent.getTreeItem().getChildren().add(getTreeItem());
    }
}
