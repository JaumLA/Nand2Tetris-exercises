/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syntaxanalyzer;

import java.security.KeyPair;
import java.util.HashMap;

/**
 *
 * @author junlis
 */
public class SymbolTable {

    public enum Kinds {
        STATIC, FIELD, ARGUMENT, VAR, NONE
    }

    private HashMap<String, HashMap<String, String>> classTable;

    private HashMap<String, HashMap<String, String>> subroutineTable;

    private HashMap<String, Integer> indexes;

    public SymbolTable() {
        classTable = new HashMap<>();
        resetClassIndexes();
    }

    public void startSubroutine() {
        subroutineTable = new HashMap<>();
        resetSubIndexes();
    }

    public void define(String name, String type, Kinds kind) {
        if (kind == Kinds.STATIC || kind == Kinds.FIELD) {
            classTable.put(name, new HashMap<>());
            classTable.get(name).put("type", type);
            classTable.get(name).put("kind", kind.toString());

            for (String inType : indexes.keySet()) {
                if (inType.equals(kind.toString())) {
                    int increment = indexes.get(inType) + 1;
                    classTable.get(name).put("index", Integer.toString(increment));
                    indexes.put(inType, increment);
                    break;
                }
            }
        } else {
            subroutineTable.put(name, new HashMap<>());
            subroutineTable.get(name).put("type", type);
            subroutineTable.get(name).put("kind", kind.toString());

            for (String inType : indexes.keySet()) {
                if (inType.equals(kind.toString())) {
                    int increment = indexes.get(inType) + 1;
                    subroutineTable.get(name).put("index", Integer.toString(increment));
                    indexes.put(inType, increment);
                }
            }
        }

    }

    public int varCount(Kinds kind) {
        int count = 1;
        String strKind = kind.toString();

        for (String type : indexes.keySet()) {
            if (type.equals(strKind)) {
                count += indexes.get(type);
                break;
            }
        }
        return count;
    }

    public Kinds kindOf(String name) {
        Kinds kind = Kinds.NONE;
        if (subroutineTable != null && subroutineTable.containsKey(name)) {
            for (Kinds type : Kinds.values()) {
                if (subroutineTable.get(name).get("kind").equals(type.toString())) {
                    kind = type;
                    break;
                }
            }
        } else if (classTable.containsKey(name)) {
            for (Kinds type : Kinds.values()) {
                if (classTable.get(name).get("kind").equals(type.toString())) {
                    kind = type;
                    break;
                }
            }
        }

        return kind;
    }

    public String typeOf(String name) {
        if (subroutineTable != null && subroutineTable.containsKey(name)) {
            return subroutineTable.get(name).get("type");
        } else if (classTable.containsKey(name)) {
            return classTable.get(name).get("type");
        }
        return "identifierTypeNotFound";
    }

    public int indexOf(String name) {
        int i = 0;
        if (subroutineTable != null && subroutineTable.containsKey(name)) {
            i = Integer.parseInt(subroutineTable.get(name).get("index"));
        } else if (classTable.containsKey(name)) {
            i = Integer.parseInt(classTable.get(name).get("index"));
        }
        return i;
    }

    private void resetClassIndexes() {
        int def = -1;
        indexes = new HashMap<>();
        indexes.put("STATIC", def);
        indexes.put("FIELD", def);
    }

    private void resetSubIndexes() {
        int def = -1;
        indexes.put("VAR", def);
        indexes.put("ARGUMENT", def);
    }
}
