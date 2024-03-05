package com.supermagic;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinifyController {

    public static final Map<File, File> FileRelations = new HashMap<>();
    public static final Map<File, String> FileContents = new HashMap<>();
    public static final List<String> DUPLICATE = new ArrayList<>();

    public static void minify(String packageName, String PATH, String[] args) throws Exception {

        ResourceFileHandler resourceFileHandler = new ResourceFileHandler(packageName, PATH);
        System.out.println("修改dimen文件");
        resourceFileHandler.handleResourceKey("dimen");
        System.out.println("修改styles文件");
        resourceFileHandler.handleResourceKey("style");
//        System.out.println("修改string文件");
//        resourceFileHandler.handleResourceKey("string");
        System.out.println("修改color文件");
        resourceFileHandler.handleResourceKey("color");

//        System.out.println("修改xml文件");
//        resourceFileHandler.handleFileResource("xml");
//        System.out.println("修改layout文件");
//        resourceFileHandler.handleFileResource("layout");
        System.out.println("修改drawable文件");
        resourceFileHandler.handleFileResource("drawable");
        resourceFileHandler.handleFileResource("mipmap");
        System.out.println("修改anim文件");
        resourceFileHandler.handleFileResource("anim");

//        ClassMinify classMinify = new ClassMinify(packageName, PATH, args);
//        classMinify.handle();


        for (Map.Entry<File, File> file : FileRelations.entrySet()) {
            File source = file.getKey();
            File targetFile = file.getValue();
            //boolean resultStatus = javaFile.renameTo(targetFile);
            boolean resultStatus;
            if (FileContents.containsKey(source)) {
                resultStatus = source.delete();
                String content = FileContents.get(source);
                Utils.writeFile(targetFile, content);
                FileContents.remove(source);
            } else {
                resultStatus = source.renameTo(targetFile);
            }
            if (resultStatus) {
                System.out.println(source.getName() + " > " + targetFile.getName() + "   : " + resultStatus);
            } else {
                System.err.println(source.getName() + " > " + targetFile.getName() + "   : " + resultStatus);
            }
        }

        for (Map.Entry<File, String> entry : FileContents.entrySet()) {
            if (!FileRelations.containsKey(entry.getKey())) {
                Utils.writeFile(entry.getKey(), entry.getValue());
            }
        }

        for (String name : DUPLICATE) {
            System.err.println("重复文件:" + name);
        }
    }
}
