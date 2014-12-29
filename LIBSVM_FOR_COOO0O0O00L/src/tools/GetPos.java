package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;


public class GetPos {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception
	{
		String filename="IEPA";
		BufferedReader br=new BufferedReader(new FileReader(filename+"ALL.txt"));
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("pos.txt",true)));
		HashSet<String>pos_hash=new HashSet<String>();
		String line=br.readLine();
		while(line!=null)
		{
			System.out.println(line);
			line=line.split("@@")[0];
			
			String words[]=line.split("@");
			int index=0;
			while(!words[index].equals("PROTX2"))
			{
				index++;
			}
			index++;
			System.out.println("index="+index);
			for(int i=index;i<words.length;i++)
			{
				String temp=words[i];
				bw.write(temp+"\r\n");		
			}
		    line=br.readLine();
		}
		bw.close();
		br.close();
		
		BufferedReader br_pos=new BufferedReader(new FileReader("pos.txt"));
		BufferedWriter bw_pos=new BufferedWriter(new FileWriter("pos_tidy.txt"));
		String p=br_pos.readLine();
		while(p!=null)
		{
			pos_hash.add(p);
			p=br_pos.readLine();
		}
		Iterator<String>it=pos_hash.iterator();
		while(it.hasNext())
		{
			bw_pos.write(it.next()+"\r\n");
		}
		bw_pos.close();
		br_pos.close();
	}

}
