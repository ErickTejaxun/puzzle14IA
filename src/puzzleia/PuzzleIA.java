/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzleia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import Algoritmo.GeneracionYPrueba.GeneracionYPrueba;

/**
 *
 * @author erick
 */
public class PuzzleIA {

    public static Interfaz ventana = null;
    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) throws FileNotFoundException 
    {       
        ventana = new Interfaz();
        ventana.setBounds(100, 100, 1000, 600);
        //ventana.setResizable(false);
        ventana.show();
    }        
    
    
    public static int[][] obtenerMatriz(String path) throws FileNotFoundException
    {   
        int data[][] = null ;
        File archivo= new File(path);    
        Scanner sc = new Scanner(archivo); 
        /*Obtenemos cada una de las lineas*/
        LinkedList<String> lineas = new LinkedList<String>();
        while (sc.hasNextLine())
        {
            lineas.add(sc.nextLine());            
        }                
        /*Verificamos que la matriz sea cuadrada*/        
        
        /*reservamos espacio para la nueva matriz*/
        data = new int[lineas.size()][lineas.size()];
        /*Obtenemos los datos de cada fila que está en la columna*/
        int y = 0;
        StringTokenizer celdas = null;
        for(int i = 0; i< lineas.size(); i++)
        {
            String lineaActual = lineas.get(i);            
            y =0 ; 
           celdas =  new StringTokenizer (lineaActual,",");
            while(celdas.hasMoreTokens())
            {
               data[i][y] = Integer.parseInt(celdas.nextToken());                
               y++;
            }            
        }
        return data;
    }
}

