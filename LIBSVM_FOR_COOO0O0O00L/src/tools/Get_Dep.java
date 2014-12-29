package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;

public class Get_Dep {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		BufferedReader br=new BufferedReader(new FileReader("dency.txt"));
		HashSet<String>hash=new HashSet<String>();
		BufferedWriter bw=new BufferedWriter(new FileWriter("depency.txt"));
		String line=br.readLine().trim();
		while(line!=null)
		{
			hash.add(line);
			line=br.readLine();
		}
		Iterator<String>it=hash.iterator();
		while(it.hasNext()){
			bw.write(it.next()+"\r\n");
		}
		bw.close();
		br.close();
	}

}
