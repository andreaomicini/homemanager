/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iotdevice;
 
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
//import com.pi4j.io.i2c.
import java.io.IOException;
 
/**
 *
 * @author Matteo
 */
public class Sensor3 {
   
    //Raspberry Pi's I2C bus
    private static final int i2cBus = 1;
    // Device address
    // private static final int address = 0x50;  
 
    // RFID addresses
    private static final int DEVICE_ADDRESS = 0x50;
 
    // Read RFID command
    private static final byte getRfidCmd = (byte) 0x01;
    private static final byte firmwareRfidCmd = (byte) 0xF0;
 
    //I2C bus
    I2CBus bus;
 
    // Device object
    private I2CDevice sl030;
    private DataInputStream sl030CalIn;
    private DataInputStream sl030In;
   
    public Sensor3() {
       
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            System.out.println("Connected to bus OK!!!");
 
            //get device itself
            sl030 = bus.getDevice(DEVICE_ADDRESS);
 
            System.out.println("Connected to device OK!!!");
            //Small delay before starting
           // Thread.sleep(500);
                 } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
                 }
    }
   
    public String readRfid() throws IOException {
       
        byte[] writeBuffer= new byte[2];
        String result="EMPTY";
        try {
            // SEND COMMAND TO DEVICE TO REQUEST FIRMWARE VERSION
            writeBuffer[0]=(byte)0x01;           // LENDTH (CMD+DATA)
            writeBuffer[1]=firmwareRfidCmd;      // COMMAND
            sl030.write(writeBuffer, 0, 2);
 
            Thread.sleep(1000);
 
            // READ DEVICE FIRMWARE RESPONSE
 
            // first just read in a single byte that represents the command+data payload
            int length = sl030.read();
 
            System.out.println("TOTAL BYTES AVAILABLE: " + length);
   
            // if there are no remaining bytes (length == 0), then we can exit the function
            if(length <= 0) {
                System.out.format("Error: %n bytes read for LENGTH/n", length);
                return "EMPTY";
            }
 
            // if there is any length of remaining bytes, then lets read them now
            byte[] readBuffer = new byte[length];    
            int readTotal = sl030.read(readBuffer, 0, length);
 
            // validate to ensure we got back at least the command and status bytes
            if (readTotal < 2) {
                System.out.format("Error: %n bytes read/n", readTotal);
                return "EMPTY";
            }
 
            byte command = readBuffer[0];  // COMMAND BYTE
            byte status  = readBuffer[1];  // STATUS BYTES
 
            // now we need to get the payload data (if there is any?)
            if(readTotal > 2){
              // we skip the first two bytes (command + status)
              result = new String(readBuffer, 2, readTotal - 2);
            }
 
            // what did we get?
            System.out.println("-- LENGTH BYTE READ  : " + length);
            System.out.println("-- COMMAND BYTE READ : " + command);
            System.out.println("-- STATUS BYTE READ  : " + status);
            System.out.println("-- DATA BYTES READ   : " + result);
 
 
//            //sl030In = new DataInputStream(new ByteArrayInputStream(bytesTemp));
//            //UT = sl030In.readUnsignedShort();
 
           
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception: " + e.getMessage());
        }
     //   System.out.println("Id RFID: " + result);
        return result;
    }
   
}