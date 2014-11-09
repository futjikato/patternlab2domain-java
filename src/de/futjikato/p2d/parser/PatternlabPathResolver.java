package de.futjikato.p2d.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternlabPathResolver {

    private static class PatternlabVisitor extends SimpleFileVisitor<Path> {

        private Map<String, File> map;

        private Pattern pattern;

        protected PatternlabVisitor() {
            super();
            map = new HashMap<String, File>();
            pattern = Pattern.compile("([0-9]+)-(.*?)\\.mustache");
        }

        public Map<String,File> getMap() {
            return map;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            File rFile = file.toFile();
            Matcher m = pattern.matcher(rFile.getName());
            if(m.matches()) {
                map.put(m.group(2), rFile);
            }
            return super.visitFile(file, attrs);
        }
    }

    public static final String ATOM_PREFIX = "atoms-";
    public static final String ORGANISM_PREFIX = "organisms-";
    public static final String MOLECULE_PREFIX = "molecules-";

    private File resourceRoot;

    private Map<String, Map<String, File>> resourceMapping = new HashMap<String, Map<String, File>>();

    public String resolve(String name) {
        try {
            if (name.substring(0, ATOM_PREFIX.length()).equals(ATOM_PREFIX)) {
                return getFromPatternDir("00-atoms", name.substring(ATOM_PREFIX.length())).getAbsolutePath();
            } else if (name.substring(0, ORGANISM_PREFIX.length()).equals(ORGANISM_PREFIX)) {
                return getFromPatternDir("02-organisms", name.substring(ORGANISM_PREFIX.length())).getAbsolutePath();
            } else if (name.substring(0, MOLECULE_PREFIX.length()).equals(MOLECULE_PREFIX)) {
                return getFromPatternDir("01-molecules", name.substring(MOLECULE_PREFIX.length())).getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setResourceRoot(File resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

    private File getFromPatternDir(String patternDir, String name) throws IOException {
        if(!resourceMapping.containsKey(patternDir)) {
            buildPatternMap(patternDir);
        }

        if(!resourceMapping.get(patternDir).containsKey(name)) {
            return null;
        }

        return resourceMapping.get(patternDir).get(name);
    }

    private void buildPatternMap(String patternDir) throws IOException {
        String patternRootPath = String.format("%s/%s/%s", resourceRoot.getAbsolutePath(), "source/_patterns", patternDir);
        File patternRoot = new File(patternRootPath);

        PatternlabVisitor visitor = new PatternlabVisitor();
        Files.walkFileTree(patternRoot.toPath(), visitor);

        resourceMapping.put(patternDir, visitor.getMap());
    }
}
