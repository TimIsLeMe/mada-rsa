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

    private int rsaByteLength = 2048;
    private long randmax = 128;
    private long randmin = 64;

    public void generateKeyPair() { // generates the key pair and also writes the files sk.txt and pk.txt
        this.pp = generatePrimeProduct(); // n
        this.e = generateNonDiv(m);
        this.d = EEA.inverseGGT(m, e);
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
                if(br.ready()) s += "\n";
            }
            return s.toCharArray();
        }
        catch(IOException exception) {
            throw new RuntimeException("error while reading file");
        }
    }
    // extended exponential algorithm:
    public BigInteger crypt(BigInteger x, BigInteger exponent) {

        BigInteger h = BigInteger.ONE; // h is always 1
        BigInteger k = x;

        int[] b = b(exponent); // get array

        for(int i = b.length-1; i >= 0; i--) {
            if(b[i] == 1) {
                h = h.multiply(k).mod(this.pp);
            }
            if(i != 0) {
                k = k.multiply(k).mod(this.pp);
            }
        }
        return h;
    }

    public int[] b(BigInteger x) {
        int[] ia = new int[2048];
        int i = 0;
        while(x.compareTo(BigInteger.ZERO) > 0) {
            ia[i] = x.mod(BigInteger.TWO).equals(BigInteger.ZERO) ? 0 : 1;
            i++;
            x = x.shiftRight(1);
            //x = x >>> 1;
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
        while(!EEA.ggT(m, BigInteger.valueOf(numb)).equals(BigInteger.ONE));
        return BigInteger.valueOf(numb);
    }
    public String encrypt(char[] message) {
        String s = "";
        BigInteger l;
        for (int i = 0; i < message.length; i++) {
            l = crypt(BigInteger.valueOf((long) message[i]), e);
            s = s + l + ((i == message.length - 1) ? "" : ",");
        }
        return s;
    }
    public String decrypt(String message) {
        String decode = "";
        String[] smt = message.split(",");
        for(String oy : smt) {
            BigInteger lo = new BigInteger(oy);
            BigInteger asciiCode = crypt(lo, d);
            /*char cha = (char) asciiCode;
            decode = decode + cha;
             */
            String cha = new String(asciiCode.toByteArray());
            decode += cha;
        }
        return decode;
    }
    public void setPrivateKey() {
        char[] sk = readFile("sk.txt");
        if(sk.length < 4) throw new RuntimeException("invalid private key");
        int i = 1; // start after bracket
        boolean passedComma = false;
        String pKey = "";
        String newPP = "";
        int commaInd = 0;
        while(i < sk.length - 1) {
            if(passedComma) pKey += sk[i];
            else {
                passedComma = ',' == sk[i];
            }
            if(!passedComma) newPP += sk[i];
            i++;
        }
        this.pp = new BigInteger(newPP);
        this.d = new BigInteger(pKey);
    }

}
