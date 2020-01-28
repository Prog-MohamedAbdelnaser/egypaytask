package com.egypaytask;

import android.util.JsonReader;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Multiple{

	private String name;

	private String value;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){


		InputStream inputStream = new ByteArrayInputStream(name.getBytes(Charset.forName("UTF-8")));
		String jsonString =Util.getStringFromInputStream(inputStream);

		return jsonString;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value+"";
	}

	public String toStringV() {
		return value;

	}
	@Override
	public String toString() {
				return name;
	}
}