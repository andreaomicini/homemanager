///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package iotdevice;
//
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.iot.raspberry.grovepi.GroveDigitalOut;
//import org.iot.raspberry.grovepi.GrovePi;
//import org.iot.raspberry.grovepi.devices.GroveRgbLcd;
//import org.iot.raspberry.grovepi.devices.GroveTemperatureAndHumiditySensor;
//import org.iot.raspberry.grovepi.pi4j.GrovePi4J;
//import sun.awt.DisplayChangedListener;
//
///**
// *
// * @author Matteo
// */
//public class Main {
//
//    public static void main(String[] args) throws IOException {
//        //Sensor3 rfid=new Sensor3();       
//        // Sensor5Python rfid= new Sensor5Python();
//        Sensor4 rfid=new Sensor4();      
//        try
//        {
//         //read forever till hit Ctrl+C
//            for (;;) {
//                Thread.sleep(1000);
//                String result=rfid.readRfid();
//                System.out.println("ID:"+result);
//            }      
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }
//}
