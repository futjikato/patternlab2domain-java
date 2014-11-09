package de.futjikato.p2d.projects;

public class ProjectEntity {

    private String name;

    private String resourceRoot;

    private String pagesSubDir;

    public ProjectEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public String getResourceRoot() {
        return resourceRoot;
    }

    public void setResourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

    public String getPagesSubDir() {
        return pagesSubDir;
    }

    public void setPagesSubDir(String pagesSubDir) {
        this.pagesSubDir = pagesSubDir;
    }
}
