using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using Windows.Networking;
using Windows.Networking.Sockets;
using Windows.Storage.Streams;

/// <summary>
/// 
/// @author Luca Marzaduri
/// 
/// </summary>
/// 


namespace AgentTucson
{
    public class Network
    {
        private StreamSocket socket;

        public Network()
        {

        }

        
        /// <summary>
        /// CONNECT TO SERVER
        /// </summary>
        /// <param name="host">Host name/IP address</param>
        /// <param name="port">Port number</param>
        /// <param name="message">Message to server</param>
        /// <returns>Response from server</returns>
        public async Task<string> connect(string host, string port, string message)
        {
            HostName hostName;
            string res;

            using (socket = new StreamSocket())
            {
                hostName = new HostName(host);

                // Set NoDelay to false so that the Nagle algorithm is not disabled
                socket.Control.NoDelay = false;

                try
                {
                    // Connect to the server
                    await socket.ConnectAsync(hostName, port);
                    // Send the message
                    await this.send(message);
                    // Read response
                    res = await this.read();
                }
                catch (Exception exception)
                {
                    switch (SocketError.GetStatus(exception.HResult))
                    {
                        case SocketErrorStatus.HostNotFound:
                            // Handle HostNotFound Error
                            throw;
                        default:
                            // If this is an unknown status it means that the error is fatal and retry will likely fail.
                            throw;

                    }
                }
            }
            return res;
        }


        /// <summary>
        /// SEND DATA
        /// </summary>
        /// <param name="message">Message to server</param>
        /// <returns>void</returns>
        public async Task send(string message)
        {
            DataWriter writer;

            // Create the data writer object backed by the in-memory stream. 
            using (writer = new DataWriter(socket.OutputStream))
            {

                // Specify the byte order of a stream.
                writer.ByteOrder = Windows.Storage.Streams.ByteOrder.BigEndian;

                writer.WriteBytes(Encoding.UTF8.GetBytes(message));


                // Send the contents of the writer to the backing stream.
                try
                {
                    await writer.StoreAsync();
                }
                catch (Exception exception)
                {
                    switch (SocketError.GetStatus(exception.HResult))
                    {
                        case SocketErrorStatus.HostNotFound:
                            // Handle HostNotFound Error
                            throw;
                        default:
                            // If this is an unknown status it means that the error is fatal and retry will likely fail.
                            throw;
                    }
                }

                await writer.FlushAsync();
                // In order to prolong the lifetime of the stream, detach it from the DataWriter
                writer.DetachStream();
            }
        }

        /// <summary>
        /// READ RESPONSE
        /// </summary>
        /// <returns>Response from server</returns>
        public async Task<String> read()
        {
            DataReader reader;

            using (reader = new DataReader(socket.InputStream))
            {
                // Set the DataReader to only wait for available data (so that we don't have to know the data size)
                reader.InputStreamOptions = Windows.Storage.Streams.InputStreamOptions.Partial;

                // The encoding and byte order need to match the settings of the writer we previously used.
                //reader.UnicodeEncoding = Windows.Storage.Streams.UnicodeEncoding.Utf16BE;
                reader.ByteOrder = Windows.Storage.Streams.ByteOrder.BigEndian;

                // Send the contents of the writer to the backing stream. 
                // Get the size of the buffer that has not been read.
                await reader.LoadAsync(10000);
                byte[] response = new byte[reader.UnconsumedBufferLength];
                reader.ReadBytes(response);

                return Encoding.UTF8.GetString(response);
            }
        }

        /// <summary>
        /// PARSE RESPONSE
        /// </summary>
        /// <param name="toParse">Response fromm server to be parsed</param>
        /// <returns>List of LogicTuples obtained parsing response from server</returns>
        public static List<LogicTuple> parse(string toParse)
        {
            List<LogicTuple> res = new List<LogicTuple>();

            if (!toParse.StartsWith("["))
            {
                return null;
            }
            else
            {
                toParse = toParse.Trim('[', ']');
                string[] tuples = toParse.Split();
                LogicTuple temp;

                foreach (string t in tuples)
                {
                    temp = new LogicTuple();

                    string[] nameAndArgs = t.Split('(', ')', ',');

                    temp.Name = nameAndArgs[0];
                    nameAndArgs[0] = "";

                    foreach (string s in nameAndArgs)
                    {
                        if (!String.IsNullOrEmpty(s))
                            temp.Arguments.Add(s);
                    }

                    res.Add(temp);
                }
            }

            return res;
        }
    }
}
