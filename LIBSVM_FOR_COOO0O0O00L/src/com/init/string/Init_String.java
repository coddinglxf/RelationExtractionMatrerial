package com.init.string;

import java.util.ArrayList;
import java.util.List;

public class Init_String {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public List<String> list = new ArrayList<String>();

	public void from_string_to_list(String str) {
		str = str.trim();

		String words[] = str.split("@");
		for (int i = 0; i < words.length; i++)
		{
			String temp=words[i].trim();
			this.list.add(temp);
			
		}
	}
	public void from_string_to_list2(String str) {
		str = str.trim();

		String words[] = str.split("\t");
		for (int i = 0; i < words.length; i++) 
		{
			String temp=words[i].trim();
			this.list.add(temp);
		}
	} 
	public void print()
	{
		System.out.println();
		for(String s:this.list)
		{
			System.out.print(s+" ");
		}
		System.out.println();
	}
}
