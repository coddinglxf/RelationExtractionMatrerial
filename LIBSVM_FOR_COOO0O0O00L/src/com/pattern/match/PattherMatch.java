package com.pattern.match;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.stanford.parser.Parser_Init;

import edu.stanford.nlp.ling.TaggedWord;

public class PattherMatch {
	public static void main(String[] args)
	{
		
	}
	//暂时去掉pattern3，pattern可以通过动词作用找到
	public boolean  all_pattern_together(Parser_Init parser,int first,int second)
	{
		return this.pattern_1(parser, first, second)||this.pattern_2(parser, first, second)
				||this.pattern_4(parser, first, second);
	}
	//pattern 1: the interactions between A and B
	private boolean pattern_1(Parser_Init parser,int first,int second)
	{
		boolean ret=false;
		if(Math.abs(first-second)<=4)
		{		
			ArrayList<TaggedWord>taggedlist=parser.taggedword;
			first=first-1;  
			second=second-1;
			//
			int sum=0;
			boolean left_flags=false;
			int prep_sum=0;
			for(int index=first-1;index>=0;index--)
			{
				TaggedWord tw=taggedlist.get(index);
				if("DTINTO".contains(tw.tag()))
				{
					prep_sum++;
					continue;
				}
				else
				{
					if(!istrigger2(tw.value()))
					{
						sum++;
					}
					else
					{
						if(!tw.tag().startsWith("V"))
						{
							break;
						}
						else 
						{
							return false;
						}
					}
				}
			}
			if(sum<=1) 
			{
				left_flags=true&&prep_sum==1;
			}
			int sum_mid=0;
			boolean mid_flags=false;
			
			for(int start=first+1;start<=second-1;start++)
			{
				TaggedWord tw=taggedlist.get(start);
				if("DTINTO".contains(tw.tag())) 
				{
					
					continue;
				}
				else
				{
					char start_char=tw.value().toLowerCase().charAt(0);
					if(97<=start_char&&start_char<=122)
					{
						sum_mid++;
					}
					else
					{
						continue;
					}
				}
			}
			
			if(sum_mid<=0)
			{
				mid_flags=true;
			}
			ret=left_flags&&mid_flags;
		}	
		return ret;
	}
	//pattern 2: a -dependement b
	private boolean pattern_2(Parser_Init parser,int first,int second)
	{
		boolean ret=false;
		
		if(Math.abs(first-second)<=3)
		{
			ArrayList<TaggedWord>taggedlist=parser.taggedword;
			first=first-1;  
			second=second-1;
			int index=first+1;
			TaggedWord tw=taggedlist.get(index);
			if(tw.value().startsWith("-"))
			{
				if(istrigger(tw.value()))
				{
					ret=true;
				}
			}
		}
		
		return ret;
	}	
	//pattern 3: A verb B 
	private boolean pattern_3(Parser_Init parser,int first,int second)
	{

		boolean ret=false;
		
		if(Math.abs(first-second)<=3)
		{
			ArrayList<TaggedWord>taggedlist=parser.taggedword;
			first=first-1;  
			second=second-1;		
			for(int index=first+1;index<second;index++)
			{
				TaggedWord tw=taggedlist.get(index);
				if(tw.tag().startsWith("V")&&(istrigger(tw.value().trim())))
				{
					return true;
				}
			}
		}
		
		return ret;
	
	}
	//pattern 4: 
	private boolean pattern_4(Parser_Init parser,int first,int second)
	{

		boolean ret=false;
		if(Math.abs(first-second)<=2)
		{		
			ArrayList<TaggedWord>taggedlist=parser.taggedword;
			first=first-1;  
			second=second-1;
			int index=second+1;
			TaggedWord tw=taggedlist.get(index);
			if(!tw.tag().startsWith("V")&&istrigger(tw.value().trim()))
			{
				return true;
			}
		}	
		return ret;
	
	}
	
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
	public boolean istrigger(String word)
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
				if(word.equals(line))
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
