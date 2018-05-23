package wardrobeAgent.model;

import java.io.IOException;

import wardrobeAgent.lpaasLibrary.LPaasLibrary;
import wardrobeAgent.model.Sensors;

public class MainTest {

	public static void main(String[] args) {

		
	
		 
		Sensors s = new Sensors();

		while (true) {

			try {
				s.displayOutTemp();

				s.ledOn();
				Thread.sleep(1000);
				s.ledOff();

				// s.doBip();

				System.out.println("TEMPERATURA: " + s.getTemperature());
				System.out.println("LUCE: " + s.getLightIntensity());

				System.out.println("Attesa lettura RFID: ");

				String rfid = s.readRfid();

				System.out.println("RFID: " + rfid);

			} catch (IOException | InterruptedException e) {

				e.printStackTrace();
			}
	}

//SensorManager sm = new SensorManager(null,"sensor", "S3ns0r@");
//sm.start();
//String token=LPaasLibrary.getToken("sensor", "S3ns0r@");
//
//System.out.println(token);








		

	}

}
