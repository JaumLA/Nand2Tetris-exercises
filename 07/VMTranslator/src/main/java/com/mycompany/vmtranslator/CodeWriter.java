/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vmtranslator;

import com.mycompany.vmtranslator.Parser.commandTypes;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junlis
 */
public class CodeWriter {

    private HashMap<String, String> arithmetic_commands;
    private HashMap<String, String> type_commands;
    private BufferedWriter outFile;
    private String fileName;
    private int ifNum;
    private int funcNum;
    private int idReturn;
    private boolean hasInit;

    public CodeWriter(String fileName, boolean needInit) {
        try {
            outFile = new BufferedWriter(new FileWriter(fileName));
        } catch (Exception e) {

        }
        String[] filePath = fileName.split("/");
        String nameExten = filePath[filePath.length - 1];
        this.fileName = nameExten;

        funcNum = 0;
        idReturn = 0;
        
        fillArithCommands();
        fillTypeCommands();
        hasInit = needInit;
        if (needInit) {
            writeInit();
        }

    }

    private void writeInit() {
        //Write: SP = 256
        String init = "@256\n";
        init += "D=A\n";
        init += "@SP\n";
        init += "M=D\n";

        try {
            outFile.write(init);
            writeCall("Sys.init", 0);
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeArithmetic(String command) {
        //D-Register holds y and M holds x
        String arithCommand = command.split(" ")[0];
        String y = getStackValue();
        String assemblyComm = "";

        try {
            if (command.matches("neg|not")) {
                assemblyComm = y + movePointerBack(); //Take topmost stack value on D-Register
                assemblyComm += "D=" + arithmetic_commands.get(arithCommand) + "D\n"; //Do operation and store on D
                assemblyComm += pushD() + movePointerForward(); //Put value of the result of operation on the stack

            } else if (command.matches("eq|gt|lt")) {
                String x = holdStackValue();
                assemblyComm = y + movePointerBack() + x;
                assemblyComm += "D=M-D\n";
                assemblyComm += movePointerBack();
                //true case
                assemblyComm += "@TRUE_" + ifNum + "\n";
                assemblyComm += "D;" + arithmetic_commands.get(arithCommand) + "\n";

                //false case
                assemblyComm += "D=0\n";
                assemblyComm += pushD() + movePointerForward();
                assemblyComm += "@FALSE_" + ifNum + "\n";
                assemblyComm += "0;JMP\n";

                assemblyComm += "(TRUE_" + ifNum + ")\n";
                assemblyComm += "D=-1\n";
                assemblyComm += pushD() + movePointerForward();

                assemblyComm += "(FALSE_" + ifNum + ")\n";

                ifNum++;
            } else {
                String x = holdStackValue(); //Select the A register with the second value
                assemblyComm = y + movePointerBack() + x;
                assemblyComm += "D=" + "M" + arithmetic_commands.get(arithCommand) + "D\n";
                assemblyComm += movePointerBack();
                assemblyComm += pushD() + movePointerForward();
            }
            outFile.write(assemblyComm);
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writePushPop(commandTypes type, String segment, int index) {
        //pop operation: remove from stack the topmost and insert it to the segment[i]
        //push operation: move the value of the segment[i] to the stack

        String assemblyCommand = "";
        if (segment.matches("local|argument|this|that|temp")) {
            if (type == commandTypes.C_POP) {
                assemblyCommand = pop(segment, index);
            } else {
                assemblyCommand = push(segment, index);
            }
        } else if (segment.matches("constant")) {
            assemblyCommand = "@" + index + "\n";
            assemblyCommand += "D=A\n";
            assemblyCommand += pushD();
            assemblyCommand += movePointerForward();

        } else if (segment.matches("static")) {
            if (type == commandTypes.C_POP) {
                assemblyCommand = getStackValue();
                assemblyCommand += "@" + fileName + "." + index + "\n";
                assemblyCommand += "M=D\n" + movePointerBack();
            } else {
                assemblyCommand = "@" + fileName + "." + index + "\n";
                assemblyCommand += "D=M\n";
                assemblyCommand += pushD() + movePointerForward();
            }
        } else if (segment.matches("pointer")) {
            if (type == commandTypes.C_POP) {
                assemblyCommand = getStackValue();
                assemblyCommand += "@" + type_commands.get("" + index) + "\n";
                assemblyCommand += "M=D\n" + movePointerBack();
            } else {
                assemblyCommand = "@" + type_commands.get("" + index) + "\n";
                assemblyCommand += "D=M\n";
                assemblyCommand += pushD();
                assemblyCommand += movePointerForward();
            }
        }
        try {
            outFile.write(assemblyCommand);
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeLabel(String label) {
        String comm = "(" + label.toUpperCase()+ ")\n";

        try {
            outFile.write(comm);
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeGoto(String label) {
        String comm = "@" + label.toUpperCase() + "\n";
        comm += "0;JMP\n";

        try {
            outFile.write(comm);
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeIf(String label) {
        String assemblyCommand = getStackValue() + movePointerBack();
        assemblyCommand += "@" + label.toUpperCase() + "\n";
        assemblyCommand += "D;JNE\n";

        try {
            outFile.write(assemblyCommand);
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeFunction(String fName, int numVars) {
        writeLabel((fName + "$").toUpperCase());
        String lclPushs = "";
        for (int i = 0; i < numVars; i++) {
            lclPushs += "D=0\n" + pushD() + movePointerForward();
        }

        try {
            outFile.write(lclPushs);
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeCall(String fName, int numArgs) {
        String saveState = "@RET_" + funcNum + "\n";
        saveState += "D=A\n" + pushD() + movePointerForward();
        saveState += "@LCL\n" + "D=M\n" + pushD() + movePointerForward();
        saveState += "@ARG\n" + "D=M\n" + pushD() + movePointerForward();
        saveState += "@THIS\n" + "D=M\n" + pushD() + movePointerForward();
        saveState += "@THAT\n" + "D=M\n" + pushD() + movePointerForward();

        String argAddress = "@SP\n" + "D=M\n";
        argAddress += "@" + (numArgs + 5) + "\n";
        argAddress += "D=D-A\n";
        argAddress += "@ARG\n" + "M=D\n";

        String lclAddress = "@SP\n" + "D=M\n";
        lclAddress += "@LCL\n" + "M=D\n";

        try {
            outFile.write(saveState + argAddress + lclAddress);
            writeGoto(fName + "$");
            outFile.write("(RET_" + funcNum + ")\n");
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        funcNum++;

    }

    public void writeReturn() {
        String comman = "@LCL\n" + "D=M\n" + "@endFrame\n" + "MD=D\n";

        comman += "@5\n" + "A=D-A\n" + "D=M\n"; //gets the return address
        comman += "@retAddr\n" + "M=D\n";

        comman += getStackValue() + movePointerBack(); //Reposition the return value
        comman += "@ARG\n" + "A=M\n" + "M=D\n";

        comman += "@ARG\n" + "D=M+1\n"; //Reposition SP
        comman += "@SP\n" + "M=D\n";

        comman += "@endFrame\n" + "D=M\n"; //Restore THAT
        comman += "A=D-1\n";
        comman += "D=M\n";
        comman += "@THAT\n" + "M=D\n";

        comman += "@endFrame\n" + "D=M\n"; //Restore THIS
        comman += "A=D-1\n" + "A=A-1\n";
        comman += "D=M\n";
        comman += "@THIS\n" + "M=D\n";

        comman += "@endFrame\n" + "D=M\n"; //Restore ARG
        comman += "A=D-1\n" + "A=A-1\n" + "A=A-1\n";
        comman += "D=M\n";
        comman += "@ARG\n" + "M=D\n";

        comman += "@endFrame\n" + "D=M\n"; //Restore LCL
        comman += "A=D-1\n" + "A=A-1\n" + "A=A-1\n" + "A=A-1\n";
        comman += "D=M\n";
        comman += "@LCL\n" + "M=D\n";

        try {
            outFile.write(comman);
            if (hasInit) {
                outFile.write("@retAddr\n" + "A=M\n" + "0;JMP\n"); //Goes to the return address
            }
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String pop(String segment, int index) {
        //Store the address of segment[i] in a temp variable
        String getSegAddress = "@" + index + "\n";
        getSegAddress += "D=A\n";
        getSegAddress += "@" + type_commands.get(segment) + "\n";
        if (segment.matches("temp")) {
            getSegAddress += "AD=A+D\n";
        } else {
            getSegAddress += "AD=M+D\n";
        }

        String temp = "@temp\n";
        String tempValue = "M=D\n";
        String tempPointer = "A=M\n";

        return getSegAddress + temp + tempValue + getStackValue()
                + temp + tempPointer + tempValue + movePointerBack();
    }

    private String push(String segment, int index) {
        String getSegAddress = "@" + index + "\n";
        getSegAddress += "D=A\n";
        getSegAddress += "@" + type_commands.get(segment) + "\n";
        if (segment.matches("temp")) {
            getSegAddress += "A=A+D\n";
        } else {
            getSegAddress += "A=M+D\n";
        }

        String segValueToD = getSegAddress + "D=M\n";

        String getSPAddress = "@SP\n" + "A=M\n" + "M=D\n";

        return segValueToD + getSPAddress + movePointerForward();
    }

    public void close() {
        String endLoop = "(END)\n";
        endLoop += "@END\n";
        endLoop += "0;JMP\n";

        try {
            outFile.write(endLoop);
            outFile.close();
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getStackValue() {
        String stack = "@SP\n";
        String getStackValue = "A=M-1\n";
        String putOnD = "D=M\n";
        return stack + getStackValue + putOnD;
    }

    private String holdStackValue() {
        String stack = "@SP\n";
        String getStackValue = "A=M-1\n";

        return stack + getStackValue;
    }

    private String movePointerBack() {
        String stack = "@SP\n";
        String removeOne = "M=M-1\n";
        return stack + removeOne;
    }

    private String movePointerForward() {
        String stack = "@SP\n";
        String AddOne = "M=M+1\n";
        return stack + AddOne;
    }

    private String getPointerAddress() {
        String stack = "@SP\n";
        String removeOne = "\n";
        return stack + removeOne;
    }

    private String pushD() {
        String stack = "@SP\n";
        String address = "A=M\n";
        String add = "M=D\n";

        return stack + address + add;
    }

    private void fillArithCommands() {
        arithmetic_commands = new HashMap<>();
        arithmetic_commands.put("add", "+");
        arithmetic_commands.put("sub", "-");
        arithmetic_commands.put("neg", "-");
        arithmetic_commands.put("eq", "JEQ");
        arithmetic_commands.put("gt", "JGT");
        arithmetic_commands.put("lt", "JLT");
        arithmetic_commands.put("and", "&");
        arithmetic_commands.put("or", "|");
        arithmetic_commands.put("not", "!");
    }

    private void fillTypeCommands() {
        type_commands = new HashMap<>();
        type_commands.put("local", "LCL");
        type_commands.put("argument", "ARG");
        type_commands.put("this", "THIS");
        type_commands.put("that", "THAT");
        type_commands.put("static", fileName);
        type_commands.put("temp", "5");
        type_commands.put("1", "THAT");
        type_commands.put("0", "THIS");
    }

    public void setFileName(String name) {
        fileName = name;
    }

    public void comment(String currentCommand) {
        try {
            outFile.write("\n//" + currentCommand + "\n");
        } catch (IOException ex) {
            Logger.getLogger(CodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
