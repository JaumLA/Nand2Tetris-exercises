
//push constant 111
@111
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 333
@333
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 888
@888
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop static 8
@SP
A=M-1
D=M
@StaticTest.asm.8
M=D
@SP
M=M-1

//pop static 3
@SP
A=M-1
D=M
@StaticTest.asm.3
M=D
@SP
M=M-1

//pop static 1
@SP
A=M-1
D=M
@StaticTest.asm.1
M=D
@SP
M=M-1

//push static 3
@StaticTest.asm.3
D=M
@SP
A=M
M=D
@SP
M=M+1

//push static 1
@StaticTest.asm.1
D=M
@SP
A=M
M=D
@SP
M=M+1

//sub
@SP
A=M-1
D=M
@SP
M=M-1
@SP
A=M-1
D=M-D
@SP
M=M-1
@SP
A=M
M=D
@SP
M=M+1

//push static 8
@StaticTest.asm.8
D=M
@SP
A=M
M=D
@SP
M=M+1

//add
@SP
A=M-1
D=M
@SP
M=M-1
@SP
A=M-1
D=M+D
@SP
M=M-1
@SP
A=M
M=D
@SP
M=M+1
(END)
@END
0;JMP
