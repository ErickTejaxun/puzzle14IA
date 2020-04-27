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
public class GeneracionYPrueba extends Algoritmo
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
        LinkedList<Nodo> soluciones = new LinkedList<>();
        boolean continuar= true;
        long inicioEjecucion = System.currentTimeMillis();
        long finEjecucion = System.currentTimeMillis();
        while(continuar)
        {
            limpiarConsola();
            imprimirConsola("Iniciando solución con algoritmo de Generación y Prueba");
            for(int x = 0; x < Algoritmo.iteraciones*10 ; x++)
            {
                Nodo solucion = explorar(Raiz);
                if(solucion.getPuntuacion()<=puzzleia.PuzzleIA.ventana.getPresicion())
                {
                    soluciones.add(solucion);
                    break;
                }
                
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
                        if(solucion.getCosto()>2000)
                        {
                            break; /*Si el costo es demasiado alto, con que encontremos una solución se acabó la ejecución.*/
                        }                        
                    }                
                }
            }
            
            finEjecucion = System.currentTimeMillis();
            imprimirConsola("Se han encontrado " + soluciones.size() +" soluciones.");            
            imprimirConsola("Tiempo utiliado: "+(double) ((finEjecucion - inicioEjecucion)/1000) +" segundos.");
            imprimirConsola("Se han generado "+ Tablero.numeroNodosCreados + " nodos");
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
            imprimirConsola("\n\nLa mejor solución es la opción número : \t" +(posicionSolucion+1)+ " Con un coste total de "+costeMenor +" pasos.");
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
        //imprimirConsola(cero1.mensajePosicion());
        //imprimirConsola(cero2.mensajePosicion());
        /*----------> Ya tenemos localizados los ceros.*/        
        /*Ahora procedemos a verificar los movimientos posibles desde cada cero.*/
        
        for(Cero cero : listaCeros)
        {
            Nodo.aplicarOperadores(lista, cero, raiz);
        }                
             
        if(lista.isEmpty())
        {   
            return raiz;
        }
        else
        {       
            /*Elegimos aleatoriamente el tablero al cual moverse*/
            Random r = new Random();
            int indice = r.nextInt(lista.size());
            if(indice<0){indice = indice*-1;}   
            Nodo sucesor = lista.get(indice);                          
            return explorar(sucesor);
//            if(sucesor.getPuntuacion()<= raiz.getPuntuacion())
//            {
//                return explorar(sucesor);
//            }                
            //return explorar(raiz);
            //return raiz;
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
