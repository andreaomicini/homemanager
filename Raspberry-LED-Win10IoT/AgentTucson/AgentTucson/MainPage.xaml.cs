using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Xml;
using Windows.ApplicationModel;
using Windows.Devices.Gpio;
using Windows.Storage;
using Windows.UI.Core;
using Windows.UI.Xaml.Controls;

// Il modello di elemento per la pagina vuota è documentato all'indirizzo http://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x410

/// <summary>
/// 
/// @author Luca Marzaduri
/// 
/// </summary>
/// 

namespace AgentTucson
{
    /// <summary>
    /// Pagina vuota che può essere utilizzata autonomamente oppure esplorata all'interno di un frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        private List<Room> rooms = new List<Room>();

        public delegate void printDelegate(string message);
        public event printDelegate print;

        public MainPage()
        {
            this.InitializeComponent();
            this.print += this.printToVideo;
            this.init();
        }

        public async void init()
        {
            var filename = System.IO.Path.Combine(Package.Current.InstalledLocation.Path, "config.xml");
            var file = await StorageFile.GetFileFromPathAsync(filename);
            var inputStream = await file.OpenSequentialReadAsync();

            XmlDocument doc = new XmlDocument();
            doc.Load(inputStream.AsStreamForRead());
            
            Room r;
            string serverip, port, tcname, netid, netidport, command, tuple;
            XmlNodeList serveriplist, portlist, tcnamelist, netidlist, netidportlist, commandlist, tuplelist;

            serveriplist = doc.DocumentElement.GetElementsByTagName("server-ip");
            portlist = doc.DocumentElement.GetElementsByTagName("port");
            tcnamelist = doc.DocumentElement.GetElementsByTagName("tcname");
            netidlist = doc.DocumentElement.GetElementsByTagName("netid");
            netidportlist = doc.DocumentElement.GetElementsByTagName("netidport");
            commandlist = doc.DocumentElement.GetElementsByTagName("command");
            tuplelist = doc.DocumentElement.GetElementsByTagName("tuple");



            if (serveriplist.Count == 1 &&
                portlist.Count == 1 &&
                tcnamelist.Count == 1 &&
                netidlist.Count == 1 &&
                netidportlist.Count == 1 &&
                commandlist.Count == 1 &&
                tuplelist.Count == 1) 
            {
                serverip = serveriplist[0].InnerText;
                port = portlist[0].InnerText;
                tcname = tcnamelist[0].InnerText;
                netid = netidlist[0].InnerText;
                netidport = netidportlist[0].InnerText;
                command = commandlist[0].InnerText;
                tuple = tuplelist[0].InnerText;
            }
            else
            {
                this.print("Configuration error!\n");
                return;
            }

            XmlNodeList nodes = doc.DocumentElement.GetElementsByTagName("room");

            foreach (XmlNode node in nodes)
            {
                if (node.HasChildNodes && node.ChildNodes.Count == 2)
                {
                    if (node.ChildNodes[0].Name == "name" && node.ChildNodes[1].Name == "pin")
                    {
                        string name = node.ChildNodes[0].InnerText;
                        int pin = Int32.Parse(node.ChildNodes[1].InnerText);
                        r = new Room(name, pin);
                        rooms.Add(r);
                    }
                }

            }

            inputStream.Dispose();

            Network net = new Network();

            this.execute(serverip, port, tcname + "@" + netid + ":" + netidport + "?" + command + "(" + tuple + ")", net);
        }

        public async void execute(string host, string port, string message, Network net)
        {

            while (true)
            { 
                string res = await net.connect(host, port, message);

                if (!res.StartsWith("["))
                {
                    this.print("Error: " + res + "\n");
                }
                else
                {
                    List<LogicTuple> tuples = Network.parse(res);

                    foreach (LogicTuple l in tuples)
                    {
                        if (l.Arguments.Count > 2)
                        {
                            foreach (Room c in rooms)
                            {
                                if (l.Arguments[0].ToLower() == c.Name.ToLower())
                                {
                                    if (l.Arguments[1].ToLower() != l.Arguments[1].ToLower())
                                    {
                                        if (l.Arguments[1].ToLower() == "on")
                                        {
                                            c.Pin.Write(GpioPinValue.High);
                                            this.print(c.Name + " LED: ON" + "\n");
                                        }

                                        if (l.Arguments[1].ToLower() == "off")
                                        {
                                            c.Pin.Write(GpioPinValue.Low);
                                            this.print(c.Name + " LED: OFF" + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public void printToVideo(string message)
        {
            textBox.Text += message;
        }

    }
}
