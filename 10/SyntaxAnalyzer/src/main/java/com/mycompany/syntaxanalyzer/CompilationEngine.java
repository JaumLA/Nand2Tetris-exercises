/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syntaxanalyzer;

import static com.mycompany.syntaxanalyzer.JackTokenizer.keywordTypes;
import com.mycompany.syntaxanalyzer.JackTokenizer.tokenTypes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junlis
 */
public class CompilationEngine {
    String opFileName = "OpList";
    List<String> ops;
    
    String currentCommand;
    JackTokenizer tokenizer;

    BufferedWriter bw;
    int tabs;

    public CompilationEngine(BufferedReader br, BufferedWriter bw) throws IOException {
        tokenizer = new JackTokenizer(br);
        this.bw = bw;
        tabs = 0;
        currentCommand = "";
        
        if(tokenizer.hasMoreTokens()){
            tokenizer.advance();
            setCommand();

            fillOps();
        
            compileClass();
        }
        bw.close();
        br.close();
    }

    private void compileClass() {
        tokenTypes type = null;
        beginNonTerminal("class");
        eat("class");
        writeIdentifier();
        eat("{");
        type = tokenizer.tokenType();
        if (type == tokenTypes.KEYWORD) {
            while (tokenizer.keyWord() == keywordTypes.STATIC
                    || tokenizer.keyWord() == keywordTypes.FIELD) {
                compileClassVarDec();
            }
        }
        type = tokenizer.tokenType();
        if (type == tokenTypes.KEYWORD){
            while(tokenizer.keyWord() == keywordTypes.CONSTRUCTOR
                    || tokenizer.keyWord() == keywordTypes.FUNCTION
                    || tokenizer.keyWord() == keywordTypes.METHOD)
                compileSubroutine();
        }
        eat("}");
        endNonTerminal("class");
    }

    private void compileClassVarDec() {
        try {
            beginNonTerminal("classVarDec");

            writeClassVarMode();
            writeType();
            writeIdentifier();
            while (",".equals(currentCommand)) {
                eat(",");
                writeIdentifier();
            }
            eat(";");

            endNonTerminal("classVarDec");

        } catch (Exception e) {
        }
    }

    private void compileSubroutine() {
        beginNonTerminal("subroutineDec");

        writeSubFunction();
        writeReturnType();
        writeIdentifier();

        eat("(");
        compileParameterList();
        eat(")");

        subroutineBody();

        endNonTerminal("subroutineDec");
    }

    private void subroutineBody() {
        beginNonTerminal("subroutineBody");
        
        eat("{");
        
        boolean isKeyword = (tokenizer.tokenType() == tokenTypes.KEYWORD);
        if(isKeyword && tokenizer.keyWord() == keywordTypes.VAR){
            while (tokenizer.keyWord() == keywordTypes.VAR) {
                compileVarDec();
            }
        }
        
        compileStatements();
        
        eat("}");
        
        endNonTerminal("subroutineBody");
    }
    
    private void compileParameterList() { 
        beginNonTerminal("parameterList");
        
        if (!")".equals(currentCommand)) {
            writeType();
            writeIdentifier();
            while (",".equals(currentCommand)) {
                eat(",");
                writeType();
                writeIdentifier();
            }
        }
        

        endNonTerminal("parameterList");
    }

    private void compileVarDec() {
        beginNonTerminal("varDec");
        
        eat("var");
        
        writeType();
        writeIdentifier();
        
        while(currentCommand.equals(",")){
            eat(",");
            writeIdentifier();
        }
        
        eat(";");
        
        endNonTerminal("varDec");
    }

    private void compileStatements() {
        beginNonTerminal("statements");
        
        while(tokenizer.tokenType() == tokenTypes.KEYWORD){
            switch(tokenizer.keyWord()){
                case LET:
                    compileLet();
                    break;
                case IF:
                    compileIf();
                    break;
                case WHILE:
                    compileWhile();
                    break;
                case DO:
                    compileDo();
                    break;
                case RETURN:
                    compileReturn();
                    break;
            }
        }
        endNonTerminal("statements");
    }

    private void compileDo() {
        beginNonTerminal("doStatement");
        
        eat("do");
        writeIdentifier();
        writeSubroutineCall();
        eat(";");
        endNonTerminal("doStatement");
    }

    private void compileLet() {
        beginNonTerminal("letStatement");
        
        eat("let");
        writeIdentifier();
        
        if(currentCommand.equals("[")){
            eat("[");
            compileExpression();
            eat("]");
        }
        
        eat("=");
        compileExpression();
        eat(";");
        
        endNonTerminal("letStatement");
    }

    private void compileWhile() {
        beginNonTerminal("whileStatement");
        
        eat("while");
        eat("(");
        compileExpression();
        eat(")");
        
        eat("{");
        compileStatements();
        eat("}");
        
        endNonTerminal("whileStatement");
    }

    private void compileReturn() {
        beginNonTerminal("returnStatement");
        
        eat("return");
        if(!currentCommand.equals(";")){
            compileExpression();
        }
        eat(";");
        
        endNonTerminal("returnStatement");
    }

    private void compileIf() {
        beginNonTerminal("ifStatement");
        
        eat("if");
        
        eat("(");
        compileExpression();
        eat(")");
        
        eat("{");
        compileStatements();
        eat("}");
        
        boolean isKeyword = tokenizer.tokenType() == tokenTypes.KEYWORD;
        if(isKeyword && tokenizer.keyWord() == keywordTypes.ELSE){
            eat("else");
            eat("{");
            compileStatements();
            eat("}");
        }
        
        endNonTerminal("ifStatement");
    }

    private void compileExpression() {
        beginNonTerminal("expression");
        
        compileTerm();
        
        while(ops.contains(currentCommand)){
            eat(currentCommand);
            compileTerm();
        }
        endNonTerminal("expression");
    }

    private void compileTerm() {
        beginNonTerminal("term");
        
        writeTermType();
        
        endNonTerminal("term");
    }

    private void compileExpressionList() {
        boolean hasExpression = !(currentCommand.equals(")"));
        beginNonTerminal("expressionList");
        if(hasExpression){
            
            compileExpression();
            while(currentCommand.equals(",")){
                eat(",");
                compileExpression();
            }
            
        }
        endNonTerminal("expressionList");
    }
    
    private void writeClassVarMode(){
        if(tokenizer.tokenType() == tokenTypes.KEYWORD){
            switch(tokenizer.keyWord()) {
                case STATIC:
                case FIELD:
                    eat(currentCommand);
                    break;
                default:
                    throw new Error("Wrong Class mode!");
            }
        }
    }
    
    private void writeType(){
        if (tokenizer.tokenType() == tokenTypes.IDENTIFIER) {
            eat(currentCommand);
        } else if(tokenizer.tokenType() == tokenTypes.KEYWORD){
            switch(tokenizer.keyWord()){
                case INT:
                case CHAR:
                case BOOLEAN:
                    eat(currentCommand);
                    break;
                default:
                    throw new Error("Wrong type!");
            }
        } else{
            throw new Error("Wrong type!");
        }
    }
    
    private void writeIdentifier(){
        if (tokenizer.tokenType() == tokenTypes.IDENTIFIER) {
            eat(currentCommand);
        } else {
            throw new Error("Give a name, not a keyword.");
        }
    }
    
    private void writeReturnType(){
        if (tokenizer.tokenType() == tokenTypes.KEYWORD) {
            if(tokenizer.keyWord() == keywordTypes.VOID){
                eat(currentCommand);
            } else{
                throw new Error("Wrong return!");
            }
        } else {
            writeType();
        }
    }
    
    private void eat(String command) {
        terminal(command);
        
        if (!command.equals(currentCommand)) {
            throw new Error("Wrong syntax.");
        } else {
            if(tokenizer.hasMoreTokens()){
                tokenizer.advance();
                setCommand();
            }
        }
    }

    private void setCommand() {
        tokenTypes type = tokenizer.tokenType();

        switch (type) {
            case KEYWORD:
                currentCommand = tokenizer.keyWord().toString().toLowerCase();
                break;

            case SYMBOL:
                currentCommand = "" + tokenizer.symbol();
                break;

            case IDENTIFIER:
                currentCommand = tokenizer.identifier();
                break;

            case INT_CONST:
                currentCommand = "" + tokenizer.intVal();
                break;

            case STRING_CONST:
                currentCommand = tokenizer.stringVal();
                break;

        }
    }

    private void terminal(String command) {
        try {
            if (command.equals("<")) {
                command = "&lt;";
            } else if (command.equals(">")) {
                command = "&gt;";
            } else if (command.equals("\"")) {
                command = "&quot;";
            } else if (command.equals("&")) {
                command = "&amp;";
            }else {

            }
            String type = "";
            switch(tokenizer.tokenType()){
                case INT_CONST:
                    type = "integerConstant";
                    break;
                case STRING_CONST:
                    type = "stringConstant";
                    break;
                default:
                    type = tokenizer.tokenType().toString().toLowerCase();
                    break;
            }
            
            
            String formatted = String.format("\t".repeat(tabs) + "<%1$s> %2$s </%1$s>", type, command);
            bw.write(formatted);
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(CompilationEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeSubFunction() {
        if (tokenizer.tokenType() == tokenTypes.KEYWORD) {
            switch (tokenizer.keyWord()) {
                case CONSTRUCTOR:
                case FUNCTION:
                case METHOD:
                    eat(currentCommand);
                    break;
                default:
                    throw new Error("Wrong syntax.");
                }
        } else {
            throw new Error("Wrong syntax.");
        }
    }
    
    private void beginNonTerminal(String nonTer){
        try {
            String formatted = String.format("\t".repeat(tabs) + "<%s>", nonTer);
            bw.write(formatted);
            bw.newLine();
            tabs++;
        } catch (IOException ex) {
            Logger.getLogger(CompilationEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void endNonTerminal(String nonTer){
        try {
            tabs--;
            String formatted = String.format("\t".repeat(tabs) + "</%s>", nonTer);
            bw.write(formatted);
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(CompilationEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeSubroutineCall(){
       if(currentCommand.equals(".")){
            eat(".");
            writeIdentifier();
        }
        eat("(");
        compileExpressionList();
        eat(")");
    }
    
    private void writeTermType() {
        tokenTypes type = tokenizer.tokenType();
        switch(type){
            case INT_CONST:
                eat(currentCommand);
                break;
            case STRING_CONST:
                eat(currentCommand);
                break;
            case IDENTIFIER:
                writeTermIdentifier();
                break;
            case KEYWORD:
                writeTermKeyword();
                break;
            case SYMBOL:
                writeTermSymbol();
                break;
            default:
                throw new Error("Wrong syntax!");
        }
    }

    private void writeTermIdentifier() {
        eat(currentCommand);
        
        if(currentCommand.equals("[")){
            eat("[");
            compileExpression();
            eat("]");
        } else if(currentCommand.equals("(") || currentCommand.equals(".")){
            writeSubroutineCall();
        }
    }
    
    private void writeTermKeyword() {
        switch(tokenizer.keyWord()){
            case TRUE:
            case FALSE:
            case NULL:
            case THIS:
                eat(currentCommand);
                break;
            default:
                throw new Error("Not a keyword constant!");
        }
    }

    private void writeTermSymbol() {
        switch(tokenizer.symbol()){
            case '(':
                eat("(");
                compileExpression();
                eat(")");
                break;
            case '-':
            case '~':
                eat(currentCommand);
                compileTerm();
                break;
            default:
                throw new Error("Wrong Symbol!");
        }
    }
    
    private void fillOps() {
        ops = new LinkedList<String>();
        try {
            BufferedReader opFile = new BufferedReader(new FileReader(opFileName));
            String line = "";
            while((line = opFile.readLine()) != null){
                ops.add(line);
            }
        } catch (Exception e) {
        }
    }
}
