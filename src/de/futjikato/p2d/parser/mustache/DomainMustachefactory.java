package de.futjikato.p2d.parser.mustache;

import com.github.mustachejava.DeferringMustacheFactory;
import com.github.mustachejava.MustacheVisitor;
import de.futjikato.p2d.parser.ProjectScanner;

public class DomainMustachefactory extends DeferringMustacheFactory {

    private ProjectScanner projectScanner;

    public DomainMustachefactory(ProjectScanner projectScanner) {
        this.projectScanner = projectScanner;
    }

    @Override
    public MustacheVisitor createMustacheVisitor() {
        return new DomainMustacheVisitor(this, projectScanner);
    }
}
