/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import Mensagens.Mensageiro;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private BufferedWriter out;
    private BufferedReader in;
    private InterfaceC i;
    private Scanner read;
    private Mensageiro mensageiro;

    public void inicio() throws IOException, InterruptedException {
        Socket s = new Socket("127.0.0.1", 12345);
        out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        i = new InterfaceC();
        read = new Scanner(System.in);
        mensageiro = new Mensageiro(in);

        showMenuPrincipal();

        s.shutdownInput();
        s.shutdownOutput();
        s.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Cliente().inicio();
    }

    public void showMenuPrincipal() throws IOException, InterruptedException {
        boolean x = true;

        while (x) {
            i.showMenu("menuPrinc");
            int n = read.nextInt();
            switch (n) {
                case 1:
                    logIn();
                    break;
                case 2:
                    registar();
                    break;
                case 0:
                    x = false;
                    break;
            }
        }
    }

    public void showMenuUtilizador() throws IOException, InterruptedException {
        boolean x = true;

        while (x) {
            i.showMenu("menuUtil");
            int n = read.nextInt();
            switch (n) {
                case 1:
                    verSvs();
                    break;
                case 2:
                    verSvsLeilao();
                    break;
                case 3:
                    alugarSV();
                    break;
                case 4:
                    verSaldo();
                    break;
                case 5:
                    verServidoresDoUtil();
                    break;
                case 0:
                    x = false;
                    break;
            }
        }
    }

    public void logIn() throws IOException, InterruptedException {
        read.nextLine();
        String user, pass, msg;
        System.out.println("Introduza o username");
        user = read.nextLine();
        System.out.println("Introduza a password");
        pass = read.nextLine();

        msg = "1-" + user + "-" + pass;
        out.write(msg);
        out.newLine();
        out.flush();

        msg = in.readLine();
        switch (msg) {
            case "UserErrado":
                System.out.println("User não existe!");
                break;
            case "PassErrada":
                System.out.println("Password errada!");
                break;
            case "TudoCerto":
                Thread t1 = new Thread(mensageiro);
                t1.start();
                showMenuUtilizador();
                break;
        }

    }

    public void registar() throws IOException {
        read.nextLine();
        String user, pass, msg;
        System.out.println("Introduza o username");
        user = read.nextLine();
        System.out.println("Introduza a password");
        pass = read.nextLine();

        msg = "2-" + user + "-" + pass;
        out.write(msg);
        out.newLine();
        out.flush();

        msg = in.readLine();
        switch (msg) {
            case "UserExiste":
                System.out.println("User já existe!");
                break;
            case "UserCriado":
                System.out.println("User criado!");
                break;
        }
    }

    public synchronized void verSvs() throws IOException, InterruptedException {
        String msg = "3-";
        out.write(msg);
        out.newLine();
        out.flush();

        //msg = in.readLine();
        msg = mensageiro.lerMensagem();
        String[] parts = msg.substring(1, msg.length() - 1).split(",");

        for (String s : parts) {
            System.out.println(s);
        }
    }

    public synchronized void verSaldo() throws IOException, InterruptedException {
        String msg = "5-";
        out.write(msg);
        out.newLine();
        out.flush();

        msg = mensageiro.lerMensagem();
        System.out.println();
        System.out.println("Você deve: " + msg);
    }

    public synchronized void verSvsLeilao() throws IOException, InterruptedException {
        String msg = "4-";
        int escolha;
        out.write(msg);
        out.newLine();
        out.flush();

        //msg = in.readLine();
        msg = mensageiro.lerMensagem();

        //String[] parts = msg.split(",");
        String[] parts = msg.split(",");

        for (String s : parts) {
            System.out.println(s);
        }

        if (!parts[0].equals("Não há leiloes")) {
            //ir para um método novo!!!
            System.out.println("Escolha o numero do servidor que quer participar ou 0 para voltar atrás!");
            escolha = read.nextInt();

            switch (escolha) {
                case 0:
                    System.out.println("ok");
                    break;
                default:
                    msg = "6-" + escolha;
                    out.write(msg);
                    out.newLine();
                    out.flush();

                    msg = mensageiro.lerMensagem();

                    System.out.println(msg);

                    participarLeilao();
                    break;
            }

        }

    }

    public synchronized void participarLeilao() throws IOException, InterruptedException {
        System.out.println("Insira o valor que está disposto a pagar ou 0 para voltar atrás!");
        Double escolha = read.nextDouble();
        if (escolha != 0) {
            String msg = "11-" + String.valueOf(escolha);
            out.write(msg);
            out.newLine();
            out.flush();

            msg = mensageiro.lerMensagem();

            System.out.println(msg);
        }
    }

    public synchronized void alugarSV() throws IOException, InterruptedException {
        System.out.println("Que tipo de servidor pretende? (1,2,3)");
        int escolha = read.nextInt();
        String msg = "7-" + escolha;
        out.write(msg);
        out.newLine();
        out.flush();

        msg = mensageiro.lerMensagem();

        //msg = in.readLine();
        //String[] parts = msg.split(",");
        String[] parts = msg.split(",");
        for (String s : parts) {
            System.out.println(s);
        }

        if (!msg.equals("Não há servidores desse tipo desponíveis!")) {
            //novo método
            System.out.println("Escolha o número do servidor que quer alugar ou 0 para voltar atrás");

            escolha = read.nextInt();
            switch (escolha) {
                case 0:
                    break;
                default:
                    msg = "8-" + escolha;
                    out.write(msg);
                    out.newLine();
                    out.flush();

                    msg = mensageiro.lerMensagem();

                    //msg = in.readLine();
                    //parts = msg.split(",");
                    parts = msg.split(",");
                    for (String s : parts) {
                        System.out.println(s + " Comprado!!");
                    }
                    break;
            }
        }
    }

    public synchronized void verServidoresDoUtil() throws IOException, InterruptedException {
        String msg = "9-";
        out.write(msg);
        out.newLine();
        out.flush();
        msg = mensageiro.lerMensagem();

        //msg = in.readLine();
        //String[] parts = msg.split(",");
        String[] parts = msg.split(",");
        System.out.println();
        for (String s : parts) {
            System.out.println(s);
        }
        //novo método
        if (!parts[0].equals("Não possui servidores!")) {
            System.out.println("Escolha o número do servidor para cancelar ou 0 para voltar atrás");
            int escolha = read.nextInt();
            switch (escolha) {
                case 0:
                    break;
                default:
                    msg = "10-" + escolha;
                    out.write(msg);
                    out.newLine();
                    out.flush();

                    msg = mensageiro.lerMensagem();

                    //System.out.println(in.readLine());
                    System.out.println(msg);
                    break;
            }
        }
    }

}
