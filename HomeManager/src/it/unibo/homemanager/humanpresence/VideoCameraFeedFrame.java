package it.unibo.homemanager.humanpresence;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class VideoCameraFeedFrame extends JFrame {
	

	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	
	public void release()
	{
		mediaPlayerComponent.release();
	}
	public void init()
	{
		this.setTitle("Video Feed");
		new NativeDiscovery().discover();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		setContentPane(mediaPlayerComponent);
		setLocation(100, 100);
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {	
                mediaPlayerComponent.release();
                setVisible(false);
            }
        });
	}
	public void play(String filename)
	{
		setVisible(true);
		mediaPlayerComponent.getMediaPlayer().playMedia(filename);
	}
	
}
