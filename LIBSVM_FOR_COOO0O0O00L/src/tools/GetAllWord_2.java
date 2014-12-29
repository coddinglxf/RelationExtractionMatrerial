package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;

public class GetAllWord_2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		HashSet<String>hash=new HashSet<String>();
		BufferedReader br=new BufferedReader(new FileReader("word_all.txt"));
		BufferedReader br_pubmed=new BufferedReader(new FileReader("result.txt"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("word_stav_pubmed.txt"));
		
		String line=br.readLine();
		while(line!=null)
		{
			hash.add(line);
			line=br.readLine();
		}
		line=br_pubmed.readLine();
		while(line!=null)
		{
			String words[]=line.split("\t")[0].split("@");
			for(int i=0;i<words.length;i++)
			{
				hash.add(words[i]);
			}
			line=br_pubmed.readLine();
		}
		Iterator<String>it=hash.iterator();
		while(it.hasNext())
		{
			bw.write(it.next()+"\r\n");
		}
		bw.close();
		br_pubmed.close();
		br.close();
	}
	
}
