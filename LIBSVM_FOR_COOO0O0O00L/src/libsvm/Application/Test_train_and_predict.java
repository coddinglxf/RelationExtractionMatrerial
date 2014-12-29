package libsvm.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import org.dom4j.DocumentHelper;

import LAMBA.Lamba;
import com.xml.solve.Xml_solve;



public class Test_train_and_predict {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception 
	{
		//"iepatrain.txt","llltrain.txt","biotrain.txt","aimtrain.txt"
		String filelist[]={"iepatrain.txt"};
		Test_train_and_predict ttap=new Test_train_and_predict();
		for(double x=0.00001;x<=0.1;x=x*10)
		{
			for(double i=0.5;i<2;i=i+0.25)
			{
				int iterator=1;
				ArrayList<measure>measurelist=new ArrayList<measure>();		
				measurelist.clear();
				while(iterator<=10)
				{
					Lamba.lamba=i;
					Lamba.lamba_for_cosin=x;
					System.out.println("test on lamba= "+Lamba.lamba+"\tlamba_for_cosin="+Lamba.lamba_for_cosin);			
					ttap.generate_train_and_test(9,"trainfile/biotrain.txt");			
					//add other train file to current file
					BufferedReader br=null;
					BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("trainfile/train.txt",true)));
					for(int k=0;k<filelist.length;k++)
					{
						String filename=filelist[k];
						br=new BufferedReader(new FileReader("trainfile/"+filename));
						String line=br.readLine().trim();
						while(line!=null)
						{
							line=line.trim();
							bw.write(line+"\r\n");
							line=br.readLine();
						}
					}
					if(bw!=null)
					{
						bw.close();
					}
					if(br!=null)
					{
						br.close();
					}
					String[] arg = {"-s","0","-h","1","-t","0","trainfile\\train.txt", // 
							"trainfile\\model_r.txt" }; // 

					String[] parg = { "trainfile\\test.txt", // 
							"trainfile\\model_r.txt", // 
							"trainfile\\out.txt" }; // 
					System.out.println("........=========SVM start run========..........");
					
					
					svm_train.main(arg); //
					svm_predict.main(parg); //
					
					measurelist.add(ttap.get_recall());
					double p=0,r=0,f=0;
					for(int it=0;it<measurelist.size();it++)
					{
						measure m=measurelist.get(it);
						p=p+m.pre;
						r=r+m.recall;
						f=f+m.f_measure;
					}
					System.out.println("p.....="+p/measurelist.size());
					System.out.println("r.....="+r/measurelist.size());
					System.out.println("f.....="+f/measurelist.size());
					System.out.println("................................................................................................................."+iterator);
					iterator++;			
				}
				ttap.wirte_res_to_xml("fight_for_freedom_withiepa_consine2//result_lamba_edit="+Lamba.lamba+"cosin="+Lamba.lamba_for_cosin+"without_iepa_.xml", measurelist);
				System.out.println("---------------------=============------------------");
				System.out.println("---------------------=============------------------");
			}
		}
	}
	public measure get_recall()
	{
		measure ret=new measure();
		int t_t_t=0;
		int t_t_f=0;
		int f_t_t=0;
		int f_t_f=0;
		BufferedReader br=null;
		BufferedReader br_out=null;
		try 
		{
			br=new BufferedReader(new FileReader("trainfile/test.txt"));
			br_out=new BufferedReader(new FileReader("trainfile/out.txt"));
			String test=br.readLine().trim();
			String out=br_out.readLine().trim();
			while(test!=null&&out!=null)
			{
				test=test.trim();
				out=out.trim();
				if(test.startsWith("1")&&out.startsWith("1"))
				{
					t_t_t++;
				}
				if(test.startsWith("1")&&out.startsWith("0"))
				{
					t_t_f++;
				}
				if(test.startsWith("0")&&out.startsWith("0"))
				{
					f_t_f++;
				}
				if(test.startsWith("0")&&out.startsWith("1"))
				{
					f_t_t++;
				}
				test=br.readLine();
				out=br_out.readLine();
			}
			System.out.println("f2f="+f_t_f);
			System.out.println("f2t="+f_t_t);
			System.out.println("t2t="+t_t_t);
			System.out.println("t2f="+t_t_f);
			double pre=(double)(t_t_t)/(double)(t_t_t+f_t_t);
			System.out.println("pre="+pre);
			double recall=(double)(t_t_t)/(double)(t_t_t+t_t_f);
			System.out.println("recall="+recall);
			double f_measure=2*pre*recall/(pre+recall);
			System.out.println("f_measure="+f_measure);
			ret.pre=pre;
			ret.recall=recall;
			ret.f_measure=f_measure;
		} catch (Exception e) {
			e.printStackTrace();
		}
		{
			try 
			{
				br_out.close();
				br.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return ret;
	}
	public  void wirte_res_to_xml(String filename,ArrayList<measure>measurelist)
	{
		Xml_solve xml=new Xml_solve();
		org.dom4j.Document document=DocumentHelper.createDocument();
		org.dom4j.Element root=document.addElement("root");
		org.dom4j.Element measures=root.addElement("measures");
		for(int i=0;i<measurelist.size();i++)
		{
			measure m=measurelist.get(i);
			org.dom4j.Element measure=measures.addElement("measure");
			org.dom4j.Element precision=measure.addElement("precision");
			precision.setText(String.valueOf(m.pre));
			org.dom4j.Element recall=measure.addElement("recall");
			recall.setText(String.valueOf(m.recall));
			org.dom4j.Element f_measure=measure.addElement("f_measure");
			f_measure.setText(String.valueOf(m.f_measure));
		}
		xml.formatwrite(filename, document);
	}
	public void generate_train_and_test(int rate,String filename)
	{		
		BufferedReader br=null;
		BufferedWriter bwtest=null,bwtrain=null;
		try
		{
			br=new BufferedReader(new FileReader(filename));
			
			File test=new File("trainfile/test.txt");
			if(test.exists())
			{
				test.delete();
				test=new File("trainfile/test.txt"); 
			}
			bwtest=new BufferedWriter(new FileWriter(test));
			File train=new File("trainfile/train.txt");
			if(train.exists())
			{
				train.delete();
				train=new File("trainfile/train.txt"); 
			}
			bwtrain=new BufferedWriter(new FileWriter(train));
			ArrayList<String>all=new ArrayList<String>();
			String line=br.readLine();
			while(line!=null)
			{
				all.add(line.trim());
				line=br.readLine();
			}			
			int train_num=(int)(((double)rate)/((double)(rate+1))*all.size());
			HashSet<Integer>hash=new HashSet<Integer>();
			while(hash.size()<train_num)
			{
				hash.add((int)(Math.random()*(all.size()-2)));
			}
			
			for(int i=0;i<all.size();i++)
			{
				if(hash.contains(i))//
				{
					bwtrain.write(all.get(i)+"\r\n");
				}
				else                //			
				{
					bwtest.write(all.get(i)+"\r\n");
				}
			}
			
			System.out.println("the size of train is....."+train_num);
			System.out.println("the size of test is....."+(all.size()-train_num));
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try 
			{
				bwtrain.close();
				bwtest.close();
				br.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
class measure
{
	double pre=0;
	double recall=0;
	double f_measure=0;
}

