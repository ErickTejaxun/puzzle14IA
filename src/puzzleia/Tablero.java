/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzleia;

/**
 *
 * @author erick
 */
public class Tablero 
{
    private int puntuacion;
    public static int[][] solucion;// = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,0,0}};       
    public static int tamMatrix =4;
    private int[][] data = new int[4][4];

    
    public static void calcularSolucion()
    {
        int contador = 1 ;
        solucion = new int[tamMatrix][tamMatrix];
        for(int x = 0 ; x < tamMatrix; x++)
        {
            for(int y = 0; y< tamMatrix; y++)
            {
                if( y < tamMatrix-2 && x < tamMatrix-1)
                {
                    solucion[x][y] = contador;
                }
                else
                {
                    solucion[x][y] = 0;
                }
                contador++;
            }
        }
    }
    
    public Tablero(int[][] dat)
    {
        /*Reservamos un nuevo espacio de memoria para no sobreescribir en los datos del padre.*/
        data = new int[Tablero.tamMatrix][Tablero.tamMatrix];
        /*Copiamos los datos en la nueva matriz*/
        for(int x=0 ; x <tamMatrix; x++)
        {
            for(int y =0; y < tamMatrix; y++)
            {
                data[x][y] = dat[x][y];
            }
        }
        this.puntuacion = 0;
    }
    
    public int getPuntuation()
    {
        puntuacion = 0;
        if(solucion==null)
        {
            calcularSolucion();
        }
        for(int i=0;i<Tablero.tamMatrix;i++)
        {
            for(int j=0;j<Tablero.tamMatrix;j++)
            {
                if(data[i][j] == solucion[i][j])
                {
                    puntuacion++;
                }
                //puntuacion = data[i][j] == solucion[i][j]? puntuacion+1 : puntuacion ;
            }
        }        
        return puntuacion;
    }
    
    public Tablero generarMovimiento()
    {                        
        Tablero nuevoTablero = new Tablero(this.data);
        
        return nuevoTablero;
    }
    
    public String getDataCadena()
    {        
        String cadena = "";
        for(int x = 0; x<Tablero.tamMatrix; x++)
        {
            for(int y = 0 ; y < Tablero.tamMatrix; y++)
            {
                cadena+= this.data[x][y] +"|";
            }
            cadena += "\n";
        }
        return cadena;
    }
    
}
