/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo;
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
public class EnfriamientoSimulado extends Algoritmo
{                 
    public EnfriamientoSimulado(Tablero t)
    {
        this.Raiz = new Nodo(t);
    }
    
    public EnfriamientoSimulado(Nodo t)
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
        while(continuar)
        {
            limpiarConsola();
            imprimirConsola("Iniciando solución con algoritmo de Generación y Prueba");
            for(int x = 0; x < 200 ; x++)
            {
                Nodo solucion = explorar(Raiz, 100);
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
            
            
            imprimirConsola("Se han encontrado " + soluciones.size() +" soluciones.");            
            
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
    
    
    public Nodo explorar(Nodo raiz, int maximo)
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
        
        aplicarOperadores(lista, cero1, raiz);
        aplicarOperadores(lista, cero2, raiz);
      
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
                
        if(posiblesMovimientos.isEmpty())
        {   
            return raiz;
        }
        if(posiblesMovimientos.size()==1)
        {
            if(maximo>1){maximo--;}
            Nodo nuevo = explorar(posiblesMovimientos.get(0),maximo);
            return nuevo;
        }
        else
        {
            /*Aquí comenzamos aplicamos el enfriamiento simulado*/            
            /*Elegimos aleatoriamente el tablero al cual moverse*/
            int countSol = posiblesMovimientos.size();
            if(countSol>maximo){countSol = maximo;}
            Random r = new Random();
            int indice = r.nextInt(countSol);
            if(indice<0){indice = indice*-1;}
            if(maximo>1){maximo--;}
            Nodo nuevo = explorar(posiblesMovimientos.get(indice), maximo);
            /*Segunda parte del enfriamiento simulado*/            
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
