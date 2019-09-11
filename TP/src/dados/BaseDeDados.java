/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dados;

import dados.Server;
import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class BaseDeDados {

    private HashMap<String, Utilizador> utilizadores;
    private HashMap<Integer, Server> servers;
    private HashMap<Integer, Leilao> leiloes;
    private ReentrantLock lUtil;
    private ReentrantLock lServers;
    private ReentrantLock lLeiloes;

    public BaseDeDados() {
        utilizadores = new HashMap<>();
        servers = new HashMap<>();
        leiloes = new HashMap<>();
        lUtil = new ReentrantLock();
        lServers = new ReentrantLock();
        lLeiloes = new ReentrantLock();
    }

    public int logIn(String user, String pass) {
        lUtil.lock();
        if (utilizadores.containsKey(user)) {
            if (utilizadores.get(user).getPass().equals(pass)) {
                lUtil.unlock();
                return 1; //tudo ok
            } else {
                lUtil.unlock();
                return 2; //user certo, pass errado
            }
        }
        lUtil.unlock();
        return 0; //user nao existe
    }

    public int registar(String user, String pass) {
        lUtil.lock();
        if (utilizadores.containsKey(user)) {
            lUtil.unlock();
            return 0; // nome já existee
        }
        Utilizador novo = new Utilizador(user, pass);
        utilizadores.put(user, novo);
        lUtil.unlock();
        return 1; //tudo ok
    }

    public String getServers() {
        //String msg = "";
        /*for(Server s: servers.values()){
            msg = msg + "-" + s.toString();
        }*/
        return servers.values().toString();
    }

    public String getServer(int numSV, String utilizador, double valorAluger, BufferedWriter out) {
        lServers.lock();
        Server s = servers.get(numSV);
        lServers.unlock();
        synchronized (s) {
            if (s.getEstado() == 1 || s.getEstado() == 4 || s.getEstado() == 3) {
                String donoAntigo;
                if (!s.getDono().equals("")) {
                    s.avisarCliente();
                    donoAntigo = s.getDono();   //Actualizar as contas do dono antigo
                    lUtil.lock();
                    Utilizador u = utilizadores.get(donoAntigo);
                    lUtil.unlock();
                    LocalDateTime d = LocalDateTime.now();
                    long difSecs = ChronoUnit.SECONDS.between(s.getHoraDeCompra(), d);
                    double newValor = difSecs * s.getValorAluger();

                    u.setValorAdever(newValor);

                }
                s.setOut(out);
                s.setDono(utilizador);
                s.setEstado(2);
                if (valorAluger != 0.0) {
                    s.setValorAluger(valorAluger);
                }
                return s.toString();
            }
        }
        return "Servidor já comprado";
    }

    public String getServersDisponiveis(int tipoSV) {
        lServers.lock();
        String msg = "";
        int count = 0;
        for (Server s : servers.values()) {
            synchronized (s) {
                if (s.getEstado() == 1 && s.getTipo() == tipoSV) {
                    msg = msg + s.toString() + ";" + " preço: " + s.getPreco() + ",";
                    count++;
                }
            }
        }
        if (count == 0) {
            for (Server s : servers.values()) {
                synchronized (s) {
                    if (s.getTipo() == tipoSV && (s.getEstado() == 4 || s.getEstado() == 3)) {
                        msg = msg + s.toString() + ";" + " preço: " + s.getPreco() + ",";
                        count++;
                    }
                }
            }
        }
        lServers.unlock();
        return msg;
    }

    public String getSvsUtil(String util) {
        lServers.lock();
        String msg = "";
        for (Server s : servers.values()) {
            synchronized (s) {
                if (s.getDono().equals(util)) {
                    msg = msg + s.toString() + ";" + " preço: " + s.getPreco() + ",";
                }
            }
        }
        if (msg.equals("")) {
            msg = "Não possui servidores!";
        }
        lServers.unlock();
        return msg;
    }

    public String getSvLeilao() {
        lLeiloes.lock();
        String msg = "";
        int count = 0;
        for (Leilao l : leiloes.values()) {
            synchronized (l) {
                if (l.getAberto() == 1 && l.getsvEstado() == 3) {
                    msg = msg + l.toString() + ",";
                    count++;
                }
            }
        }
        if (count == 0) {
            msg = "Não há leiloes";
        }
        lLeiloes.unlock();
        return msg;
    }

    public double getClienteSaldo(String user) {
        return utilizadores.get(user).getValorAdever();
    }

    public String getLeilao(int numsvLeilao) {
        lLeiloes.lock();
        String msg = "";
        msg = leiloes.get(numsvLeilao).toString();
        lLeiloes.unlock();
        return msg;
    }

    public String cancelaAluguer(int numsvLeilao, String utilizador) {
        lServers.lock();
        String msg = "Aluguer cancelado!";
        Server s = servers.get(numsvLeilao);
        synchronized (s) {

            LocalDateTime d = LocalDateTime.now();
            long difSecs = ChronoUnit.SECONDS.between(s.getHoraDeCompra(), d);
            s.setEstado(1);
            double newValor = difSecs * s.getValorAluger();
            lUtil.lock();
            Utilizador u = utilizadores.get(utilizador);
            lUtil.unlock();
            u.setValorAdever(newValor);
        }
        lServers.unlock();
        return msg;
    }

    public String licitarServidor(int numSV, double valor, BufferedWriter out, String utilizador) {
        lLeiloes.lock();
        String msg = "";
        int flag;
        
        Leilao l = leiloes.get(numSV);

        flag = l.setOfertaAtual(valor, out, utilizador);
        if (flag == 1) {
            msg = "Valor de leilao aceite!";
        } else {
            msg = "Valor de leilao rejeitado!";
        }

        lLeiloes.unlock();
        return msg;
    }

    public void povoarServers() { //funcao de testes
        int i = 1, t = 1;
        Server s;
        while (i < 7) {
            s = new Server(i, t);
            t++;
            if (t == 4) {
                t = 1;
            }
            servers.put(i, s);
            i++;
        }

        s = new Server(2, 3);
        Server s2 = new Server(5, 2);
        servers.put(2, s);
        servers.put(5, s2);

        servers.get(2).setEstado(3);
        servers.get(5).setEstado(3);
        Leilao l1 = new Leilao(servers.get(2));
        Leilao l2 = new Leilao(servers.get(5));

        leiloes.put(2, l1);
        leiloes.put(5, l2);

        Thread t1 = new Thread(l1);
        Thread t2 = new Thread(l2);

        t1.start();
        t2.start();

        Utilizador u = new Utilizador("leicam", "123");
        Utilizador u2 = new Utilizador("ruca", "123");
        utilizadores.put("leicam", u);
        utilizadores.put("ruca", u2);
        /*Server s1 = new Server(1,1);
        Server s2 = new Server(2,1);
        Server s3 = new Server(3,2);
        Server s4 = new Server(4,3);
        Server s5 = new Server(5,3);
        Server s6 = new Server(6,2);*/
    }

}
