package org.moddedmc.wiki.toolkit.dsl;

import org.gradle.api.provider.Property;

public abstract class Origin {
    public abstract Property<String> getRepositoryUrl();

    public abstract Property<String> getBranch();
}
