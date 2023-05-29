/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vmtranslator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junlis
 */
public class Parser {
    
    private BufferedReader fileStream;
    private String currentCommand;
    public enum commandTypes{
        C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, 
        C_RETURN, C_CALL
    }
    
    public Parser(String file){
        try {
            fileStream = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            
        }
    }
    
    public boolean hasMoreCommands(){
        boolean hasCommand = false;
        try {
            String nextLine;
            do{
                fileStream.mark(1000);
                nextLine = fileStream.readLine();
                hasCommand = !(nextLine.isBlank() || nextLine.matches("//.*"));
                if(hasCommand && nextLine != null){
                    break;
                }
                
            } while (nextLine != null);
            fileStream.reset();
        } catch (Exception e) {
            
        }
        return hasCommand;
    }
    
    public void advance(){
        if(hasMoreCommands()){
            try {
                currentCommand = fileStream.readLine();
                currentCommand = currentCommand.replaceAll("//.*", "").trim();
            } catch (IOException ex) {
                
            }
        }
        else{
            System.out.println("No more lines.");
        }
    }
    
    public commandTypes commandType(){
        commandTypes type = null;
        
        if(currentCommand.matches("push.*")){
            return type.C_PUSH;
        }
        else if(currentCommand.matches("pop.*")){
            return type.C_POP;
        }
        else if(currentCommand.matches("label.*")){
            return type.C_LABEL;
        }
        else if(currentCommand.matches("goto.*")){
            return type.C_GOTO;
        }
        else if(currentCommand.matches("if-goto.*")){
            return type.C_IF;
        }
        else if(currentCommand.matches("add|sub|neg|eq|gt|lt|and|or|not")){
            return type.C_ARITHMETIC;
        }
        else if(currentCommand.matches("function.*")){
            return type.C_FUNCTION;
        }
        else if(currentCommand.matches("call.*")){
            return type.C_CALL;
        }
        else if(currentCommand.matches("return")){
            return type.C_RETURN;
        }
        
        return null;
    }
    
    public String arg1(){
        if(commandType() == commandTypes.C_ARITHMETIC || commandType() == commandTypes.C_RETURN){
            return currentCommand;
        }
        return currentCommand.split(" ")[1].toLowerCase().trim();
    }
    
    public int arg2(){
        String arg2 = currentCommand.split(" ")[2].toLowerCase().trim();
        return Integer.parseInt(arg2);
    }
    
    public void close() {
        try {
            fileStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getCommand(){
        return currentCommand;
    }
}
