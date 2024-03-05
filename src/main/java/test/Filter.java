package test;

import java.io.FileInputStream;
import java.io.IOException;

public class Filter {
    public static void main(String[] args) throws IOException {
        String x = new String(new FileInputStream("/Users/rockie/love.txt").readAllBytes());

        String[] split = x.split("\n");
        for (String str : split) {
            if (str.contains("facebook") || str.contains("meta:"))
                System.out.println(str);
        }
    }
}
