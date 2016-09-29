///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package iotdevice;
//
///**
// *
// * @author Matteo
// */
public class Lettore {
//    // Node.js module for i2c SL030 RFID readers on the Raspberry Pi
//// (c) Copyright 2014 MCQN Ltd (http://www.mcqn.com)
//// Released under an Apache v2.0 license
//
//// We have the libbcm2835.so file in the same directory as this file
//// FIXME It would be better to spin this out as a separate node module
//// FIXME so that other Node.js Raspberry Pi SPI and I2C projects could use it
//var libbcm2835 = ffi.Library(path.dirname(module.filename)+'/libbcm2835.so', {
//    'bcm2835_init': ['uint8', [] ],
//    'bcm2835_close': ['uint8', [] ],
//    // SPI functions
//    'bcm2835_spi_begin': ['uint8', [] ],
//    'bcm2835_spi_end': ['uint8', [] ],
//    'bcm2835_spi_transfer': ['uint8', ['uint8'] ],
//    'bcm2835_spi_transfernb': ['void', ['pointer', 'pointer', 'uint'] ],
//    'bcm2835_spi_setClockDivider': ['void', ['uint8'] ],
//    'bcm2835_spi_setBitOrder': ['void', ['uint8'] ],
//    'bcm2835_spi_chipSelect': ['void', ['uint8'] ],
//    'bcm2835_spi_setDataMode': ['void', ['uint8'] ],
//    'bcm2835_spi_setChipSelectPolarity': ['void', ['uint8', 'uint8'] ],
//    // I2C functions
//    'bcm2835_i2c_begin': ['void', [] ],
//    'bcm2835_i2c_end': ['void', [] ],
//    'bcm2835_i2c_setSlaveAddress': ['void', ['uint8'] ],
//    'bcm2835_i2c_setClockDivider': ['void', ['uint16'] ],
//    'bcm2835_i2c_set_baudrate': ['void', ['uint32'] ],
//    'bcm2835_i2c_write': ['uint8', ['pointer', 'uint32'] ],
//    'bcm2835_i2c_read': ['uint8', ['pointer', 'uint32'] ],
//    'bcm2835_i2c_read_register_rs': ['uint8', ['pointer', 'pointer', 'uint32'] ],
//    'bcm2835_i2c_write_read_rs': ['uint8', ['pointer', 'uint32', 'pointer', 'uint32'] ]
//});
//
//function RFID_SL030() {
//    this.kCardTypeNames = [ 'Mifare 1K', 'Mifare Pro', 'Mifare Ultralight', 'Mifare 4K', 'Mifare ProX', 'Mifare DesFire'];
//    // RFID card types
//    this.kMifare1K = 0x01;
//    this.kMifarePro = 0x02;
//    this.kMifareUltralight = 0x03;
//    this.kMifare4K = 0x04;
//    this.kMifareProX = 0x05;
//    this.kMifareDesFire = 0x06;
//    // Command constants
    int kCmdIdle = 0x00;
    int kCmdSelect = 0x01;
//    int kCmdLogin = 0x02;
//    int kCmdRead16 = 0x03;
//    int kCmdWrite16 = 0x04;
//    int kCmdReadValue = 0x05;
//    int kCmdWriteValue = 0x06;
//    int kCmdWriteKey = 0x07;
//    int kCmdIncValue = 0x08;
//    int kCmdDecValue = 0x09;
//    int kCmdCopyValue = 0x0A;
//    int kCmdRead4 = 0x10;
//    int kCmdWrite4 = 0x11;
//    int kCmdSeek = 0x20;
//    int kCmdSetLED = 0x40;
//    int kCmdSleep = 0x50;
//    int kCmdReset = 0xFF;
//    // Various packet offsets
//    int kResponseOffsetLength = 0;
//    int kResponseOffsetCommand = 1;
//    int kResponseOffsetStatus = 2;
//    int kResponseOffsetData = 3;
//    // Variables
//    this.libbcm2835 = null; // To access the Pi's i2c bus
//}
//exports.RFID_SL030 = RFID_SL030;
//
//RFID_SL030.prototype.init = function() {
//    // Check if we've the right permissions (we need to be root
//    // to access /dev/mem).  Otherwise we'll segfault in the bcm2835init()
//    // function, at least this will be a bit friendlier
//    f = fs.openSync('/dev/mem', 'r');
//    fs.closeSync(f);
//    // Check done.
//    this.libbcm2835 = libbcm2835;
//    this.libbcm2835.bcm2835_init();
//    this.libbcm2835.bcm2835_i2c_begin();
//    this.libbcm2835.bcm2835_i2c_setSlaveAddress(0x50); // FIXME Magic number
//}
//
//RFID_SL030.prototype.sectorForBlock = function(aBlock) {
//    return aBlock >> 2;
//}
//
//RFID_SL030.prototype.sendCommand = function(aCommandCode, aData) 
//    public int sendCommand (int aCommandCode, byte[] aData)
//    {
//    int dataLen = aData.length;
//    int[] cmd = new Buffer(2+dataLen);
//    cmd[0] = dataLen+1; // length of the command
//    cmd[1] = aCommandCode;
//    if (dataLen > 0) {
//        // Copy the data in
//        aData.copy(cmd, 2);
//    }
//    return this.libbcm2835.bcm2835_i2c_write(cmd, cmd.length);
//}
//
//RFID_SL030.prototype.selectTag = function() {
//    this.sendCommand(this.kCmdSelect);
//    sleep.usleep(50000);
//    // We'll get an 11 byte response to this
//    var respHeader = struct({
//        len: ref.types.byte,
//        command: ref.types.byte,
//        status: ref.types.byte
//    });
//    var resp = new Buffer(11);
//    var ret = (this.libbcm2835.bcm2835_i2c_read(resp, resp.length));
//    if (ret == 0) {
//        // Successfully read a tag
//        var hdr = respHeader(resp.slice(0,3));
//        //console.log("Read:");
//        //console.log("- len: "+hdr.len);
//        //console.log("- cmd: "+hdr.command);
//        //console.log("- status: "+hdr.status);
//
//        // Sanity check the response
//        if ((hdr.len >= 2) && (hdr.command == this.kCmdSelect)) {
//            if (hdr.status == 0) {
//                var uid;
//                var cardType;
//                if (hdr.len == 10) {
//                    // 7 byte UID
//                    uid = resp.slice(3,10)
//                    cardType = this.kCardTypeNames[resp[10]-1];
//                } else if (hdr.len == 7) {
//                    // 4 byte UID
//                    uid = resp.slice(3,7);
//                    cardType = this.kCardTypeNames[resp[7]-1];
//                }
//                var uidString = "0x";
//                for (i = 0; i < uid.length; i++) {
//                    if (uid[i] < 0x10) {
//                        uidString += "0";
//                    }
//                    uidString += uid[i].toString(16);
//                }
//                return { tagID: uid, tagIDString: uidString, tagType: cardType };
//            }
//        } else {
//            // This isn't the sort of response we expected
//            console.log("Invalid response from RFID reader. Check modprobe baud rate");
//        }
//    } else {
//        console.log("read failed");
//    }
//    return null;
//}

}
