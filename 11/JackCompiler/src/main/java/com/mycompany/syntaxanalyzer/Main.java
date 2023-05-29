/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syntaxanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author junlis
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            System.out.println("Give the path to the .jack file or directory with .jack files.");
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
            String fullPath = path + "/" + fileName;
            
            BufferedReader br = new BufferedReader(new FileReader(fullPath + ".jack"));
            VMWriter bw = new VMWriter(fullPath + ".vm", fileName);
            CompilationEngine ce = new CompilationEngine(br, bw);
        }
    }

}
