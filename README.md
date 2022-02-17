# Hangman Game - Khaled Elorbany

This project is about the famous Hangman words guessing game.

This project is designed as human vs computer kind of game. 

**As human:** \
    - Computer chooses a word and human tries to guess the word by 
    guessing letter by letter. \
    - There are 10 tries before losing, every wrong decisions leads
    the program to draw a hangman figure. 

**As computer:** \
    - The human chooses a word and the computer tries to predict that word. \
    - The strategy is when the computer asks for word's length, it \
    filters all the words and narrows down the words set to all the \
    the words with the given length. \
    - Then letter by letter it narrows down the words set until it \
    catches a pattern and guess all the rest letters right.