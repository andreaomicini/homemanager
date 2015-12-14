using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

/// <summary>
/// 
/// @author Luca Marzaduri
/// 
/// </summary>
/// 


namespace AgentTucson
{
    public class LogicTuple
    {
        private string name = "";
        private List<string> arguments = new List<string>();

        public LogicTuple()
        {
       
        }

        public LogicTuple(string name)
        {
            this.name = name;
        }

        public LogicTuple(string name, string argument) : this(name)
        {   
            this.arguments.Add(argument);
        }

        public LogicTuple(string name, List<string> arguments) : this(name)
        {
            this.arguments = arguments;
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

        public List<string> Arguments
        {
            get
            {
                return this.arguments;
            }

            set
            {
                this.arguments = value;
            }
        }

        public void addArgument(string argument)
        {
            this.Arguments.Add(argument);
        }

       
        public override string ToString()
        {
            string res = "";
            res += this.Name + "(";

            foreach (string s in this.Arguments)
            {
                res += s + ",";
            }

            res = res.Substring(0, res.Length - 1) + ")";
            return res;
        }


    }
}
