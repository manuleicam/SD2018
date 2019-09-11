/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import java.io.BufferedReader;
import java.io.IOException;


public class Mensageiro implements Runnable {
    private String msg;
    private int flag;
    private BufferedReader in;
    
    public Mensageiro(BufferedReader in){
        msg = "";
        flag = 0;
        this.in = in;
    }
    
    public synchronized String lerMensagem() throws InterruptedException{
        while(flag==0){
            wait();
        }
        
        flag =0;
        return this.msg;
    }
    
    public synchronized void guardarMensagem(String msg){
        this.msg = msg;
        flag = 1;
        notifyAll();
    }

    @Override
    public void run() {
        boolean x = true;
        String msg;
        String[] parts;
        while (x == true) {
            try {
                msg = in.readLine();
                parts = msg.split("-");
                switch (parts[0]) {
                    case "1":
                        System.out.println(parts[1]);
                        break;
                    default:
                        guardarMensagem(msg);
                        break;
                }

            } catch (IOException ex) {
            }

        }
    }
}
