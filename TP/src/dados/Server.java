/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dados;

import Mensagens.MensageiroSV;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    private int id;
    private int estado; //1-livre; 2-ocupado; 3-emleilao; 4-ocupadoPorLeilao
    private int tipo; //1-pequeno, 2-media, 3-grande
    private int preco;
    private LocalDateTime horaDeCompra;
    private BufferedWriter out;
    private String dono; //quem ocupa
    private double valorAluger;
    private ReentrantLock r;
    
    public Server(int id, int tipo){
        this.id = id;
        this.estado = 1;
        this.tipo = tipo;
        this.dono = "";
        this.preco = 5*tipo;
        this.horaDeCompra = null;
        this.valorAluger = this.preco;
        this.out = null;
        this.r = new ReentrantLock();
    }
    
    public synchronized LocalDateTime getHoraDeCompra(){
        return this.horaDeCompra;
    }
    
    public synchronized void setValorAluger(double preco){
        this.valorAluger = preco;
    }
    
    public synchronized double getValorAluger(){
        return this.valorAluger;
    }
    
    public synchronized ReentrantLock getLock(){
        return this.r;
    }
    
    public synchronized int getPreco(){
        return preco;
    }
    
    public synchronized void setOut(BufferedWriter out){
        this.out = out;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized int getEstado() {
        return estado;
    }

    public synchronized int getTipo() {
        return tipo;
    }

    public synchronized String getDono() {
        return dono;
    }
    
    public synchronized void setDate(){

        this.horaDeCompra = LocalDateTime.now();
        
    }

    public synchronized void setEstado(int estado) {
        if(estado == 2 || estado == 4){
            setDate();
        }
        if(estado == 1){
            this.dono = "";
            this.horaDeCompra = null;
        }
        this.estado = estado;
    }

    public synchronized void setDono(String dono) {
        this.dono = dono;
    }
    
    public void avisarCliente(){
        String msg = "1-O seu servidor de id: "+ this.id + " foi atribuido a outro cliente";
        MensageiroSV m = new MensageiroSV(this.out, msg);
        Thread t1 = new Thread(m);
        t1.start();
    }
    
    /*public String getLeilaoString(){
        String msg = "Server: "+ id + " " + this.leilao.toString();
        return msg;
    }
    
    public void startLeilao(){
        this.leilao = new Leilao();
        Thread t1 = new Thread(leilao);
        t1.start();
    }*/

    @Override
    public synchronized String toString() {
        return "Server:" + "" + id + "; tipo=" + tipo + "; estado=" + estado ;
    }
    
    
  
}
