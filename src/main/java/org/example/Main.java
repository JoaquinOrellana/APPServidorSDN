package org.example;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;
public class Main {
    static String dpidSwpri="";

    static Logger LOGGER = Logger.getLogger("MyLog");
    static String SERVER_KEY_STORE="";
    static String SERVER_KEY_STORE_PASSWORD="123123";

    public static void main(String[] args) {

        if (args.length == 0) {
            //FloodlightProvider.setIP(args[0]);
            //String resp="";
            //try {
            //	if(!FirewallJSON.isEnabled()) {
            //		resp=FirewallJSON.enable(true);
            //	}
            //}catch(JSONException | IOException e1){
            //	e1.printStackTrace();
            //}

            //String pid=datosSwitches();
            ////Tráfico ARP
            //setRegla("","","","ARP","","","","","","","","","",pid);
            ////Tráfico comunicacion Socket
            //setRegla("","","","","","","","","TCP","8443","","","",pid);
            //setRegla("","","","","","","","","TCP","","8443","","",pid);
            Log();
            //Thread h = new Thread(new Ejecutar());
            //h.start();
            Thread conexiones = new Thread(new EscucharConexiones());
            conexiones.start();
        } else {
            System.out.println("Falta la direccion IP del controlador SDN");
        }

    }

    public static void conexionCliente() {
        try {
            Socket cliente=null;
            ServerSocket socketServ=null;
            //System.setProperty("javax.net.trustStore",SERVER_KEY_STORE);
            //SSLContext context=SSLContext.getInstance("TLS");
            //KeyStore ks=KeyStore.getInstance("jceks");
            //ks.load(new FileInputStream(SERVER_KEY_STORE), null);
            //KeyManagerFactory kf=KeyManagerFactory.getInstance("SunX509");
            //kf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
            //context.init(kf.getKeyManagers(),null,null);
            //ServerSocketFactory factory= context.getServerSocketFactory();
            socketServ = new ServerSocket(8445);
            //((SSLServerSocket) socket).setNeedClientAuth(false);
            while(true) {
                cliente=socketServ.accept();
                if(cliente.isConnected()) {
                    System.out.println("Usuario conectado");
                    Thread hcliente=new Thread(new Conexion(cliente));
                    hcliente.start();
                }
            }
        }catch(Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }





    public static void Log() {
        Handler fileHandler;
        try {


            fileHandler=new FileHandler("/home/ubuntu/test.log");
            //fileHandler=new FileHandler("E:\\TEST_REGISTRO_SERVIDOR\\pruebita\\pruebita.log");
            SimpleFormatter simpleFormatter=new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);

            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);

        }catch(SecurityException | IOException e) {
            e.printStackTrace();
        }
    }








}