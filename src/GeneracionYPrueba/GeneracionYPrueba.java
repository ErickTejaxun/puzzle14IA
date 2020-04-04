/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneracionYPrueba;

import puzzleia.Tablero;

/**
 *
 * @author erick
 */
public class GeneracionYPrueba 
{
    private Tablero tableroInicial;
    
    public GeneracionYPrueba(Tablero t)
    {
        this.tableroInicial = t;
    }
    
    public void Run()
    {
        String data = tableroInicial.getDataCadena(); 
        System.out.println(data);
        System.out.println(tableroInicial.getPuntuation());        
    }
}
