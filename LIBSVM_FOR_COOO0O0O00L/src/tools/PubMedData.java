package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class PubMedData {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception
	{
		PubMedData pubmed=new PubMedData();
		pubmed.init_hash_word();
		BufferedReader br=new BufferedReader(new FileReader("result.txt"));
		String line=br.readLine();
		ArrayList<String>list=new ArrayList<String>();
		while(line!=null)
		{
			list.add(line.split("\t")[0]);
			line=br.readLine();
		}
		//≤‚ ‘“ª≤ø∑÷
		int num=1000;
		HashSet<Integer>hash=new HashSet<Integer>();
		while(hash.size()<num)
		{
			int temp=(int)(Math.random()*(list.size()-2));
			hash.add(temp);
		}
		Iterator<Integer>it=hash.iterator();
		while(it.hasNext())
		{
			String linetemp=list.get(it.next());
			pubmed.genaratedata("trainfile//pubmed.txt", linetemp);
		}
	}
	HashMap<String, Integer>hash_word=new HashMap<String, Integer>();
	public void genaratedata(String filename,String line)
	{
		String words[]=line.split("@");
		String res="1 ";
		for(int i=0;i<words.length;i++)
		{
			int index=this.hash_word.get(words[i]);
			res=res+String.valueOf(i+1)+":"+index+" ";
		}
		this.write(res.trim(), filename);
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
