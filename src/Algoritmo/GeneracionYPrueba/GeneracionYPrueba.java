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
import java.util.Random;

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
        LinkedList<Nodo> soluciones = new LinkedList<>();
        boolean continuar= true;
        while(continuar)
        {
            for(int x = 0; x < 100 ; x++)
            {
                Nodo solucion = buscarSolucion(Raiz);
                boolean flag = true;
                for(Nodo nodo: soluciones)
                {
                    if(nodo.getRutaSolucion().equals(solucion.getRutaSolucion()))
                    {
                        if(!soluciones.isEmpty())
                        {
                            flag = false;
                            break;                        
                        }
                    }
                }
                if(flag)
                {
                    if(solucion.getPuntuacion()==0)
                    {
                        soluciones.add(solucion);
                    }                
                }
            }
            
            imprimirConsola("Se han encontrado " + soluciones.size());
            
            if(soluciones.isEmpty())
            {
                continuar = puzzleia.PuzzleIA.ventana.buscarDeNuevo();
                if(!continuar){break;}
            }
            else
            {
                continuar = false;
            }
            
            
            int indiceTmp = 1;        
            int costeMenor = 100000;
            for(Nodo nodo: soluciones)
            {
                int costo = nodo.getCosto();
                imprimirConsola(indiceTmp+")\tCosto: "+costo);
                imprimirConsola("\t"+nodo.getRutaSolucion());
                indiceTmp++;
                if(costo <= costeMenor)
                {
                    costeMenor = costo;                
                }
            }

            /*Mostramos la mejor solución*/
            int posicionSolucion = 0;
            Nodo tmp = null;
            for(Nodo sol: soluciones)
            {
                if(sol.getCosto()==costeMenor)
                {
                    tmp = sol;
                    break;
                }
                posicionSolucion++;
            }
            imprimirConsola("\n\nLa mejor solución es la opción número : \t" + posicionSolucion+1);        

            if(puzzleia.PuzzleIA.ventana.mostrarRuta())
            {
                String rutaTablero = "";
                int indexMovimiento = costeMenor; // indice del movimiento.
                while(tmp!=null)
                {
                    rutaTablero =  indexMovimiento-- +")  Aplicando movimiento: "+ tmp.movimientoAnterior +"\n"+ tmp.tablero.getDataCadena()+"\n\n"+ rutaTablero ;
                    tmp = tmp.ptr_padre;
                }
                imprimirConsola(rutaTablero);            
            }


            //imprimirConsola("Ruta a la solución: "+solucion.getRutaSolucion());            
        }                
        
    }
    
    public Nodo buscarSolucion(Nodo raiz)
    {                        
        return calcularMovimientos(raiz);
    }
    
    public Nodo calcularMovimientos(Nodo raiz)
    {     
        /*Si el tablero actual supera cierto humbral de h' detenemos*/
        double puntuacion = raiz.tablero.getPuntuacion();        
        int costo = raiz.getCosto();
        /*imprimirConsola("Puntuación :"+puntuacion  + " \t Con un costo de :"+costo);        
        imprimirConsola(raiz.tablero.getDataCadena());*/
        if( puntuacion <= puzzleia.PuzzleIA.ventana.getPresicion() ||  costo > puzzleia.PuzzleIA.ventana.getCostoMaximo())
        {
            return raiz;
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
        if(cero1==null || cero2==null){return raiz;}
        //imprimirConsola(cero1.mensajePosicion());
        //imprimirConsola(cero2.mensajePosicion());
        /*----------> Ya tenemos localizados los ceros.*/        
        /*Ahora procedemos a verificar los movimientos posibles desde cada cero.*/
        
        hacerMovimientos(lista, cero1, raiz);
        hacerMovimientos(lista, cero2, raiz);
      
        double minimo = 10000000;
        LinkedList<Nodo> posiblesMovimientos = new LinkedList<>();
        for(Nodo n: lista)
        {
            if(n.tablero.getPuntuacion() < minimo)
            {
                minimo = n.tablero.getPuntuacion();                
            }
            //buscarSolucion(n);
            //imprimirConsola("Puntuacion:\t"+ n.tablero.getPuntuacion());
            //imprimirConsola(n.tablero.getDataCadena());
        }
        
        for(Nodo n:lista)
        {
            if(n.tablero.getPuntuacion()==minimo)
            {
                posiblesMovimientos.add(n);
            }            
        }
                
        if(posiblesMovimientos.size()==0)
        {   
            return raiz;
        }
        if(posiblesMovimientos.size()==1)
        {
            Nodo nuevo = buscarSolucion(posiblesMovimientos.get(0));
            return nuevo;
        }
        else
        {
            /*Elegimos aleatoriamente el tablero al cual moverse*/
            Random r = new Random();
            int indice = r.nextInt(posiblesMovimientos.size());
            if(indice<0){indice = indice*-1;}
            Nodo nuevo = buscarSolucion(posiblesMovimientos.get(indice));
            return nuevo;
        }                                                         
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
            
            Nodo nuevo = new Nodo(nuevoTab,raiz,"E"+tmpValor);
            Nodo auxiliar = raiz;
            boolean flag = true;
            while(auxiliar!=null)
            {
                if(auxiliar.tablero.esIgual(nuevoTab))
                {
                    flag = false;
                    break;
                }
                auxiliar = auxiliar.ptr_padre;
            }
            if(flag)
            {
                lista.add(nuevo);
            }
           
        }
        /*Movimiento a la derecha. */
        if(cero.x + 1 <= Tablero.tamMatrix-1)
        {        
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y, cero.x+1);
            nuevoTab.setValor(cero.y, cero.x + 1, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor); 
            
            Nodo nuevo = new Nodo(nuevoTab,raiz,"O"+tmpValor);
            Nodo auxiliar = raiz;
            boolean flag = true;
            while(auxiliar!=null)
            {
                if(auxiliar.tablero.esIgual(nuevoTab))
                {
                    flag = false;
                    break;
                }
                auxiliar = auxiliar.ptr_padre;
            }
            if(flag)
            {
                lista.add(nuevo);
            }         
        }
        /*Movimiento hacia arriba*/
        if(cero.y - 1 >= 0)
        {            
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y-1, cero.x);
            nuevoTab.setValor(cero.y - 1, cero.x, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor);  

            Nodo nuevo = new Nodo(nuevoTab,raiz,"S"+tmpValor);
            Nodo auxiliar = raiz;
            boolean flag = true;
            while(auxiliar!=null)
            {
                if(auxiliar.tablero.esIgual(nuevoTab))
                {
                    flag = false;
                    break;
                }
                auxiliar = auxiliar.ptr_padre;
            }
            if(flag)
            {
                lista.add(nuevo);
            }            
        }
        /*Movimiento hacia abajo*/
        if(cero.y + 1 <= Tablero.tamMatrix -1 )
        {       
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y+1, cero.x);
            nuevoTab.setValor(cero.y + 1, cero.x, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor); 

            Nodo nuevo = new Nodo(nuevoTab,raiz,"N"+tmpValor);
            Nodo auxiliar = raiz;
            boolean flag = true;
            while(auxiliar!=null)
            {
                if(auxiliar.tablero.esIgual(nuevoTab))
                {
                    flag = false;
                    break;
                }
                auxiliar = auxiliar.ptr_padre;
            }
            if(flag)
            {
                lista.add(nuevo);
            }            
        }
                   
    }
    
    public void imprimirConsola(Object s)
    {
        puzzleia.PuzzleIA.ventana.imprimirConsola(s);
    }
}
