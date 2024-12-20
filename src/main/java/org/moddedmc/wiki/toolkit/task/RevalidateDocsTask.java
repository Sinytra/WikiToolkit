package org.moddedmc.wiki.toolkit.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public abstract class RevalidateDocsTask extends DefaultTask {
    public static final String API_PATH = "api/docs/%s/invalidate";

    @Input
    public abstract Property<URI> getTargetURI();

    @Input
    public abstract Property<String> getProjectId();
    
    @Input
    public abstract Property<String> getAccessToken();

    @TaskAction
    public void run() {
        URI base = getTargetURI().get();
        String projectId = getProjectId().get();
        URI uri = base.resolve(API_PATH.formatted(projectId));
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
            .version(HttpClient.Version.HTTP_1_1)
            .timeout(Duration.ofSeconds(5))
            .POST(HttpRequest.BodyPublishers.noBody());
        if (getAccessToken().isPresent()) {
            builder.header("Authorization", getAccessToken().get());
        }
        HttpRequest request = builder.build();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Response returned code " + response.statusCode() + ", body: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error executing revalidate HTTP request", e);
        }
    }
}
