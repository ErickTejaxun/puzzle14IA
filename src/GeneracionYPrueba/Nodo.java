/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneracionYPrueba;

import java.util.LinkedList;
import puzzleia.Tablero;

/**
 *
 * @author erick
 */
public class Nodo 
{
    private Nodo ptr_padre;
    private LinkedList<Nodo> hijos;
    private Tablero tablero;
    
    public Nodo(Tablero tab)
    {
        this.tablero = tab;
        hijos = new LinkedList<Nodo>();
    }
    
    public void IniciarBusqueda()
    {
        
    }
    
    
}
