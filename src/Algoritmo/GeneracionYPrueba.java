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
        LinkedList<Nodo> soluciones = new LinkedList<>(); /*Lista de soluciones*/
        boolean continuar= true;  
        while(continuar)
        {
            limpiarConsola(); // Limpiamos la consola.
            imprimirConsola("Iniciando solución con algoritmo de Generación y Prueba");
            
            
            /*Ya que es al azar la búsqueda, se harán al menos 100 intentos para ver si es posible encontrar una
              solución, si no, se le preguntará al usuario si quiere volver a lanzar otros 100 intentos.
            */
            for(int x = 0; x < 100 ; x++)
            {
                Nodo solucion = buscarSolucion(Raiz);
                boolean flag = true;
                
                // Verificamos que la nueva solución no sea igual a una ya encontrada. 
                // Si no es igual, lo insertamos.
                for(Nodo nodo: soluciones)
                {
                    if(nodo.getRutaSolucion().equals(solucion.getRutaSolucion()))
                    {
                        flag = false;
                        break;                        
                    }
                }
                
                /*Verificamos si se puede o no insertar*/
                if(flag)
                {
                    if(solucion.getPuntuacion()==0)
                    {
                        soluciones.add(solucion);
                    }                
                }
            }
            
            imprimirConsola("Se han encontrado " + soluciones.size() +" soluciones.");
            
            
            /*Si no se han encontrado soluciones, preguntamos al usuario si quiere volver a buscar. */
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
        double puntuacion = raiz.tablero.getPuntuacion();        
        int costo = raiz.getCosto();        // Obtenemos el costo de la raiz. Si es mayor al coste máximo, terminamos la búsqueda.
        if( puntuacion <= puzzleia.PuzzleIA.ventana.getPresicion() 
                ||  costo > puzzleia.PuzzleIA.ventana.getCostoMaximo())
        {
            return raiz;
        }                         
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
        if(cero1==null || cero2==null){return raiz;} // Si no se encuentran los dos ceros, hubo un error. 
        
        /*----------> Ya tenemos localizados los ceros.*/        
        /*Ahora procedemos a verificar los movimientos posibles desde cada cero.*/
        /*Elegimos aleatoriamente el cero a utilizar*/
        Random random  = new Random();
        int indiceCero = random.nextInt(2-1);
        if(indiceCero<0){indiceCero*=-1;}      
        
        if(indiceCero==0)
        {
            return buscarSolucion(aplicarOperadores(cero1, raiz));
        }
        else
        {
            return buscarSolucion(aplicarOperadores(cero2, raiz));
        }                       
    }
    
    /*
      En este caso tenemos cuatro operadores.
        -Movimiento al Norte
        -Movimiento al Sur
        -Movimiento al Oeste
        -Movimiento al Este    
    */        
    public Nodo aplicarOperadores(Cero cero, Nodo raiz)
    {      
       Random random = new Random();
       int indiceOperador = random.nextInt(3);
       if(indiceOperador<0){indiceOperador*=-1;}
       switch(indiceOperador)
       {
            case 0:
                return operadorNorte(raiz, cero);
            case 1:
                return operadorSur(raiz, cero);
            case 2:
                return operadorOeste(raiz, cero);
            case 3:
                return operadorEste(raiz, cero);               
       }
       return raiz;         
    }
    
    
    public Nodo operadorNorte(Nodo raiz, Cero cero)
    {
        /* Operador 4: Movimiento hacia Arriba*/        
        if(cero.y + 1 <= Tablero.tamMatrix -1)
        {                   
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            int tmpValor = nuevoTab.getValor(cero.y+1, cero.x);
            nuevoTab.setValor(cero.y + 1, cero.x, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor); 
            return new Nodo(nuevoTab,raiz,tmpValor+"N");           
        }     
        return raiz;// Si no es posible aplicar este operador, se retorna la raiz. 
    }
    
    public Nodo operadorSur(Nodo raiz, Cero cero)
    {
        /* Operador 4: Movimiento hacia Abajo*/        
        if(cero.y - 1 >= 0)
        {                   
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            int tmpValor = nuevoTab.getValor(cero.y-1, cero.x);
            nuevoTab.setValor(cero.y - 1, cero.x, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor); 

            return new Nodo(nuevoTab,raiz,tmpValor+"S");           
        }     
        return raiz;// Si no es posible aplicar este operador, se retorna la raiz. 
    }    
    
    
    public Nodo operadorEste(Nodo raiz, Cero cero)
    {
        if(cero.x -1 >= 0)
        {                  
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            int tmpValor = nuevoTab.getValor(cero.y, cero.x-1);
            nuevoTab.setValor(cero.y, cero.x -1, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor);              
            return new Nodo(nuevoTab,raiz,tmpValor+"E");           
        }     
        return raiz;// Si no es posible aplicar este operador, se retorna la raiz. 
    }     
    
    public Nodo operadorOeste(Nodo raiz, Cero cero)
    {
        if(cero.x + 1 <= Tablero.tamMatrix-1)
        {                  
            Tablero nuevoTab = new Tablero(raiz.tablero.getData());
            int tmpValor = nuevoTab.getValor(cero.y, cero.x+1);
            nuevoTab.setValor(cero.y, cero.x +1, 0);
            nuevoTab.setValor(cero.y, cero.x, tmpValor);              
            return new Nodo(nuevoTab,raiz,tmpValor+"O");           
        }     
        return raiz;// Si no es posible aplicar este operador, se retorna la raiz. 
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
