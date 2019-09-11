/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceC {
    
    private String[] menuPrinc = {"LogIn", "Registar"};
    private String[] menuUtil = {"Ver Servidores", "Ver Servidores em Leilao","Alugar Servidor", "Ver Valor que deve", "Ver servidores que tem"};
    private Map<String,String[]> menus;
    
    public InterfaceC(){
        menus = new HashMap<>();
        menus.put("menuPrinc", menuPrinc);
        menus.put("menuUtil", menuUtil);
    }
    
    public void showMenu(String menuOp) {
        String[] opcoes = menus.get(menuOp);
        List<String> menu = Arrays.asList(opcoes);
        System.out.println("\n *** Menu *** ");
        for (int i = 0; i < menu.size(); i++) {
            System.out.print(i + 1);
            System.out.print(" - ");
            System.out.println(menu.get(i));
        }
        System.out.println("0 - Sair");
    }
}
