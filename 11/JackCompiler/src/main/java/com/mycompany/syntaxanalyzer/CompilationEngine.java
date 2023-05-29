/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syntaxanalyzer;

import static com.mycompany.syntaxanalyzer.JackTokenizer.keywordTypes;
import com.mycompany.syntaxanalyzer.JackTokenizer.tokenTypes;
import com.mycompany.syntaxanalyzer.SymbolTable.Kinds;
import static com.mycompany.syntaxanalyzer.SymbolTable.Kinds.NONE;
import com.mycompany.syntaxanalyzer.VMWriter.Segment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.api.tree.Tree;

/**
 *
 * @author junlis
 */
public class CompilationEngine {

    private String opFileName = "OpList";
    private HashMap<String, VMWriter.Commands> ops;

    private SymbolTable st;

    private String currentCommand;
    private JackTokenizer tokenizer;

    private VMWriter writer;
    private int tabs;

    private String className;
    private int locals;
    private int args;

    private String whileLabel = "WHILE_";
    private int whileNum;

    private String ifLabel = "IF_";
    private int ifNum;

    private boolean firstPriority = false;
    
    private boolean hasPreviousTerm = false;

    private String returnType;
    private String actualIdetifierType;
    private Kinds actualidentifierKind;

    public CompilationEngine(BufferedReader br, VMWriter writer) throws IOException {
        tokenizer = new JackTokenizer(br);
        this.writer = writer;
        currentCommand = "";

        actualIdetifierType = "";
        actualidentifierKind = null;
        className = "";
        args = 0;
        locals = 0;
        whileNum = 0;
        ifNum = 0;

        if (tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            setCommand();

            fillOps();

            compileClass();
        }
        writer.close();
        br.close();
    }

    private void compileClass() {
        tokenTypes type = null;
        actualIdetifierType = "class";

        eat("class");
        st = new SymbolTable();
        className = currentCommand;
        writeClassIdentifier();
        eat("{");
        type = tokenizer.tokenType();
        if (type == tokenTypes.KEYWORD) {
            while (tokenizer.keyWord() == keywordTypes.STATIC
                    || tokenizer.keyWord() == keywordTypes.FIELD) {
                compileClassVarDec();
            }
        }
        type = tokenizer.tokenType();
        if (type == tokenTypes.KEYWORD) {
            while (tokenizer.keyWord() == keywordTypes.CONSTRUCTOR
                    || tokenizer.keyWord() == keywordTypes.FUNCTION
                    || tokenizer.keyWord() == keywordTypes.METHOD) {
                st.startSubroutine();
                compileSubroutine();
                locals = 0;
            }
        }
        eat("}");
    }

    private void compileClassVarDec() {
        getClassVarMode();
        eat(currentCommand);
        writeType();
        writeIdentifier();
        while (",".equals(currentCommand)) {
            eat(",");
            writeIdentifier();
        }
        eat(";");
    }

    private void compileSubroutine() {
        String subName = "";
        actualIdetifierType = tokenizer.keyWord().toString().toLowerCase();
        keywordTypes subroutineType = tokenizer.keyWord();
        eat(currentCommand);

        getReturnType();
        eat(currentCommand);
        subName = currentCommand;
        writeIdentifier();
        if (subroutineType == keywordTypes.METHOD){
            st.define("this", className, Kinds.ARGUMENT);
        }
        eat("(");
        compileParameterList();
        eat(")");
        if (subroutineType == keywordTypes.CONSTRUCTOR) {
            constructorBody(subName);

        } else if (subroutineType == keywordTypes.METHOD) {
            methodBody(subName);
        } else {
            subroutineBody(subName);
        }
    }

    private void subroutineBody(String subName) {

        eat("{");
        boolean isKeyword = (tokenizer.tokenType() == tokenTypes.KEYWORD);
        if (isKeyword && tokenizer.keyWord() == keywordTypes.VAR) {
            while (tokenizer.keyWord() == keywordTypes.VAR) {
                compileVarDec();
            }
        }
        writer.writeFunction(className + "." + subName, locals);
        compileStatements();

        eat("}");

    }

    private void methodBody(String subName) {

        eat("{");
        boolean isKeyword = (tokenizer.tokenType() == tokenTypes.KEYWORD);
        if (isKeyword && tokenizer.keyWord() == keywordTypes.VAR) {
            while (tokenizer.keyWord() == keywordTypes.VAR) {
                compileVarDec();
            }
        }
        writer.writeFunction(className + "." + subName, locals);
        writer.writePush(Segment.ARGUMENT, 0);
        writer.writePop(Segment.POINTER, 0);
        
        compileStatements();

        eat("}");

    }

    private void constructorBody(String subName) {

        eat("{");

        boolean isKeyword = (tokenizer.tokenType() == tokenTypes.KEYWORD);
        if (isKeyword && tokenizer.keyWord() == keywordTypes.VAR) {
            while (tokenizer.keyWord() == keywordTypes.VAR) {
                compileVarDec();
            }
        }
        writer.writeFunction(className + "." + subName, locals);

        writer.writePush(Segment.CONSTANT, st.varCount(Kinds.FIELD));
        writer.writeCall("Memory.alloc", 1);
        writer.writePop(Segment.POINTER, 0);

        compileStatements();

        eat("}");

    }

    private void compileParameterList() {
        actualidentifierKind = Kinds.ARGUMENT;
        if (!")".equals(currentCommand)) {
            String type = getParamType();
            eat(currentCommand);
            if (IsIdentifier()) {
                st.define(currentCommand, type, actualidentifierKind);
            }
            eat(currentCommand);
            while (",".equals(currentCommand)) {
                eat(",");
                writeType();
                writeIdentifier();
            }
        }
    }

    private String getParamType() {
        String type = "";
        if (tokenizer.tokenType() == tokenTypes.IDENTIFIER) {
            type = currentCommand;
            return type;
        } else if (tokenizer.tokenType() == tokenTypes.KEYWORD) {
            switch (tokenizer.keyWord()) {
                case INT:
                case CHAR:
                case BOOLEAN:
                    type = currentCommand;
                    return type;
                default:
                    throw new Error("Wrong type!");
            }
        } else {
            throw new Error("Wrong type!");
        }
    }

    private void compileVarDec() {

        eat("var");
        actualidentifierKind = Kinds.VAR;
        String varType = currentCommand;
        String name = "";

        if (IsIdentifier()) {
            actualIdetifierType = currentCommand;
            eat(currentCommand);
        } else if (!(tokenizer.tokenType() == tokenTypes.KEYWORD && isVarType())) {
            throw new Error("Not type.");
        } else {
            eat(varType);
        }

        if (tokenizer.tokenType() != tokenTypes.IDENTIFIER) {
            throw new Error("Not a identifier.");
        }
        name = currentCommand;
        st.define(name, varType, actualidentifierKind);
        eat(currentCommand);
        locals++;
        while (currentCommand.equals(",")) {
            eat(",");
            IsIdentifier();
            name = currentCommand;
            eat(name);
            st.define(name, varType, actualidentifierKind);
            locals++;
        }

        eat(";");

    }

    private void compileStatements() {

        while (tokenizer.tokenType() == tokenTypes.KEYWORD) {
            switch (tokenizer.keyWord()) {
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
    }

    private void compileDo() {

        eat("do");
        writeSubroutineCall();
        eat(";");
        writer.writePop(VMWriter.Segment.TEMP, 0);
    }

    private void compileLet() {

        eat("let");
        if (st.kindOf(currentCommand) == NONE) {
            throw new Error("The variable " + currentCommand + " is not defined.");
        }
        String varName = currentCommand;
        eat(currentCommand);

        if (currentCommand.equals("[")) {
            writer.writePush(getSegment(varName), st.indexOf(varName));
            eat("[");
            compileExpression();
            eat("]");
            writer.writeArithmetic(VMWriter.Commands.ADD);
            
            eat("=");
            compileExpression();
            eat(";");
            writer.writePop(Segment.TEMP, 0);
            writer.writePop(Segment.POINTER, 1);
            writer.writePush(Segment.TEMP, 0);
            writer.writePop(Segment.THAT, 0);
        } else {
            eat("=");
            compileExpression();
            eat(";");

            Segment seg = getSegment(varName);
            writer.writePop(seg, st.indexOf(varName));
        }

    }

    private void compileWhile() {
        int initialWhile = whileNum;
        whileNum++;
        writer.writeLabel(whileLabel + "F_" + initialWhile);
        eat("while");
        eat("(");
        compileExpression();
        eat(")");

        writer.writeArithmetic(VMWriter.Commands.NOT);
        writer.writeIf(whileLabel + "T_" + initialWhile);

        eat("{");
        compileStatements();
        eat("}");
        writer.writeGoto(whileLabel + "F_" + initialWhile);
        writer.writeLabel(whileLabel + "T_" + initialWhile);

    }

    private void compileReturn() {

        eat("return");
        if (!currentCommand.equals(";")) {
            compileExpression();
        }
        eat(";");
        if (returnType.equals(keywordTypes.VOID.toString().toLowerCase())) {
            writer.writePush(VMWriter.Segment.CONSTANT, 0);
        }
        writer.writeReturn();

    }

    private void compileIf() {
        int initialIfNum = ifNum;
        ifNum++;
        eat("if");

        eat("(");
        compileExpression();
        eat(")");
        writer.writeArithmetic(VMWriter.Commands.NOT);
        writer.writeIf(ifLabel + "T_" + initialIfNum);
        eat("{");
        compileStatements();
        eat("}");

        writer.writeGoto(ifLabel + "F_" + initialIfNum);
        writer.writeLabel(ifLabel + "T_" + initialIfNum);
        boolean isKeyword = tokenizer.tokenType() == tokenTypes.KEYWORD;
        if (isKeyword && tokenizer.keyWord() == keywordTypes.ELSE) {
            eat("else");
            eat("{");
            compileStatements();
            eat("}");
        }

        writer.writeLabel(ifLabel + "F_" + initialIfNum);
    }

    private void compileExpression() {
        compileTerm();

        while (ops.containsKey(currentCommand)) {
            VMWriter.Commands command = ops.get(currentCommand);
            eat(currentCommand);
            hasPreviousTerm = true;
            compileTerm();
            if (command == VMWriter.Commands.NEG && hasPreviousTerm) {
                writer.writeArithmetic(VMWriter.Commands.SUB);
            } else {
                writer.writeArithmetic(command);
            }

        }
        hasPreviousTerm = false;
    }
    
    private void compileExpressionFirst() {
        compileTerm();

        while (ops.containsKey(currentCommand)) {
            VMWriter.Commands command = ops.get(currentCommand);
            eat(currentCommand);
            hasPreviousTerm = true;
            compileTerm();
            if (command == VMWriter.Commands.NEG && hasPreviousTerm) {
                writer.writeArithmetic(VMWriter.Commands.SUB);
            } else {
                writer.writeArithmetic(command);
            }

        }
        hasPreviousTerm = false;
    }

    private void compileTerm() {
        tokenTypes type = tokenizer.tokenType();
        switch (type) {
            case INT_CONST:
                writer.writePush(VMWriter.Segment.CONSTANT, Integer.parseInt(currentCommand));
                eat(currentCommand);
                break;
            case STRING_CONST:
                writer.writePush(Segment.CONSTANT, currentCommand.length());
                writer.writeCall("String.new", 1);
                for (char c : currentCommand.toCharArray()) {
                    writer.writePush(Segment.CONSTANT, (int) c);
                    writer.writeCall("String.appendChar", 2);
                }
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

    private void compileExpressionList() {
        boolean hasExpression = !(currentCommand.equals(")"));
        if (hasExpression) {
            args++;
            compileExpression();
            while (currentCommand.equals(",")) {
                eat(",");
                compileExpression();
                args++;
            }
        }
    }

    private void getClassVarMode() {
        switch (tokenizer.keyWord()) {
            case STATIC:
                actualidentifierKind = Kinds.STATIC;
                break;
            case FIELD:
                actualidentifierKind = Kinds.FIELD;
                break;
            default:
                throw new Error("Wrong Class mode!");
        }
    }

    private void writeType() {
        if (tokenizer.tokenType() == tokenTypes.IDENTIFIER) {
            actualIdetifierType = currentCommand;
            eat(currentCommand);
        } else if (tokenizer.tokenType() == tokenTypes.KEYWORD) {
            switch (tokenizer.keyWord()) {
                case INT:
                case CHAR:
                case BOOLEAN:
                    actualIdetifierType = currentCommand;
                    eat(currentCommand);
                    break;
                default:
                    throw new Error("Wrong type!");
            }
        } else {
            throw new Error("Wrong type!");
        }
    }

    private void writeClassIdentifier() {
        if (tokenizer.tokenType() == tokenTypes.IDENTIFIER) {
            if (!actualIdetifierType.matches("class")) {
                throw new Error("Missing a 'class' keyword.");
            }
            eat(currentCommand);
        } else {

        }
    }

    private void writeIdentifier() {
        if (tokenizer.tokenType() == tokenTypes.IDENTIFIER) {
            if (!actualIdetifierType.matches("class|constructor|function|method")) {
                st.define(currentCommand, actualIdetifierType, actualidentifierKind);
            }
            eat(currentCommand);
        } else {
            throw new Error("Give a name that isn't a keyword.");
        }
    }

    private void getReturnType() {
        if (tokenizer.tokenType() == tokenTypes.KEYWORD) {
            keywordTypes key = tokenizer.keyWord();
            switch (key) {
                case INT:
                case CHAR:
                case BOOLEAN:
                case VOID:
                    returnType = key.toString().toLowerCase();
                    break;
                default:
                    throw new AssertionError();
            }
        } else if (IsIdentifier()) {
            returnType = currentCommand;
        } else {
            throw new Error("Wrong return type.");
        }
    }

    private void eat(String command) {
        //terminal(command);

        if (!command.equals(currentCommand)) {
            throw new Error("Wrong syntax.");
        } else {
            if (tokenizer.hasMoreTokens()) {
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
        String formatted = "";
        String type = "";
        tokenTypes tokenType = tokenizer.tokenType();
        if (tokenType == tokenType.IDENTIFIER) {
            formatted = getIdentifierInfo(command);
        } else {
            type = tokenizer.tokenType().toString().toLowerCase();
        }

        formatted = String.format("\t".repeat(tabs) + "<%1$s> %2$s </%1$s>", type, command);
    }

    private String getIdentifierInfo(String command) {
        String formatted = "";
        if (actualIdetifierType.matches("class|constructor|function|method")
                || actualIdetifierType.equals(command)) {
            formatted = String.format("\t".repeat(tabs) + "<%1$s> %2$s </%1$s>", actualIdetifierType, command);
        } else {
            String type = st.typeOf(command);
            String category = st.kindOf(command).toString().toLowerCase();
            String index = Integer.toString(st.indexOf(command));
            formatted = String.format("\t".repeat(tabs)
                    + "<identifier> %1$s %2$s %3$s %4$s </identifier>", category, type, command, index);
        }

        return formatted;
    }

    private void writeSubroutineCall() {
        String className = "";
        String allName = "";
        boolean hasOneWord = true;
        if (IsIdentifier() && st.kindOf(currentCommand) != NONE) {
            writer.writePush(getSegment(currentCommand), st.indexOf(currentCommand));
            args++;
            className = st.typeOf(currentCommand);

        } else {
            className = currentCommand;
        }
        eat(currentCommand);
        if (currentCommand.equals(".")) {
            eat(".");
            allName = className + "." + currentCommand;
            IsIdentifier();
            eat(currentCommand);
        } else {
            allName = this.className + "." + className;
            writer.writePush(Segment.POINTER, 0);
            args++;
        }
        eat("(");
        compileExpressionList();
        eat(")");

        writer.writeCall(allName, args);
        args = 0;
    }

    private void writeSubroutineCall(String className) {
        String subName = "";
        if (IsIdentifier() && st.kindOf(className) != NONE) {
            writer.writePush(getSegment(className), st.indexOf(className));
            args++;
            className = st.typeOf(className);
        }
        subName = "." + currentCommand;
        IsIdentifier();
        eat(currentCommand);
        eat("(");
        compileExpressionList();
        eat(")");

        writer.writeCall(className + subName, args);
        args = 0;
    }

    private boolean IsIdentifier() {
        if (tokenizer.tokenType() == tokenTypes.IDENTIFIER) {
            return true;
        } else {
            return false;
        }
    }

    private void writeTermIdentifier() {
        String preName = currentCommand;
        eat(currentCommand);

        if (currentCommand.equals("[")) {
            writer.writePush(getSegment(preName), st.indexOf(preName));
            eat("[");
            compileExpression();
            eat("]");
            writer.writeArithmetic(VMWriter.Commands.ADD);
            writer.writePop(Segment.POINTER, 1);
            writer.writePush(Segment.THAT, 0);
        } else if (currentCommand.equals("(")) {
            writeSubroutineCall();
        } else if (currentCommand.equals(".")) {
            eat(".");
            writeSubroutineCall(preName);
        } else {
            writer.writePush(getSegment(preName), st.indexOf(preName));
        }
    }

    private void writeTermKeyword() {
        switch (tokenizer.keyWord()) {
            case TRUE:
                writer.writePush(Segment.CONSTANT, 0);
                writer.writeArithmetic(VMWriter.Commands.NOT);
                break;
            case FALSE:
            case NULL:
                writer.writePush(Segment.CONSTANT, 0);
                break;
            case THIS:
                writer.writePush(Segment.POINTER, 0);
                break;
            default:
                throw new Error("Not a keyword constant!");
        }
        eat(currentCommand);
    }

    private void writeTermSymbol() {
        switch (tokenizer.symbol()) {
            case '(':
                eat("(");
                compileExpressionFirst();
                eat(")");
                break;
            case '-':
                if (hasPreviousTerm) {
                    eat(currentCommand);
                    compileTerm();
                    writer.writeArithmetic(VMWriter.Commands.NEG);
                } else {
                    eat(currentCommand);
                    compileTerm();
                    writer.writeArithmetic(VMWriter.Commands.NOT);
                }
                break;
            case '~':
                eat(currentCommand);
                compileTerm();
                writer.writeArithmetic(VMWriter.Commands.NOT);
                break;
            default:
                throw new Error("Wrong Symbol!");
        }
    }

    private void fillOps() {
        ops = new HashMap<String, VMWriter.Commands>();
        ops.put("+", VMWriter.Commands.ADD);
        ops.put("-", VMWriter.Commands.SUB);
        ops.put("~", VMWriter.Commands.NOT);
        ops.put("*", VMWriter.Commands.MULT);
        ops.put("/", VMWriter.Commands.DIV);
        ops.put("&", VMWriter.Commands.AND);
        ops.put("|", VMWriter.Commands.OR);
        ops.put("<", VMWriter.Commands.LT);
        ops.put(">", VMWriter.Commands.GT);
        ops.put("=", VMWriter.Commands.EQ);
    }

    private Segment getSegment(String varName) {
        Segment seg = null;
        switch (st.kindOf(varName)) {
            case ARGUMENT:
                seg = VMWriter.Segment.ARGUMENT;
                break;
            case STATIC:
                seg = VMWriter.Segment.STATIC;
                break;
            case FIELD:
                seg = VMWriter.Segment.THIS;
                break;
            case VAR:
                seg = VMWriter.Segment.LOCAL;
                break;
            default:
                throw new AssertionError();
        }
        return seg;
    }

    private boolean isVarType() {
        if (tokenizer.tokenType() == tokenTypes.KEYWORD) {
            switch (tokenizer.keyWord()) {
                case INT:
                case CHAR:
                case BOOLEAN:
                    actualIdetifierType = currentCommand;
                    return true;
                default:
                    throw new Error("Wrong type!");
            }
        } else {
            throw new Error("Wrong type!");
        }
    }

}
