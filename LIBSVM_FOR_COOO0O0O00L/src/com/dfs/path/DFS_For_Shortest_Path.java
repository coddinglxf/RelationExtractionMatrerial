package com.dfs.path;

import java.util.ArrayList;

public class DFS_For_Shortest_Path {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int matrix[][]={
				{0,1,0,1,0},
				{0,0,1,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{1,0,1,0,0}
				};
		DFS_For_Shortest_Path shortest=new DFS_For_Shortest_Path();
		for(int i=0;i<matrix.length;i++)
		{
			for(int j=0;j<matrix.length;j++)
			{
				System.out.println("i="+i+"\tj="+j);
				shortest.init_and_run(matrix, i, j);
			}
		}
		
	}
	int min=Integer.MAX_VALUE;
	public ArrayList<Integer>path=new ArrayList<Integer>();
	boolean flags[];
	public void init_and_run(int matrix[][],int i,int j)
	{
		flags=new boolean[matrix.length];
		ArrayList<Integer>templist=new ArrayList<Integer>();
		this.dfs(i, j, i, templist, matrix, flags);
		templist.clear();
		flags=new boolean[matrix.length];
		templist.clear();
		this.min=Integer.MAX_VALUE;
	}
	public void dfs(int i,int j,int current,ArrayList<Integer>templist,int matrix[][],boolean flags[])
	{
		if(current==j) //已经搜索到最短路径
		{
			flags[current]=true;
			templist.add(current);
			if(templist.size()<min)
			{
				min=templist.size();
				path.clear();
				for(int k=0;k<templist.size();k++)
				{
					System.out.print(templist.get(k)+"\t");
					path.add(templist.get(k));
				}
				System.out.println();
				System.out.println("=====....................=====");
			}		
			flags[current]=false;
			templist.remove(templist.size()-1);
			return;
		}
		ArrayList<Integer>adj=this.search_for_adj(current, matrix,flags);
		for(int x=0;x<adj.size();x++)
		{
			int index=adj.get(x);
			templist.add(current);
			flags[current]=true;
			this.dfs(i, j, index, templist, matrix,flags);
			templist.remove(templist.size()-1);
			flags[current]=false;
		}
	}
	private ArrayList<Integer> search_for_adj(int i,int matrix[][],boolean flags[])
	{
		ArrayList<Integer>ret=new ArrayList<Integer>();
		for(int x=0;x<matrix.length;x++)
		{
			if((matrix[x][i]==1||matrix[i][x]==1)&&flags[x]==false)
			{
				ret.add(x);
			}
		}
		return ret;
	}
}
