package model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
	private int id;
	private String name;
	private int cookingTime;
	private int temperature;
	private List<Ingredient> ingredients;

	public Recipe() {
		ingredients = new ArrayList<Ingredient>();
	}

	public Recipe(int id, String name, int cookingTime, int temperature, List<Ingredient> ingredients) {
		this.id = id;
		this.name = name;
		this.cookingTime = cookingTime;
		this.temperature = temperature;
		this.ingredients = ingredients;
	}

	public int getId() {
		return (id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return (name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCookingTime() {
		return (cookingTime);
	}

	public void setCookingTime(int cookingTime) {
		this.cookingTime = cookingTime;
	}

	public int getTemperature() {
		return (temperature);
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public List<Ingredient> getIngredients() {
		return (ingredients);
	}

	public void addIngredient(Ingredient ingredient) {
		if (getIngredients().contains(ingredient)) {
			Ingredient existing = getIngredients().get(getIngredients().indexOf(ingredient));
			existing.setQuantity(existing.getQuantity() + ingredient.getQuantity());
		}
		else 
			getIngredients().add(ingredient);
	}

	public void updateIngredient(String name, int newQuantity) {
		Ingredient toUpdate = new Ingredient(name, newQuantity);	
		if ( getIngredients().contains(toUpdate) )
			removeIngredient(name);
		addIngredient(toUpdate);
	}

	public boolean removeIngredient(String name) {
		Ingredient toRemove = new Ingredient(name, -1);
		return (getIngredients().remove(toRemove));
	}

	public List<Ingredient> getMissingIngredients(List<Ingredient> fridgeIngredients) {
		List<Ingredient> missingIngredients = new ArrayList<Ingredient>();
		for (Ingredient recipeIngredient : getIngredients())
			if ( !fridgeIngredients.contains(recipeIngredient) )
				missingIngredients.add( new Ingredient(recipeIngredient.getName(), recipeIngredient.getQuantity()) );
			else {
				Ingredient fridgeIngredient = fridgeIngredients.get(fridgeIngredients.indexOf(recipeIngredient));
				if (recipeIngredient.getQuantity() > fridgeIngredient.getQuantity()) {
					int quantity = recipeIngredient.getQuantity() - fridgeIngredient.getQuantity();
					missingIngredients.add( new Ingredient(recipeIngredient.getName(), quantity) );
				}
			}
		return (missingIngredients);
	}
	
	public boolean checkRecipe(List<Ingredient> fridgeIngredients) {
		List<Ingredient> missingIngredients = getMissingIngredients(fridgeIngredients);
		return (missingIngredients.size() == 0);
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("--- Recipe information ---" + "\n");
		stringBuilder.append("Name: " + getName() + "\n");
		stringBuilder.append("Cooking time: " + getCookingTime() + "\n");
		stringBuilder.append("Temperature: " + getTemperature() + "\n");
		stringBuilder.append("Ingredients (count: " + getIngredients().size() + "): " + "\n");
		for (Ingredient ingredient : getIngredients())
			stringBuilder.append(ingredient.toString() + "\n");
		return (stringBuilder.toString());
	}

}
