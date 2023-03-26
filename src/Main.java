import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.generateKeyPair();
        rsa.writeFile("chiffre.txt", rsa.encrypt("Hello there!\nGeneral Kenobi?".toCharArray()));
        String decryptedMessage = rsa.decrypt(String.valueOf(rsa.readFile("chiffre.txt")));
        System.out.println(decryptedMessage);
    }
}