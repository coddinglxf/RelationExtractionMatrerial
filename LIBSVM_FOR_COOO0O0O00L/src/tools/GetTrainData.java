package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class GetTrainData {
	public static void main(String[] args)
	{
		String filename="IEPA";
		String lowcasename=filename.toLowerCase();
		GetTrainData gtd=new GetTrainData();
		//gtd.genaratedata("AIMALL.txt","trainfile/aimtrain.txt");
		gtd.genaratedata(filename+"ALL.txt","trainfile/"+lowcasename+"train.txt","trainfile/"+lowcasename+"trainflags.txt");
		//gtd.genaratedata("IEPAALL.txt","trainfile/iepatrain.txt");
		//gtd.genaratedata("LLLALL.txt","trainfile/llltrain.txt");
	}
	HashMap<String, Integer>hash_word=new HashMap<String, Integer>();
	public void genaratedata(String filename,String desiantion,String flags_name)
	{
		this.init_hash_word();
		BufferedReader br=null;
		BufferedWriter bw=null;
		try 
		{
			bw=new BufferedWriter(new FileWriter(flags_name));
			br=new BufferedReader(new FileReader(filename));
			String line=br.readLine();
			while(line!=null)
			{
				String ttt[]=line.split("@@");				
				bw.write(line+"\r\n"); //–¥»Î±Í÷æ
				
				line="";
				for(int i=0;i<ttt.length-2;i++)
				{
					line=line+ttt[i]+"@";
				}
				
				String words[]=line.split("@");
				String temp="";
				if(words[0].equals("true"))
				{
					temp=temp+"1"+" ";
				}
				else
				{
					temp=temp+"0"+" ";
				}
				for(int i=1;i<words.length;i++)
				{
					String word=words[i];
					if(!this.hash_word.containsKey(word))
					{
						System.out.println(word);
					}
					int index=this.hash_word.get(word);				
					temp=temp+i+":"+index+" ";
				}
				this.write(temp, desiantion);
				line=br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try 
			{
				br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void init_hash_word()
	{
		BufferedReader br=null;
		try 
		{
			br=new BufferedReader(new FileReader("word_stav_pubmed.txt"));
			int index=1;
			String line=br.readLine();
			while(line!=null)
			{
				hash_word.put(line, index);
				index++;
				line=br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try 
			{
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void write(String line,String filename)
	{
		BufferedWriter bw=null;
		try 
		{
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename,true)));
			bw.write(line+"\r\n");
		} catch (Exception e) {
			// TODO: handle exception
		}finally
		{
			try
			{
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
