package de.futjikato.p2d.projects;

import com.google.gson.Gson;
import javafx.collections.ModifiableObservableListBase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectStorage extends ModifiableObservableListBase<ProjectEntity> {

    List<ProjectEntity> projects = new ArrayList<ProjectEntity>();

    private File base;

    public ProjectStorage() {
        String homeDir = System.getProperty("user.home");
        String fileSeperator = System.getProperty("file.separator");
        String projectDir = ".viewmodel";

        String baseDir = String.format("%s%s%s", homeDir, fileSeperator, projectDir);

        File base = new File(baseDir);
        if(!base.exists()) {
            if(!base.mkdir()) {
                // todo log failure
                return;
            }
        }

        this.base = base;
        load(base);
    }

    public void load(File baseDir) {
        File[] projectFiles = baseDir.listFiles();
        if(projectFiles != null) {
            for (File project : projectFiles) {
                try {
                    if (project.isFile() && project.canRead()) {
                        loadProject(project);
                    }
                } catch (IOException e) {
                    // todo log failure
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadProject(File project) throws IOException {
        FileReader reader = new FileReader(project);
        Gson gson = new Gson();
        ProjectEntity entity = gson.fromJson(reader, ProjectEntity.class);

        add(entity);
    }

    private void saveProject(ProjectEntity entity) throws IOException {
        if(base == null) {
            return;
        }

        String fileSeperator = System.getProperty("file.separator");
        String absFile = String.format("%s%s%s", base.getAbsolutePath(), fileSeperator, entity.getName());
        File file = new File(absFile);

        FileWriter writer = new FileWriter(file);
        Gson gson = new Gson();
        gson.toJson(entity, writer);
        writer.flush();
        writer.close();
    }

    public void save(String name, String projectDir, String pagesDir) {
        ProjectEntity entity = new ProjectEntity(name);
        entity.setResourceRoot(projectDir);
        entity.setPagesSubDir(pagesDir);

        try {
            saveProject(entity);
        } catch (IOException e) {
            // todo log failure
            e.printStackTrace();
        }

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
