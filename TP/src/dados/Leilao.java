/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dados;

import Mensagens.MensageiroSV;
import dados.Server;
import java.io.BufferedWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Leilao implements Runnable {

    private double ofertaInicial;
    private double ofertaAtual;
    private int aberto; //1- podes leiloar, 0- tempo acabou
    private String comprador;
    private BufferedWriter out;
    //private BufferedWriter oldOut;
    private ReentrantLock r;
    private Server server;
    private int flag;

    public Leilao(Server s) {
        this.ofertaInicial = 0.0;
        this.ofertaAtual = 0.0;
        this.aberto = 1;
        this.comprador = "";
        this.r = new ReentrantLock();
        this.server = s;
        this.out = null;
        //this.oldOut = null;
        this.flag = 0;
        s.setEstado(3);
    }

    public synchronized int getAberto() {
        return this.aberto;
    }

    public synchronized double getOfertaInicial() {
        return ofertaInicial;
    }

    public synchronized double getOfertaAtual() {
        return ofertaAtual;
    }

    public synchronized int getTempo() {
        return aberto;
    }

    public synchronized String getComprador() {
        return comprador;
    }

    public synchronized void setOfertaInicial(double ofertaInicial) {
        this.ofertaInicial = ofertaInicial;
    }

    public synchronized int setOfertaAtual(double ofertaAtual, BufferedWriter outnew, String comprador) {
        r.lock();
        if (aberto == 1) {
            if (this.ofertaAtual < ofertaAtual) {
                if (this.ofertaAtual != 0) {
                    enviaMensagem();
                }
                this.ofertaAtual = ofertaAtual;
                this.out = outnew;
                this.comprador = comprador;
                r.unlock();
                return 1;
            }
            r.unlock();
            return 0;
        }
        r.unlock();
        return 0;
    }

    public void enviaMensagem() {
        String msg = "1-A sua proposta no leilao: " + server.getId() + " foi batida";
        MensageiroSV m = new MensageiroSV(this.out, msg);
        Thread t1 = new Thread(m);
        t1.start();
    }

    public synchronized int getsvEstado() {
        return this.server.getEstado();
    }

    public synchronized void setTempo(int aberto) {
        this.aberto = aberto;
    }

    public synchronized void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public synchronized String toStringToLeiloes() {
        return "Server: " + server.getId() + " " + "ofertaInicial=" + ofertaInicial + "; ofertaAtual=" + ofertaAtual;
    }

    @Override
    public synchronized String toString() { //teste
        return "Server: " + server.getId() + "; Tipo= " + server.getTipo() + " " + "; ofertaInicial=" + ofertaInicial + "; ofertaAtual=" + ofertaAtual;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                wait(50000);
            } catch (InterruptedException ex) {

            }
            if (server.getEstado() == 3) {
                String msg = "";
                aberto = 0;
                if (ofertaAtual != 0.0) {
                    synchronized (server) {
                        server.setEstado(4);
                        server.setValorAluger(ofertaAtual);
                        server.setDono(comprador);
                        server.setOut(out);
                        msg = "1-Parabéns ganhou o leilao do servidor: " + server.getId();
                    }
                    MensageiroSV m = new MensageiroSV(out, msg);
                    Thread t1 = new Thread(m);
                    t1.start();
                } else {
                    server.setEstado(1);
                }
            }
        }
    }
}
