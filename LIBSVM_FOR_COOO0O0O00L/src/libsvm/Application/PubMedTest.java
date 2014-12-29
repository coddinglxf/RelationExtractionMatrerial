package libsvm.Application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;



public class PubMedTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception 
	{
		//首先还是需要利用bio数据，训练好模型
		//System.out.println("===svm train=====");
//		String[] arg = {"-s","0","-h","1","-t","0","trainfile\\biotrain.txt", // 
//		"trainfile\\model_r.txt" }; // 
//		svm_train.main(arg); //
//		System.out.println("===svm train===stop");
		
		String[] parg = { "trainfile\\pubmed.txt", //
				"trainfile\\model_r.txt", //
				"trainfile\\out.txt" }; //
		
		solution sol=new solution();
		BufferedReader br=new BufferedReader(new FileReader("result.txt"));
		String line=br.readLine();
		int index=1;
		while(line!=null)
		{
			System.out.println("index=\t"+index);
			BufferedWriter bw_test=new BufferedWriter(new FileWriter("trainfile//pubmed.txt"));
			BufferedReader br_out=new BufferedReader(new FileReader("trainfile//out.txt"));
			BufferedWriter bw_log=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("trainfile\\log.txt",true)));
			
			String result=sol.genaratedata(line.split("\t")[0].trim()).trim();

			//write one test file
			bw_test.write(result);
			//close current file
			bw_test.close();
			
			//predict this file
			svm_predict.main(parg); 
			//read out put file
			String res=br_out.readLine();
			//write log file
			if(res.startsWith("1"))
			{
				bw_log.write(index+":"+line+"\r\n");
			}
			
			bw_log.close();
			br_out.close();		
			index++;
			line=br.readLine();
		}
	 }

}
class solution
{
	String[] parg = { "trainfile\\pubmed.txt", //
			"trainfile\\model_r.txt", //
			"trainfile\\out.txt" }; //
	public solution()
	{
		this.init_hash_word();
	}
	public void predict(ArrayList<String>listtest)
	{
		BufferedWriter bw=null;
		BufferedWriter bw_testfile=null;
		BufferedReader br=null;
		BufferedWriter bw_log=null;
		try 
		{
			bw=new BufferedWriter(new FileWriter("trainfile\\pubmed.txt"));
			bw_testfile=new BufferedWriter(new FileWriter("trainfile\\pubmed_test.txt"));
			br=new BufferedReader(new FileReader("trainfile\\out.txt"));
			bw_log=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("trainfile\\log.txt",true)));//追加文件
			for(int i=0;i<listtest.size();i++)
			{
				String temp=listtest.get(i).split("\t")[0].trim();
				bw_testfile.write(listtest.get(i)+"\r\n");
				bw.write(this.genaratedata(temp).trim()+"\r\n");
			}
			System.out.println("===start...predict....==="+listtest.size());
			svm_predict.main(parg); //
			System.out.println("===end.....predict....===");
			System.out.println("======start...write....log......file======");
			String line=br.readLine();
			int index=0;
			while(line!=null)
			{
				if(line.startsWith("1"))
				{
					bw_log.write(index+"\t"+listtest.get(index)+"\r\n");
				}
				index++;
				line=br.readLine();
			}
			System.out.println("======end.....write....log......file======");
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try
			{
				bw_log.close();
				br.close();
				bw_testfile.close();
				bw.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	HashMap<String, Integer>hash_word=new HashMap<String, Integer>();
	public String genaratedata(String line)
	{
		String words[]=line.split("@");
		String res="1 ";
		for(int i=0;i<words.length;i++)
		{
			if(!this.hash_word.containsKey(words[i]))
			{
				System.out.println(words[i]);
			}
			int index=this.hash_word.get(words[i]);
			res=res+String.valueOf(i+1)+":"+index+" ";
		}
		return res;
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