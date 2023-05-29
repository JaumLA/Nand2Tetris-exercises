/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syntaxanalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author junlis
 */
public class JackTokenizer {

    public enum keywordTypes {
        CLASS, METHOD, FUNCTION, CONSTRUCTOR, INT,
        BOOLEAN, CHAR, VOID, VAR, STATIC, FIELD, LET,
        DO, IF, ELSE, WHILE, RETURN, TRUE, FALSE, NULL, THIS
    }

    public enum tokenTypes {
        KEYWORD, SYMBOL, IDENTIFIER,
        INT_CONST, STRING_CONST
    }

    String keywordFileList = "KeywordList";
    LinkedHashSet<String> keywords;

    String symbolFileList = "SymbolList";
    LinkedHashSet<String> symbols;

    String currentToken;
    LinkedList<String> lineTokens;

    BufferedReader br;

    public JackTokenizer(BufferedReader br) {
        this.br = br;
        lineTokens = new LinkedList<>();
        currentToken = "";
        fillSymbols();
        fillKeywords();

        System.out.println("Ready!");
    }

    public boolean hasMoreTokens() {
        boolean hasToken = false;
        boolean isComment = false;
        try {
            if (!lineTokens.isEmpty()) {
                hasToken = true;
            } else {
                br.mark(1000);
                String line = "";
                while ((line = br.readLine()) != null) {
                    line = removeComments(line);

                    if (line.contains("/**")) {
                        isComment = true;
                    }
                    if (line.contains("*/")) {
                        isComment = false;
                        line = line.replaceAll(".*[*]/", "");
                    }
                    if (line.isBlank() || isComment) {
                        br.mark(1000);
                        continue;
                    } else {
                        hasToken = true;
                        br.reset();
                        break;
                    }
                }

            }
        } catch (Exception e) {
        }

        return hasToken;
    }

    public void advance() {
        if (!lineTokens.isEmpty()) {
            currentToken = lineTokens.poll();
        } else {
            try {
                String[] lineWOComments = removeComments(br.readLine()).split(" ");
                for (int i = 0; i < lineWOComments.length; i++) {
                    String token = lineWOComments[i];

                    if (token.contains("\"")) {
                        i = addString(i, lineWOComments);
                    } else if (!token.isBlank()) {
                        lineTokens.add(token);
                    }
                }
                currentToken = lineTokens.poll();
            } catch (IOException ex) {
                Logger.getLogger(JackTokenizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void removeDocumentation() throws IOException {
        while (!br.readLine().contains("*/")) {
            br.mark(1000);
        }
        br.reset();
    }

    public tokenTypes tokenType() {
        tokenTypes type = tokenTypes.IDENTIFIER;
        if (keywords.contains(currentToken)) {
            type = tokenTypes.KEYWORD;
        } else if (symbols.contains(currentToken)) {
            type = tokenTypes.SYMBOL;
        } else if (currentToken.startsWith("\"")) {
            type = tokenTypes.STRING_CONST;
        } else if (currentToken.matches("\\d*")) {
            type = tokenTypes.INT_CONST;
        } else {
            for (String symbol : symbols) {
                if (currentToken.contains(symbol)) {
                    type = separateComponents(symbol);
                    break;
                }
            }
        }
        return type;
    }

    public keywordTypes keyWord() {
        for (keywordTypes type : keywordTypes.values()) {
            String typeLower = type.toString().toLowerCase();
            boolean isThisType = currentToken.trim().equals(typeLower);
            if (isThisType) {
                return type;
            }
        }
        return null;
    }

    public char symbol() {
        return currentToken.trim().charAt(0);
    }

    public String identifier() {
        return currentToken;
    }

    public int intVal() {
        int val = Integer.parseInt(currentToken.trim());
        return val;
    }

    public String stringVal() {
        return currentToken.substring(1, currentToken.length() - 1);
    }

    public void close() {
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(JackTokenizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private tokenTypes separateComponents(String symbol) {
        int symbolIndex = currentToken.indexOf(symbol);
        String beforeSymbol = currentToken.substring(0, symbolIndex);
        String rest = currentToken.substring(symbolIndex + 1);

        if (!rest.isBlank()) {
            lineTokens.addFirst(rest);
        }
        lineTokens.addFirst(symbol);
        if (!beforeSymbol.isBlank()) {
            lineTokens.addFirst(beforeSymbol);
        }

        currentToken = lineTokens.poll();

        return tokenType();
    }

    private int addString(int i, String[] tokens) {
        String var = tokens[i];
        if(!var.startsWith("\"")){
            String beforeSymbol = var.substring(0, var.indexOf("\""));
            var = var.substring(var.indexOf("\""));
            lineTokens.add(beforeSymbol);
            
        }
        
        i++;
        while (i < tokens.length && !tokens[i].matches(".*\".*")) {
            var += " " + tokens[i];
            i++;
        }
        var += " " + tokens[i].substring(0, tokens[i].lastIndexOf("\"") + 1);
                
        lineTokens.add(var);
        String afterSymbol = tokens[i].substring(tokens[i].lastIndexOf("\"") + 1);
        if (!afterSymbol.isBlank()) {
            lineTokens.add(afterSymbol);
        }

        return i;
    }

    private void fillKeywords() {
        keywords = new LinkedHashSet<>();
        try {
            BufferedReader keyFile = new BufferedReader(new FileReader(keywordFileList));
            String line;
            while ((line = keyFile.readLine()) != null) {
                keywords.add(line.trim());
            }
        } catch (Exception e) {
        }

    }

    private void fillSymbols() {
        symbols = new LinkedHashSet<>();
        try {
            BufferedReader symbolFile = new BufferedReader(new FileReader(symbolFileList));
            String line;
            while ((line = symbolFile.readLine()) != null) {
                symbols.add(line.trim());
            }
        } catch (Exception e) {
        }
    }

    private String removeComments(String line) {
        String test = line.replaceAll("//.*|", "");
        return test.trim();
    }

}
