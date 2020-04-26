/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author erick
 * 
 */
public class AlgoritmoA extends Algoritmo
{            
    public AlgoritmoA(Tablero t)
    {
        this.Raiz = new Nodo(t);
    }
    
    public AlgoritmoA(Nodo t)
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
        LinkedList<Nodo> soluciones = new LinkedList<>();
        boolean continuar= true;
        long inicioEjecucion = System.currentTimeMillis();
        long finEjecucion;// = System.currentTimeMillis();
        while(continuar)
        {
            limpiarConsola();
            imprimirConsola("Iniciando solución con algoritmo A* ");
            Nodo solucion = explorar(Raiz);
            if(solucion.getPuntuacion()<=puzzleia.PuzzleIA.ventana.getPresicion())
            {
                soluciones.add(solucion);
            }             
           
            finEjecucion = System.currentTimeMillis();
            imprimirConsola("Se han encontrado " + soluciones.size() +" soluciones.");     
            imprimirConsola("Tiempo utiliado: "+(double) ((finEjecucion - inicioEjecucion)/1000) +" segundos.");
            
            /*Si no hemos encontrado solución, preguntamos al usuario
            si quiere volver a lanzar otra iteración de búsqueda.*/
            if(soluciones.isEmpty())
            {
                continuar = puzzleia.PuzzleIA.ventana.buscarDeNuevo();
                //Si el usuario dice que no quiere volver a buscar, se termina la ejecución.
                if(!continuar)
                {
                    break;
                }
                else
                {
                    continue;
                }
            }
            else
            {                
                continuar = false;                
            }
                        
            int indiceTmp = 1;        
            int costeMenor = 100000;
            //Buscamos el coste menor.
            for(Nodo nodo: soluciones)
            {
                int costo = nodo.getCosto();
                if(puzzleia.PuzzleIA.ventana.mostrarTodasLasSoluciones())
                {
                    imprimirConsola(indiceTmp+")\tCosto: "+costo);
                    imprimirConsola("\t"+nodo.getRutaSolucion());                    
                }
                indiceTmp++;
                if(costo <= costeMenor)
                {
                    costeMenor = costo;                
                }
            }

            /*Buscamos la solución con el menor */
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
            imprimirConsola("\n\nLa mejor solución es la número : \t" + (posicionSolucion+1) + " Con un coste total de "+costeMenor +" pasos.");
            imprimirConsola("\n"+tmp.getRutaSolucion()+"\n");
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
        }/*---------> Fin del while */              
        
        
    }
    
   
    
    public Nodo explorar(Nodo raiz)
    {                   
        LinkedList<Nodo> listaAbiertos = new LinkedList<>();
        LinkedList<Nodo> listaCerrados = new LinkedList<>();  
        
        /*Agreagamos la raiz a la lista de nodos abiertos.*/
        /*1.*/
        listaAbiertos.add(raiz);  
        
        
        Nodo mejorNodo = null;
        int posicion = 0;
        double tmpMejorF = 10000000;        
        
        while(true)
        {
            tmpMejorF = 10000000;
            posicion = 0;               
            /*2. */
            if(listaAbiertos.isEmpty())
            {
                return raiz; // Error                
            }                                                            
            
            /*Buscamos el mejor nodo*/         
            for(Nodo tmpNodo :listaAbiertos)
            {                
                //double tmp = tmpNodo.f();
                double tmp = tmpNodo.getPuntuacion();
                if(tmpMejorF > tmp )
                {
                    tmpMejorF = tmp;
                }                
            }    

            
            LinkedList<Nodo> mejoresNodos = new LinkedList<>();
            /*Ahora obtenemos los mejores*/
            for(Nodo tmpNodo: listaAbiertos)
            {
                if(tmpMejorF==tmpNodo.getPuntuacion())
                {
                    mejoresNodos.add(tmpNodo);
                }
            }
                      
            /*Elegimos aleatoriamente el mejor nodo*/
            Random r = new Random();
            int indice = r.nextInt(mejoresNodos.size());
            if(indice<0){indice = indice*-1;}
            mejorNodo = mejoresNodos.get(indice);            

            if(mejorNodo==null){return raiz;} // Error
            /*3.
            **Quitamos el mejor nodo de abiertos y lo pasamos a cerrados
            */
            //listaCerrados.add(mejorNodo);
            listaAbiertos.remove(mejorNodo);                                    

            /*4.
            **Si mejor nodo es meta, indicar que es la solución
            **/
            if(mejorNodo.getPuntuacion() <= puzzleia.PuzzleIA.ventana.getPresicion())               
            {
                return mejorNodo;
            }

            if(mejorNodo.getCosto() >= puzzleia.PuzzleIA.ventana.getCostoMaximo())
            {
                return mejorNodo;
            }
            //System.out.println("Costo \t"+mejorNodo.getPuntuacion() +"\tAbiertos:  " + listaAbiertos.size() +"\tCerrados "+listaCerrados.size() +" Prof :"+mejorNodo.getCosto());
            /*5.
            **Expandimos el mejorNodo
            */
            LinkedList<Nodo> sucesoresMejorNodo = new LinkedList<>();
            LinkedList<Cero> listaCeros = mejorNodo.buscarCeros();
            for(Cero cero : listaCeros)
            {
                Nodo.aplicarOperadores(sucesoresMejorNodo, cero, mejorNodo);
            } 

            /*Si no tiene sucesores Regresamos a paso 2*/
            if(sucesoresMejorNodo.isEmpty()){continue;} 

            /*6*/            
            for(Nodo sucesor: sucesoresMejorNodo)
            {                                           
                //if(sucesor.getPuntuacion() <=  mejorNodo.getPuntuacion())
                {                
                    /*Si sucesor existe en nodos abiertos.*/
                    boolean estaEnAbierto = false;
                    boolean estaEnCerrado = false;
                    Nodo nodoViejo  = null;
                    for(Nodo nodoAbierto: listaAbiertos)
                    {
                        if(nodoAbierto.tablero.esIgual(sucesor.tablero))
                        {
                            estaEnAbierto = true;
                            nodoViejo = nodoAbierto;
                            if(nodoViejo.getCosto() > sucesor.getCosto())
                            {
                                nodoViejo.ptr_padre = mejorNodo;
                                nodoViejo.f();                        
                            }                                                                
                        }
                    }
                    /*Ahora lo buscamos en cerrados*/
                    if(!estaEnAbierto)                                               
                    {
                        for(Nodo nodoCerrado: listaCerrados)
                        {
                            if(nodoCerrado.tablero.esIgual(sucesor.tablero))
                            {
                                estaEnCerrado = true;
                                nodoViejo = nodoCerrado;
                                if(nodoViejo.getCosto() > sucesor.getCosto())
                                {
                                    nodoViejo.ptr_padre = mejorNodo;
                                    nodoViejo.setCosto(sucesor.getCosto());
                                }
                                break;
                            }
                        }
                        if(!estaEnAbierto)        
                        {                       
                            listaAbiertos.add(sucesor);
                        }
                    }               
                }
            }                 
           /* } */          
        }
    }
    
    
    public void imprimirConsola(Object s)
    {
        puzzleia.PuzzleIA.ventana.imprimirConsola(s);
    }
    
    public void limpiarConsola()
    {
        puzzleia.PuzzleIA.ventana.limpiarConsola();
    }
}
