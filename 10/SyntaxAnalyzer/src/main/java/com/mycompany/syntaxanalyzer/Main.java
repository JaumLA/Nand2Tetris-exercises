/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syntaxanalyzer;

import com.mycompany.syntaxanalyzer.JackTokenizer.tokenTypes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junlis
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            System.out.println("Give the path to the .jack file or a directory with .jack files");
            System.exit(1);
        }
        File path = new File(args[0]);
        
        if(path.isDirectory()){
            for (String fileName : path.list()) {
                compileJack(fileName, path.getAbsolutePath());
            }
        } else if(path.isFile()){
            compileJack(path.getName(), path.getParent());
        }
        
        System.out.println("Finished!");

    }

    private static void compileJack(String fileName, String path) throws IOException {
        if (fileName.endsWith(".jack")) {
            fileName = fileName.substring(0, fileName.indexOf("."));
            
            BufferedReader br = new BufferedReader(new FileReader(path + "/" + fileName + ".jack"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(path + "/" + fileName + ".XML"));
            CompilationEngine ce = new CompilationEngine(br, bw);
        }
    }

}
