public class Main {
    public static void main(String[] args) {

        RSA rsa = new RSA();
        rsa.generateKeyPair();
        System.out.println((rsa.e * rsa.d) % rsa.phi.longValue() == 1);
    }
}