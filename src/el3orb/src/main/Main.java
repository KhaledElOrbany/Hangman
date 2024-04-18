package el3orb.src.main;

import el3orb.src.hangman.HangmanAI;
import el3orb.src.hangman.HangmanHuman;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        while (true) {
            int number = 0;
            String computerVsHuman;
            Scanner scanner = new Scanner(System.in);

            System.out.print("Please select who will start (c: computer, h: human, e: exit) -> ");
            computerVsHuman = scanner.nextLine();
            char choice = computerVsHuman.charAt(0);
            try {
                if (choice == 'c' || choice == 'C') {
                    System.out.print("I determine the number of letters: ");
                    number = scanner.nextInt();
                    new HangmanAI(number);
                } else if (choice == 'h' || choice == 'H') {
                    System.out.print("Enter the number of letters: ");
                    number = scanner.nextInt();
                    new HangmanHuman(number);
                } else if (choice == 'e' || choice == 'E') {
                    break;
                } else {
                    System.out.println("Please enter either (c) or (h)..");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
