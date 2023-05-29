/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vmtranslator;

import com.mycompany.vmtranslator.Parser.commandTypes;
import com.sun.tools.javac.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.time.Clock;
import java.util.HashMap;

/**
 *
 * @author junlis
 */
public class VMTranslator {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Give the path to the .vm file or directory with .vm files.");
            System.exit(1);
        }
        File dir = new File(args[0]);
        String path = dir.getAbsolutePath();
        boolean needInit = false;
        
        CodeWriter writer;
        if(dir.isDirectory()){
            needInit = true;
            String[] files = dir.list();
            writer = new CodeWriter(path + "/" + dir.getName() + ".asm", needInit);
            for (String f: files){
            if(f.endsWith(".vm")){
                translate(path, f, writer);
                }
            }
        } else{
            writer = new CodeWriter(path.replace(".vm", ".asm"), needInit);
            translate(path, writer);
        }
        
        writer.close();
        
        System.out.println("Translated!");

    }

    private static void translate(String path, String fileName, CodeWriter writer) {
            Parser parser = new Parser(path + "/" + fileName);
            writer.setFileName(fileName.replace(".vm", ".asm"));
            
            while (parser.hasMoreCommands()) {
            parser.advance();
            
            String arg1 = parser.arg1();
            int arg2 = 0;
            
            String command = parser.getCommand();
            commandTypes type = parser.commandType();
            writer.comment(command);
            
            switch (type) {
                case C_ARITHMETIC:
                    writer.writeArithmetic(command);
                    break;
                case C_POP:
                case C_PUSH:
                    arg2 = parser.arg2();
                    writer.writePushPop(type, arg1, arg2);
                    break;
                case C_LABEL:
                    writer.writeLabel(arg1);
                    break;
                case C_GOTO:
                    writer.writeGoto(arg1);
                    break;
                case C_IF:
                    writer.writeIf(arg1);
                    break;
                case C_FUNCTION:
                    arg2 = parser.arg2();
                    writer.writeFunction(arg1, arg2);
                    break;
                case C_RETURN:
                    writer.writeReturn();
                    break;
                case C_CALL:
                    arg2 = parser.arg2();
                    writer.writeCall(arg1, arg2);
                    break;
                default:
                    throw new AssertionError(type.name());
            } 
        }
        
        parser.close();
    }

    private static void translate(String path, CodeWriter writer) {
            Parser parser = new Parser(path);
            
            while (parser.hasMoreCommands()) {
            parser.advance();
            
            String arg1 = parser.arg1();
            int arg2 = 0;
            
            String command = parser.getCommand();
            commandTypes type = parser.commandType();
            writer.comment(command);
            
            switch (type) {
                case C_ARITHMETIC:
                    writer.writeArithmetic(command);
                    break;
                case C_POP:
                case C_PUSH:
                    arg2 = parser.arg2();
                    writer.writePushPop(type, arg1, arg2);
                    break;
                case C_LABEL:
                    writer.writeLabel(arg1);
                    break;
                case C_GOTO:
                    writer.writeGoto(arg1);
                    break;
                case C_IF:
                    writer.writeIf(arg1);
                    break;
                case C_FUNCTION:
                    arg2 = parser.arg2();
                    writer.writeFunction(arg1, arg2);
                    break;
                case C_RETURN:
                    writer.writeReturn();
                    break;
                case C_CALL:
                    writer.writeCall(arg1, arg2);
                    break;
                default:
                    throw new AssertionError(type.name());
            } 
        }
        
        parser.close();
    }
}
