package it.unibo.homemanager.agents.wardrobe;

import java.util.Vector;

public class Outfit {

	private Dress top;
	private Dress bot;
	private Vector<String> accessories;

	public Outfit(Dress top, Dress bot) {
		this.top = top;
		this.bot = bot;
		this.accessories = new Vector<>();
	}

	public Outfit(Dress top, Dress bot, Vector<String> accessories) {
		this.top = top;
		this.bot = bot;
		this.accessories = accessories;
	}

	public Dress getTop() {
		return top;
	}

	public Dress getBot() {
		return bot;
	}

	public Vector<String> getAccessories() {

		return accessories;
	}

	public void addAccessory(String accessory) {
		getAccessories().add(accessory);
	}

}
