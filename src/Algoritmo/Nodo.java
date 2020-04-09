/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo;

import java.util.LinkedList;

/**
 *
 * @author erick
 */
public class Nodo 
{
    public Nodo ptr_padre;         /*Puntero hacia el padre*/ 
    public LinkedList<Nodo> hijos; /*Puntero hacia los hijos*/
    public Tablero tablero;
    public String movimientoAnterior;
    
        
    
    public Nodo(Tablero tab)
    {
        this.tablero = tab;
        hijos = new LinkedList<>();
        this.movimientoAnterior = "Tablero inicial";
    }
    public Nodo(Tablero t, Nodo p)
    {
        this.ptr_padre = p;
        this.tablero = t;
        this.hijos = new LinkedList<>();
        this.movimientoAnterior = "Tablero inicial";
    }   
    
    public Nodo(Tablero t, Nodo p, String m)
    {
        this.ptr_padre = p;
        this.tablero = t;
        this.hijos = new LinkedList<>();
        this.movimientoAnterior = m;
    }       
    
    public int getCosto()
    {
        Nodo ptrAux = this;
        int costo = 0;
        while(ptrAux.ptr_padre != null)
        {
            costo++;
            ptrAux = ptrAux.ptr_padre;
        }                
        return costo;
    }
    
    public double getPuntuacion()
    {
        return tablero.getPuntuacion();
    }
    
    public String getRutaSolucion()
    {
        Nodo ptrAux = this;
        String ruta = "";
        while(ptrAux.ptr_padre != null)
        {
            ruta = ptrAux.movimientoAnterior + "\t" + ruta;
            ptrAux = ptrAux.ptr_padre;
        }                
        return ruta;        
    }
}
