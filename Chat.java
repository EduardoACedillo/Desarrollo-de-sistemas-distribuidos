import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.net.MulticastSocket;
import java.util.Scanner;

class Chat{

	static class Worker extends Thread{
		public void run(){
			try{
				for(;;){ //Ciclo infinito
					InetAddress grupo = InetAddress.getByName("230.0.0.0"); //grupo 230.0.0.0
					MulticastSocket socket = new MulticastSocket(50000);    //através del puerto 50000
					socket.joinGroup(grupo); //unimos el socket al grupo
					byte[] a = recibe_mensaje_multicast(socket,1000); //recibe un string 
					System.out.println(new String(a,"UTF-8"));  //desplegar en  pantalla con acentos y signos de interrogacion
					socket.leaveGroup(grupo);
					socket.close();
				}
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}

	static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto)throws IOException{
		DatagramSocket socket = new DatagramSocket();
		socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
		socket.close();
	}

	static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje)throws IOException{
		byte[] buffer = new byte[longitud_mensaje];
		DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
		socket.receive(paquete);
		return paquete.getData();
	}
}

	public static void main(String[] args)throws Exception{

			Scanner scan = new Scanner(System.in);
			String text; //texto del mensaje
			String usuario = args[0];   //usuario de ese chat

			Worker w = new Worker();
			w.start();

			System.out.println("\nBienvenido " + usuario + "\nIngrese el mensaje a enviar\n");
			for(;;){    //ciclo infinito
				text  = usuario + ": " + scan.nextLine(); //mensaje a enviar por el chat
				w.envia_mensaje_multicast(text.getBytes(), "230.0.0.0", 50000); //enviar el mensaje al grupo 230.0.0.0 a través del puerto 50000
			}
		
	}

}