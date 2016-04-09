package it.unibo.homemanager.fridge;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
	
private int id;
private String name;
private int cookingtime;
private int temperature;
private List<Ingredient> ingredients;

public Recipe(){
	this.ingredients=new ArrayList<Ingredient>();
}
public Recipe(int id, String name, int cookingtime, int temperature, List<Ingredient> ingredients){
this.id=id;
this.name=name;
this.cookingtime=cookingtime;
this.temperature=temperature;
this.ingredients=ingredients;
}

public Recipe(int id, String name, int cookingtime, int temperature){
this.id=id;
this.name=name;
this.cookingtime=cookingtime;
this.temperature=temperature;
this.ingredients=new ArrayList<Ingredient>();
}

public Recipe(int id, String name, List<Ingredient> ingredients){
this.id=id;
this.name=name;
this.ingredients=ingredients;
}

public Recipe(int id, String name){
this.id=id;
this.name=name;
this.ingredients=new ArrayList<Ingredient>();
}

public void setId(int id){
	this.id=id;
}

public void setName(String name){
	this.name=name;
}

public void setCookingtime(int cookingtime){
	this.cookingtime=cookingtime;
}

public void setTemperature(int temperature){
	this.temperature=temperature;
}

public void setIngredients(List<Ingredient> ingredients){
	this.ingredients=ingredients;
}

public int getId(){
	return this.id;
}

public String getName(){
	return this.name;
}

public int getCookingtime(){
	return this.cookingtime;
}

public int getTemperature(){
	return this.temperature;
}

public List<Ingredient> getIngredients(){
	return this.ingredients;
}

public Ingredient getIngredient(int index){
	return this.ingredients.get(index);
}
 
public int sizeIngredients(){
	return this.ingredients.size();
}

public void addIngredient(Ingredient ingredient){
	this.ingredients.add(ingredient);
}

public void updateIQuantity(String name, int quantity){
	for(int i=0;i<this.ingredients.size();i++)
		if(this.ingredients.get(i).getName().equalsIgnoreCase(name))
			  this.ingredients.get(i).setQuantity(quantity);
}

public void removeIngredient(String name){
	for(int i=0;i<this.ingredients.size();i++)
		if(this.ingredients.get(i).getName().equalsIgnoreCase(name))
			this.ingredients.remove(i);
}

public boolean checkRecipe(List<Ingredient> ingredients){
	boolean test=true;
	for(int i=0;i<this.ingredients.size();i++){
		if(!this.ingredients.get(i).checkIngredient(ingredients))
			test=false;
	}
	
	return test;
}

public List<Ingredient> getMissingIngredients(List<Ingredient> ingredients){
	ArrayList<Ingredient> list= new ArrayList<Ingredient>();
    boolean found=false;
    int quantity;
	for(Ingredient temp1:this.getIngredients()){
		for(Ingredient temp2:ingredients){
			if(temp1.getName().equalsIgnoreCase(temp2.getName())){
				found=true;
				if(!temp1.isContained(temp2)){
				quantity=temp1.getQuantity()-temp2.getQuantity();
				list.add(new Ingredient(temp1.getName(),quantity));
				}
			}
		}
		if(found==false)
			list.add(new Ingredient(temp1.getName(),temp1.getQuantity()));
		
		found=false;
	}
return list;	
}

public String toString(){
	String s="";
	s=s/*+this.getId()+" "*/+this.getName()+"  "+this.getCookingtime()+"  "+this.getTemperature()+"\n";
	/*for(Ingredient temp:this.getIngredients())
		s=s+temp.toString();*/
			return s;
}

}
