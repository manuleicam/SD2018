/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dados;

public class Utilizador {
    
    private String nome;
    private String pass;
    private double valorAdever;
    
    public Utilizador(String nome, String pass){
        this.nome = nome;
        this.pass = pass;
        this.valorAdever = 0;
    }

    public synchronized String getNome() {
        return nome;
    }

    public synchronized String getPass() {
        return pass;
    }

    public synchronized double getValorAdever() {
        return valorAdever;
    }

    public synchronized void setNome(String nome) {
        this.nome = nome;
    }

    public synchronized void setPass(String pass) {
        this.pass = pass;
    }

    public synchronized void setValorAdever(double valorAdever) {
        this.valorAdever += valorAdever;
    }
    
}
