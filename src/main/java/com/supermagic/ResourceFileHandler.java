package com.supermagic;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceFileHandler {

    private final ArrayList<File> targetFiles;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private String packageName;
    private String PATH;


    public ResourceFileHandler(String packageName, String path) {
        this.packageName = packageName;
        this.PATH = path;
        targetFiles = new ArrayList<File>();
        File root = new File(PATH);
        Utils.iterateForTargetFile(root, targetFiles, Factory.PATH_MATTHER_ALL);
    }

    public void handleResourceKey(String key) throws Exception {
        File root = new File(PATH);
        Map<String, String> map = new HashMap<>();
        List<File> stringFiles = new ArrayList<File>();
        //Utils.iterateForTargetFile(root, stringFiles, "glob:**/values/*.xml");
        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/main/res/values*/*.xml");
        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/gotubi/res/values*/*.xml");
        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/luckypanda/res/values*/*.xml");
        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/tikplay/res/values*/*.xml");
        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/cashbird/res/values*/*.xml");
        //Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/main/res/values-*/*.xml");
        List<String> existKeys = new ArrayList<>();
        retrieveKeys(key, stringFiles, existKeys);

        List<String> singleStringKey = replaceResourceValues(key, stringFiles, key12 -> NameUtils.encodeResource(key12, packageName, map, existKeys));

        replaceRelatedValue(singleStringKey, key, key1 -> NameUtils.encodeResource(key1, packageName, map, existKeys));
    }

    private void retrieveKeys(String string, List<File> stringFiles, List<String> existKeys) throws ParserConfigurationException, IOException, SAXException {
        for (File file : stringFiles) {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodes = document.getElementsByTagName(string);
            int size = nodes.getLength();
            for (int index = 0; index < size; index++) {
                Node node = nodes.item(index);
                String key = node.getAttributes().getNamedItem("name").getNodeValue();
                existKeys.add(key);
            }
        }
    }

    public void handleFileResource(String resource) {
        Map<String, String> map = new HashMap<>();
        File root = new File(PATH);
        List<File> stringFiles = new ArrayList<File>();
//        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/main/res/" + resource + "*/*.{xml,png,jpg,webp}");
//        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/gotubi/res/" + resource + "*/*.{xml,png,jpg,webp}");
//        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/luckypanda/res/" + resource + "*/*.{xml,png,jpg,webp}");
//        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/tikplay/res/" + resource + "*/*.{xml,png,jpg,webp}");
//        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/cashbird/res/" + resource + "*/*.{xml,png,jpg,webp}");
//        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/tikshorts/res/" + resource + "*/*.{xml,png,jpg,webp}");
        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/*/res/" + resource + "*/*.{xml,png,jpg,webp}");
        Utils.iterateForTargetFile(root, stringFiles, "glob:**/src/*/res/" + resource + "-*/*.{xml,png,jpg,webp}");


        for (File file : stringFiles) {
            String key = file.getName();
            int lastIndexOf = key.indexOf(".");
            String resourceName = key.substring(0, lastIndexOf);

            String newResourceName = NameUtils.encodeResource(resourceName, packageName, map, null);

            System.out.println(resourceName + " > " + newResourceName);

            //file.renameTo(new File(file.getParentFile(), newResourceName + key.substring(lastIndexOf)));
            File newFile = new File(file.getParentFile(), newResourceName + key.substring(lastIndexOf));
            MinifyController.FileRelations.put(file, newFile);


            for (File target : targetFiles) {
                String content;
                if (MinifyController.FileContents.containsKey(target)) {
                    content = MinifyController.FileContents.get(target);
                } else {
                    content = Utils.readFileString(target);
                }


                if (!Utils.isEmpty(content) && content.contains(resourceName)) {
                    try {
                        //content = content.replace(String.format("R.%s.%s", resource, resourceName), String.format("R.%s.%s", resource, newResourceName));
                        content = replaceContent(content, resource, resourceName, newResourceName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Utils.writeFile(target, content);
                    MinifyController.FileContents.put(target, content);
                }
            }
        }
    }

    private List<String> replaceResourceValues(String string, List<File> stringFiles, Callable callable)
            throws Exception {
        List<String> singleStringKey = new ArrayList<String>();
        for (File file : stringFiles) {
            String content;
            if (MinifyController.FileContents.containsKey(file)) {
                content = MinifyController.FileContents.get(file);
            } else {
                content = Utils.readFileString(file);
            }

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodes = document.getElementsByTagName(string);
            int size = nodes.getLength();
            for (int index = 0; index < size; index++) {
                Node node = nodes.item(index);
                String key = node.getAttributes().getNamedItem("name").getNodeValue();
                if ("style".equals(string)) {
                    if (key.contains(".")) {
                        System.err.println("style 如果含有子类就不处理 : " + key);
                        continue;
                    }
                }
                if (!singleStringKey.contains(key)) {
                    singleStringKey.add(key);
                }
                System.out.println(string + "  " + key + "  > " + callable.getReplaceValue(key));
                content = content.replace("\"" + key + "\"", "\"" + callable.getReplaceValue(key) + "\"");
                if ("style".equals(string)) {
                    content = content.replace("\"" + key + ".", "\"" + callable.getReplaceValue(key) + ".");
                }
            }
            //Utils.writeFile(file, content);
            MinifyController.FileContents.put(file, content);
        }
        return singleStringKey;
    }


    private void replaceRelatedValue(List<String> singleDrawableKeys, String string, Callable callable)
            throws Exception {
        File root = new File(PATH);
        for (File file : targetFiles) {
            String content;
            if (MinifyController.FileContents.containsKey(file)) {
                content = MinifyController.FileContents.get(file);
            } else {
                content = Utils.readFileString(file);
            }

            if (!Utils.isEmpty(content)) {
                for (String key : singleDrawableKeys) {
                    try {
                        if (content.contains(key)) {
                            //content = content.replace("android.R." + string + "." + key, "R." + string + "." + callable.getReplaceValue(key));
                            content = replaceContent(content, string, key, callable.getReplaceValue(key));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("@" + string + "/" + key + "  -> " + "@" + string + "/" + callable.getReplaceValue(key));
                    }
                }

                //Utils.writeFile(file, content);
                MinifyController.FileContents.put(file, content);
            }
        }
    }

    private String replaceContent(String content, String string, String key, String replaceValue) {
        content = content.replace("R." + string + "." + key + ".", "R." + string + "." + replaceValue + ".");
        content = content.replace("R." + string + "." + key + "\n", "R." + string + "." + replaceValue + "\n");
        content = content.replace("R." + string + "." + key + "}", "R." + string + "." + replaceValue + "}");

        content = content.replace("R." + string + "." + key + ")", "R." + string + "." + replaceValue + ")");
        content = content.replace("R." + string + "." + key + ";", "R." + string + "." + replaceValue + ";");
        content = content.replace("R." + string + "." + key + "*", "R." + string + "." + replaceValue + "*");
        content = content.replace("R." + string + "." + key + ",", "R." + string + "." + replaceValue + ",");
        content = content.replace("R." + string + "." + key + "<", "R." + string + "." + replaceValue + "<");
        content = content.replace("R." + string + "." + key + " ", "R." + string + "." + replaceValue + " ");
        content = content.replace("@" + string + "/" + key + "\"", "@" + string + "/" + replaceValue + "\"");
        content = content.replace("@" + string + "/" + key + "\n", "@" + string + "/" + replaceValue + "\n");
        content = content.replace("@" + string + "/" + key + "<", "@" + string + "/" + replaceValue + "<");
        if ("style".equals(string)) {
            content = content.replace("@style/" + key + ".", "@style/" + replaceValue + ".");
        }
        content = content.replace(String.format("<%s name=\"%s\">", string, key), String.format("<%s name=\"%s\">", string, replaceValue));
        return content;
    }


    interface Callable {
        String getReplaceValue(String key);
    }

}
