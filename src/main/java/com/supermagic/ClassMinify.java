package com.supermagic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassMinify {
    private static List<String> excludeModulePackages = new ArrayList();

    private final String packageName;
    private final String[] args;
    private String PATH;

    public ClassMinify(String packageName, String path, String[] args) {
        this.packageName = packageName;
        this.PATH = path;
        this.args = args;
    }

    public void handle() throws Exception {
        File f = new File(PATH + "/excludeModulePackages");
        if (f.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                excludeModulePackages.add(line);
            }
        }

        Map<String, String> componentMap = new HashMap<>();
        List<File> javaFiles = new ArrayList<File>();
        Utils.iterateForTargetFile(new File(PATH), javaFiles, "glob:**/src/main/java/**/*.java");

        List<File> targetFiles = new ArrayList<File>();
        Utils.iterateForTargetFile(new File(PATH), targetFiles, "glob:**/*.{java,kt,xml,txt,xml,gradle}");

        for (File javaFile : javaFiles) {
            String javaContent;
            if (MinifyController.FileContents.containsKey(javaFile)) {
                javaContent = MinifyController.FileContents.get(javaFile);
            } else {
                javaContent = Utils.readFileString(javaFile);
            }

            //String javaContent = Utils.readFileString(javaFile);
            if (!javaContent.contains(" extends ") && !javaContent.contains(" implements ")) {
                System.out.println("忽略 " + javaFile.getName());
                continue;
            }
            System.out.println("处理java类 " + javaFile.getName());

            if (isExcludedClass(javaFile)) {
                System.out.println("忽略 " + javaFile.getName());
                continue;
            }
            String javaName = javaFile.getName();
            int lastIndex = javaName.lastIndexOf(".");
            String className = javaName.substring(0, lastIndex);
            if (!Utils.isNotUniqueMatch(PATH, className)) {
                System.err.println("重复java类 " + javaFile.getName());
                MinifyController.DUPLICATE.add(javaFile.getName());
                continue;
            }
            String newActivityName = NameUtils.encodeClass(className, packageName, componentMap);

            for (File file : targetFiles) {
                //String content = Utils.readFileString(file);
                if ("strings.xml".equals(file.getName())) {
                    continue;
                }
                String content;
                if (MinifyController.FileContents.containsKey(file)) {
                    content = MinifyController.FileContents.get(file);
                } else {
                    content = Utils.readFileString(file);
                }

                if (!content.contains(className)) {
                    continue;
                }

                content = content.replace(" " + className + " ", " " + newActivityName + " ");
                //public class my extends RecyclerView.my {
                content = content.replace(" " + className + ".", " " + newActivityName + ".");
                content = content.replace(" " + className + "(", " " + newActivityName + "(");
                content = content.replace(" " + className + ")", " " + newActivityName + ")");
                content = content.replace(" " + className + ";", " " + newActivityName + ";");
                content = content.replace(" " + className + "\"", " " + newActivityName + "\"");
                //public abstract class ErrorDialogFragmentFactory<T> {
                content = content.replace(" " + className + "<", " " + newActivityName + "<");

                //xml <com.flash.app.manager.views.CpuNewView
                //          android:width
                content = content.replace("." + className + "\n", "." + newActivityName + "\n");
                //</com.flash.app.manager.locker.locker.PatternView>
                content = content.replace("." + className + ">", "." + newActivityName + ">");
                //mBorderWidth = a.getDimensionPixelSize(kyv.R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
                content = content.replace("." + className + ",", "." + newActivityName + ",");
                //kyv.R.styleable.CircleImageView_border_color,
                content = content.replace("." + className + "_", "." + newActivityName + "_");
                content = content.replace("." + className + " ", "." + newActivityName + " ");
                content = content.replace("." + className + ".", "." + newActivityName + ".");
                //content = content.replace("." + className + "(", "." + newActivityName + "(");//这种是方法名，不是类名
                //TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleFrameLayout);
                content = content.replace("." + className + ")", "." + newActivityName + ")");
                content = content.replace("." + className + ";", "." + newActivityName + ";");
                content = content.replace("." + className + "\"", "." + newActivityName + "\"");

                content = content.replace("," + className + " ", "," + newActivityName + " ");
                content = content.replace("," + className + ".", "," + newActivityName + ".");
                //content = content.replace("." + className + "(", "." + newActivityName + "(");//这种是方法名，不是类名
                //content = content.replace("." + className + ")", " " + newActivityName + ")");
                content = content.replace("," + className + ";", "," + newActivityName + ";");
                content = content.replace("," + className + "\"", "," + newActivityName + "\"");

                //onEvent(Event event)
                content = content.replace("(" + className + " ", "(" + newActivityName + " ");
                //daoConfigMap.get(Share.class)
                content = content.replace("(" + className + ".", "(" + newActivityName + ".");
                //(Event(AbsEvent)
                content = content.replace("(" + className + "(", "(" + newActivityName + "(");
                //((BaseFragment)pagerAdapter.getFragmentByPosition(position))
                content = content.replace("(" + className + ")", "(" + newActivityName + ")");
                content = content.replace("(" + className + ";", "(" + newActivityName + ";");
                content = content.replace("(" + className + "\"", "(" + newActivityName + "\"");

                content = content.replace("<" + className + " ", "<" + newActivityName + " ");
                content = content.replace("<" + className + ".", "<" + newActivityName + ".");
                //private Property<RippleFrameLayout, Float> radiusProperty
                content = content.replace("<" + className + ",", "<" + newActivityName + ",");
                content = content.replace("<" + className + "(", "<" + newActivityName + "(");
                //content = content.replace("<" + className + ")", "<" + newActivityName + ")");
                content = content.replace("<" + className + ";", "<" + newActivityName + ";");
                content = content.replace("<" + className + "\"", "<" + newActivityName + "\"");
                //private final ArrayList<FileDataInfo> mList = new ArrayList<>();
                content = content.replace("<" + className + ">", "<" + newActivityName + ">");

                content = content.replace("{" + className + " ", "{" + newActivityName + " ");
                content = content.replace("{" + className + ".", "{" + newActivityName + ".");
                //content = content.replace("{" + className + "(", "{" + newActivityName + "(");
                //content = content.replace("{" + className + ")", "{" + newActivityName + ")");
                content = content.replace("{" + className + ";", "{" + newActivityName + ";");
                content = content.replace("{" + className + "\"", "{" + newActivityName + "\"");

                content = content.replace("!" + className + " ", "!" + newActivityName + " ");
                content = content.replace("!" + className + ".", "!" + newActivityName + ".");
                //content = content.replace("!" + className + "(", "!" + newActivityName + "(");
                //content = content.replace("!" + className + ")", "!" + newActivityName + ")");
                content = content.replace("!" + className + ";", "!" + newActivityName + ";");
                content = content.replace("!" + className + "\"", "!" + newActivityName + "\"");


                content = content.replace("=" + className + " ", "=" + newActivityName + " ");
                content = content.replace("=" + className + ".", "=" + newActivityName + ".");
                content = content.replace("=" + className + "(", "=" + newActivityName + "(");
                content = content.replace("=" + className + ")", "=" + newActivityName + ")");
                content = content.replace("=" + className + ";", "=" + newActivityName + ";");
                content = content.replace("=" + className + "\"", "=" + newActivityName + "\"");

                content = content.replace(">" + className + " ", ">" + newActivityName + " ");
                content = content.replace(">" + className + ".", ">" + newActivityName + ".");
                content = content.replace(">" + className + "(", ">" + newActivityName + "(");
                content = content.replace(">" + className + ")", ">" + newActivityName + ")");
                content = content.replace(">" + className + ";", ">" + newActivityName + ";");
                content = content.replace(">" + className + "\"", ">" + newActivityName + "\"");

                //<declare-styleable name="CircleImageView">
                content = content.replace("=\"" + className + "\"", "=\"" + newActivityName + "\"");
                //Utils.writeFile(file, content);
                MinifyController.FileContents.put(file, content);
            }
            File newFile = new File(javaFile.getParent(), newActivityName + ".java");
            MinifyController.FileRelations.put(javaFile, newFile);
        }
    }

    private boolean isExcludedClass(File javaFile) {
        String javaPath = javaFile.getAbsolutePath();
        String srcJavaPath = Utils.join(new String[]{"src", "main", "java"}, File.separator);
        int srcJavaIndex = javaPath.indexOf(srcJavaPath);
        String fullJavaPath = javaPath.substring(srcJavaIndex + srcJavaPath.length()).replace(File.separator, ".");
        if (fullJavaPath.length() > 0) {
            fullJavaPath = fullJavaPath.substring(1);
        }
        for (String exclude : excludeModulePackages) {
            if (fullJavaPath.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }
}
