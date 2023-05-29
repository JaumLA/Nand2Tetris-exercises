// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

@isblack
M=0

(RESTART)
@SCREEN
D=M

//Check if some key is pressed
@24576
D=M;
@CHANGEBLACK
D;JNE

//if isblack=1, we need to change to white
@isblack
D=M
@CHANGEWHITE
D;JNE
@isblack
M=0

//loop
@RESTART
0;JMP

(CHANGEBLACK)
//if it's already black, just go to RESTART
@isblack
D=M
@RESTART
D;JNE

//blackaddr keep track of the actual register
@16384
D=A
@blackaddr
M=D

(LOOPBLACK)
@blackaddr
A=M
M=0
M=!M
@blackaddr
M=M+1

//if address >= 24575, stop the loop
D=M
@24575
D=D-A
@LOOPBLACK
D;JLE

@isblack
M=1
@REStART
0;JMP


(CHANGEWHITE)
//whiteaddr keep track of the actual register
@SCREEN
D=A
@whiteaddr
M=D

(LOOPWHITE)
@whiteaddr
A=M
M=0
@whiteaddr
M=M+1

//if address >= 24576, stop the loop
D=M
@24576
D=D-A
@LOOPWHITE
D;JLE

@isblack
M=0
@REStART
0;JMP
