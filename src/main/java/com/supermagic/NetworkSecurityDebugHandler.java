package com.supermagic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.utils.FileUtil;

public class NetworkSecurityDebugHandler {

    private String PATH;

    public NetworkSecurityDebugHandler(String path) {
        this.PATH = path;
    }

    public void handleNetwork() {
        List<File> networks = new ArrayList<File>();
        Utils.iterateForTargetFile(new File(PATH), networks, "glob:**/src/main/res/**/xml/*.xml");
        for (File file : networks) {
            if (file.getName().equals("network-debug")) {
                continue;
            }
            String readFileString = Utils.readFileString(file);
            if (readFileString.contains("network-security-config")) {
                readFileString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                        "<network-security-config>\r\n" +
                        "    <base-config cleartextTrafficPermitted=\"true\" />\r\n" +
                        "</network-security-config>";
                Utils.writeFile(file, readFileString);
            }
        }
    }

    public void handleDebug() {
        List<File> networks = new ArrayList<File>();
        Utils.iterateForTargetFile(new File(PATH), networks, "glob:**/src/main/java/**/DebugUtil.java");
        for (File file : networks) {
            String string = Utils.readFileString(file);
            string = string.replace("true", "false");
            Utils.writeFile(file, string);
        }
    }
}
