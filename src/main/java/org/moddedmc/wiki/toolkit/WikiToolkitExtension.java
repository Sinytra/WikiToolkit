package org.moddedmc.wiki.toolkit;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.moddedmc.wiki.toolkit.docs.DocumentationRoot;
import org.moddedmc.wiki.toolkit.dsl.Origin;

import javax.inject.Inject;

public abstract class WikiToolkitExtension {
    private final NamedDomainObjectContainer<DocumentationRoot> docs;
    private final Origin origin;

    @Inject
    public WikiToolkitExtension(Project project) {
        this.docs = project.container(DocumentationRoot.class);
        this.origin = project.getObjects().newInstance(Origin.class);
    }

    public Origin getOrigin() {
        return this.origin;
    }

    public void origin(Action<Origin> action) {
        action.execute(this.origin);
    }

    public abstract Property<String> getWikiAccessToken();

    public NamedDomainObjectContainer<DocumentationRoot> getDocs() {
        return this.docs;
    }

    public void docs(Action<NamedDomainObjectContainer<DocumentationRoot>> action) {
        action.execute(this.docs);
    }
}
