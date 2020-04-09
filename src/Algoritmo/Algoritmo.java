/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo;

/**
 *
 * @author erick
 */
public abstract class Algoritmo 
{    
    public Nodo Raiz;        
    public abstract Nodo getNodoInicial();
    public abstract void setNodoInicial(Nodo n);
    public abstract void Run();
}
