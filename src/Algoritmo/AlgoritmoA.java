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
            imprimirConsola("Iniciando solución con algoritmo de Generación y Prueba");
            for(int x = 0; x < Algoritmo.iteraciones ; x++)
            {
                Nodo solucion = explorar(Raiz);
                boolean flag = true;
                /*Verificamos que la solución no haya sido ya almacenada.*/
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
                //Si la solución no es igual a otra que ya está almacenada, se almacena.
                //Si es igual, se descarga.
                if(flag)
                {
                    //Verificamos que tenga la presición deseada. 0 = Solución total
                    if(solucion.getPuntuacion()<=puzzleia.PuzzleIA.ventana.getPresicion())
                    {
                        soluciones.add(solucion);
                    }                
                }
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
                imprimirConsola(indiceTmp+")\tCosto: "+costo);
                imprimirConsola("\t"+nodo.getRutaSolucion());
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
            imprimirConsola("\n\nLa mejor solución es la opción número : \t" + posicionSolucion+1 + " Con un coste total de "+costeMenor +" pasos.");

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
        listaAbiertos.add(raiz); // Agreagamos la raiz a la lista de nodos abiertos.
        Nodo mejorNodo = null;
        double tmpMejorF = 10000000;
        for(Nodo tmpNodo :listaAbiertos)
        {
            double tmpF = tmpNodo.getCosto() + tmpNodo.getPuntuacion();
            if(tmpMejorF> tmpF)
            {
                mejorNodo = tmpNodo;
            }
        }
        if(mejorNodo==null){return raiz;} // Error
        
        /*Verificamos si mejorNodo es nodo meta o si ya ha sobrepasado el coste permitido*/
        double puntuacion = mejorNodo.getPuntuacion();        
        int costo = mejorNodo.getCosto();
        if( puntuacion <= puzzleia.PuzzleIA.ventana.getPresicion() ||  costo >= puzzleia.PuzzleIA.ventana.getCostoMaximo())
        {
            return mejorNodo;
        }  

        
        /*Expandimos el mejorNodo------------------------------------------------------------------------------->*/        
        /*Localizamos los ceros*/
        LinkedList<Cero> listaCeros = new LinkedList<>();
        
        for(int y = 0; y < Tablero.tamMatrix; y++)
        {
            for(int x = 0; x < Tablero.tamMatrix; x++)
            {
                if(raiz.tablero.obtenerValor(y, x) == 0)
                {
                    listaCeros.add(new Cero(x,y));
                }
            }
        }
        if(listaCeros.isEmpty()){return raiz;}        
        /*----------> Ya tenemos localizados los ceros.*/        
        /*Ahora procedemos a verificar los movimientos posibles desde cada cero.*/
        
        // Creamos la estructura para almacenar los posibles movimientos.
        LinkedList<Nodo> posiblesMovimientos = new LinkedList<>();
        for(Cero cero : listaCeros)
        {
            aplicarOperadores(posiblesMovimientos, cero, raiz);
        }            
        
        if(posiblesMovimientos.isEmpty()){return mejorNodo;}
        
        /*Buscamos el mejor f(n) = g(n) + h'(n)*/
        double minimo = 10000000;            
        for(Nodo n: posiblesMovimientos)
        {
            double tmp = n.getPuntuacion() + n.getCosto();
            if(tmp < minimo)
            {
                minimo = tmp;
            }
        }
                        
        LinkedList<Nodo> nodosAbiertos = new LinkedList<>();
        for(Nodo n:posiblesMovimientos)
        {
            double tmp = n.getPuntuacion() + n.getCosto();
            if(tmp==minimo)
            {
                nodosAbiertos.add(n);
            }            
        }
        
        
        /*Vemos los hijos*/
        
        if(nodosAbiertos.isEmpty())
        {   
            return raiz;
        }
        
        
        for(Nodo nodo : nodosAbiertos)
        {
            
        }
        
        
        /*Verificamos*/
        if(nodosAbiertos.size()==1)
        {
            Nodo nuevo = explorar(nodosAbiertos.get(0));
            return nuevo;
        }
        else
        {
            /*Elegimos aleatoriamente el tablero al cual moverse*/
            Nodo nuevo = null;
            Random r = new Random();
            int indice = r.nextInt(nodosAbiertos.size());
            if(indice<0){indice = indice*-1;}
            nuevo = explorar(nodosAbiertos.get(indice));
            return nuevo;
        }                                                         
    }
    
    /*
      En este caso tenemos cuatro operadores.
        -Movimiento al Norte
        -Movimiento al Sur
        -Movimiento al Oeste
        -Movimiento al Este    
    */    
    
    public void aplicarOperadores(LinkedList<Nodo> lista, Cero cero, Nodo raiz)
    {                
        int tmpValor = 0;        
        /*Operador 1 : Movimientos a la izquierda*/
        if(cero.x -1 >= 0)
        {                  
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y, cero.x-1);
            if(tmpValor!=0)
            {
                nuevoTab.setValor(cero.y, cero.x -1, 0);
                nuevoTab.setValor(cero.y, cero.x, tmpValor);              
                Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"E");
                Nodo auxiliar = raiz;
                boolean flag = true;
                if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                {
                    while(auxiliar!=null)
                    {
                        if(auxiliar.tablero.esIgual(nuevoTab))
                        {
                            flag = false;
                            break;
                        }
                        auxiliar = auxiliar.ptr_padre;
                    }                
                }
                if(flag)
                {
                    lista.add(nuevo);
                }                
            }          
        }
        /*Operador 2: Movimiento a la derecha. */
        if(cero.x + 1 <= Tablero.tamMatrix-1)
        {        
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y, cero.x+1);
            if(tmpValor!=0)
            {
                nuevoTab.setValor(cero.y, cero.x + 1, 0);
                nuevoTab.setValor(cero.y, cero.x, tmpValor); 

                Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"O");
                Nodo auxiliar = raiz;
                boolean flag = true;
                if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                {
                    while(auxiliar!=null)
                    {
                        if(auxiliar.tablero.esIgual(nuevoTab))
                        {
                            flag = false;
                            break;
                        }
                        auxiliar = auxiliar.ptr_padre;
                    }                
                }
                if(flag)
                {
                    lista.add(nuevo);
                }                 
            }        
        }
        /* Operador 3: Movimiento hacia arriba*/
        if(cero.y - 1 >= 0)
        {            
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y-1, cero.x);
            if(tmpValor!=0)
            {
                nuevoTab.setValor(cero.y - 1, cero.x, 0);
                nuevoTab.setValor(cero.y, cero.x, tmpValor);  

                Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"S");
                Nodo auxiliar = raiz;
                boolean flag = true;
                if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                {
                    while(auxiliar!=null)
                    {
                        if(auxiliar.tablero.esIgual(nuevoTab))
                        {
                            flag = false;
                            break;
                        }
                        auxiliar = auxiliar.ptr_padre;
                    }                
                }
                if(flag)
                {
                    lista.add(nuevo);
                }                 
            }           
        }
        /* Operador 4: Movimiento hacia Arriba*/
        if(cero.y + 1 <= Tablero.tamMatrix -1 )
        {       
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            tmpValor = nuevoTab.getValor(cero.y+1, cero.x);
            if(tmpValor!=0)
            {
                nuevoTab.setValor(cero.y + 1, cero.x, 0);
                nuevoTab.setValor(cero.y, cero.x, tmpValor); 

                Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"N");
                Nodo auxiliar = raiz;
                boolean flag = true;
                if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                {
                    while(auxiliar!=null)
                    {
                        if(auxiliar.tablero.esIgual(nuevoTab))
                        {
                            flag = false;
                            break;
                        }
                        auxiliar = auxiliar.ptr_padre;
                    }                
                }
                if(flag)
                {
                    lista.add(nuevo);
                }                
            }            
        }
        
        /*Movimentos diagonales*/
        if(puzzleia.PuzzleIA.ventana.movimientosDiagonal())
        {
            /*Movimiento NorOeste */
            if((cero.y + 1 <= Tablero.tamMatrix -1 )  && (cero.x + 1 <= Tablero.tamMatrix-1))
            {
                Tablero nuevoTab = new Tablero(raiz.tablero.getData());
                tmpValor = nuevoTab.getValor(cero.y+1, cero.x+1);
                if(tmpValor!=0)
                {
                    nuevoTab.setValor(cero.y + 1, cero.x +1 , 0);
                    nuevoTab.setValor(cero.y, cero.x, tmpValor); 

                    Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"N-O");
                    Nodo auxiliar = raiz;
                    boolean flag = true;
                    if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                    {
                        while(auxiliar!=null)
                        {
                            if(auxiliar.tablero.esIgual(nuevoTab))
                            {
                                flag = false;
                                break;
                            }
                            auxiliar = auxiliar.ptr_padre;
                        }                
                    }
                    if(flag)
                    {
                        lista.add(nuevo);
                    }                
                }                 
            }
            
            /*Movimiento SurOeste*/
            if((cero.y - 1 >= 0)  && (cero.x + 1 <= Tablero.tamMatrix-1))
            {
                Tablero nuevoTab = new Tablero(raiz.tablero.getData());
                tmpValor = nuevoTab.getValor(cero.y- 1, cero.x+1);
                if(tmpValor!=0)
                {
                    nuevoTab.setValor(cero.y - 1, cero.x +1 , 0);
                    nuevoTab.setValor(cero.y, cero.x, tmpValor); 

                    Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"S-O");
                    Nodo auxiliar = raiz;
                    boolean flag = true;
                    if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                    {
                        while(auxiliar!=null)
                        {
                            if(auxiliar.tablero.esIgual(nuevoTab))
                            {
                                flag = false;
                                break;
                            }
                            auxiliar = auxiliar.ptr_padre;
                        }                
                    }
                    if(flag)
                    {
                        lista.add(nuevo);
                    }                
                }                 
            }  
            
            /*Movimiento NorEste */
            if((cero.y + 1 <= Tablero.tamMatrix -1 )  && (cero.x -1 >= 0))
            {
                Tablero nuevoTab = new Tablero(raiz.tablero.getData());
                tmpValor = nuevoTab.getValor(cero.y+1, cero.x-1);
                if(tmpValor!=0)
                {
                    nuevoTab.setValor(cero.y + 1, cero.x -1 , 0);
                    nuevoTab.setValor(cero.y, cero.x, tmpValor); 

                    Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"N-E");
                    Nodo auxiliar = raiz;
                    boolean flag = true;
                    if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                    {
                        while(auxiliar!=null)
                        {
                            if(auxiliar.tablero.esIgual(nuevoTab))
                            {
                                flag = false;
                                break;
                            }
                            auxiliar = auxiliar.ptr_padre;
                        }                
                    }
                    if(flag)
                    {
                        lista.add(nuevo);
                    }                
                }                 
            }
            
            /*Movimiento SurEste*/
            if((cero.y - 1 >= 0)  && (cero.x -1 >= 0))
            {
                Tablero nuevoTab = new Tablero(raiz.tablero.getData());
                tmpValor = nuevoTab.getValor(cero.y-1, cero.x-1);
                if(tmpValor!=0)
                {
                    nuevoTab.setValor(cero.y - 1, cero.x -1 , 0);
                    nuevoTab.setValor(cero.y, cero.x, tmpValor); 

                    Nodo nuevo = new Nodo(nuevoTab,raiz,tmpValor+"S-E");
                    Nodo auxiliar = raiz;
                    boolean flag = true;
                    if(puzzleia.PuzzleIA.ventana.verificarEstadosRepetidos())
                    {
                        while(auxiliar!=null)
                        {
                            if(auxiliar.tablero.esIgual(nuevoTab))
                            {
                                flag = false;
                                break;
                            }
                            auxiliar = auxiliar.ptr_padre;
                        }                
                    }
                    if(flag)
                    {
                        lista.add(nuevo);
                    }                
                }                
            }                                      
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
