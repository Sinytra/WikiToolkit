package org.sinytra.toolkit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class MavenUtil {
    public static final String MAVEN_URL = "https://maven.su5ed.dev/releases/";
    public static final String ITEM_ASSET_EXPORTER_GROUP = "org.sinytra";
    public static final String ITEM_ASSET_EXPORTER_NAME = "item-asset-export-neoforge";

    public static List<String> getAvailableModVersions() throws Throwable {
        String url = MAVEN_URL + ITEM_ASSET_EXPORTER_GROUP.replace('.', '/') + "/" + ITEM_ASSET_EXPORTER_NAME + "/maven-metadata.xml";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());

        doc.getDocumentElement().normalize();

        NodeList versionsList = doc.getElementsByTagName("versions");

        List<String> versions = new ArrayList<>();

        if (versionsList.getLength() > 0) {
            Element versionsElement = (Element) versionsList.item(0);

            NodeList versionNodes = versionsElement.getElementsByTagName("version");

            for (int i = 0; i < versionNodes.getLength(); i++) {
                String version = versionNodes.item(i).getTextContent();
                versions.add(version);
            }
        }

        return versions;
    }
}
