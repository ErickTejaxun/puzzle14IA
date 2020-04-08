/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo.GeneracionYPrueba;
import Algoritmo.Cero;
import Algoritmo.Nodo;
import java.util.LinkedList;
import Algoritmo.Tablero;

/**
 *
 * @author erick
 * 
 */

public class GeneracionYPrueba extends Algoritmo.Algoritmo
{            
    public GeneracionYPrueba(Tablero t)
    {
        this.Raiz = new Nodo(t);
    }
    
    public GeneracionYPrueba(Nodo t)
    {
        this.Raiz = t;
    }

    @Override
    public Nodo getNodoInicial() 
    {
        return this.Raiz;
    }
    
    @Override
    public void setNodoInicial(Nodo t)
    {
        this.Raiz = t;
    }
    
    @Override
    public void Run()
    {
        imprimirConsola("Iniciando solución con algoritmo de Generación y Prueba");
        buscarSolucion(Raiz);                
    }
    
    public void buscarSolucion(Nodo raiz)
    {                        
        calcularMovimientos(raiz);
    }
    
    public LinkedList<Nodo> calcularMovimientos(Nodo raiz)
    {
        /*Si el tablero actual supera cierto humbral de h' detenemos*/
        double puntuacion = raiz.tablero.getPuntuacion();        
        imprimirConsola("Puntuación :"+puntuacion);
        //System.out.println("Puntuación :"+puntuacion);
        imprimirConsola(raiz.tablero.getDataCadena());
        if( puntuacion < 24 )
        {
            return null;
        }
        
        LinkedList<Nodo> lista = new LinkedList<>();// Creamos la estructura para almacenar los posibles movimientos.
        /*Localizamos los ceros*/
        Cero cero1 =null, cero2 = null;
        for(int y = 0; y < Tablero.tamMatrix; y++)
        {
            for(int x = 0; x < Tablero.tamMatrix; x++)
            {
                if(raiz.tablero.obtenerValor(y, x) == 0)
                {
                    if(cero1==null)
                    {
                        cero1 = new Cero(x,y);
                    }
                    else
                    {
                        cero2 = new Cero(x,y);
                        break;
                    }
                }
            }        
        }
        if(cero1==null || cero2==null){return lista;}
        //imprimirConsola(cero1.mensajePosicion());
        //imprimirConsola(cero2.mensajePosicion());
        /*----------> Ya tenemos localizados los ceros.*/        
        /*Ahora procedemos a verificar los movimientos posibles desde cada cero.*/
        
        hacerMovimientos(lista, cero1, raiz);
        hacerMovimientos(lista, cero2, raiz);
               
//        Random r = new Random();
//        int indice = r.nextInt(lista.size());
//        buscarSolucion(lista.get(indice));

        for(Nodo n: lista)
        {
            //buscarSolucion(n);
            imprimirConsola("Puntuacion:\t"+ n.tablero.getPuntuacion());
            imprimirConsola(n.tablero.getDataCadena());
        }
        
        return null;        
    }

    
    public void hacerMovimientos(LinkedList<Nodo> lista, Cero cero, Nodo raiz)
    {                
        int tmpValor = 0;
        /*Movimientos a la izquierda*/
        if(cero.x -1 >= 0)
        {            
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y, cero.x-1);
            nuevoTab.setValor(cero.y, cero.x -1, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor);  
            if(!nuevoTab.esIgual(raiz.tablero))
            {
                lista.add(new Nodo(nuevoTab));
            }            
        }
        /*Movimiento a la derecha. */
        if(cero.x + 1 < Tablero.tamMatrix)
        {        
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y, cero.x+1);
            nuevoTab.setValor(cero.y, cero.x + 1, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor);      
            if(!nuevoTab.esIgual(raiz.tablero))
            {
                lista.add(new Nodo(nuevoTab));
            }            
        }
        /*Movimiento hacia arriba*/
        if(cero.y - 1 >= 0)
        {            
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y-1, cero.x);
            nuevoTab.setValor(cero.y - 1, cero.x, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor);  
            if(!nuevoTab.esIgual(raiz.tablero))
            {
                lista.add(new Nodo(nuevoTab));
            }            
        }
        /*Movimiento hacia abajo*/
        if(cero.y + 1 < Tablero.tamMatrix)
        {       
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y+1, cero.x);
            nuevoTab.setValor(cero.y + 1, cero.x, 0);
            nuevoTab.setValor(cero.y + 1, cero.x, tmpValor); 
            if(!nuevoTab.esIgual(raiz.tablero))
            {
                lista.add(new Nodo(nuevoTab));
            }            
        }
                   
    }
    
    public void imprimirConsola(Object s)
    {
        puzzleia.PuzzleIA.ventana.imprimirConsola(s);
    }
}
