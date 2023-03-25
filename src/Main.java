import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.generateKeyPair();
        System.out.println(rsa.pp.longValue() + " ----- "  + rsa.m.longValue());
        System.out.println(EEA.ggT(rsa.pp.longValue(), rsa.m.longValue()) == 1);
    }
}