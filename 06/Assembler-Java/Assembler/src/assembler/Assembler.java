/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
/**
 *
 * @author junlis
 */
public class Assembler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Give the path of the .asm file.");
            System.exit(1);
        }
        System.out.println(args[0]);
        Translater t = new Translater(args[0]);
        t.translate();
        System.out.println("File translated!");
    }
    
}
