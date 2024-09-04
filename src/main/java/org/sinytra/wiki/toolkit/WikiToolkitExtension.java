package org.sinytra.wiki.toolkit;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.sinytra.wiki.toolkit.docs.DocumentationRoot;

import javax.inject.Inject;

public abstract class WikiToolkitExtension {
    private final NamedDomainObjectContainer<DocumentationRoot> docs;

    @Inject
    public WikiToolkitExtension(Project project) {
        this.docs = project.container(DocumentationRoot.class);
    }

    public abstract Property<String> getWikiRepositoryUrl();

    public abstract Property<String> getWikiAccessToken();

    public NamedDomainObjectContainer<DocumentationRoot> getDocs() {
        return this.docs;
    }

    public void docs(Action<NamedDomainObjectContainer<DocumentationRoot>> action) {
        action.execute(this.docs);
    }
}
