@256
D=A
@SP
M=D
@RET_0
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@SYS.INIT$
0;JMP
(RET_0)

//function Main.fibonacci 0
(MAIN.FIBONACCI$)

//push argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push constant 2
@2
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
@TRUE_0
D;JLT
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

//if-goto IF_TRUE
@SP
A=M-1
D=M
@SP
M=M-1
@IF_TRUE
D;JNE

//goto IF_FALSE
@IF_FALSE
0;JMP

//label IF_TRUE
(IF_TRUE)

//push argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//return
@LCL
D=M
@endFrame
MD=D
@5
A=D-A
D=M
@retAddr
M=D
@SP
A=M-1
D=M
@SP
M=M-1
@ARG
A=M
M=D
@ARG
D=M+1
@SP
M=D
@endFrame
D=M
A=D-1
D=M
@THAT
M=D
@endFrame
D=M
A=D-1
A=A-1
D=M
@THIS
M=D
@endFrame
D=M
A=D-1
A=A-1
A=A-1
D=M
@ARG
M=D
@endFrame
D=M
A=D-1
A=A-1
A=A-1
A=A-1
D=M
@LCL
M=D
@retAddr
A=M
0;JMP

//label IF_FALSE
(IF_FALSE)

//push argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push constant 2
@2
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

//call Main.fibonacci 1
@RET_1
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@6
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@MAIN.FIBONACCI$
0;JMP
(RET_1)

//push argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push constant 1
@1
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

//call Main.fibonacci 1
@RET_2
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@6
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@MAIN.FIBONACCI$
0;JMP
(RET_2)

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

//return
@LCL
D=M
@endFrame
MD=D
@5
A=D-A
D=M
@retAddr
M=D
@SP
A=M-1
D=M
@SP
M=M-1
@ARG
A=M
M=D
@ARG
D=M+1
@SP
M=D
@endFrame
D=M
A=D-1
D=M
@THAT
M=D
@endFrame
D=M
A=D-1
A=A-1
D=M
@THIS
M=D
@endFrame
D=M
A=D-1
A=A-1
A=A-1
D=M
@ARG
M=D
@endFrame
D=M
A=D-1
A=A-1
A=A-1
A=A-1
D=M
@LCL
M=D
@retAddr
A=M
0;JMP

//function Sys.init 0
(SYS.INIT$)

//push constant 4
@4
D=A
@SP
A=M
M=D
@SP
M=M+1

//call Main.fibonacci 1
@RET_3
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@6
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@MAIN.FIBONACCI$
0;JMP
(RET_3)

//label WHILE
(WHILE)

//goto WHILE
@WHILE
0;JMP
(END)
@END
0;JMP
