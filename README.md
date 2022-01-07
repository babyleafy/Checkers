# Checkers

## Rules and Instructions

A comprehensive two-player checkers game coded using Java Swing. 
This game uses standard American checkers rules, instructions for which 
can be found in a pop-up panel prior to running the game and also at 
https://www.ultraboardgames.com/checkers/game-rules.php

Added functionality includes an undo function and a save function. The 
"undo" button will reverse a single previous move, not the entire turn.
The "save" button will store the game's moves in the checkers.text text
file, which can be opened and used to restore the game status via 
the "open" button.

This game is two player, meaning that only pieces of the player whose 
turn it is can be selected and moved. 
- To select a piece, click on it and
if it is valid, the piece will display a cyan outline as well as its 
possible moves. 
- To move, select the square of the desired move. 
- For jumping moves involving multiple jumps, the player may choose how 
many jumps to take. To end the turn, simply click on the piece again.
To continue jumping, simply continue clicking on the desired square of 
the jump. Enjoy playing!

## Class Overview

- Checkers.java acts a model for the game, containing all the functions 
for manipulating and storing the game state. It calculates possible moves 
for pieces and can modify the board based on information it receives from
mouseclicks.
- GameBoard.java initiates a checkers game. 
It handles player input and saving / undoing functionality by calling 
methods of Checkers.java when the player makes a move or clicks the 
reset, undo, save, or open buttons.
- RunCheckers.java represents the UI design of the game, including the 
rules/instructions window and the button interface. This portion is
coded using Java Swing and uses action listeners to determine functionality
when buttons are pressed. 
- FileLineIterator.java is a wrapper class of BufferedReader that enables 
the game to restore its state using information found in the saved game file.
- Move.java stores move objects consisting of two points, the initial and final
positions of a piece. This information is extracted when undoing a move and 
restoring the game state.
- Piece.java stores piece objects with information about the location, color, 
and king status of a piece. These pieces are the objects stored in the board
2D array.

## Design Concepts
  1. 2D arrays. The 2D array represents the current state of the board on which 
  the checkers game is played. The state of each square will be stored in as an 
  object in the array. At the beginning of the game, the board is reset using a
  nested for loops that traverse each array and store the default positions of 
  pieces. The piece objects in the array are changed when the user makes or 
  undos a move. 
  2. Collections. Collections in the form of LinkedLists are used to store 
  moves that could be extracted in the undo function. Each time a move is made,
  it is stored at the end of the LinkedList, and each time undo is called, the 
  last stored move is removed. The user may undo any amount of moves, unless 
  there are no moves left to be undone.
  3. File I/O. File writing/reading is used to save the state of the game board
  and move history so that when a user opens it after closing, the user has an 
  option of retrieving the stored game state. The save function writes the list
  of stored moves separating 2 pairs of coordinates by commas (","), and each 
  move is stored on its own line. The open function reads and parses the data 
  according to the commas, with the first two numbers being the x,y coordinates
  of the piece's initial position, and the last two being the x,y coordinates of 
  the final position. Using this data, the game simulates the moves being played
  in order, starting from the default positionm, to return to the saved game state. 
  This minimizes the amount of storage needed, as the entire 2D array is not stored.



