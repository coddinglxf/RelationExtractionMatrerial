package com.path.filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.dfs.path.DFS_For_Parents;
import com.get.matrix.Matrix;
import com.stanford.parser.Parser_Init;

public class PathFilter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println("is:".split(regex));
	}
	
	boolean contain_more_than_two_nsubj=false;
	public boolean is_a_relation=false;
	ArrayList<Integer>merger=new ArrayList<Integer>();
	ArrayList<Integer>nsubj_list=new ArrayList<Integer>();
	
	public void filter(DFS_For_Parents dfs,Matrix matrix, Parser_Init p)
	{
		ArrayList<Integer>left=dfs.left;
		ArrayList<Integer>right=dfs.right;
		
		int index_left=left.get(left.size()-1);
		int index_right=right.get(right.size()-1);
//		
//		int trigger_sum=0;
//		//
//		for(int i=left.size()-1;i>=0;i--)
//		{
//			int temp=left.get(i);
//			if(index_left<=temp&&temp<=index_right)
//			{
//				this.merger.add(temp);
//				TaggedWord tw=p.taggedword.get(temp-1);
//				if(this.istrigger2(tw.value())&&tw.tag().startsWith("V"))
//				{
//					trigger_sum++;
//				}
//			}
//		}
//		for(int i=1;i<right.size();i++)
//		{
//			int temp=right.get(i);
//			if(index_left<=temp&&temp<=index_right)
//			{
//				this.merger.add(temp);
//			}
//			TaggedWord tw=p.taggedword.get(temp-1);
//			if(this.istrigger2(tw.value())&&tw.tag().startsWith("V"))
//			{
//				trigger_sum++;
//			}
//		}
		
		//.......计算nsubj.................
		for(int i=0;i<left.size();i++)
		{
			int index=left.get(i);
			for(int j=0;j<matrix.matrix.length;j++)
			{
				if(matrix.matrix[index][j]==1)
				{
					int temp=index*100000+j;
					if(matrix.hash_dependency.get(temp).equals("nsubj")||matrix.hash_dependency.get(temp).equals("nsubjpass"))
					{					
						if(!this.nsubj_list.contains(index))
						{
							this.nsubj_list.add(index);
						}					
					}
				}
			}
		}
		for(int i=1;i<right.size();i++)  //
		{

			int index=right.get(i);
			for(int j=0;j<matrix.matrix.length;j++)
			{
				if(matrix.matrix[index][j]==1)
				{
					int temp=index*100000+j;
					if(matrix.hash_dependency.get(temp).equals("nsubj")||matrix.hash_dependency.get(temp).equals("nsubjpass"))
					{
						if(!this.nsubj_list.contains(index))
						{
							this.nsubj_list.add(index);
						}	
					}
				}
			}		
		}
		
		boolean istrigger=this.istrigger2(p.taggedword.get(left.get(0)-1).value()); //鍒ゆ柇鐖惰妭鐐规槸涓嶆槸涓�釜trigger锛岃繖涓緢閲嶈
		
		if(this.nsubj_list.size()>1)
		{
			this.is_a_relation=false;
		}
		else if(this.nsubj_list.size()==1)
		{
			int index=nsubj_list.get(0); 
			if(index==left.get(0)) 
			{
				if(istrigger) 
				{
					if(index_left<index&&index<index_right)
					{
						this.is_a_relation=true;
					}
				}
			}
		}
//		else  
//		{
//			System.out.println();
//			System.out.println("nusbj==0");
//			int index=left.get(0);
//			if(istrigger&&!p.taggedword.get(index-1).tag().startsWith("V"))  //鐖惰妭鐐规槸涓猼rigger锛岃�涓旇繖涓瘝璇笉鏄姩璇嶏紝鑰屾槸灞炰簬鍔ㄨ瘝褰㈠紡
//			{
//				if(index_left<index&&index<index_right)
//				{
//					if(index_right-index_left<=5)
//					{
//						this.is_a_relation=true;
//					}
//				}
//				else
//				{
//					int max=Math.max(Math.abs(index-index_left), Math.abs(index-index_right));
//					if(max<=3)
//					{
//						this.is_a_relation=true;
//					}
//				}
//			}
//			
//			if(istrigger&&p.taggedword.get(index-1).tag().startsWith("V"))
//			{
//				if(index_left<index&&index<index_right)
//				{
//					this.is_a_relation=true;
//				}
//			}
//			//
//			if(trigger_sum==1)
//			{
//				System.out.println("sdf");
//				this.is_a_relation=true;
//			}
//		}

		
		if(Math.abs(index_left-index_right)>=5)
		{
			this.is_a_relation=false;
		}	
//		System.out.println();
//		System.out.println("父节点触法状态......."+istrigger);
//		for(int index:this.nsubj_list)
//		{
//			System.out.println("index_nsubj_nsubjpass............."+index+"\t"+p.taggedword.get(index-1).toString());
//		}
//		System.out.println("using filter and obtain result................................."+this.is_a_relation);
	}
	
//	public boolean istrigger(String word)
//	{
//		word=word.toLowerCase();
//		//System.out.println("word=.........."+word);
//		boolean flags=false;
//		BufferedReader br=null;
//		try 
//		{
//			br=new BufferedReader(new FileReader("verb2.txt"));
//			String line=br.readLine().trim();
//			while(line!=null)
//			{
//				if(word.equals(line))
//				{
//					flags=true;
//					break;
//				}
//				line=br.readLine().trim();
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}finally
//		{
//			try
//			{
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//		}
//		return flags;
//	}
//	
	public boolean istrigger2(String word)
	{
		word=word.toLowerCase();
		//System.out.println("word=.........."+word);
		boolean flags=false;
		BufferedReader br=null;
		try 
		{
			br=new BufferedReader(new FileReader("verb2.txt"));
			String line=br.readLine().trim();
			while(line!=null)
			{
				if(word.startsWith(line)||line.startsWith(word)||line.endsWith(word)||word.endsWith(line))
				{
					flags=true;
					break;
				}
				line=br.readLine().trim();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally
		{
			try
			{
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return flags;
	}
}
