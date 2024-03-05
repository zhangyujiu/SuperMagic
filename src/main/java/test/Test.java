package test;

public class Test {
    public static void main(String[] args) {
        String config = "[common]\n" +
                "server_addr = %s\n" +
                "server_port = %d\n" +
                "token = %s\n" +
                "\n" +
                "[download]\n" +
                "type = tcp\n" +
                "plugin = %s\n" +
                "remote_port = %d";
        config = String.format(config,"127.0.0.1", 7000, ".//tokenizer{0}", "socks5", 1009);
        System.out.println(config);
    }



    public static byte[] o00ooO(byte[] bArr, byte[] bArr2) {
        int length = bArr.length;
        int length2 = bArr2.length;
        int i6 = 0;
        int i7 = 0;
        while (i6 < length) {
            if (i7 >= length2) {
                i7 = 0;
            }
            bArr[i6] = (byte) (bArr[i6] ^ bArr2[i7]);
            i6++;
            i7++;
        }
        return bArr;
    }

}
