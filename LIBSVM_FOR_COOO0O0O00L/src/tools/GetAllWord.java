package tools;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.Element;
import com.xml.solve.Xml_solve;



public class GetAllWord
{
	HashMap<Integer, Integer>hash=new HashMap<Integer, Integer>();
	HashSet<String>hash_words=new HashSet<String>();
	public static void main(String[] args) throws Exception
	{
		BufferedWriter bw_true=new BufferedWriter(new FileWriter("pos.txt"));
		BufferedWriter bw_false=new BufferedWriter(new FileWriter("neg.txt"));
		GetAllWord extraction=new GetAllWord();
		String path="F:\\word_presentation\\stav_xml_new\\";
		File f_list[]=new File(path).listFiles();
		int k=1;
		for(int i=0;i<f_list.length;i++)
		{
			String filename=f_list[i].getName();
		    System.out.println("index of solve...................................................="+k+"\tfilename="+filename);
			relation rel=extraction.solution(path+filename);
			if(rel.getHash_pairs().size()>0)
			{
				bw_true.write(rel.getSen_context()+"\r\n");
			}
			else
			{
				bw_false.write(rel.getSen_context()+"\r\n");
			}
		    k++;
		}	
		BufferedWriter bw=new BufferedWriter(new FileWriter("word_aim.txt"));
		Iterator<String>it=extraction.hash_words.iterator();
		while(it.hasNext())
		{
			bw.write(it.next()+"\r\n");
		}
		bw.close();
		bw_false.close();
		bw_true.close();
	}
		

	@SuppressWarnings("unchecked")
	public relation solution(String filename)
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
			if(!relation.getText().equals("")) 
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
		
		String words[]=rel.getSen_context().split("@");
		String temp="";
		if(filename.contains("AIMed"))
		{
			for(int i=0;i<words.length;i++)
			{
				String word=words[i];
				if(rel.getGene_tagged_list().contains(i+1))//
				{
					hash_words.add("PROTX0");
					temp=temp+"PROTX"+"@";
				}
				else
				{
					hash_words.add(word);
					temp=temp+word+"@";
				}
			}
			rel.setSen_context(temp);
		}
		return rel;
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
	//integer浠ｈ〃hash鍊�
	private HashMap<Integer, ArrayList<Integer>>hash_path=new HashMap<Integer, ArrayList<Integer>>();
	//鐪熸鐨勫叧绯�
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
