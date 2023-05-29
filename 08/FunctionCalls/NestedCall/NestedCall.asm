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

//function Sys.init 0
(SYS.INIT$)

//push constant 4000
@4000
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 0
@SP
A=M-1
D=M
@THIS
M=D
@SP
M=M-1

//push constant 5000
@5000
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 1
@SP
A=M-1
D=M
@THAT
M=D
@SP
M=M-1

//call Sys.main 0
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
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@SYS.MAIN$
0;JMP
(RET_1)

//pop temp 1
@1
D=A
@5
AD=A+D
@temp
M=D
@SP
A=M-1
D=M
@temp
A=M
M=D
@SP
M=M-1

//label LOOP
(LOOP)

//goto LOOP
@LOOP
0;JMP

//function Sys.main 5
(SYS.MAIN$)
D=0
@SP
A=M
M=D
@SP
M=M+1
D=0
@SP
A=M
M=D
@SP
M=M+1
D=0
@SP
A=M
M=D
@SP
M=M+1
D=0
@SP
A=M
M=D
@SP
M=M+1
D=0
@SP
A=M
M=D
@SP
M=M+1

//push constant 4001
@4001
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 0
@SP
A=M-1
D=M
@THIS
M=D
@SP
M=M-1

//push constant 5001
@5001
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 1
@SP
A=M-1
D=M
@THAT
M=D
@SP
M=M-1

//push constant 200
@200
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop local 1
@1
D=A
@LCL
AD=M+D
@temp
M=D
@SP
A=M-1
D=M
@temp
A=M
M=D
@SP
M=M-1

//push constant 40
@40
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop local 2
@2
D=A
@LCL
AD=M+D
@temp
M=D
@SP
A=M-1
D=M
@temp
A=M
M=D
@SP
M=M-1

//push constant 6
@6
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop local 3
@3
D=A
@LCL
AD=M+D
@temp
M=D
@SP
A=M-1
D=M
@temp
A=M
M=D
@SP
M=M-1

//push constant 123
@123
D=A
@SP
A=M
M=D
@SP
M=M+1

//call Sys.add12 1
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
@SYS.ADD12$
0;JMP
(RET_2)

//pop temp 0
@0
D=A
@5
AD=A+D
@temp
M=D
@SP
A=M-1
D=M
@temp
A=M
M=D
@SP
M=M-1

//push local 0
@0
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push local 1
@1
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push local 2
@2
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push local 3
@3
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push local 4
@4
D=A
@LCL
A=M+D
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

//function Sys.add12 0
(SYS.ADD12$)

//push constant 4002
@4002
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 0
@SP
A=M-1
D=M
@THIS
M=D
@SP
M=M-1

//push constant 5002
@5002
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 1
@SP
A=M-1
D=M
@THAT
M=D
@SP
M=M-1

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

//push constant 12
@12
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
(END)
@END
0;JMP
