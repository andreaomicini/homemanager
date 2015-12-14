using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Devices.Gpio;

/// <summary>
/// 
/// @author Luca Marzaduri
/// 
/// </summary>
/// 


namespace AgentTucson
{
    public class Room
    {
        private GpioPin openPin = null;
        private string name;
        private int pin;

        public Room (string name, int pin)
        {
            this.initialize(pin);
            this.name = name;
            this.pin = pin;
        }

        public GpioPin Pin
        {
            get
            {
                return this.openPin;
            }

            set
            {
                openPin = value;
            }
        }

        public string Name
        {
            get
            {
                return this.name;
            }

            set
            {
                this.name = value;
            }
        }

        private void initialize(int pin)
        {
            var gpio = GpioController.GetDefault();

            if (gpio == null)
            {
                return;
            }

            GpioPin p = gpio.OpenPin(pin);
            p.Write(GpioPinValue.Low);
            p.SetDriveMode(GpioPinDriveMode.Output);
            this.openPin = p;
        }
    }
}
