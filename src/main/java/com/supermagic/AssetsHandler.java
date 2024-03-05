package com.supermagic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssetsHandler {
    private final String packageName;
    private String ROOT;
    private ArrayList<File> targetFiles;
    private HashMap<String, String> map;
    private List<String> preloadList = new ArrayList<>();

    public AssetsHandler(String path, String packageName) {
        this.ROOT = path;
        this.packageName = packageName;
    }

    public void handle(String[] args) {
        if (args != null && args.length > 1) {
            System.out.println("原项目包名:" + args[1]);
        }
        File root = new File(ROOT);
        targetFiles = new ArrayList<File>();
        Utils.iterateForTargetFile(root, targetFiles, Factory.PATH_MATTHER_ALL);
        map = new HashMap<>();

        List<File> jsonList = new ArrayList<File>();
        Utils.iterateForTargetFile(new File(ROOT), jsonList, "glob:**/src/main/assets/**.json");
        for (File file : jsonList) {
            File it = file;
            List<String> sourceList = new ArrayList<>();
            sourceList.add(file.getName());
            while (true) {
                String parentName = it.getParentFile().getName();
                if ("assets".equals(parentName)) {
                    break;
                }
                sourceList.add(0, parentName);
                it = it.getParentFile();
            }
            String sourcePath = Utils.join(sourceList, File.separator);
            System.out.println("preload : " + sourcePath);
            preloadList.add(sourcePath);
        }

        for (File file : jsonList) {
            System.out.println("处理 asset 文件:" + file.getAbsolutePath());
            handleJSONFile(file, args);
        }
    }

    private void handleJSONFile(File file, String[] args) {
        String assetContent = Utils.readFileString(file);
        String output;
        if (args != null && args.length > 1) {
            assetContent = Utils.decodeByPackageName(args[1], assetContent);
        }
        output = Utils.encodeByPackageName(packageName, assetContent);

        Utils.writeFile(file, output);
        String name = file.getName();
        String suffix = "";
        if (name.contains(".")) {
            int i = name.lastIndexOf(".");
            suffix = name.substring(i);
        }

        List<String> sourceList = new ArrayList<>();
        List<String> targetList = new ArrayList<>();
        sourceList.add(file.getName());

        File it = file;
        while (true) {
            String parentName = it.getParentFile().getName();
            if ("assets".equals(parentName)) {
                break;
            }
            sourceList.add(0, parentName);
            targetList.add(0, parentName);
            it = it.getParentFile();
        }

        String sourcePath = Utils.join(sourceList, File.separator);

        String newName = NameUtils.encodeAsset(sourcePath, packageName, map, preloadList,suffix);
        targetList.add(newName);
        String targetPath = Utils.join(targetList, File.separator);
        System.out.println("asset 路径: " + sourcePath + " -> " + targetPath);

        file.renameTo(new File(file.getParentFile(), newName));

        for (File target : targetFiles) {
            String content = Utils.readFileString(target);
            if (content.contains(sourcePath)) {
                content = content.replace("\"" + sourcePath + "\"", "\"" + targetPath + "\"");
                Utils.writeFile(target, content);
            }
        }
    }


    public static void main(String[] args) {
        String s = Utils.decodeByPackageName("com.refreshing.stopwatch.timer", "");
        System.out.println(s);
    }
}
