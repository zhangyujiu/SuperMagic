package com.supermagic;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static final String xxx = "abcdefghijklmnopgrstuvwxyz";
    public static final String yyy = "123456789abcdefghi123456789jklmnopgrstuvwxyz123456789";
    public static List<String> keywords = Arrays.asList("abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "strictfp", "short", "static", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "xml", "layout", "values", "drawable");

    public static boolean isNotUniqueMatch(String PATH, String reg) {
        List<File> matchs = new ArrayList<File>();
        Utils.iterateForTargetFile(new File(PATH), matchs, "glob:**/src/main/java/**/" + reg + ".java");
        if (matchs.size() > 1) {
            for (File file : matchs) {
                String srcJavaPath = Utils.join(new String[]{"src", "main", "java"}, File.separator);
                String javaPath = file.getAbsolutePath();
                int srcJavaIndex = javaPath.indexOf(srcJavaPath);
                String fullJavaPath = javaPath.substring(srcJavaIndex + srcJavaPath.length()).replace(File.separator, ".");
                System.out.println(fullJavaPath);
            }
        }
        return matchs.size() == 1;
    }

    public static String join(String[] pac, String seprator) {
        String data = "";
        for (String string : pac) {
            data += seprator;
            data += string;
        }
        return data.substring(seprator.length());
    }

    public static String join(List<String> pac, String seprator) {
        String data = "";
        for (String string : pac) {
            data += seprator;
            data += string;
        }
        return data.substring(seprator.length());
    }

    public static String randomConstant() {
        int x = randomNumber(15) + 10;
        String xx = "";
        for (int i = 0; i < x; i++) {
            xx += yyy.charAt(randomNumber(yyy.length()));
        }
        return xx;
    }


    public static String getDrawableName(String name) {
        int index = name.indexOf(".");
        return name.substring(0, index);
    }

    public static String randomVariant(int size) {
        int x = randomNumber(2) + size;
        String xx = "";
        for (int i = 0; i < x; i++) {
            xx += xxx.charAt(randomNumber(xxx.length()));
        }
        if (keywords.contains(xx)) {
            return randomVariant(size);
        }
        return xx;
    }

    public static String randomVariant() {
        int x = randomNumber(3) + 3;
        String xx = "";
        for (int i = 0; i < x; i++) {
            xx += xxx.charAt(randomNumber(xxx.length()));
        }
        if (keywords.contains(xx)) {
            return randomVariant();
        }
        return xx;
    }

    public static int randomNumber(int value) {
        return ((int) (Math.random() * 1000000)) % value;
    }

    public static boolean isRandomHit(int value) {
        return (int) (Math.random() * 100) < value;
    }

    public static void iterateForTargetFile(final File root, final List<File> list, final String... pathExpressList) {
        if (root == null) {
            return;
        }
        root.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    if (file.getAbsolutePath().contains("build\\generated")
                            || file.getAbsolutePath().contains("build\\intermediates")
                            || file.getAbsolutePath().contains(".git")
                            || file.getAbsolutePath().contains("selector\\src"))
                    {
                        return false;
                    }
                    iterateForTargetFile(file, list, pathExpressList);
                } else {
                    if (file.getName().contains(".so") || file.getName().contains(".aar")
                            || file.getName().contains(".jar") || file.getName().contains(".jar")) {
                        return false;
                    }
                    Path path = Paths.get(file.getAbsolutePath());
                    for (String pathExpress : pathExpressList) {
                        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pathExpress);
                        if (matcher.matches(path)) {
                            list.add(file);
                        }
                    }
                }
                return false;
            }
        });
    }

    public static String readFileString(File file) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
            return new String(input, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized void writeFile(File file, String content) {
        OutputStreamWriter fileWriter;
        try {
            fileWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(String content) {
        return content == null || content.contentEquals("");
    }

    public static String randomString(String activityName) {
        int length = activityName.length();
        if (length > 4) {
            char[] charArray = activityName.toCharArray();
            int index = randomNumber(length);
            charArray[index] = xxx.charAt(randomNumber(xxx.length()));
            index = randomNumber(length);
            charArray[index] = xxx.charAt(randomNumber(xxx.length()));
            index = randomNumber(length);
            charArray[index] = xxx.charAt(randomNumber(xxx.length()));

            if (length > 10) {
                index = randomNumber(length);
                charArray[index] = xxx.charAt(randomNumber(xxx.length()));
            }

            if (length > 15) {
                index = randomNumber(length);
                charArray[index] = xxx.charAt(randomNumber(xxx.length()));
            }
            if (length > 20) {
                index = randomNumber(length);
                charArray[index] = xxx.charAt(randomNumber(xxx.length()));
            }
            if (length > 30) {
                index = randomNumber(length);
                charArray[index] = xxx.charAt(randomNumber(xxx.length()));
            }
            if (keywords.contains(new String(charArray))) {
                return randomString(activityName);
            }
            return new String(charArray);
        }
        return activityName;
    }

    public static String encodeByPackageName(String packageName, String vv) {
        byte[] bytes = vv.getBytes();
        byte[] xx = packageName.getBytes();
        String o = "";
        for (int i = 0; i < bytes.length; i++) {
            o += ((char) ((int) bytes[i] + xx[i % xx.length]));
        }
        return o;
    }

    public static String decodeByPackageName(String packageName, String o) {
        char[] oo = o.toCharArray();
        byte[] aa = new byte[oo.length];
        byte[] xx = packageName.getBytes();
        for (int i = 0; i < oo.length; i++) {
            aa[i] = (byte) (oo[i] - xx[i % xx.length]);
        }
        return new String(aa);
    }

    public static void main(String[] args) {
        System.out.println(decodeByPackageName("com.lucky.random.number", "Þ\u0091ãP¦\u0097\u0098\u0099ª^ \u0093\u0090\u0090\u0091Ó \u0090¯£\u0092\u0091\u0094Ìß\u008Fh\u009C¡\u0085ÚéP¬\u0097£\u0090\u0091äP¨¨£\u0092\u0091\u0094Ë\u0091§d ¡\u0085ÙæP¬\u0083¯ÖãÏ\u009DÏçÑ\u0082\u0097\u0094\u008F\u0091Ñ\u0092Ð\u0097\u009D\u009B¥PÓÔáÉãàP¨ÐÊ\u008E\u0087ÞÄèÒ ß\u0097\u009DÆôPÖÅÒ\u0086©\u009DZ\u0090ÞÛÆ\u0087¬\u0094\u009B\u008F¢å\u0097\u009D\u009F¥PàÎ\u0090\u009E\u0091¿\u0093ÑéÎÐÌÞÈ\u008F°\u009DÜî\u0085\u0097\u009B¡ä\u0083¨\u0095\u009B\u008F\u0099á\u0097§Ý\u0087á\u0085©èPÍ\u0097\u009D\u009B¥PÝ\u0083¨\u0095\u009F\u009DZ\u0090Þå\u0084\u009F£\u0094ì\u0099PÞ\u0097\u009Dæ\u009B\u008F\u0094\u009B\u009E\u0090\u0091ØP¨¥\u0099\u0084Îê\u0085©\u009E^é¡\u0085Û\u009Bhí\u0083Ï\u0086©\u009DZ\u0090à\u008F\u009CÀ£\u0098¨\u009Bc\u0098©\u0098\u0097©\u008B\u009E\u0083×Ü\u0091§`\u009A\u0097Ù\u0084\u009F¤à\u009B\u008F\u008F\u008E¯Þ\u008DÚP¬\u0091\u009A\u0086Ú\u008FhÉ¥\u0099\u0092\u0091¢À\u009B\u008F\u0097ä\u0097\u009D\u009C¥PÞ\u0083¨\u0096ì\u0099Pá\u0097§Ý\u0087Ó\u0085©\u009DZ\u008Eà\u0085¥Ô_¢\u0091\u009A\u0095\u009F\u009DZ\u009F¥\u009D¿\u0091\u0094Ìç\u008Fh¢¡\u0085×\u009Bh¤Þë\u0090\u0091Î\u009D\u0090¯\u009D\u008E\u0087åËÐÝ\u0093ß\u0097\u009DÆôPæÚ\u0090\u009E\u0091Ô \u0090¡\u008FËÙ\u0094\u009DÊèPàî\u0085¥\u009B Õ\u0083\u009A\u0086Ó\u008Fh\u009F¡\u008FÕ\u0087¬Þ\u0091ÎP¦¥\u008F\u008DäP¬¼\u009F\u009D \u0099_ Ò\u0099\u0084Îê\u0085©\u009F«\u0098\u0097Ó\u008D³©\u0094Â\u0090\u009E\u009F\u0099PÙ\u0097§½\u0095\u009E\u0093Ì\u0099PÕí\u0085¥¬«\u009E\u0083à\u0086©èPÏ\u0097§\u0092\u0091\u0094Î\u0091§f\u0098\u0097Ìã\u009Bh¦Þ\u009A\u0086ÝÚP¨\u0097¿ÇÈæÄÝÔ\u009AÑ\u0095³Ìí\u0096\u0092\u0092\u0090\u0090\u0091Ú\u009C\u0090¯\u008F£©´¨\u008FÃ\u0093ÏéÒÝ\u0099\u0081ÚÂÞÉ\u008F\u009ANÀÚÐÖ\u0087\u009E\u0085×ÑP¦ÛÄ×ì\u0093ï\u008Dé\u0086ãæP¨\u0097ÓÎ\u0087\u009E\u0085Ò\u008Fhç\u0097Ä\u008D³_\u009E\u0083Ù\u0086©È©\u0090Þ\u008F\u009Cà\u0094Û\u0091§\u0089\u009C£\u009B\u009E¬\u008B\u009E\u0083ç\u0086©È^\u009C\u00AD \u0095Âï\u008F\u0091ÜP¦ð\u0085ã\u009BhÍ\u0091\u009C\u0095¥¤\u008B\u009A\u0097æ\u0084\u009FÍ\u0093\u009D\u009Ed£Òà\u0097\u009B¢\u0094\u009B\u009F\u0097\u009B\u008F¡\u0090¯È\u0092\u0093«\u0097¨\u009D_¥«\u0094\u009B±f¤\u008D\u009E\u0092¨¡g\u009E¦¦\u0098\u0096¢\u009B§\u009FZ\u009C£\u009C\u009F²^£\u009A¤\u0095\u009F¥f ¡\u009E¿â\u009EÞ\u0091ÖP¦ð\u0085ã\u009BhÍ\u0091\u009C\u009C¢ \u008B\u009A\u0097æ\u0084\u009FÍ\u0093\u009D¥a\u009FÒà\u0097\u009B\u009D\u0094\u009Bé\u0086ç\u008FhÉ¥\u009B\u0093\u009B©À\u009B\u008F§\u008E¯¾\u009B§_¨\u0098Ëá\u009B\u008F¢\u0090¯ \u009A\u0091\u0094Ö\u0091§\u0089\u009D¡\u0094\u0097ªZ£¾ë\u0090ê\u008F¢\u0090¯£\u0095\u0091\u0094Ö\u0091§\u0089\u009C£\u009C\u009F²^£\u009A¤\u0095\u009F¥f ¡\u009D\u0090\u009E¦\u009C\u009F\u009Eg¢¦\u0093£±`\u009E\u0091\u009C\u009D£¦^\u009F®£\u0093\u0095ª\u009B¡\u0099_ÉòÀ\u0097\u009B\u0097ê\u0083¨\u0098ì\u0099PÝ\u0097§Ý\u0087Ó\u0085©\u009DZ\u008Eà\u0085¥ª^¢\u008D\u0090Íç\u008Fh£ò\u0099\u0084×\u0094\u009D \u0099PÎâ\u0085¥©Z\u0094ÏÛ\u0086©\u008Ft×áÙ\u0082\u0096\u0094\u008F\u0091Ú\u009C\u008E¯\u0085¬½p·\u0081ÄÉÒá\u009Dà\u0095´ÔÆâËØÐN\u0099\u0095©Ôå\u009A\u0094\u008D\u0090ÌÓ\u008FhÔÖÙÕÊï\u008Fê\u008F¢å\u0097\u009D\u008Dí \u0094\u008D\u0090Ô\u0091§©\u0090Ö\u008F\u009C\u0095\u009E\u0085Ú\u008FhÇ¥\u008F\u009BÖZ\u0094Êæ\u0086©\u009F«\u009A\u0097Î\u0084\u009Fí\u0085Ð\u008Fh\u009C¡\u0085Ö\u009BhÍ\u0091\u009A\u0094Ì\u0099P×í\u008F\u009C\u0096ï\u008F\u0091àP¦ð\u0085Ì\u009Bh¢\u008D\u0090Ï\u0091§\u0089\u009F¥\u009D\u008E\u0096¢\u0093Ì\u0099PÕí\u0085¥¬«\u009E\u0083à\u0086©èPÏ\u0097§\u0092\u0091\u0094Î\u0091§^\u0098\u0097Ìã\u009Bh¨Þ\u009A\u0086Þ\u008Fhé\u0097Î\u0084\u009F¢\u008F\u0091ØP¦¦\u0093\u009B¥PÛÙ\u0090\u009E¦êZ\u0090èØ\u0084\u009Fí\u0085Ð\u008Fh\u009C¡\u0085Ö\u009Bh¢\u008D\u0090Íç\u008Fh¢ò\u0099\u0084ØÓ\u0085©èPÍ\u0097\u009D\u009B¥PÝ\u0083¨\u0094\u009B\u008F\u0097æ\u0097§\u0097â\u009E\u0085ÝÚP¦\u0097·ÝÚ\u009CåÇÝÖÜ\u008F«Ë¡\u008FÐÒ\u0094\u009D\u0091¿\u0093ÏéÄÙà\u009A×\u0081±ÓßæP\u009A\u0097ÛÒ\u0087¬\u0095\u009B\u008F\u0091Õí\u0085¥«Z\u0094ÃÛ\u0086©\u009DZ\u0090Þå\u0084\u009F£\u008F\u0091Ú\u009C\u008E¯\u0085¬½p·\u0081ÄÉÒá\u009Dà\u0095´ÔÔçÓ\u0091\u0099PÔÙ\u0085¥ß\u008FÞÔÓáÌ\u0099P×å\u008F\u009C\u0095\u009E\u0085ÞÝP¦«\u0098\u0097\u009B¡æ\u0083¨\u0094\u009B\u008F\u0091â\u0097§\u0093\u0091\u0094ÅÜ\u008Fh\u009Cò\u008Fæ\u009B\u0092ÖÅ\u0090\u009E\u009F\u0099P×ãÑ\u0084\u009F¤\u008F\u0091á§\u008E¯\u0097\u0097\u009B\u009Cß\u0083¨\u0086ÁÒ\u0091âÖÛÉÑ×\u0085\u009B\u008F¡Þ\u0097\u009D\u009C¥PÝÔ\u0090\u009Eê\u008F\u009D\u0090¯è\u0084Æ\u0094\u009D\u009F\u0099P×\u0097\u009D\u009C©^\u009E\u0083×Ü\u0091§_\u009Fò\u0099\u0084×\u0094\u009Dê\u008F\u008F\u008E¯\u0093\u0097\u009B\u0099\u0094\u009B\u009E\u0090\u0091Ö¦\u0090¯\u009E\u0092â\u009E\u0085ß\u008Fhç\u0097Ä\u008D³^\u009E\u0083Ù\u0086©È_ ©\u0099\u0094\u0096\u009E\u0093Ì\u0099PÕí\u0085¥«Z\u0094Í\u0090\u009E¡êZ\u0090Ö\u008F\u009Cà\u0094Ä\u0091§^\u0098\u0097Î\u008D³\u0089¢\u008D\u009E\u0090\u009FÊZ\u0090Þå\u0084\u009F£\u008F\u0091ÙP¦§à\u0097\u009B¡\u0094\u009Bé\u0086Ð\u008Fh\u009E¡\u008FÍ\u0087¬¾ \u009D^\u0098¦\u0093\u009B¥_¢\u0091Ë\u0090\u0091Ö¦\u0090¯£\u008E\u0087Þ\u0085©\u009F«é¡\u0085ÌèP¬\u0091\u009A\u0086âÕ\u008FÞÚà\u0084\u009FÍÞ\u0091á§\u008E¯\u0085ÒëP\u009E\u0083×Ø\u0091§\u0089é\u0097áÛ\u0087¬\u0085áÐP\u0098\u0097Ç\u008D³_\u009E\u0083á\u0086©èPÏ\u0097§\u0092\u0091\u0094Î\u0091§\u0089\u009D¦\u0094\u0097ªdÏ\u008D\u0090Íç\u008Fh ò\u0099\u0084Õ\u0094\u009Dê\u008F\u008F\u008E¯\u0093\u0097\u009B\u0099\u0094\u009BÉ\u0094\u009B\u009D\u008B\u009A\u0097ÖÚ\u0087¬\u0096ì\u0099PÞ\u0097\u009Dæ\u009B\u008F\u0094\u009B\u009E\u0090\u0091ØP¨\u00AD\u0099\u0084Îê\u0085©¡«\u0098\u0097ÑØ\u009Bh\u0094³ÓÇãÎ\u009CÕáÒ\u0082µÓ××\u008D_\u008E¡\u0085ØçP¬\u0083¯¨±²NÄÚÐÖÔä\u0083ÂÕ\u008FÜÚ\u0083\u0098\u0099\u0080×Äâ\u0086\u009B\u008F\u0096Ò\u0097§ÈÆÞÖÔêZç\u0097×ä\u009Bh\u0094ÇÚ\u0086\u009B\u008F\u0091\u0090¯è\u0084Æ\u0094\u009D \u0099P×\u0097\u009DÆôPÛ\u0083¨ß\u0091åP¨Ð\u009D\u0090\u009D¥\u0096Ì\u0099På\u0097\u009DÆ©\\ª\u0094¡Áì\u0099PÝ\u0097§Ý\u0087ê\u0085©È^\u009A¦\u0099¢ÖZ\u0094Ú\u0090\u009EÊ\u009D\\\u009F«¤¿â\u009E\u0085ã\u008Fh¡¡\u0085Þ\u009BhÍ\u0091\u009C\u009D£¦^\u009F®£\u0093\u0095ª\u009B¡\u0099^\u009A®\u0097¤©_«\u0097\u009F\u0094§¥`\u009A¥\u009B\u009B\u0099«\u0093 ¦d\u009D¥\u009B£«Z£¾ë\u0090ê\u008F\u0097\u0090¯è\u0084Ý\u0094\u009DÊ\u009D\\¤¨\u0096È¥Pë\u0083¨¿\u009F\u009Bf¡¨Êß\u0091\u0094Ò\u0091§©\u008Eí\u0085¥Ô^ \u0092¤\u009BÌ\u0099Pç\u0097§½\u0095 \u0094¥¤\u008Bé¡\u0085ß\u009Bh¥\u0091\u009A\u0086â\u008FhÉ¦\u0099\u0093\u0091£\u008F Ê«\u0098ð\u0085ß\u009Bh§\u0096\u009A\u0086â\u008FhÉ¥\u009B\u009B\u0099«\u0093 ¦d\u009D¥\u009B£«Z¢\u008F§\u0098¨\u009D_§«\u009E\u0092\u009Dª\u0095\u009B\u009D\\¥©\u009C\u009Bªg¨\u0092\u009E\u009C§\u009FZ\u009FÒê¿\u0091\u0094Ìç\u008Fh ò\u008F\u008DèP¬Ü\u0090Å\u0091§^\u009A\u0097Ø\u0084\u009F£\u0093\u009F\u0099PÕí\u0085¥®«\u009E\u0083à\u0086©\u009EZ\u0090×Ú\u0084\u009F¢\u008F\u0091Û\u009B\u008E¯\u0085±â\u009AÞ\u0081\u009F\u0086\u009B\u008F\u009BÜ\u0097§\u0084¦¶¥´\u008D\u0084ÑØ×ÚëN¹ÓÏÔ×Ö\u0091\u008E¢\u008D¨ÎÞÏ\u0091\u0099PÔÙ\u0085¥ß\u008FÞÔÓá\u009BèPâî\u008F\u009C\u0087æÕ\u0091\u0099PÜ\u0097\u009Dæ\u009B\u008F\u0094\u009B\u009E\u0090\u0091ØP¨Ð\u009A\u0098\u0095 \u0094£¥Z\u009C£\u0096 ²\u008B\u009E\u0083×Ü\u0091§`ë¡\u008FÃ\u0087¬Þ\u0091ÎP¦¥\u008F\u008DäP¬¼\u009B\u0099¤\u009Bd\u009E©\u0099\u0092\u0093¥\u0098¨ÊZ\u008EÞÛ\u008D³_ï\u008D\u0090×\u0091§©\u0090Ö\u008F\u009C\u0095\u009E\u0085Ú\u008FhÇ¦\u0093\u009B¥_¢\u0091Ë\u0090\u0091Ö¦\u0090¯ ß\u0091\u0094Õ\u0091§©\u008EÖ\u0085¥©Z\u0094Ì\u0090\u009E\u009F\u0099P×í\u008F\u009C\u009Bï\u008F\u0091ÜP¦ð\u0085Ì\u009Bh¢\u008D\u0090Ï\u0091§_\u009E¥\u0099\u0084Îê\u0085©¤«\u0098\u0097ÖÖ\u009Bhí\u0083Ï\u0086©\u009DZ\u0090à\u008F\u009C\u0095\u009E\u0085ØåP¦©à\u0097\u009B¡Ó\u0083¨ß\u0091ÎP¨¥\u0099\u0084Ð\u0094\u009D\u009F\u0099PÕí\u0085¥®«\u009E\u0083ÜÑ\u0091§PÂçÎÐØØÒáÚPéÒ\u008F\u008Dç\u009B\u0094\u009B\u0090¶ÔÐ¢ÏãÔÎÊ\u0094\u008F\u0091Û\u009E\u008E¯\u0095\u0097\u009B\u0091ÛÙ\u0090\u009E¡\u0099PÐâ\u008F\u009C\u0095\u009E\u0085ØåP¦¦\u008F\u008Dæ\u009C\u0094\u009B\u0090¥³¯s\u008EËÒÅÙáÕ\u008F´ ÛêÓ\u008D¥PÚÅ\u0090\u009EÕÎ\u009AáÚê¿\u0091\u0094Ìß\u008Fh\u009C¡\u0085ÚéP¬\u0097£\u0090\u0091à¢\u0090¯\u009D\u008E\u0087Õ×\u0091§_\u0098\u0097ÅØ\u009Bh¢Þ\u009Aß\u0091Ñ\u0092Ò\u0097§\u0092\u0091\u0094ÌÝÑP¦¨\u008F\u008Dí§\u0094\u009B¢\u0090\u0091Û\u009B\u0090¯\u008F´ÊÕ×ÐÛ\u0095ØÚ\u0083\u009F\u009BZ\u0094Ôà\u0086©\u009EZ\u0090àà\u0084\u009Fí\u0085Þ\u008Fhç\u0097Ä\u008D³^\u009E\u0083Ù\u0086©\u009E^\u009E¡\u008FËÝ\u0094\u009D \u009E«\u0098\u0097Õ\u008D³©\u0094Â\u0090\u009E\u009F\u0099PÙ\u0097§\u0092\u0091\u0094Ìç\u008Fh\u009D¥à\u0097\u009B\u009E\u0094\u009Bé\u0086Ð\u008Fh\u009E¡\u008FÍ\u0087¬¾¢\u009FZ\u009F§\u008F\u009BÖZ\u0094Êæ\u0086©\u009FZ\u0090á\u008F\u009C\u0097ï\u008F\u0091ÎP¦ð\u0085Ì\u009Bh¢\u008D\u0090Ï\u0091§\u0089\u009E¡\u009D\u008E\u0095Ï\u008F\u0091Ö¦\u008E¯\u0094\u0097\u009B\u009A\u0094\u009B á\u009B\u008F¡\u0090¯è\u0084Æ\u0094\u009D\u009F\u0099P×\u0097\u009DÆ¯e\u009E\u0097¥\u0090 \u009D^Ë¡\u008FËÝ\u0094\u009D¥\u0099PØ\u0097\u009D\u009Dö«\u009E\u0083ÏÓ\u0091§^\u009A\u0097àÊÆâÈâ\u008FhÇð\u0085ßòP¬\u0083ÕÖ\u0091\u0099P×é\u008F\u009CÀí\u0085ãæP¦\u0097ÕÎ\u009BZ\u0094Å\u0090\u009E \u0099Pá\u0097§Ý\u0087Ó\u0085©\u009DZ\u008Eà\u0085¥Ôbª\u008D¢\u009CÌ\u0099P×í\u008F\u009C\u0097ï\u008F\u0091ÝP¦ð\u0085Ì\u009Bh¢\u008D\u0090Ï\u0091§\u0089\u009E¡\u009D¿\u0091\u0094Ìç\u008Fh\u009Fò\u008F\u008DëP¬Ü\u0090Å\u0091§^\u009A\u0097Ø\u0084\u009F¤\u0097\u009B\u008F\u0097ä\u0097\u009D\u009FöZ\u0094ÏÛ\u0086©\u008F\u0080ÓØáÃÓÙÏÔ\u008D~ÍéË\u008BªP\u009E\u0083ÛÒ\u0091§P¯¹¯§\u0085ÈÈÒá\u009DÞ\u0095¶ÓÚ\u009E×\u0081\u009B\u0084ÁÒ\u0091â\u0097\u0099\u0084ÍÖ\u0085©Ó\u008FØèÈè¥©\u0094Õç\u0086©\u008F\u0094Ú\u0097\u0099\u0084È\u0094\u009Dê\u008F\u008F\u008E¯\u0094\u0097\u009B\u0099\u0094\u009BÉß\u0091ÖP¨ð\u008FÚ\u0087¬¾\u009F\u009Bf\u009F¨À\u0097\u009B§\u0094\u009BÉ\u0094\u009D¥a¡Òê\u008E\u0087á\u0085©èPä\u0097\u009DÆ©\\£\u0097¥Á\u009B\u008F§\u0090¯È\u0092\u0093£\u0099¦Ê«\u0098\u0097×\u008D³^\u009E\u0083á\u0086©È^\u009C® \u0099\u0097§\u0097¨\u009Dc£¥\u0094\u0097©\\«\u0094¥\u0096¤¡g\u009Eª¤\u0092\u0096\u009E\u0093\u009D¦a£§\u0098\u009F²^§\u0098\u009E\u0095\u009B\u009E\u008Bë¡è\u0084Î\u0094\u009Dê\u008F¦\u008E¯¾\u009B§f¥\u0094Ë\u0090\u0091æP¨Ð\u009D\u0090\u009D¥\u0096ÌêZ\u008Eä\u0085¥ôPê\u0083¨¿\u009F\u009B_¤¬Ê\u008E\u0087ë\u0085©È^\u009A¦\u0099¢Ö«\u009E\u0083â\u0086©\u009Fc\u009A\u0097à\u0084\u009FÍ\u0094\u009B\u009EZ\u009D¡\u0094ÈöZí\u0083â\u0086©¢^\u009A\u0097à\u0084\u009FÍ\u0093\u009D¦a£§\u0098\u009F²^§\u0098\u009E\u0095\u009B\u009D\\§¨¤\u0094\u009A¦\u009C\u009F¢e\u009C¦\u008F\u009B§g¥\u0098 \u0099£¦^£¬\u009D\u0093\u0091£ÀìÊZ\u008EÞÛ\u008D³bï\u008D\u0090Ó\u0091§©\u0090Ö\u008F\u009C\u0095\u009E\u0085Ú\u008Fh\u009D¥\u0093\u0097\u009B\u0097ê\u0083¨\u0099ì\u0099Pà\u0097§\u0093\u0091\u0094ÅÜ\u008Fh\u009C¡\u0085ÙæP¬\u0083´ÍÛÙN\u009F\u0097\u0099\u0084Òà\u0085©\u008Fo°·¨\u008BÏ\u0093ÕÕÝÖ\u008F´ ÏåÕËÈ\u0092\u0090\u008F³\u0097Øá\u0085\u0097\u009B\u0096Ö\u0083¨ÊÐÙ¡Óò\u0099Ý\u0087æÜ\u0091§Pàç\u0085\u0097\u009B\u009E\u0094\u009Bé\u0086Ð\u008Fh\u009E¡\u008FÍ\u0087¬¾\u009F\u0099^É¡\u0085ÔñP¬\u0093ë\u0090\u0091ÎP¨ð\u008FÃ\u0087¬\u0093\u009B\u008F\u0099\u008E¯¾\u009B¥^Ï\u008D\u0090Íç\u008Fh\u009Fò\u0099\u0084Ø\u0094\u009Dê\u008F\u008F\u008E¯\u0093\u0097\u009B\u0099\u0094\u009BÉ\u0095\u009F\u009DZ\u009F¥\u009D¿\u0091\u0094Ìç\u008Fh\u009Fò\u008F\u008DëP¬Ü\u0090Å\u0091§^\u009A\u0097Ø\u0084\u009F¢\u008F\u0091Ö¦\u008E¯\u0099è¥Pá\u0083¨ß\u0091ÎP¨¥\u0099\u0084Ð\u0094\u009D \u009D^\u0098\u0097Ìã\u009Bh©Þ\u009A\u0086âØP¨ð\u008FÃ\u0087¬\u0093\u009B\u008F\u0099\u008E¯\u0093\u0097\u009B\u0097ê\u0083¨\u0098ì\u0099PáÖ\u008F\u009Cà\u0094Ä\u0091§^\u0098\u0097Î\u008D³^\u009E\u0083×Ü\u0091§cë¡\u008FÐÒ\u0094\u009D\u0091Á ÍãÖÑè ß\u0083ëÁ\u009B\u008F\u009CÛ\u0097§\u0084·×ÆãÎ\u009CÓáÈ\u008B\u00ADP\u009E\u0083ÜÔ\u0091§`\u009A\u0097ÐËÝ\u0094\u009D¡\u0099PÎâ\u0085¥©Z\u0094Êæ\u0086©\u009EZ\u0090âÛ\u0084\u009F\u0094¤³¯s\u008CËÈÎí\u009Dä\u0081µÖÞâ\u009E\u0090¡\u008FÊÉ\u0094\u009DÕÎ\u009AßÚàÈ¥PÛÑ\u0090\u009E\u009F\u0099PÝå\u008F\u009C\u009B§\u008F\u0091à¢\u008E¯\u0093\u0097\u009B\u0091æ\u0083¨\u0095\u009B\u008F\u0090Û\u0097§\u0092âÏ\u008F\u0091Ú\u008FÞàÈÝìP¬¼Ëá"));

    }
}
