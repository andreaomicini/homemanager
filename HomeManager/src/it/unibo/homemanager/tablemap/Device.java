/*
 * Device.java
 *
 * Created on 13 ottobre 2006, 14.40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.unibo.homemanager.tablemap;

/**
 *
 * @author admin
 */

import it.unibo.homemanager.dbmanagement.dbexceptions.DuplicatedRecordDbException;
import it.unibo.homemanager.dbmanagement.dbexceptions.ResultSetDbException;
import it.unibo.homemanager.dbmanagement.dbexceptions.NotFoundDbException;
import it.unibo.homemanager.dbmanagement.Database;
import java.sql.*;


public class Device {

  public int idDev, roomId;
  public String name, param;
  public String type;
  public float energy;
  
  /** Creates a new instance of Device */
  public Device(int id, String nm, float e, String p, String t, int ri) {                  

    idDev = id;
    name = nm;
    energy = e;
    param = p;
    type = t;
    roomId = ri;
  }

    public String toString() {
	return "Name: "+name+"\n"+
		"Device Type: "+type+"\n"+
		"Energy Consumption: "+energy+" kW\n"+
		"Parameters: "+param;
    }
}