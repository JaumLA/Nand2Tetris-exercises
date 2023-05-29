
//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//eq
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
@TRUE_0
D;JEQ
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_0
0;JMP
(TRUE_0)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_0)

//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 16
@16
D=A
@SP
A=M
M=D
@SP
M=M+1

//eq
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
@TRUE_1
D;JEQ
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_1
0;JMP
(TRUE_1)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_1)

//push constant 16
@16
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//eq
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
@TRUE_2
D;JEQ
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_2
0;JMP
(TRUE_2)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_2)

//push constant 892
@892
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//lt
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
@TRUE_3
D;JLT
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_3
0;JMP
(TRUE_3)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_3)

//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 892
@892
D=A
@SP
A=M
M=D
@SP
M=M+1

//lt
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
@TRUE_4
D;JLT
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_4
0;JMP
(TRUE_4)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_4)

//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//lt
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
@TRUE_5
D;JLT
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_5
0;JMP
(TRUE_5)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_5)

//push constant 32767
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//gt
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
@TRUE_6
D;JGT
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_6
0;JMP
(TRUE_6)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_6)

//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 32767
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1

//gt
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
@TRUE_7
D;JGT
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_7
0;JMP
(TRUE_7)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_7)

//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//gt
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
@TRUE_8
D;JGT
D=0
@SP
A=M
M=D
@SP
M=M+1
@FALSE_8
0;JMP
(TRUE_8)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(FALSE_8)

//push constant 57
@57
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 31
@31
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 53
@53
D=A
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

//push constant 112
@112
D=A
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

//neg
@SP
A=M-1
D=M
@SP
M=M-1
D=-D
@SP
A=M
M=D
@SP
M=M+1

//and
@SP
A=M-1
D=M
@SP
M=M-1
@SP
A=M-1
D=M&D
@SP
M=M-1
@SP
A=M
M=D
@SP
M=M+1

//push constant 82
@82
D=A
@SP
A=M
M=D
@SP
M=M+1

//or
@SP
A=M-1
D=M
@SP
M=M-1
@SP
A=M-1
D=M|D
@SP
M=M-1
@SP
A=M
M=D
@SP
M=M+1

//not
@SP
A=M-1
D=M
@SP
M=M-1
D=!D
@SP
A=M
M=D
@SP
M=M+1
(END)
@END
0;JMP
