package de.futjikato.p2d.parser.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.DefaultMustacheVisitor;
import com.github.mustachejava.TemplateContext;
import de.futjikato.p2d.parser.ProjectScanner;

public class DomainMustacheVisitor extends DefaultMustacheVisitor {

    private ProjectScanner projectScanner;

    public DomainMustacheVisitor(DefaultMustacheFactory df, ProjectScanner projectScanner) {
        super(df);

        this.projectScanner = projectScanner;
    }

    @Override
    public void partial(TemplateContext tc, String variable) {
        projectScanner.notifyImport(variable);
    }
}
