package el3orb.src.hangman;

import el3orb.src.Utils.Globals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HangmanAI implements IHangman {
    private final Scanner fileScanner;
    private Scanner keyScanner;

    // This value represents the number of letters in word
    private final int number;

    // List of all filtered words
    private final List<String> words;

    // A Hashmap contains unique index for each word
    // K: index, V: the word
    private final HashMap<Integer, String> words2tokens;

    // A Hashmap contains the length of each word
    // K: words possible length, V: list of possible words with that length
    private final HashMap<Integer, List<Integer>> tokens2length;

    // A Hashmap contains a table of each letter's frequency in each word
    // K: tokenized word, V: table of each letter's frequency in that word
    private final HashMap<Integer, HashMap<Integer, Integer>> letterFrequenciesInWords;

    // This hashmap keeps track of guessed letters and their positions
    private final HashMap<Integer, Character> letterPositions;

    // constructor
    public HangmanAI(int number) throws FileNotFoundException {
        this.number = number;
        this.words = new ArrayList<>();
        this.words2tokens = new HashMap<>();
        this.tokens2length = new HashMap<>();
        this.letterFrequenciesInWords = new HashMap<>();
        this.letterPositions = new HashMap<>();

        this.keyScanner = new Scanner(System.in);
        this.fileScanner = new Scanner(new File(Globals.DictionaryPathTR));

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
        GetAllWordsWithGivenLength();
        CalculateLettersFrequencyInWords();
        Globals.InitLettersArray();
    }

    // This method converts each word to a unique token
    // It creates a table of token as key and words as values
    private void InitWords2Tokens() {
        int indexKey = 0;

        while (this.fileScanner.hasNext()) {
            String word = this.fileScanner.nextLine();
            word = word.substring(0, word.indexOf(" "));
            this.words2tokens.put(indexKey, word);
            indexKey++;
        }
    }

    // This method associate each tokenized word with its length
    // It creates a table of length as key and words of that length as value
    private void InitTokensLength() {
        int i;
        // 24 is just for the sake of testing, for there is no word is longer than 24 letter
        for (i = 0; i < 24; i++) {
            this.tokens2length.put(i, new ArrayList<>());
        }

        // This loop categorizes words according to length
        for (i = 0; i < this.words2tokens.size(); i++) {
            String tempWord = this.words2tokens.get(i);
            int tempLength = tempWord.length();
            this.tokens2length.get(tempLength).add(i);
        }
    }

    // This method gets all the words that have the given length
    private void GetAllWordsWithGivenLength() {
        for (int token : this.tokens2length.get(this.number)) {
            this.words.add(this.words2tokens.get(token));
        }
    }

    // This method gets the frequency of each letter in each word.
    // It works on filtered words according to length.
    private void CalculateLettersFrequencyInWords() {
        this.letterFrequenciesInWords.clear();
        for (int i = 0; i < this.words.size(); i++) {
            this.letterFrequenciesInWords.put(i, null);

            String tempWord = this.words.get(i);
            FindLetterFrequencyInWord(tempWord, i);
        }
    }

    // This method is part of (InitLettersFrequencyInWords).
    // It loops through each word to find the frequency of each letter in the given word
    private void FindLetterFrequencyInWord(String word, int index) {
        HashMap<Integer, Integer> letterCount = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Integer val = letterCount.get((int) c);
            if (val != null) {
                letterCount.put((int) c, val + 1);
            } else {
                letterCount.put((int) c, 1);
            }
        }
        this.letterFrequenciesInWords.put(index, letterCount);
    }

    @Override
    public void Play() {
        int wrongCount = 0, correctCount = 0;
        while (true) {
            Random random = new Random();
            char letter = Globals.letters.get(random.nextInt(Globals.letters.size()));
            Globals.letters.remove((Character) letter);

            GameStatePrint();

            System.out.println("Harf tahmin ediyorum: " + letter);
            System.out.print("Bildim mi? (e/h): ");

            this.keyScanner = new Scanner(System.in);
            String answer = this.keyScanner.nextLine().toLowerCase(Locale.ROOT);

            if (wrongCount < Globals.TriesNumber) {
                if (answer.charAt(0) == 'e') {
                    System.out.print("Kaç harf bildim: ");
                    this.keyScanner = new Scanner(System.in);
                    int guessedLetterFrequency = this.keyScanner.nextInt();
                    int[] guessedLetterPositions = new int[guessedLetterFrequency];

                    correctCount += guessedLetterFrequency;

                    for (int i = 0; i < guessedLetterFrequency; i++) {
                        if (guessedLetterFrequency > 1) {
                            System.out.print((i + 1) + ". harfin sırası: ");
                        } else {
                            System.out.print("Harfin sırası: ");
                        }
                        this.keyScanner = new Scanner(System.in);
                        int letterPosition = this.keyScanner.nextInt() - 1;
                        this.letterPositions.put(letterPosition, letter);
                        guessedLetterPositions[i] = letterPosition;
                    }

                    FilterWords(letter, guessedLetterFrequency);
                    Prediction(letter, guessedLetterPositions);

                    if (correctCount == this.number) {
                        GameStatePrint();
                        System.out.println("Oyunu kazandım, " + wrongCount + ". tahminde :D");
                        return;
                    }

                } else if (answer.charAt(0) == 'h') {
                    wrongCount++;
                    Globals.printHangedMan(wrongCount);
                    if (wrongCount == Globals.TriesNumber) {
                        System.out.println("Oyunu kaybettim.");
                        break;
                    }
                    System.out.println((Globals.TriesNumber - wrongCount) + " hakkım kaldı.");
                }
            } else {
                GameStatePrint();
                System.out.println("Oyunu kaybettim.");
                break;
            }
        }
    }

    // This method filters words according to given frequency for predicted letter
    private void FilterWords(char letter, int frequency) {
        List<String> filteredWords = new ArrayList<>();
        for (int wordKey : this.letterFrequenciesInWords.keySet()) {
            var temp = this.letterFrequenciesInWords.get(wordKey);
            for (int frequencyKey : temp.keySet()) {
                if (frequencyKey == ((int) letter) && frequency == temp.get(frequencyKey)) {
                    filteredWords.add(words.get(wordKey));
                }
            }
        }
        this.words.clear();
        this.words.addAll(filteredWords);
    }

    // This method narrows down the set of words and check for the letter frequency
    private void Prediction(char letter, int[] guessedLetterPositions) {
        int counter;
        List<String> filteredWords = new ArrayList<>();

        // this loop goes through all words, word by word and letter by letter
        // to get all the words that has the same frequency and positions of given letter/s
        for (String word : this.words) {
            counter = 0;
            for (int i = 0; i < word.length(); i++) {
                for (int guessedLetterPosition : guessedLetterPositions) {
                    if (word.charAt(i) == letter && i == guessedLetterPosition) {
                        counter++;
                    }

                    if (counter == guessedLetterPositions.length) {
                        if (!filteredWords.contains(word)) {
                            filteredWords.add(word);
                        }
                    }
                }
            }
        }

        // clears all exciting words and adding the new filtered ones
        this.words.clear();
        this.words.addAll(filteredWords);

        // then filtering all the letters to narrow down letters list to
        // just all the letters in filtered words
        Globals.FilterLetters(this.words);

        // then calculate all the letters frequencies in new words to narrow
        // down word set
        CalculateLettersFrequencyInWords();
    }

    // This method print the word in dashes and letters
    private void GameStatePrint() {
        for (int i = 0; i < this.number; i++) {
            if (this.letterPositions.containsKey(i)) {
                System.out.print(this.letterPositions.get(i));
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
    }
}
