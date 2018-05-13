package wardrobeAgent.model;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.iot.raspberry.grovepi.GroveDigitalOut;
import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.devices.GroveLed;
import org.iot.raspberry.grovepi.devices.GroveLightSensor;
import org.iot.raspberry.grovepi.devices.GroveRgbLcd;
import org.iot.raspberry.grovepi.devices.GroveTemperatureAndHumiditySensor;
import org.iot.raspberry.grovepi.devices.GroveTemperatureAndHumidityValue;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Sensors {
	// Raspberry Pi's I2C bus
	private static final int i2cBus = 1;

	// RFID addresses
	private static final int DEVICE_ADDRESS = 0x50;

	// Read RFID command
	private static final byte getRfidCmd = (byte) 0x01;
	private static final byte firmwareRfidCmd = (byte) 0xF0;

	// I2C bus
	I2CBus bus;

	// Device object
	private I2CDevice sl030;

	// Sensori

	private GrovePi grovePi;
	private GroveLightSensor lightSensor;
	private GroveTemperatureAndHumiditySensor temperatureAndHumiditySensor;
	// private GroveTemperatureAndHumidityValue temperatureAndHumidityValue;
	private GroveRgbLcd lcd;
	private GroveLed led;
	private GroveDigitalOut bip;

	public Sensors() {

		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
			System.out.println("Connected to bus OK!!!");

			// get device itself
			sl030 = bus.getDevice(DEVICE_ADDRESS);

			System.out.println("Connected to device OK!!!");
			// Small delay before starting
			// Thread.sleep(500);
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
		}

		try {
			grovePi = new GrovePi4J();

			temperatureAndHumiditySensor = new GroveTemperatureAndHumiditySensor(grovePi, 2, GroveTemperatureAndHumiditySensor.Type.DHT11);

			led = new GroveLed(grovePi, 3);
			// led= grovePi.getDigitalOut(3);
			bip = grovePi.getDigitalOut(4);
			lightSensor = new GroveLightSensor(grovePi, 5);

			lcd = grovePi.getLCD();

		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
		}

	}

	public void displayOutTemp() throws IOException {

		int[] color = new int[] { 0, 128, 0 };
		lcd.setRGB(color[0], color[1], color[2]);

		System.out.println("" + temperatureAndHumiditySensor.get().toString());
		double temperatura = temperatureAndHumiditySensor.get().getTemperature();
		lcd.setText("TEMPERATURA\nESTERNA: " + temperatura + " C");

	}

	public void displayAction(String action, Dress dress) throws IOException {

		int[] color = new int[] { 0, 128, 0 };
		lcd.setRGB(color[0], color[1], color[2]);

		lcd.setText(action.toUpperCase() + "\n" + dress.getName());

	}

	public double getTemperature() throws IOException {

		return temperatureAndHumiditySensor.get().getTemperature();

	}

	public double getHumidity() throws IOException {

		return temperatureAndHumiditySensor.get().getHumidity();

	}

	public double getLightIntensity() throws IOException {

		return lightSensor.get();

	}

	public void doBip() throws IOException, InterruptedException {

		bip.set(true);
		Thread.sleep(100);
		bip.set(false);

	}

	public void ledOn() throws IOException {
		led.set(true);

	}

	public void ledOff() throws IOException {

		led.set(false);

	}

	public String readRfid() throws IOException {

		byte[] writeBuffer = new byte[2];
		String result = "NO-TAG";
		try {
			// SEND COMMAND TO DEVICE TO REQUEST FIRMWARE VERSION
			writeBuffer[0] = (byte) 0x01; // LENDTH (CMD+DATA)
			writeBuffer[1] = getRfidCmd; // COMMAND
			sl030.write(writeBuffer, 0, 2);

			Thread.sleep(1000);

			// READ SELECT-TAG RESPONSE
			// first just read in a single byte that represents the
			// command+status+data payload
			int length = sl030.read();

			System.out.println("TOTAL BYTES AVAILABLE: " + length);

			// if there are no remaining bytes (length == 0), then we can exit
			// the function
			if (length <= 0) {
				System.out.format("Error: %n bytes read for LENGTH/n", length);
				return "NO-TAG";
			}

			// if there is any length of remaining bytes, then lets read them
			// now
			byte[] readBuffer = new byte[length + 1];
			int readTotal = sl030.read(readBuffer, 0, length);
			System.out.println("Letti: " + readTotal);
			// validate to ensure we got back at least the command and status
			// bytes
			if (readTotal <= 3) {
				System.out.format("Error: %n bytes read/n", readTotal);
				return "NO-TAG";
			}

			byte leng = readBuffer[0];
			byte command = readBuffer[1]; // COMMAND BYTE
			byte status = readBuffer[2]; // STATUS BYTES
			byte type = readBuffer[readBuffer.length - 1];
			byte[] uidByte = new byte[readBuffer.length - 4];
			int k = 3;
			for (int i = 0; i < uidByte.length; i++) {
				uidByte[i] = readBuffer[i + k];
			}
			// now we need to get the payload data (if there is any?)
			String uid = "?";

			if (readTotal > 3) {
				// uid=Base64.encodeBase64String(uidByte);
				uid = DatatypeConverter.printHexBinary(uidByte);
			}
			// what did we get?
			// System.out.println("-- LENGTH BYTE READ : " + leng);
			// System.out.println("-- COMMAND BYTE READ : " + command);
			// System.out.println("-- STATUS BYTE READ : " + status);
			// System.out.println("-- TYPE BYTE READ : " + type);
			// System.out.println("-- DATA BYTES READ : " + uid);

			result = uid;
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception: " + e.getMessage());
		}
		// System.out.println("Id RFID: " + result);
		// if (!result.equalsIgnoreCase("NO-TAG"))
		// {
		// this.Display(result);
		// }
		return result;
	}

}
