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
import java.util.Base64;
import sun.misc.BASE64Encoder;
 
/**
 *
 * @author Matteo
 */
public class Sensor5Python {
   
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
   
    public Sensor5Python() {
       
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
            writeBuffer[1]=getRfidCmd;      // COMMAND
            sl030.write(writeBuffer, 0, 2);
 
            Thread.sleep(5000);
 
            // READ SELECT-TAG RESPONSE
              // We'll get an 11 byte response to this
   
    byte[] resp = new byte[11];
    int ret = sl030.read(resp,0,resp.length);
    if (ret > 0) {
        // Successfully read a tag
         byte lenght = resp[0]; //LENGHT BYTE
         byte command = resp[1];  // COMMAND BYTE
         byte status  = resp[2];  // STATUS BYTES
        //console.log("Read:");
        //console.log("- len: "+hdr.len);
        //console.log("- cmd: "+hdr.command);
        //console.log("- status: "+hdr.status);

        // Sanity check the response
        if ((lenght >= 2) && (command == getRfidCmd)) {
            if (status == 0) {
                byte[] uid = null;
                int cardType;
                if (lenght == 10) {
                    // 7 byte UID
                    uid= new byte[7];
                    for (int i=0; i<uid.length;i++)
                        uid[i]=resp[i+3];             
                    cardType = resp[10]-1;
                } else if (lenght == 7) {
                    // 4 byte UID
                    uid= new byte[4];
                    for (int i=0; i<uid.length;i++)
                        uid[i]=resp[i+3];             
                    cardType = resp[7]-1;
                }
                String uidString = "0x";
                for (int i = 0; i < uid.length; i++) {
                    if (uid[i] < 0x10) {
                       // uidString += "0";
                    }
                    uidString =uidString+uid[i];
                }    
                System.out.println("UID: "+uidString);
                return uidString;
            }
        } else {
            // This isn't the sort of response we expected
            System.out.println("Invalid response from RFID reader. Check modprobe baud rate");
        }
    } else   System.out.println("Error Reading!");
       
//            // first just read in a single byte that represents the command+status+data payload
//            int length = sl030.read();
// 
//            System.out.println("TOTAL BYTES AVAILABLE: " + length);
//   
//            // if there are no remaining bytes (length == 0), then we can exit the function
//            if(length <= 0) {
//                System.out.format("Error: %n bytes read for LENGTH/n", length);
//                return "EMPTY";
//            }
// 
//            // if there is any length of remaining bytes, then lets read them now
//            byte[] readBuffer = new byte[length];    
//            int readTotal = sl030.read(readBuffer, 1, length);
//            System.out.println("Letti: "+readTotal);
//            // validate to ensure we got back at least the command and status bytes
//            if (readTotal < 2) {
//                System.out.format("Error: %n bytes read/n", readTotal);
//                return "EMPTY";
//            }
//            
//            // lenght = readBuffer[0]; //LENGHT BYTE
//            byte command = readBuffer[0];  // COMMAND BYTE
//            byte status  = readBuffer[1];  // STATUS BYTES
//            byte type= readBuffer[length-1];
//            byte[] uidByte=new byte[length-3];
//            int k=2;
//            for(int i=0; i<uidByte.length; i++)
//            {
//                uidByte[i]=readBuffer[i+k];
//            }
//            // now we need to get the payload data (if there is any?)
//            String uid="?";
//            String uid2="?";
//            String uid3="?";
//            String uid4="?";
//            String uid5="?";
//            String uid6="?";
//            String uid7="?";
//            if(readTotal > 2){
//              // we skip the first two bytes (command + status)
//              uid= new String(uidByte,"UTF-8");
//              uid2= new String(uidByte);
//              uid3= new String(uidByte,"ASCII");
//              uid4=new String(uidByte,"Latin-1");
//              uid5 = new sun.misc.BASE64Encoder().encode(uidByte);
//              uid6= base64Encode(uidByte);
//              uid7= new String(Base64.getEncoder().encodeToString(uidByte));
//              //result = new String(readBuffer, 3, readTotal - 3, "UTF-8");
//            }
//            // what did we get?
//            System.out.println("-- LENGTH BYTE READ  : " + length);
////            System.out.println("-- COMMAND BYTE READ : " + unsignedToBytes(command));
////            System.out.println("-- STATUS BYTE READ  : " + unsignedToBytes(status));
////            System.out.println("-- TYPE BYTE READ  : " + unsignedToBytes(uid));
//            System.out.println("-- COMMAND BYTE READ : " + command);
//           // System.out.println("-- STATUS BYTE READ  : " + status);
//            System.out.println("-- STATUS BYTE READ  : " + unsignedToBytes(status));          
//            System.out.println("-- TYPE BYTE READ  : " + type);
//            System.out.println("-- DATA BYTES READ   : " + uid);
//            System.out.println("-- DATA BYTES READ   : " + uid2);
//            System.out.println("-- DATA BYTES READ   : " + uid3);
//            System.out.println("-- DATA BYTES READ   : " + uid4);
//            System.out.println("-- DATA BYTES READ   : " + uid5);
//            System.out.println("-- DATA BYTES READ   : " + uid6);
//            System.out.println("-- DATA BYTES READ   : " + uid7);
//            
////            //sl030In = new DataInputStream(new ByteArrayInputStream(bytesTemp));
////            //UT = sl030In.readUnsignedShort();
// 
//           
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
           System.out.println("Interrupted Exception: " + e.getMessage());
        }
     //   System.out.println("Id RFID: " + result);
        return result;
    }
   public static int unsignedToBytes(byte a)
{
    int b = a & 0xFF;
    return b;
}
   private static String base64Encode(byte[] bytes)
{
    return new BASE64Encoder().encode(bytes);
}

}