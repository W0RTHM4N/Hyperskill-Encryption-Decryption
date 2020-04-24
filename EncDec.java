import java.util.Scanner;
import java.io.*;


public class EncDec {
    public static void main(String[] args) {
        String action = "enc";
        String algorithm = "shift";
        String text = "";
        String output = "";
        String pathOut = "";
        boolean hasData = false;
        boolean hasOut = false;
        int key = 0;
        Alghorithm alg;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-mode") && args[i + 1].equals("dec")) {
                action = "dec";
            }
            if (args[i].equals("-key")) {
                key = Integer.parseInt(args[i + 1]);
                if (key < 0) {
                    key = 0;
                }
            }
            if (args[i].equals("-data")) {
                hasData = true;
                text = args[i + 1];
            }
            if (args[i].equals("-alg") && args[i + 1].equals("unicode")) {
                algorithm = "unicode";
            }
            if (args[i].equals("-in") && !hasData) {
                File file = new File("./" + args[i + 1]);
                try (Scanner scanner = new Scanner(file)) {
                    text = scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Error: input file not found.");
                }
            }
            if (args[i].equals("-out")) {
                hasOut = true;
                pathOut = args[i + 1];
            }
        }

        switch (algorithm) {
            case "unicode" :
                alg = new UnicodeAlghorithm(text, key);
                break;
            default :
                alg = new ShiftAlghorithm(text, key);
                break;
        }

        switch (action) {
            case "enc" :
                output = alg.encrypt();
                break;
            case "dec" :
                output = alg.decrypt();
                break;
        }

        if (hasOut) {
            File file = new File("./" + pathOut);
            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.println(output);
            } catch (Exception e) {
                System.out.println("Error: output file not found.");
            }
        }
        else {
            System.out.println(output);
        }
    }


}

abstract class Alghorithm {

    static char[] textArray;
    static int key;

    Alghorithm(String text, int key) {
        this.textArray = text.toCharArray();
        this.key = key;
    }

    abstract String encrypt();
    abstract String decrypt();
}

class ShiftAlghorithm extends Alghorithm {

    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    ShiftAlghorithm(String text, int key) {
        super(text, key);
    }

    public String encrypt() {

        StringBuilder encryptedText = new StringBuilder();

        for (char letter : textArray) {
            if (letter >= 65 && letter <= 90) {
                encryptedText.append(alphabet.toUpperCase().charAt((letter + key - 65) % 26));
            }
            if (letter >= 97 && letter <= 122) {
                encryptedText.append(alphabet.charAt((letter + key - 97) % 26));
            }
            else {
                encryptedText.append(letter);
            }
        }
        return encryptedText.toString();
    }

    public String decrypt() {

        StringBuilder encryptedText = new StringBuilder();

        for (char letter : textArray) {
            if (letter >= 65 && letter <= 90) {
                if (key > letter - 65) {
                    letter += 26;
                }
                encryptedText.append(alphabet.toUpperCase().charAt((letter - key - 65) % 25));
            }
            if (letter >= 97 && letter <= 122) {
                if (key > letter - 97) {
                    letter += 26;
                }
                encryptedText.append(alphabet.charAt((letter - key - 97) % 25));
            }
            else {
                encryptedText.append(letter);
            }
        }
        return encryptedText.toString();
    }
        }

class UnicodeAlghorithm extends Alghorithm {

    UnicodeAlghorithm(String text, int key) {
        super(text, key);
    }

    public String encrypt() {

        StringBuilder encryptedText = new StringBuilder();

        for (char letter : textArray) {
            letter += key;
            encryptedText.append(letter);
        }

        return encryptedText.toString();
    }

    public String decrypt() {

        StringBuilder decryptedText = new StringBuilder();

        for (char letter : textArray) {
            letter -= key;
            decryptedText.append(letter);
        }

        return decryptedText.toString();
    }
}