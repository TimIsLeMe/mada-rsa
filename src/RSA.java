import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class RSA {
    public long d;
    public long e;
    private BigInteger pp;
    public BigInteger phi;
    private final static int RANDLIMIT = 1000;
    private final static int RANDMIN = 500;
    public void generateKeyPair() {
        this.pp = generatePrimeProduct(); // (n)
        this.e = generateNonDiv(phi.longValue());
        this.d = generateSecondNonDiv(phi.longValue(), e);
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
        byte[] bytes = new byte[16];
        rnd.nextBytes(bytes);
        BigInteger p = BigInteger.probablePrime(bytes.length, rnd);
        BigInteger q = BigInteger.probablePrime(bytes.length, rnd);
        this.phi = (p.add(BigInteger.valueOf(-1l))).multiply(q.add(BigInteger.valueOf(-1l)));
        return p.multiply(q);
    }
    public long generateNonDiv(long phi) {
        Random rnd = new Random();
        int numb;
        do {
            numb = rnd.nextInt(RANDLIMIT - RANDMIN + 1) + RANDMIN;
        }
        while(ggT(phi, numb) != 1);
        return numb;
    }
    public long generateSecondNonDiv(long o, long div1) {
        Random rnd = new Random();
        int numb;
        do {
            numb = rnd.nextInt(RANDLIMIT - RANDMIN + 1) + RANDMIN;
        }
        while(div1 == numb && div1 * numb != 1 % o);
        return numb;
    }
    public long ggT(long num1, long num2) {
        long divider = num1 < num2 ? num1 : num2;
        while(divider > 1) {
            if (num1 % divider == 0 && num2 % divider == 0) {
                return divider;
            }
            divider--;
        }
        return divider;
    }
}
