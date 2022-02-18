package el3orb.src.main;

import el3orb.src.hangman.HangmanAI;
import el3orb.src.hangman.HangmanHuman;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        //TODO: aşk
        while (true) {
            int number = 0;
            String computerVsHuman = "";
            Scanner scanner = new Scanner(System.in);

            System.out.print("Lütfen kimin başlayacağını seçin (b: bilgisayar, i: insan, ç: çıkış) -> ");
            computerVsHuman = scanner.nextLine();
            char choice = computerVsHuman.charAt(0);
            try {
                if (choice == 'b' || choice == 'B') {
                    System.out.print("Harf sayısını belirliyorum: ");
                    number = scanner.nextInt();
                    new HangmanAI(number);
                } else if (choice == 'i' || choice == 'I' || choice == 'İ') {
                    System.out.print("Harf sayısını giriniz: ");
                    number = scanner.nextInt();
                    new HangmanHuman(number);
                } else if (choice == 'ç' || choice == 'Ç') {
                    scanner.close();
                    break;
                } else {
                    System.out.println("Lütfen ya (i) ya da (b) giriniz..");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
