package it.unibo.homemanager.fridge;

import java.util.List;

public class Ingredient {

	private String name;
	private int quantity;
	
	public Ingredient(String name, int quantity){
		this.name=name;
		this.quantity=quantity;
	}
	public Ingredient(String name){
		this.name=name;
	}
	public Ingredient(){}
	public void setName(String name){
		this.name=name;
	} 
	
	public void setQuantity(int quantity){
		this.quantity=quantity;
	}
	
	public String getName(){
		return this.name;
	}
	public int getQuantity(){
		return this.quantity;
	}
	
	public boolean isContained(Ingredient ingredient){
		if((this.name.equalsIgnoreCase(ingredient.getName()))&&(this.quantity<=ingredient.getQuantity()))
			return true;
		else
			return false;					
	}
	
	public boolean checkIngredient(List<Ingredient> ingredients){
		boolean test=false;
		for(Ingredient temp:ingredients){
			if(this.isContained(temp))
				test=true;
		}
		return test;
	}

	public int getIngredientQuantityFromList(List<Ingredient> ingredients){
	for(Ingredient temp:ingredients){
		if(this.getName().equalsIgnoreCase(temp.getName()))
			return temp.getQuantity();
	}
	return 0;
}
	public String toString(){
		String s="";
		
		s=s+this.getName()+" "+this.getQuantity()+"\n";
		return s;}
}