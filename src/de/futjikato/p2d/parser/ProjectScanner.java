package de.futjikato.p2d.parser;

import com.github.mustachejava.MustacheFactory;
import de.futjikato.p2d.parser.mustache.DomainMustachefactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Observable;

public class ProjectScanner extends Observable implements Runnable {

    public enum ScannerNotificationName {
        IMPORT,
        END
    }

    public class ScannerNotification {
        private ScannerNotificationName name;
        private String id;
        private String[] arguments;

        public ScannerNotification(String id, ScannerNotificationName name) {
            this.name = name;
            this.id = id;
        }

        public ScannerNotificationName getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String[] getArguments() {
            return arguments;
        }

        public void setArguments(String[] arguments) {
            this.arguments = arguments;
        }
    }

    private String templateFilePath;

    private String id;

    public ProjectScanner(String id, String templateFile) {
        this.templateFilePath = templateFile;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            MustacheFactory mf = new DomainMustachefactory(this);
            mf.compile(new FileReader(templateFilePath), id);

            ScannerNotification notification = new ScannerNotification(id, ScannerNotificationName.END);
            notification.setArguments(new String[] {templateFilePath});
            setChanged();
            notifyObservers(notification);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void notifyImport(String variable) {
        ScannerNotification notification = new ScannerNotification(id, ScannerNotificationName.IMPORT);
        notification.setArguments(new String[] {variable});

        setChanged();
        notifyObservers(notification);
    }
}
