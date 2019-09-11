/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MensageiroSV implements Runnable {

    private String msg;
    private BufferedWriter out;

    public MensageiroSV(BufferedWriter out, String msg) {
        this.out = out;
        this.msg = msg;
    }

    /*public void enviaMensagem() throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }*/

    @Override
    public void run() {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException ex) {
        }
    }

}
