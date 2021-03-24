import java.net.*;
import java.io.*;
import java.nio.*;
class Token
{
	static DataInputStream entrada;
	static DataOutputStream salida;
	static boolean primera_vez = true;
	static String ip;
	static int token = 0;
	static int nodo;
	static int contador=0;

	static class Worker extends Thread
	{
		public void run()
		{
			//Algoritmo 1
			try
			{
				ServerSocket servidor = new ServerSocket(50000);
				Socket conexion = servidor.accept();
				entrada = new DataInputStream(conexion.getInputStream());				
			}
			catch(Exception e)
			{
				System.err.println(e.getMessage());
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		if (args.length != 2)
		{
		  System.err.println("Se debe pasar como parametros el numero de nodo y la IP del siguiente nodo");
		  System.exit(1);
		}

		nodo = Integer.valueOf(args[0]);  // el primer parametro es el numero de nodo
		ip = args[1];  // el segundo parametro es la IP del siguiente nodo en el anillo
		//Algoritmo 2
		Worker w = new Worker();
		w.start();
		Socket conexion = null;
		

		for(;;)
		{
			try
			{
				conexion = new Socket(ip, 50000);
				System.out.println("Conectado a servidor");
				break;
			}catch(Exception e){
				Thread.sleep(500);
				System.out.println("Esperando conexion al servidor");
			}
		}	
		salida = new DataOutputStream(conexion.getOutputStream());
		w.join();
		System.out.println("Nodo: "+nodo);
		
		for(;;)
		{
			if(nodo == 0)
			{
				if(primera_vez==true)
				{
					primera_vez=false;
					token=1;
				}	
				else
				{	
					token=entrada.readInt();
					contador++;
					System.out.println("Nodo: "+nodo);
					System.out.println("Contador: "+contador);
					System.out.println("Token: "+token);
				}
				if(nodo==0 && contador==1000)
				break;
			}
			else
			{
				//si no es 0
				System.out.println("Nodo que no es 0");
				token=entrada.readInt();
				contador++;
				System.out.println("Nodo: "+nodo);
				System.out.println("Contador: "+contador);
				System.out.println("Token: "+token);
	
			
			}
				salida.writeInt(token);
		}
	}
}
