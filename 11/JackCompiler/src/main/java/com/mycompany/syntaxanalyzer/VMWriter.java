/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syntaxanalyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junlis
 */
public class VMWriter {

    public enum Commands {
        ADD, SUB, NEG, EQ, GT, LT, AND, OR, NOT, MULT, DIV
    }

    public enum Segment {
        CONSTANT, ARGUMENT, LOCAL, STATIC, THIS, THAT,
        POINTER, TEMP
    }

    private BufferedWriter bw;
    private String fileName;

    public VMWriter(String path, String fileName) {
        try {
            this.fileName = fileName;
            bw = new BufferedWriter(new FileWriter(path));
        } catch (Exception e) {

        }
    }

    public void writePush(Segment seg, int index) {
        String formatted = String.format("push %1$s %2$d", seg.toString().toLowerCase(), index);
        write(formatted);

    }

    public void writePop(Segment seg, int index) {
        String formatted = String.format("pop %1$s %2$d", seg.toString().toLowerCase(), index);
        write(formatted);

    }

    public void writeArithmetic(Commands command) {
        switch (command) {
            case MULT:
                writeCall("Math.multiply", 2);
                break;
            case DIV:
                writeCall("Math.divide", 2);
                break;
            default:
                String formatted = String.format("%1$s", command.toString().toLowerCase());
                write(formatted);
                break;
        }
    }

    public void writeLabel(String label) {
        String formatted = String.format("label %1$s", label);
        write(formatted);
    }

    public void writeGoto(String label) {
        String formatted = String.format("goto %1$s", label);
        write(formatted);
    }

    public void writeIf(String label) {
        String formatted = String.format("if-goto %1$s", label);
        write(formatted);
    }

    public void writeCall(String name, int nArgs) {
        String formatted = String.format("call %1$s %2$d", name, nArgs);
        write(formatted);
    }

    public void writeFunction(String name, int nLocals) {
        String formatted = String.format("function %1$s %2$d", name, nLocals);
        write(formatted);

    }

    public void writeReturn() {
        write("return");
    }

    private void write(String formatted) {
        try {
            bw.write(formatted);
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(VMWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void close() {
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(VMWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
