import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class RSA {
    public BigInteger d;
    public BigInteger e;
    public BigInteger pp;
    public BigInteger m;

    private int rsaByteLength = 32; // 64 * 2 * 8 = 1024 bit -> which should be a reasonable length
    private long randmax = 1000;
    private long randmin = 500;

    public void generateKeyPair() {
        this.pp = generatePrimeProduct(); // n
        System.out.println(this.pp);
        System.out.println(randmax + " --- " + randmin);
        this.e = generateNonDiv(m);
        this.d = generateSecondNonDiv(m, e);
        String privateKey = "(" + pp.toString() + "," + d + ")";
        String publicKey = "(" + pp.toString() + "," + e + ")";
        try {
            writeFile("sk.txt", privateKey);
            writeFile("pk.txt", publicKey);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void writeFile(String fileName, String content) throws IOException {
        try {
            FileOutputStream fs = new FileOutputStream(fileName);
            fs.write(content.getBytes(StandardCharsets.UTF_8));
        }
        catch(IOException exception) {
            throw exception;
        }
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
        while(!ggT(m, BigInteger.valueOf(numb)).equals(BigInteger.ONE));
        return BigInteger.valueOf(numb);
    }
    public BigInteger generateSecondNonDiv(BigInteger m, BigInteger div1) {
        Random rnd = new Random();
        long numb;
        do {
            numb = rnd.nextLong(randmax - randmin + 1l) + randmin;
        }
        while(div1.equals(BigInteger.valueOf(numb)) || !ggT(m, BigInteger.valueOf(numb)).equals(BigInteger.ONE));
        return BigInteger.valueOf(numb);
    }
    public BigInteger ggT(BigInteger num1, BigInteger num2) {
        BigInteger divider = num1.compareTo(num2) > 0 ? num1 : num2;
        //BigInteger divider = num1 < num2 ? num1 : num2;
        while(divider.compareTo(BigInteger.valueOf(1)) > 0) {
            if (num1.mod(divider).equals(BigInteger.ZERO) && num2.mod(divider).equals(BigInteger.ZERO)) {
                return divider;
            }
            divider = divider.subtract(BigInteger.ONE);
        }
        return divider;
    }
}
