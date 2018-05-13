package it.unibo.homemanager.agents.wardrobe;

public class OutfitColorFilter implements OutfitFilter {

	private String color;

	public OutfitColorFilter(String color) {

		this.color = color;
	}

	@Override
	public boolean filter(Outfit o) {
		if (color == null || color.equalsIgnoreCase("NO COLOR")) {
			return true;
		}

		return o.getTop().getColor().equalsIgnoreCase(color) || o.getBot().getColor().equalsIgnoreCase(color);

	}

}
