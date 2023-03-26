import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;


public class RSA {
    public BigInteger d;
    public BigInteger e;
    public BigInteger pp;
    public BigInteger m;

    private int rsaByteLength = 64; // 64 * 2 * 8 = 1024 bit -> which should be a reasonable length
    private long randmax = 10000;
    private long randmin = 1000;

    public void generateKeyPair() {
        this.pp = generatePrimeProduct(); // n
        this.e = generateNonDiv(m);
        this.d = generateSecondNonDiv(m, e);
        String privateKey = "(" + pp.toString() + "," + d + ")";
        String publicKey = "(" + pp.toString() + "," + e + ")";
        try {
            writeFile("sk.txt", privateKey);
            writeFile("pk.txt", publicKey);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public void writeFile(String fileName, String content) {
        try {
            FileOutputStream fs = new FileOutputStream(fileName);
            fs.write(content.getBytes(StandardCharsets.UTF_8));
        }
        catch(IOException exception) {

        }
    }

    public char[] readFile(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = "";
            String line;
            while ((line = br.readLine()) != null) {
                s += line;
            }
            return s.toCharArray();
        }
        catch(IOException exception) {
            //don't
            return null;
        }
    }

    public long crypt(long x, long exponent) {

        long h = 1L; // h is always 1
        long k = x;

        int[] b = b(exponent); // get array

        for(int i = b.length-1; i >= 0; i--) {
            if(b[i] == 1) {
                h = h * k % this.pp.longValue();
            }
            if(i != 0) {
                k = k * k % this.pp.longValue();
            }
        }
        return h;
    }

    public int[] b(long x) {

        int[] ia = new int[64];
        int i = 0;
        while(x != 0L) {
            ia[i] = x%2==0 ? 0 : 1;
            i++;
            x = x >>> 1;
        }
        return reverseAndCut(ia);
    }

    private int[] reverseAndCut(int[] ia) {
        int newLength = 0;
        for (int i = ia.length -1; i >= 0; i--) {
            if(ia[i] == 1) {
                newLength = i + 1;
                break;
            }
        }
        int[] result = new int[newLength];
        for(int i = 0; i < result.length; i++) {
            result[i] = ia[result.length - i - 1];
        }
        return result;
    }

    public BigInteger generatePrimeProduct() {
        Random rnd = new Random();
        byte[] bytes = new byte[rsaByteLength];
        rnd.nextBytes(bytes);
        BigInteger p = BigInteger.probablePrime(bytes.length, rnd);
        BigInteger q;
        do {
           q = BigInteger.probablePrime(bytes.length, rnd);
        }
        while(q.equals(p));
        this.m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        return p.multiply(q);
    }
    public BigInteger generateNonDiv(BigInteger m) {
        Random rnd = new Random();
        long numb;
        do {
            numb = rnd.nextLong(randmax - randmin + 1l) + randmin;
        }
        while(EEA.ggT(m.longValue(), numb) != 1l);
        return BigInteger.valueOf(numb);
    }
    public BigInteger generateSecondNonDiv(BigInteger m, BigInteger div1) {
        Random rnd = new Random();
        long numb;
        do {
            numb = rnd.nextLong(randmax - randmin + 1l) + randmin;
        }
        while(div1.equals(BigInteger.valueOf(numb)) || EEA.ggT(m.longValue(), numb) != 1l);
        return BigInteger.valueOf(numb);
    }
//    public BigInteger ggT(BigInteger num1, BigInteger num2) {
//        BigInteger divider = num1.compareTo(num2) > 0 ? num1 : num2;
//        //BigInteger divider = num1 < num2 ? num1 : num2;
//        while(divider.compareTo(BigInteger.valueOf(1)) > 0) {
//            if (num1.mod(divider).equals(BigInteger.ZERO) && num2.mod(divider).equals(BigInteger.ZERO)) {
//                return divider;
//            }
//            divider = divider.subtract(BigInteger.ONE);
//        }
//        return divider;
//    }
}
