package el3orb.src.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class holds shared variables and methods
// the reason of declaring its fields as static is
// to use it without initiating an object
// and to be there is a single instance of each.
public class Globals {
    public static final int TriesNumber = 10;
    public static List<Character> letters;
    public static String DictionaryPathTR = "src/el3orb/resources/turkish_dictionary.txt";

    // this method initiates the list of letters
    public static void InitLettersArray() {
        letters = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'ç', 'd', 'e', 'f', 'g', 'ğ', 'h', 'ı', 'i',
                'j', 'k', 'l', 'm', 'n', 'o', 'ö', 'p', 'r', 's', 'ş', 't', 'u', 'ü', 'v', 'y', 'z'));
    }

    // this method filters letters according to words
    public static void FilterLetters(List<String> words) {
        List<Character> restOfLetters = new ArrayList<>();
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                if (!restOfLetters.contains(word.charAt(i))) {
                    if (letters.contains(word.charAt(i))) {
                        restOfLetters.add(word.charAt(i));
                    }
                }
            }
        }
        letters.clear();
        letters.addAll(restOfLetters);
    }

    // This method draws an illustration to the hangman
    public static void printHangedMan(int wrongCount) {
        if (wrongCount >= 1) {
            System.out.println(" ------");
        }

        if (wrongCount >= 2) {
            System.out.print(" |    ");
            if (wrongCount >= 3) {
                System.out.println("|");
            } else {
                System.out.println();
            }
        }

        if (wrongCount >= 4) {
            System.out.println(" O");
        }

        if (wrongCount >= 5) {
            System.out.print("\\ ");
            if (wrongCount >= 6) {
                System.out.println("/");
            } else {
                System.out.println("");
            }
        }

        if (wrongCount >= 7) {
            System.out.println(" |");
        }

        if (wrongCount >= 8) {
            System.out.println(" |");
        }

        if (wrongCount >= 9) {
            System.out.print("/ ");
            if (wrongCount >= 10) {
                System.out.println("\\");
            } else {
                System.out.println("");
            }
        }
        System.out.println("");
    }
}
