package com.egypaytask;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
public class ViewItem implements Comparable<ViewItem> {

	@SerializedName("name")
	private String name;


	@Nullable
	@SerializedName("multiple")
	private String multiple="";

	@SerializedName("default_value")
	private String defaultValue;

	@SerializedName("id")
	private int id;

	@SerializedName("sort")
	private String sort;

	@SerializedName("type")
	private String type;

	@SerializedName("required")
	private String required;

	private String value ;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/*
	public ArrayList<Multiple> getMultiple() {
		return multiple;
	}

	public void setMultiple(ArrayList<Multiple> multiple) {
		this.multiple = multiple;
	}
*/

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDefaultValue(String  defaultValue){
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue(){
		return defaultValue;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSort(String sort){
		this.sort = sort;
	}

	public String getSort(){
		return sort;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setRequired(String required){
		this.required = required;
	}

	public Boolean getIsRequired(){
		return required.equals("yes");
	}

	public ArrayList<Multiple> getMultiple() {
		MultipleItems multiples = new MultipleItems();
		if (multiple!=null || !multiple.isEmpty()) {
			Gson gson = new Gson();

		 multiples = gson.fromJson(multiple, MultipleItems.class);

			Log.i("getMultiple", "getMultiple" + multiples.get(0).toString());
		}
		return multiples;
	}

	public void setMultiple(String multiple) {


		this.multiple = multiple;
	}
	@Override
 	public String toString(){
		return 
			"ViewItem{" + 
			"name = '" + name + '\'' + 
			",default_value = '" + defaultValue + '\'' +
			",id = '" + id + '\'' + 
			",sort = '" + sort + '\'' + 
			",type = '" + type + '\'' + 
			",required = '" + required + '\'' + 
			"}";
		}

	@Override
	public int compareTo(ViewItem compareItem) {
		int compareage= Integer.parseInt(((ViewItem)compareItem).getSort());
		/* For Ascending order*/
		int current  =Integer.parseInt(this.getSort());

		return current - compareage;
	}
}