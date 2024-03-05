package com.supermagic;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceReceiverHandler {
    private final String packageName;
    private String PATH;

    public ActivityServiceReceiverHandler(String packageName, String path) {
        this.packageName = packageName;
        this.PATH = path;
    }

    public void handle() throws Exception {
        Map<String, String> componentMap = new HashMap<>();
        List<File> manifests = new ArrayList<File>();
        Utils.iterateForTargetFile(new File(PATH), manifests, "glob:**/src/main/AndroidManifest.xml");
        for (File manifest : manifests) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(manifest);

            NodeList activitys = document.getElementsByTagName("activity");
            int size = activitys.getLength();
            for (int i = 0; i < size; i++) {
                Node node = activitys.item(i);
                String key = node.getAttributes().getNamedItem("android:name").getNodeValue();
                if (key.startsWith("com.facebook") || key.startsWith("com.google")) {
                    continue;
                }
                int lastIndexOf = key.lastIndexOf(".");
                String activityName = key.substring(lastIndexOf + 1);
                if (!Utils.isNotUniqueMatch(PATH, activityName)) {
                    continue;
                }
                if (activityName.contains("$")) {
                    continue;
                }
                String newActivityName = NameUtils.encodeClass(activityName, packageName, componentMap);
                System.out.println(activityName + " > " + newActivityName);
                List<File> targetFiles = new ArrayList<File>();
                Utils.iterateForTargetFile(new File(PATH), targetFiles, "glob:**/*.{java,kt,xml,pro,txt,xml,gradle}");

                for (File file : targetFiles) {
                    String content = Utils.readFileString(file);
                    content = content.replace(activityName + " ", newActivityName + " ");
                    content = content.replace(activityName + ".", newActivityName + ".");
                    content = content.replace(activityName + "(", newActivityName + "(");
                    content = content.replace(activityName + ";", newActivityName + ";");
                    content = content.replace(activityName + "\"", newActivityName + "\"");
                    Utils.writeFile(file, content);
                }

                List<File> javas = new ArrayList<File>();
                Utils.iterateForTargetFile(new File(PATH), javas, "glob:**/java/**/" + activityName + ".java");

                for (File java : javas) {
                    java.renameTo(new File(java.getParent(), newActivityName + ".java"));
                }
            }

            NodeList receivers = document.getElementsByTagName("receiver");
            for (int i = 0; i < receivers.getLength(); i++) {
                Node node = receivers.item(i);
                String key = node.getAttributes().getNamedItem("android:name").getNodeValue();
                if (key.startsWith("com.facebook") || key.startsWith("com.google")) {
                    continue;
                }
                int lastIndexOf = key.lastIndexOf(".");
                String activityName = key.substring(lastIndexOf + 1);
                if (activityName.contains("$")) {
                    continue;
                }

                String newActivityName = NameUtils.encodeClass(activityName, packageName, componentMap);
                System.out.println("处理receiver类:" + activityName + " > " + newActivityName);
                List<File> targetFiles = new ArrayList<File>();
                Utils.iterateForTargetFile(new File(PATH), targetFiles, "glob:**/*.{java,kt,xml,pro,txt,xml,gradle}");

                for (File file : targetFiles) {
                    String content = Utils.readFileString(file);
                    content = content.replace(activityName + " ", newActivityName + " ");
                    content = content.replace(activityName + ".", newActivityName + ".");
                    content = content.replace(activityName + "(", newActivityName + "(");
                    content = content.replace(activityName + ";", newActivityName + ";");
                    content = content.replace(activityName + "\"", newActivityName + "\"");
                    Utils.writeFile(file, content);
                }

                List<File> javas = new ArrayList<File>();
                Utils.iterateForTargetFile(new File(PATH), javas, "glob:**/java/**/" + activityName + ".java");

                for (File java : javas) {
                    java.renameTo(new File(java.getParent(), newActivityName + ".java"));
                }
            }

            NodeList services = document.getElementsByTagName("service");
            for (int i = 0; i < services.getLength(); i++) {
                Node node = services.item(i);
                String key = node.getAttributes().getNamedItem("android:name").getNodeValue();
                if (key.startsWith("com.facebook") || key.startsWith("com.google")) {
                    continue;
                }
                int lastIndexOf = key.lastIndexOf(".");
                String activityName = key.substring(lastIndexOf + 1);
                if (activityName.contains("$")) {
                    continue;
                }
                String newActivityName = NameUtils.encodeClass(activityName, packageName, componentMap);
                System.out.println("处理Service类:" + activityName + " > " + newActivityName);
                List<File> targetFiles = new ArrayList<File>();
                Utils.iterateForTargetFile(new File(PATH), targetFiles, "glob:**/*.{java,kt,xml,pro,txt,xml,gradle}");

                for (File file : targetFiles) {
                    String content = Utils.readFileString(file);
                    content = content.replace(activityName + " ", newActivityName + " ");
                    content = content.replace(activityName + ".", newActivityName + ".");
                    content = content.replace(activityName + "(", newActivityName + "(");
                    content = content.replace(activityName + ";", newActivityName + ";");
                    content = content.replace(activityName + "\"", newActivityName + "\"");
                    Utils.writeFile(file, content);
                }

                List<File> javas = new ArrayList<File>();
                Utils.iterateForTargetFile(new File(PATH), javas, "glob:**/java/**/" + activityName + ".java");

                for (File java : javas) {
                    java.renameTo(new File(java.getParent(), newActivityName + ".java"));
                }
            }
        }
    }

}
