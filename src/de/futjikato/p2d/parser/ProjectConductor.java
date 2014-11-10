package de.futjikato.p2d.parser;

import de.futjikato.p2d.detail.TargetItem;
import de.futjikato.p2d.projects.ProjectEntity;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import java.util.concurrent.*;

public class ProjectConductor implements Observer {

    private Runnable onLoadingCompleteCallback;

    private TargetItem targetItem;

    private ExecutorService executorService;

    private Map<String, NodeBuilder> nodeBuilders;

    private Semaphore mutex;

    private PatternlabPathResolver pathResolver;

    private int running = 0;

    public void onLoadingComplete(Runnable callback) {
        onLoadingCompleteCallback = callback;
        executorService = Executors.newCachedThreadPool();
    }

    public void runProject(ProjectEntity project) {
        targetItem = new TargetItem("root");
        nodeBuilders = new ConcurrentHashMap<String, NodeBuilder>();
        mutex = new Semaphore(1);

        File resourceRoot = new File(project.getResourceRoot());
        File pagesDir = new File(project.getPagesSubDir());

        pathResolver = new PatternlabPathResolver();
        pathResolver.setResourceRoot(resourceRoot);

        if(pagesDir.exists() && pagesDir.isDirectory()) {

            String[] mustacheFiles = pagesDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(".mustache");
                }
            });

            for(String startFile : mustacheFiles) {
                String fullPath = String.format("%s/%s", pagesDir.getAbsolutePath(), startFile);

                NodeBuilder builder = new NodeBuilder(startFile);
                nodeBuilders.put(startFile, builder);
                DomainNode node = builder.createTreeItem();
                node.setType(DomainNode.AtomicType.PAGE);
                targetItem.getChildren().add(node);
                ProjectScanner scanner = new ProjectScanner(startFile, fullPath, node);
                scanner.addObserver(this);

                executorService.execute(scanner);

                running++;
            }
        }
    }

    public TreeItem<String> getTargetTree() {
        return targetItem;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof ProjectScanner && arg instanceof ProjectScanner.ScannerNotification) {
            ProjectScanner parentScanner = (ProjectScanner) o;
            ProjectScanner.ScannerNotification notification = (ProjectScanner.ScannerNotification) arg;
            handleScannerNotification(parentScanner, notification);
        }
    }

    private void handleScannerNotification(ProjectScanner scanner, ProjectScanner.ScannerNotification notification) {
        NodeBuilder builder = nodeBuilders.get(notification.getId());
        if(builder == null) {
            System.err.println("Critical error! " + notification.getId() + " has no node builder?!");
            return;
        }
        switch(notification.getName()) {
            case IMPORT:
                String[] arguments = notification.getArguments();
                if(arguments.length == 1 && arguments[0] != null) {
                    try {
                        mutex.acquire();
                        if (!nodeBuilders.containsKey(arguments[0])) {
                            NodeBuilder newBuilder = new NodeBuilder(arguments[0]);
                            nodeBuilders.put(arguments[0], newBuilder);
                            String fullPath = pathResolver.resolve(arguments[0]);
                            if (fullPath != null) {
                                DomainNode node = newBuilder.createTreeItem();
                                builder.addChild(node);

                                ProjectScanner newScanner = new ProjectScanner(arguments[0], fullPath, node);
                                newScanner.addObserver(this);
                                executorService.execute(newScanner);

                                running++;
                            } else {
                                System.err.println("Unable to fetch file for " + arguments[0]);
                            }
                        } else {
                            NodeBuilder newBuilder = nodeBuilders.get(arguments[0]);
                            DomainNode node = newBuilder.createTreeItem();
                            builder.addChild(node);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                         mutex.release();
                    }
                }
                break;

            case END:
                if(--running == 0) {
                    executorService.shutdown();
                    if(onLoadingCompleteCallback != null) {
                        finalizeBuilders();
                        onLoadingCompleteCallback.run();
                    } else {
                        System.err.println("No finish callback in conductor ?!");
                    }
                }
                break;
        }
    }

    private void finalizeBuilders() {
        for(NodeBuilder builder : nodeBuilders.values()) {
            builder.finalizeBuilder();
        }
    }
}
