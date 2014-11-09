package de.futjikato.p2d.projects;

import javafx.collections.ModifiableObservableListBase;

import java.util.ArrayList;
import java.util.List;

public class ProjectStorage extends ModifiableObservableListBase<ProjectEntity> {

    List<ProjectEntity> projects = new ArrayList<ProjectEntity>();

    public void load(String baseDir) {

    }

    public void save(String name, String projectDir, String pagesDir) {
        ProjectEntity entity = new ProjectEntity(name);
        entity.setResourceRoot(projectDir);
        entity.setPagesSubDir(pagesDir);

        add(entity);
    }

    @Override
    public ProjectEntity get(int i) {
        return projects.get(i);
    }

    @Override
    public int size() {
        return projects.size();
    }

    @Override
    protected void doAdd(int i, ProjectEntity projectEntity) {
        projects.add(i, projectEntity);
    }

    @Override
    protected ProjectEntity doSet(int i, ProjectEntity projectEntity) {
        return projects.set(i, projectEntity);
    }

    @Override
    protected ProjectEntity doRemove(int i) {
        return projects.remove(i);
    }
}
