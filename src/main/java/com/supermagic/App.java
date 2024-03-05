package com.supermagic;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static String PATH = null;
    static String source = "";


    public static void main(String[] args) throws Throwable {
       // MinifyController.minify("com.big.junk.remove", "/Users/rockie/android-work/PHONE_CLEAN/", args);

        File root = new File("./");
        System.out.println(root.getAbsolutePath() + "  " + new File(root.getAbsolutePath()).isDirectory());
        root = new File(root.getAbsolutePath());
        PATH = root.getAbsolutePath();
        if (!new File(PATH, "build.gradle").exists() || !new File(PATH, "gradle.properties").exists()) {
            System.err.println("jar包放置位置有误,必须放在项目的根目录位置!");
            return;
        }
        List<File> javaFiles = new ArrayList<File>();
        Utils.iterateForTargetFile(root, javaFiles, "glob:**/app/src/main/AndroidManifest.xml");
        for (File file : javaFiles) {
            decodePackageName(file);
        }

        if (args.length > 0) {
            String command = args[0];
            if ("-minify".equals(command)) {
                long start = System.currentTimeMillis();
                MinifyController.minify(source, PATH, args);
                System.out.println("耗时:" + (System.currentTimeMillis() - start) / 1000 + "  秒");
            } else if ("-asset".equals(command)) {
                AssetsHandler assetsHandler = new AssetsHandler(PATH, source);
                assetsHandler.handle(args);
            }
        }

    }

    private static void decodePackageName(File file) throws Exception {
        System.out.println("AndroidManifest >>>" + file.getAbsolutePath());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        String value = document.getDocumentElement().getAttribute("package");
        source = value;
        System.out.println("package name >>>" + source);
    }
}
