# Assignment 2

## Compilation

The project is implemented in Java SE 21.

When using the Intellij terminal for compilation, it is necessary to enter the second src file inside the g17 one. 

Example: 
```
cd C:\Users\Utilizador\Desktop\Joana\Feup\3ano\2_semestre\CPD\g17\assign2\src\src
```

Then, use the command `javac *.java` to compile all the java files.

## Run the server

To run the server, use the command `java Server`. 

Note: The lobby size is the number of players that are going to play on the same game!

## Run the Clients

To run the clients, use the command `java Player` and then authenticate the player.

## Game rules 

The objective of the game is to guess the 5 letter word.
Each player has 6 tentatives and the winner is the player that guesses first.
When you insert a word:
    - if the letters turn green, it means that the letter is in the right place
    - if the letters turn yellow, it means that the letter is in the word but on the wrong place
    - if the color of the letters doesn't chanche, it means that the letter is not in the word



