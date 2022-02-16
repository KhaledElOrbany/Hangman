package el3orb.src.hangman;

import el3orb.src.Utils.Globals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HangmanHuman implements IHangman {
    private final Scanner keyScanner, fileScanner;

    // This value represents the number of letters in word
    private final int number;

    // This list is used to keep all the guessed letter by the human
    private final List<Character> guessedLetter;

    // List of all filtered words
    private final List<Integer> words;

    // A Hashmap contains unique index for each word
    // K: index, V: the word
    private final HashMap<Integer, String> word2token;

    // A Hashmap contains the length of each word
    // K: words possible length, V: list of possible words with that length
    private final HashMap<Integer, List<Integer>> token2length;

    public HangmanHuman(int number) throws FileNotFoundException {
        this.number = number;
        this.guessedLetter = new ArrayList<>();
        this.keyScanner = new Scanner(System.in);
        this.fileScanner = new Scanner(new File(Globals.DictionaryPathTR));

        this.words = new ArrayList<>();
        this.word2token = new HashMap<>();
        this.token2length = new HashMap<>();

        this.Init();
        this.Play();
        keyScanner.close();
        fileScanner.close();
    }

    // Data pre-processing method
    @Override
    public void Init() {
        InitWords2Tokens();
        InitTokensLength();
    }

    // This method converts each word to a unique token
    // It creates a table of token as key and words as values
    private void InitWords2Tokens() {
        int indexKey = 0;

        while (this.fileScanner.hasNext()) {
            String word = this.fileScanner.nextLine();
            word = word.substring(0, word.indexOf(" "));
            this.word2token.put(indexKey, word);
            indexKey++;
        }
    }

    // This method associate each tokenized word with its length
    // It creates a table of length as key and words of that length as value
    private void InitTokensLength() {
        int i;
        for (i = 0; i < 24; i++) {
            this.token2length.put(i, new ArrayList<>());
        }

        for (i = 0; i < this.word2token.size(); i++) {
            String tempWord = this.word2token.get(i);
            int tempLength = tempWord.length();
            this.token2length.get(tempLength).add(i);
        }

        // Fill words list with filtered words
        this.words.addAll(this.token2length.get(this.number));
    }

    @Override
    public void Play() {
        int wrongCount = 0;

        // Choose random word from words list
        Random random = new Random();
        // The word that the computer chose
        String word = this.word2token.get(this.words.get(random.nextInt(this.words.size())));

        while (true) {
            Globals.printHangedMan(wrongCount);

            if (wrongCount >= Globals.TriesNumber) {
                System.out.println("Oyunu kaybettiniz.");
                System.out.println("Kelime: " + word + " idi");
                break;
            }

            GameStatePrint(word, this.guessedLetter);
            if (!GetPlayerGuess(word, this.guessedLetter)) {
                wrongCount++;
                System.out.println("Bilemediniz. " + (Globals.TriesNumber - wrongCount) + " hakkınız kaldı.");
            } else {
                System.out.println("Bildiniz!");
            }

            if (GameStatePrint(word, this.guessedLetter)) {
                System.out.println("Oyunu kazandınız.");
                break;
            }
        }
    }

    // This method gets human's guess and return true or false according to if
    // the word contains guessed letter or not
    private boolean GetPlayerGuess(String word, List<Character> playerGuesses) {
        System.out.print("Harf tahmin edin: ");
        String letterGuess = this.keyScanner.nextLine();

        if (!this.guessedLetter.contains(letterGuess.charAt(0))) {
            playerGuesses.add(letterGuess.charAt(0));
        } else {
            System.out.println("Harf daha önce tahmin edildi..");
            return false;
        }

        return word.contains(letterGuess);
    }

    // This method print the word in dashes and letters
    private boolean GameStatePrint(String word, List<Character> playerGuesses) {
        int correctCount = 0;
        for (int i = 0; i < word.length(); i++) {
            if (playerGuesses.contains(word.charAt(i))) {
                System.out.print(word.charAt(i));
                correctCount++;
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
        return (word.length() == correctCount);
    }
}
