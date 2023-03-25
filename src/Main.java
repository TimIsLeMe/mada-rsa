import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {

        RSA rsa = new RSA();
        rsa.generateKeyPair();
        // System.out.println(Math.pow(Math.pow(rsa.pp.longValue(), rsa.e), rsa.d) == rsa.pp.longValue() % rsa.m.longValue());
        System.out.println(rsa.pp.longValue() + " ----- "  + rsa.m.longValue());
        System.out.println(rsa.ggT(rsa.pp, rsa.m).equals(BigInteger.ONE));
    }
}