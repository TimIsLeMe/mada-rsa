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

    private int rsaByteLength = 16;
    private long randmax = 128;
    private long randmin = 64;

    public void generateKeyPair() { // generates the key pair and also writes the files sk.txt and pk.txt
        this.pp = generatePrimeProduct(); // n
        this.e = generateNonDiv(m);
        this.d = BigInteger.valueOf(EEA.inverseGGT(m.longValue(), e.longValue()));
        String privateKey = "(" + pp.toString() + "," + d.toString() + ")";
        String publicKey = "(" + pp.toString() + "," + e.toString() + ")";
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
            throw new RuntimeException("error while writing file");
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
            throw new RuntimeException("error while reading file");
        }
    }
    // extended exponential algorithm:
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
        // because both primes are multiplied the product has a byte length of: 2 * rsaByteLength / 2 == rsaByteLength
        byte[] bytes = new byte[rsaByteLength >> 1];
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
    public String encrypt(char[] message) {
        String s = "";
        long l;

        for (int i = 0; i < message.length; i++) {
            l = crypt(message[i], e.longValue());
            s = s + l + ((i == message.length - 1) ? "" : ",");
        }
        return s;
    }
    public String decrypt(String message) {
        String decode = "";
        String[] smt = message.split(",");
        for(String oy : smt) {
            long lo = Long.parseLong(oy);
            long why = crypt(lo, d.longValue());
            char cha = (char)why;
            decode = decode + cha;
        }
        return decode;
    }

}
