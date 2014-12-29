package com.dfs.path;

import java.util.ArrayList;
import java.util.HashSet;

public class DFS_For_Parents {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int matrix[][]={
				{0,0,0,1,0,0},
				{0,0,1,0,0,0},
				{1,0,0,0,0,0},
				{0,1,0,0,0,0},
				{0,0,1,1,0,0},
				{1,1,0,0,0,0}};
		DFS_For_Parents dfs=new DFS_For_Parents();
		dfs.init_and_run(matrix, 2, 3, 4);
	}
	public DFS_For_Parents()
	{
		this.left.clear();
		this.right.clear();
		this.legal=true;
		this.min1=Integer.MAX_VALUE;
		this.min2=Integer.MAX_VALUE;;
	}
	public void init_and_run(int matrix[][],int left,int right,int aim)
	{
		boolean flags[]=new boolean[matrix.length];
		ArrayList<Integer>list=new ArrayList<Integer>();	    
	    DFS(matrix, aim, left, aim, flags, list);	 
	    
	    flags=new boolean[matrix.length];
	    this.min1=Integer.MAX_VALUE;
	    
		DFS2(matrix,aim,right,aim,flags,list);
	
		this.min2=Integer.MAX_VALUE;
		flags=new boolean[matrix.length];
		this.legal();
	}

	int min1=Integer.MAX_VALUE;
	int min2=Integer.MAX_VALUE;
	public ArrayList<Integer>left=new ArrayList<Integer>();
	public ArrayList<Integer>right=new ArrayList<Integer>();
	public boolean legal=true;
	public void DFS(int matrix[][],int i,int j,int i_cur,boolean flags[],ArrayList<Integer>list)
	{
		if(i_cur==j)
		{
			flags[i_cur]=true;
			list.add(i_cur);
			if(list.size()<min1)
			{
				min1=list.size();
				left.clear();
				for(int k=0;k<list.size();k++)
				{		
					left.add(list.get(k));
				}
			}
			list.remove(list.size()-1);
			flags[i_cur]=false;
			return;
		}
		ArrayList<Integer>temp=this.get_parent(matrix, i_cur, flags);
		for(int index:temp)
		{
			flags[i_cur]=true;
			list.add(i_cur);
			this.DFS(matrix, i, j,index, flags,list);
			list.remove(list.size()-1);
			flags[i_cur]=false;
		}
	}
	public void DFS2(int matrix[][],int i,int j,int i_cur,boolean flags[],ArrayList<Integer>list)
	{
		if(i_cur==j)
		{
			flags[i_cur]=true;
			list.add(i_cur);
			if(list.size()<min2)
			{
				min2=list.size();
				right.clear();
				for(int k=0;k<list.size();k++)
				{		
					right.add(list.get(k));
				}
			}
			list.remove(list.size()-1);
			flags[i_cur]=false;
			return;
		}
		ArrayList<Integer>temp=this.get_parent(matrix, i_cur, flags);
		for(int index:temp)
		{
			flags[i_cur]=true;
			list.add(i_cur);
			this.DFS2(matrix, i, j,index, flags,list);
			list.remove(list.size()-1);
			flags[i_cur]=false;
		}
	}

	
	public ArrayList<Integer> get_parent(int matrix[][],int i_cur,boolean flags[])
	{
		ArrayList<Integer>ret=new ArrayList<Integer>();
		
		for(int i=0;i<matrix.length;i++)
		{
			if(matrix[i_cur][i]==1&&flags[i]!=true)
			{
				ret.add(i);
			}
		}		
		return ret;
	} 
	public void legal()
	{
		if(left.size()<2||right.size()<2)
		{
			this.legal=false;
			return ;
		}
		HashSet<Integer>hash=new HashSet<Integer>();
		for(int i=1;i<this.left.size();i++)
		{
			int index=this.left.get(i);
			if(hash.contains(index))
			{
				System.out.println("<><><>");
				this.legal=false;
				return;
			}
			else
			{
				hash.add(index);
			}
		}
		for(int i=1;i<this.right.size();i++)
		{
			int index=this.right.get(i);
			if(hash.contains(index))
			{
				this.legal=false;
				return;
			}
			else
			{
				hash.add(index);
			}
		}
	}
}
