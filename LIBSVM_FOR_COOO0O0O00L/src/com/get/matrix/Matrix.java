package com.get.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.stanford.parser.Parser_Init;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.TypedDependency;

public class Matrix {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	public int matrix[][];
	public HashMap<Integer, String>hash_dependency=new HashMap<Integer, String>();
//	public void getmatrix(Parser_Init p)
//	{
//		ArrayList<TaggedWord>taggedwordslist=p.taggedword;
//		List<TypedDependency>dependencies=p.dependencies;
//		matrix=new int[taggedwordslist.size()+1][taggedwordslist.size()+1];  //
//		for(int i=0;i<dependencies.size();i++)
//		{
//			TypedDependency td=dependencies.get(i);
//			int x=td.dep().index();
//			int y=td.gov().index();
//			matrix[x][y]=1;
//			matrix[y][x]=1;
//		}
//	}
	public void getmatrix(Parser_Init p)
	{

		ArrayList<TaggedWord>taggedwordslist=p.taggedword;
		List<TypedDependency>dependencies=p.dependencies;
		matrix=new int[taggedwordslist.size()+1][taggedwordslist.size()+1];  //
		for(int i=0;i<dependencies.size();i++)
		{
			TypedDependency td=dependencies.get(i);
			int x=td.dep().index();
			int y=td.gov().index();
			if(!td.reln().toString().equals("conj_and"))
			{
				matrix[y][x]=1;
				int index=y*100000+x;
				this.hash_dependency.put(index, td.reln().toString());
			}		
		}
	
	}
}
