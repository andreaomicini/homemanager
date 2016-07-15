package it.unibo.homemanager.humanpresence;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LightPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel image;
	private int id;
	private String nm;
	private int idRoom;
	private int status;
	private JLabel stateLabel;
	private String[] images;
	private String[] state = { "off" , "on" };
	
	
	public LightPanel(int id,int idRoom,int status) {
		setPreferredSize(new Dimension(450, 40));
		setMinimumSize(new Dimension(450, 40));
		setMaximumSize(new Dimension(450, 40));
		
		this.id=id;
		this.idRoom=idRoom;
		this.status=status;
		images  = new String[2];
		images[0]=LightPanel.class.getResource("/lightOff.png").getPath();
		images[1]=LightPanel.class.getResource("/lightOn.png").getPath();	
		if(System.getProperty("os.name").startsWith("Windows")){
			images[0]=images[0].substring(1);
			images[1]=images[1].substring(1);
		}
					
        stateLabel = new JLabel(nm);
        stateLabel.setBounds(20, 12, 150, 21);
        stateLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        
        stateLabel.setText("Lamp id: "+this.id+" in room: "+idRoom+", current state: "+state[status]);
		        
        image= new JLabel();
        image.setIcon(new ImageIcon(new ImageIcon(images[status]).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        this.setAlignmentY(LEFT_ALIGNMENT);
        this.setLayout(new FlowLayout());
        add(image);
        add(stateLabel);
	}	

	public void setStatus(int stato){
		this.status=stato;
		stateLabel.setText("Lamp with id: "+this.id+" , current state: "+state[stato]);
		refresh();
	}

	private void refresh() {
		image.setIcon(new ImageIcon(new ImageIcon(images[status]).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(int idRoom) {
		this.idRoom = idRoom;
	}

	public int getStatus() {
		return status;
	}
}
