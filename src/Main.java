import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.generateKeyPair();
        rsa.writeFile("chiffre.txt", rsa.encrypt(rsa.readFile("text.txt")));
        String decryptedMessage = rsa.decrypt(String.valueOf(rsa.readFile("chiffre.txt")));
        rsa.writeFile("text-d.txt", decryptedMessage);
        System.out.println(decryptedMessage);
    }
}