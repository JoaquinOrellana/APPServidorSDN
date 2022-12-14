package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

public class Conexion extends Main implements Runnable{

    Socket cliente;
    public Conexion(Socket cliente){
        this.cliente=cliente;
    }
    public void run(){
        try {



            System.out.print("Direccion ip user:"+cliente.getInetAddress().getHostAddress());

            BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            PrintWriter writer=new PrintWriter(cliente.getOutputStream());
            salto:
            for(int intentos=0;intentos<3;intentos++){
                String userName=reader.readLine();
                String password=reader.readLine();



                //File ficheroEnt=new File("/home/ubuntu/credenciales.txt");
                File ficheroEnt=new File("E:\\TEST_REGISTRO_SERVIDOR\\aea\\credenciales.txt");
                if(!ficheroEnt.exists()){
                    LOGGER.log(Level.INFO,"["+cliente.hashCode()+"] User: "+userName+"Intentos: "+intentos);
                    System.err.println("No existe el fichero de entrada especificado");
                }else{
                    Scanner scan1=new Scanner(ficheroEnt);
                    Scanner scan2=new Scanner(ficheroEnt);
                    int contador=0, contador1=0;
                    while(scan2.hasNext()){
                        scan2.nextLine();
                        contador ++;
                    }
                    scan2.close();
                    while(scan1.hasNext()){
                        String linea=scan1.nextLine();
                        contador1 ++;
                        try{
                            if(userName.equals(linea.split(",")[0]) && password.equals(linea.split(",")[1])){
                                System.out.print("Usuarios y contraseña correctos"+userName);
                                LOGGER.log(Level.INFO,"["+cliente.hashCode()+"] Usuario y contraseña correcto: "+userName);
                                writer.println("valido");

                                break salto;
                            }else if(contador==contador1 && intentos<2){
                                System.out.print("Usuario o contraseña incorrectos\n");
                                LOGGER.log(Level.WARNING,"["+cliente.hashCode()+"] Usuario o contraseña incorrectos");
                                writer.println("invalido,true");
                            }else if(contador==contador1&&intentos==2){
                                System.out.print("Usuario o contraseña incorrectos. \n Intento superados");
                                LOGGER.log(Level.WARNING,"["+cliente.hashCode()+"] Usuario o contraseña incorrecto / Número máximo de intentos superados");
                                writer.println("invalido,false");

                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    scan1.close();
                }


                System.out.println("User+passwd:"+userName+"-"+password);
                writer.flush();
            }
            writer.close();
            cliente.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
