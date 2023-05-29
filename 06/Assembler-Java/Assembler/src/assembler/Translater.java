import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Translater {

    private String path;

    private HashMap<String, String> symbolTable;

    public Translater(String path) {
        this.path = path;
    }

    public void translate() {
        createPreSymbol();
        fillSymbolTable();
        parse();
    }

    private void parse() {
        String line = "";
        int varAddr = 16;
        String outputPath = path.replace(".asm", ".hack");
        try (BufferedReader br = new BufferedReader(new FileReader(path));
                BufferedWriter file = new BufferedWriter(new FileWriter(outputPath))) {

            while ((line = br.readLine()) != null) {

                String onlyCommand = getCommand(line);

                if (onlyCommand.isEmpty() || onlyCommand.matches("[(].*")) {
                    continue;
                }

                if (onlyCommand.matches("@.*[a-zA-Z].*")) {
                    String symbol = onlyCommand.substring(1);
                    boolean hasSymbol = symbolTable.containsKey(symbol);
                    String val = symbolTable.get(symbol);

                    if (hasSymbol && val == null) {
                        val = "" + varAddr;
                        symbolTable.replace(symbol, val);
                        String binInstruction = decTo16Bin(val);

                        file.write(binInstruction + "\n");
                        varAddr++;

                    } else if (hasSymbol) {
                        String binInstruction = decTo16Bin(val);

                        file.write(binInstruction + "\n");
                    }
                } else if (onlyCommand.matches("@.*")) {
                    String val = onlyCommand.substring(1);
                    String binInstruction = decTo16Bin(val);
                    file.write(binInstruction + "\n");

                } else {
                    String destPart = getDest(onlyCommand);
                    String compPart = getComp(onlyCommand);
                    String jumpPart = getJump(onlyCommand);
                    file.write("111" + compPart + destPart + jumpPart + "\n");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Translater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Translater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getJump(String onlyCommand) {
        boolean hasJump = onlyCommand.matches(".*;.*");
        String jump;
        if (hasJump) {
            int jIndex = onlyCommand.indexOf(";") + 1;
            String jumpPart = onlyCommand.substring(jIndex);

            HashMap<String, String> jumpCommands = new HashMap<>();
            jumpCommands.put("JGT", "001");
            jumpCommands.put("JEQ", "010");
            jumpCommands.put("JGE", "011");
            jumpCommands.put("JLT", "100");
            jumpCommands.put("JNE", "101");
            jumpCommands.put("JLE", "110");
            jumpCommands.put("JMP", "111");

            jump = jumpCommands.get(jumpPart);

        } else {
            jump = "000";
        }

        return jump;
    }

    private String getComp(String onlyCommand) {
        String compPart = onlyCommand.replaceFirst("(.*=)|(;.*)", "");
        HashMap<String, String> compCommands = new HashMap<>();
        String registerVal = (compPart.contains("M")) ? "M" : "A";
        String aBit = (registerVal.equals("M")) ? "1" : "0";

        compCommands.put("0", "101010");
        compCommands.put("1", "111111");
        compCommands.put("-1", "111010");
        compCommands.put("D", "001100");
        compCommands.put(registerVal, "110000");
        compCommands.put("!D", "001101");
        compCommands.put("!" + registerVal, "110001");
        compCommands.put("-D", "001111");
        compCommands.put("-" + registerVal, "110011");
        compCommands.put("D+1", "011111");
        compCommands.put("1+D", "011111");
        compCommands.put(registerVal + "+1", "110111");
        compCommands.put("1+" + registerVal, "110111");
        compCommands.put("D-1", "001110");
        compCommands.put(registerVal + "-1", "110010");
        compCommands.put("D+" + registerVal, "000010");
        compCommands.put(registerVal + "+D", "000010");
        compCommands.put("D-" + registerVal, "010011");
        compCommands.put(registerVal + "-D", "000111");
        compCommands.put("D&" + registerVal, "000000");
        compCommands.put(registerVal + "&D", "000000");
        compCommands.put("D|" + registerVal, "010101");
        compCommands.put(registerVal + "|D", "010101");

        String instruction = compCommands.get(compPart);

        return aBit + instruction;
    }

    private String getDest(String onlyCommand) {
        boolean hasDest = onlyCommand.matches("[AMD]{1,3}=.*");
        String aR;
        String dR;
        String mR;
        if (hasDest) {
            String destPart = onlyCommand.substring(0, onlyCommand.indexOf("="));
            aR = (destPart.contains("A") ? "1" : "0");
            dR = (destPart.contains("D") ? "1" : "0");
            mR = (destPart.contains("M") ? "1" : "0");

        } else {
            aR = "0";
            dR = "0";
            mR = "0";
        }

        return aR + dR + mR;
    }

    private String decTo16Bin(String decimal) {
        int num = Integer.parseInt(decimal);
        return String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0');
    }

    private String getCommand(String command) {
        String line = command;

        //remove comments
        line = line.replaceAll("//.*", "");

        //remove white spaces
        line = line.replaceAll("\\s+", "");

        return line;
    }

    private void fillSymbolTable() {
        String line = "";
        int lineCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            while ((line = br.readLine()) != null) {

                String onlyCommand = getCommand(line);

                if (onlyCommand.isEmpty()) {
                    continue;
                }

                if (onlyCommand.matches("@.*[a-zA-Z].*")) {
                    String symbol = onlyCommand.substring(1);
                    boolean hasSymbol = symbolTable.containsKey(symbol);

                    if (!hasSymbol) {
                        symbolTable.put(symbol, null);
                    }

                    lineCount++;

                } else if (onlyCommand.matches("[(].+[)]")) {
                    String symbol = onlyCommand.substring(1, onlyCommand.length() - 1);
                    boolean hasSymbol = symbolTable.containsKey(symbol);

                    if (hasSymbol) {
                        symbolTable.replace(symbol, "" + lineCount);
                    } else {
                        symbolTable.put(symbol, "" + lineCount);
                    }
                } else {
                    lineCount++;
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createPreSymbol() {
        symbolTable = new HashMap<>();

        symbolTable.put("SP", "0");
        symbolTable.put("LCL", "1");
        symbolTable.put("ARG", "2");
        symbolTable.put("THIS", "3");
        symbolTable.put("THAT", "4");

        for (int i = 0; i <= 15; i++) {
            symbolTable.put("R" + i, "" + i);
        }

        symbolTable.put("SCREEN", "16384");
        symbolTable.put("KBD", "24576");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
