/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import dados.BaseDeDados;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor implements Runnable {

    private BufferedWriter out;
    private BufferedReader in;
    private BaseDeDados bd;
    private String utilizador;

    public Servidor(Socket s, BaseDeDados bd) throws IOException {

        this.utilizador = "";
        this.out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.bd = bd;

    }

    @Override
    public void run() {
        boolean x = true;
        String msg;
        String[] parts;
        try {
            while (x) {

                msg = in.readLine();
                parts = msg.split("-");

                switch (parts[0]) {
                    case "1":
                        logIn(parts[1], parts[2]);
                        break;
                    case "2":
                        registar(parts[1], parts[2]);
                        break;
                    case "3":
                        verSvs();
                        break;
                    case "4":
                        verSvsEmLeilao();
                        break;
                    case "5":
                        verSaldo();
                        break;
                    case "6":
                        participarLeilao(parts[1]);
                        break;
                    case "7":
                        verSvsParaAlugarSV(parts[1]);
                        break;
                    case "8":
                        alugarSV(parts[1]);
                        break;
                    case "9":
                        verSvsUtilizador(utilizador);
                        break;
                    case "10":
                        cancelarSV(parts[1]);
                        break;
                    case "0":
                        break;
                }

            }
        } catch (IOException ex) {
        }
    }

    public void logIn(String user, String pass) throws IOException {
        if (bd.logIn(user, pass) == 0) {
            out.write("UserErrado");
            out.newLine();
            out.flush();
        } else if (bd.logIn(user, pass) == 2) {
            out.write("PassErrada");
            out.newLine();
            out.flush();
        } else {
            out.write("TudoCerto");
            out.newLine();
            out.flush();
            this.utilizador = user;
        }
    }

    public void registar(String user, String pass) throws IOException {

        if (bd.registar(user, pass) == 0) {
            out.write("UserExiste");
            out.newLine();
            out.flush();
        } else {
            out.write("UserCriado");
            out.newLine();
            out.flush();
        }
    }

    public void verSvs() throws IOException { //manda ao client os servidores
        String msg = bd.getServers();
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void verSvsEmLeilao() throws IOException {
        String msg = bd.getSvLeilao();
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void verSaldo() throws IOException {
        double saldo = bd.getClienteSaldo(utilizador);
        String msg = Double.toString(saldo);
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void participarLeilao(String numLeilao) throws IOException { // verificar se o numero é válido para um sv em leilao
        int num = Integer.parseInt(numLeilao);
        String msg;
        msg = bd.getLeilao(num);
        out.write(msg);
        out.newLine();
        out.flush();

        msg = in.readLine();
        String[] parts = msg.split("-");
        if (parts[0].equals("11")) {
            licitarServidor(num, parts[1]);
        }
    }

    public void verSvsParaAlugarSV(String tipoSV) throws IOException {
        int tipo = Integer.parseInt(tipoSV);
        String msg = bd.getServersDisponiveis(tipo);
        if (msg.equals("")) {
            out.write("Não há servidores desse tipo desponíveis!");
            out.newLine();
            out.flush();
        } else {
            out.write(msg);
            out.newLine();
            out.flush();
        }
    }

    public void alugarSV(String numSv) throws IOException {
        int num = Integer.parseInt(numSv);
        String msg;
        msg = bd.getServer(num, utilizador, 0.0, out);
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void verSvsUtilizador(String utilizador) throws IOException {
        String msg = bd.getSvsUtil(utilizador);
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void cancelarSV(String numSV) throws IOException {
        int num = Integer.parseInt(numSV);
        String msg;
        msg = bd.cancelaAluguer(num, utilizador);
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void licitarServidor(int numSV, String valor) throws IOException {
        double valorLicitar = Double.parseDouble(valor);
        String msg = bd.licitarServidor(numSV, valorLicitar, out, utilizador);
        out.write(msg);
        out.newLine();
        out.flush();
    }

}
