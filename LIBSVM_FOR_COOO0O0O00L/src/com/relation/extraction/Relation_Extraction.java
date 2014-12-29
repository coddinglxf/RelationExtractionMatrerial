package com.relation.extraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.dfs.path.DFS_For_Parents;
import com.get.matrix.Matrix;
import com.init.string.Init_String;

import com.stanford.parser.Parser_Init;
import com.xml.solve.Xml_solve;



public class Relation_Extraction
{
	static String filename="Bio";
	Parser_Init parser=new Parser_Init();
	HashMap<Integer, Integer>hash=new HashMap<Integer, Integer>();
	HashSet<String>hash_words=new HashSet<String>();
	public static void main(String[] args) throws Exception
	{
		Relation_Extraction extraction=new Relation_Extraction();
		String path="F:\\word_presentation\\stav_xml_new\\";
		File f_list[]=new File(path).listFiles();
		int k=1;
		boolean flags=false;
		for(int i=0;i<f_list.length;i++)
		{
			String filename=f_list[i].getName();
			if(filename.contains("BioInfer_827_1.xml"))
				flags=true;
			if(filename.contains(Relation_Extraction.filename)&&flags)
			{
				System.out.println("index of solve...................................................="+k+"\tfilename="+filename);
				extraction.solution(path+filename);
				k++;
			}	
		}
		
//		BufferedWriter bw=new BufferedWriter(new FileWriter("words_BioInfer.txt"));
//		Iterator<String>it=extraction.hash_words.iterator();
//		while(it.hasNext())
//		{
//			bw.write(it.next()+"\r\n");
//		}
//		bw.close();
	}
	@SuppressWarnings("unchecked")
	public void solution(String filename)
	{
		relation rel=new relation();
		Xml_solve xml=new Xml_solve();
		Document document=xml.read(filename);
		Element root=document.getRootElement();
		Element sentence=root.element("sentence");
		
		//tag 
		Element tags=root.element("tags");
		Iterator<Element>tags_it=tags.elementIterator();
		ArrayList<Integer> gene_tagged_list=new ArrayList<Integer>();
		while(tags_it.hasNext())
		{
			Element tag=tags_it.next();
			gene_tagged_list.add(Integer.parseInt(tag.attributeValue("pos")));
		}
		
		//Relations path 
		Element relations=root.element("relations");
		Iterator<Element>rel_it=relations.elementIterator();
		HashMap<Integer, ArrayList<Integer>>hash_path=new HashMap<Integer, ArrayList<Integer>>();
		while(rel_it.hasNext())
		{
			Element relation=rel_it.next();
			int key=Integer.parseInt(relation.attributeValue("position"));
			ArrayList<Integer>path_list=new ArrayList<Integer>();
			if(!relation.getText().equals("")) //
			{
				String path[]=relation.getText().trim().split(" ");			
				for(int i=0;i<path.length;i++)
				{
					path_list.add(Integer.parseInt(path[i]));
				}
				hash_path.put(key,path_list);
			}
		}
		//
		Element pairs=root.element("pairs");
		Iterator<Element>pairs_it=pairs.elementIterator();
		HashSet<Integer>hash_pairs=new HashSet<Integer>();
		while(pairs_it.hasNext())
		{
			Element pair=pairs_it.next();
			hash_pairs.add(Integer.parseInt(pair.getText().trim()));
			int pair_num=Integer.parseInt(pair.getText().trim());
			int length=Math.abs(pair_num/100000-pair_num%100);
			if(this.hash.containsKey(length))
			{
				this.hash.put(length, this.hash.get(length)+1);
			}
			else
			{
				this.hash.put(length, 1);
			}
		}
		//add to sentence	
		rel.setGene_tagged_list(gene_tagged_list);
		rel.setHash_pairs(hash_pairs);
		rel.setHash_path(hash_path);
		rel.setSen_context(sentence.getText().trim());
		
		Tools tools=new Tools();
		tools.solution(rel,parser,filename);
		String words[]=rel.getSen_context().split("@");
		for(int i=0;i<words.length;i++)
		{
			String word=words[i];
			if(rel.getGene_tagged_list().contains(i+1))//
			{
				hash_words.add("PROTX0");
			}
			else
			{
				hash_words.add(word);
			}
		}	
	}
}
class Tools
{
	public void solution(relation rel,Parser_Init parser,String filename)
	{
		String sen_context=rel.getSen_context();
		
		ArrayList<Integer> gene_tagged_list=rel.getGene_tagged_list();
		
		HashSet<Integer>hash_pairs=rel.getHash_pairs();
		System.out.println(hash_pairs);
		//init 
		//
		Init_String init_string = new Init_String();
		init_string.from_string_to_list(sen_context);

		parser.get_all_parments(init_string.list);

		Matrix matrix = new Matrix();
		matrix.getmatrix(parser);
		
		Iterator<Integer>it=matrix.hash_dependency.keySet().iterator();
		while(it.hasNext())
		{
			Integer key=it.next();
			this.write(matrix.hash_dependency.get(key),"dependency.txt");
		}
		gene_tagged_list.add(24);
		for(int i=0;i<gene_tagged_list.size();i++)
		{
			for(int j=i+1;j<gene_tagged_list.size();j++)
			{
				int first=Math.min(gene_tagged_list.get(i), gene_tagged_list.get(j));
				int second=Math.max(gene_tagged_list.get(i), gene_tagged_list.get(j));	
				

				DFS_For_Parents dfs=new DFS_For_Parents();
				ArrayList<Integer>shortest=new ArrayList<Integer>();
				int min=Integer.MAX_VALUE;
				for (int k = 0; k < matrix.matrix.length; k++)
				{
						// System.out.println(matrix.matrix[34][30]+" "+matrix.matrix[34][32]);
					    dfs = new DFS_For_Parents();
						dfs.init_and_run(matrix.matrix,first, second, k);					
						// System.out.println(dfs.legal);
						if (dfs.legal == true)
						{
							System.out.println("######################" + k);
							if((dfs.left.size()+dfs.right.size()-1)<min)
							{
								shortest.clear();
								min=dfs.left.size()+dfs.right.size()-1; //min鏀逛负杩欎釜鏁板�
								for(int x=dfs.left.size()-1;x>=0;x--)
								{
									shortest.add(dfs.left.get(x));
								}
								for(int y=1;y<dfs.right.size();y++)
								{
									shortest.add(dfs.right.get(y));
								}
							}
						}
				}
				if(shortest.size()!=0)
				{
					String line="PROTX1@";
					for(int index=1;index<shortest.size()-1;index++)
					{
						int s=shortest.get(index);
						int f=shortest.get(index-1);
						int hashvalue_1=s*100000+f;
						int hashvalue_2=f*100000+s;
						String dep="";
						if(matrix.hash_dependency.containsKey(hashvalue_1))
						{
							dep=matrix.hash_dependency.get(hashvalue_1);
						}
						else
						{
							dep=matrix.hash_dependency.get(hashvalue_2);
						}
						if(gene_tagged_list.contains(s))
						{
							line=line+dep+"@PROTX0@";
						}
						else
						{
							line=line+dep+"@"+parser.taggedword.get(s-1).value()+"@";
						}
					}
					//add end
					int tempvalue=shortest.get(shortest.size()-2)*100000+shortest.get(shortest.size()-1);
					line=line+matrix.hash_dependency.get(tempvalue)+"@PROTX2";
					
					int key=first*100000+second;
					
					ArrayList<String>pathlist=parser.get_path_from_two_node(first, second, sen_context);
					String path_of_s="@";
					for(int xx=0;xx<pathlist.size();xx++)
					{
						path_of_s=path_of_s+pathlist.get(xx)+"@";
					}
					if(hash_pairs.contains(key))   //pos
					{
						line="true"+"@"+line+path_of_s+"@@"+filename+"@@"+key;
						this.write(line,Relation_Extraction.filename+"TRUE.txt");
					}else                          //neg
					{
						line="false"+"@"+line+path_of_s+"@@"+filename+"@@"+key;
						this.write(line, Relation_Extraction.filename+"FALSE.txt");
					}
					this.write(line,Relation_Extraction.filename+ "ALL.txt");
				}
			//System.out.println("-------------------鎴戞槸瀹岀編鐨勫垎鍓茬嚎--------------------\n"+shortest+"\n");
			}
		}
		//System.out.println("t_to_t="+this.t_to_t+"\tt_to_f="+this.t_to_f+"\ttrue_all="+this.true_all+"\tfalse_all="+this.false_all);
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
 class relation
{
	public String getSen_context() {
		return sen_context;
	}
	public void setSen_context(String sen_context) {
		this.sen_context = sen_context;
	}
	public ArrayList<Integer> getGene_tagged_list() {
		return gene_tagged_list;
	}
	public void setGene_tagged_list(ArrayList<Integer> gene_tagged_list) {
		this.gene_tagged_list = gene_tagged_list;
	}
	public HashMap<Integer, ArrayList<Integer>> getHash_path() {
		return hash_path;
	}
	public void setHash_path(HashMap<Integer, ArrayList<Integer>> hash_path) {
		this.hash_path = hash_path;
	}
	public HashSet<Integer> getHash_pairs() {
		return hash_pairs;
	}
	public void setHash_pairs(HashSet<Integer> hash_pairs) {
		this.hash_pairs = hash_pairs;
	}
	private String sen_context="";
	private ArrayList<Integer> gene_tagged_list=new ArrayList<Integer>();
	//
	private HashMap<Integer, ArrayList<Integer>>hash_path=new HashMap<Integer, ArrayList<Integer>>();
	//
	private HashSet<Integer>hash_pairs=new HashSet<Integer>();
}

/*
 * 						this.write(parser.taggedword.get(dfs.left.get(0)-1).value(),"trigger.txt");
						if(filter.is_a_relation==true)
						{		
							int key=first*100000+second;
							if(hash_pairs.contains(key))
							{
								this.t_to_t++;
								this.write(filename+"\t"+(second-first), "index.txt");
							}
							else
							{
								this.f_to_t++;
								this.write("", "log.txt");
								this.write(rel.getSen_context(),"log.txt");
								this.write(filename+"\t"+k+"\t"+dfs.left+"----\t---"+dfs.right,"log.txt");
							}
							break;
						}
						else
						{
							int key=first*100000+second;
							if(hash_pairs.contains(key))
							{
								this.write(filename+"\t"+dfs.left+"\t"+dfs.right+"\t","true no find.txt");
							}
						}
 * */
