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
public class Tablero 
{
    private double puntuacion;
    public static int[][] solucion;// = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,0,0}};       
    public static int tamMatrix =4;
    private int[][] data = new int[4][4];
    public static int funcionHeuristica = 2; 

    
    /***
     Este método genera un tablero solucionado seguń el tamaño de la matriz que se presenta en el archivo de entrada.     
     **/
    public static void calcularSolucion()
    {
        int contador = 1 ;
        solucion = new int[tamMatrix][tamMatrix];
        for(int y = 0 ; y < tamMatrix; y++)
        {
            for(int x = 0; x< tamMatrix; x++)
            {
                if(y == tamMatrix -1 )
                {
                    if(x < tamMatrix-2 )
                    {
                        solucion[y][x] = contador;    
                    }
                    else
                    {
                        solucion[y][x]= 0;
                    }
                }
                else
                {
                    solucion[y][x] = contador;
                }
                contador++;
            }
        }
    }
    /*
    Constructor de la clase. Recibe una matriz que contiene la data.
    */
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
    
    /*
    *Método que devuelve la puntuación del tablero actual según la función heuristica seleccionado.
    */
    
    public double getPuntuacion()
    {
        switch(funcionHeuristica)
        {
            case 1:
                return PosicionesCorrectas();
            case 2:
                return distanciaManhatan();
        }
        return 0;
    }
    
    /*
    *Función heuristica número uno. Simplemente cuenta el número de números que se encuentran ya en su lugar final y según eso
    *genera una puntuación. 
    */            
    public double PosicionesCorrectas()
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
    
    /*
    *En este caso se se compara cada casilla con el valor que debería de tener y se calcula la 'distancia' que indica el número aproximado de 
    *movimientos necesarios para llevarle a su posición. 
    */
    public double distanciaManhatan()
    {
        puntuacion = 0;
        /*Verificamos que la matriz solución esté calculada.*/
        if(solucion==null)
        {
            calcularSolucion();
        }
        /*Calculamos la sumatoria de las distancias*/
        int auxPuntuacion = 0;
         for(int i=0;i<Tablero.tamMatrix;i++)
        {
            for(int j=0;j<Tablero.tamMatrix;j++)
            {
                //auxPuntuacion += Math.abs(data[i][j] - solucion[i][j]);
                int valor = data[i][j];
                int distancia = 0 ;
                if(valor!=0)
                {
                    int xReal = (valor -1)  % Tablero.tamMatrix ;
                    int yReal = (valor - 1) / Tablero.tamMatrix;
                    distancia =  Math.abs(xReal - j) + Math.abs(yReal- i);
                }
                //System.out.println(valor+")\tDistancia: "+distancia);
                auxPuntuacion+= distancia;                                
            }
        }        
        //puntuacion = 1000/auxPuntuacion;
        puntuacion = auxPuntuacion;
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
            cadena += "|";
            for(int y = 0 ; y < Tablero.tamMatrix; y++)
            {
                cadena+= " "+this.data[x][y] +"|";
            }
            cadena += "\n";
        }        
        return cadena;
    }
    
    public int obtenerValor(int y, int x)
    {
        if( y < Tablero.tamMatrix && x < Tablero.tamMatrix)
        {
            return this.data[y][x];
        }
        return -1;
    }

    public static int[][] getSolucion() {
        return solucion;
    }

    public static int getTamMatrix() {
        return tamMatrix;
    }

    public int[][] getData() {
        return data;
    }

    public static int getFuncionHeuristica() {
        return funcionHeuristica;
    }
    
    public void setValor(int y, int x, int v)
    {
        data[y][x]= v;
    }
    
    public int getValor(int y, int x)
    {
        if( y < Tablero.tamMatrix && x < Tablero.tamMatrix)
        {
            return this.data[y][x];
        }
        return -1;        
    }
    
    
    public boolean esIgual(Tablero t)
    {               
        for(int y = 0 ; y < Tablero.tamMatrix; y++)
        {
            for(int x = 0 ; x < Tablero.tamMatrix; x++)
            {
                if(t.getValor(y, x) != this.getValor(y, x))
                {
                    return false;
                }
            }                            
        }                
        return true;
    }
    
    public boolean esIgualOriginal(Tablero t)
    {       
        for(int y = 0 ; y < Tablero.tamMatrix; y++)
        {
            for(int x = 0 ; x < Tablero.tamMatrix; x++)
            {
                if(t.getValor(y, x) != this.getValor(y, x))
                {
                    return false;
                }
            }                            
        }                
        return true;
    }    
    
    
}
