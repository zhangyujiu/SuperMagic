package test;


import com.supermagic.RandomString;
import com.supermagic.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerateClazz {

    public static void main(String[] args) throws IOException {

        String x = "package k;\n" +
                "\n" +
                "public class %s extends h {\n" +
                "\n" +
                "    @Override // h5.d\n" +
                "    public final void e() {\n" +
                "    }\n" +
                "}\n";
        String y = "<activity android:theme=\"@style/txvy\" android:label=\" \" android:icon=\"@drawable/iycs\" android:name=\"k.%s\" android:taskAffinity=\":xyz\" android:excludeFromRecents=\"true\" android:screenOrientation=\"portrait\"/>";

        String root = "/Users/rockie/android-work/PHONE_CLEAN/app/src/main/java/k/";
        File ro = new File(root);
        List<String> ll = new ArrayList<>();
        for (int i = 0; i < 200; ) {
            String clazz = RandomString.randomeString(2);
            if (!ll.contains(clazz)) {
                System.out.println(clazz);
                ll.add(clazz);
                i++;
            } else {
                continue;
            }
            File file = new File(ro, clazz + ".java");
            file.createNewFile();
            Utils.writeFile(file, String.format(x, clazz));
        }
        System.out.println(ll.size());

        for (String name : ll) {
            String format = String.format(y, name);
            System.out.println(format);
        }
    }
}
