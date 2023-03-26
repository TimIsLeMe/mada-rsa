import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.generateKeyPair();
        System.out.println(rsa.pp.longValue() + " ----- "  + rsa.m.longValue());
        System.out.println(EEA.ggT(rsa.pp.longValue(), rsa.m.longValue()) == 1);

//        rsa.pp = BigInteger.valueOf(33);
//        rsa.m = BigInteger.valueOf(20);
//        rsa.e = BigInteger.valueOf(7);
//        rsa.d = BigInteger.valueOf(3);
//        long garbage = rsa.crypt(11, 38);
//        System.out.println(garbage);

        String s = "";
        long l;
        char[] message = rsa.readFile("text.txt");
        for (int i = 0; i < message.length; i++) {
            l = rsa.crypt(message[i], rsa.e.longValue());
            s = s + l + ((i == message.length-1) ? "" : ",");
        }
        rsa.writeFile("chiffre.txt", s);

        String decode = "";
        String[] smt = s.split(",");
        for(String oy : smt) {
            long lo = Long.parseLong(oy);
            long why = rsa.crypt(lo, rsa.d.longValue());
            char cha = (char)why;
            decode = decode + cha;
        }
        System.out.println(decode);
    }
}