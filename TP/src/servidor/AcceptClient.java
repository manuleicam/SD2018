/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import dados.BaseDeDados;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptClient {

    /**
     * @param args the command line arguments
     */
    
    public AcceptClient(){
        
    }
    
    public void inicio() throws IOException{
        
        ServerSocket ss = new ServerSocket(12345);
        
        Socket s;
        BaseDeDados bd = new BaseDeDados();
        bd.povoarServers();
        while(true){
            
            s = ss.accept();
            Servidor sv = new Servidor(s,bd);
            Thread t1 = new Thread(sv);
            t1.start();
            
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        new AcceptClient().inicio();
    }
    
    
}
