package com.pattern.rule.base.relation.extraction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.dfs.path.DFS_For_Parents;
import com.get.matrix.Matrix;
import com.init.string.Init_String;
import com.path.filter.PathFilter;
import com.pattern.match.PattherMatch;
import com.stanford.parser.Parser_Init;
import com.xml.solve.Xml_solve;



public class Rule_Based_Relation
{
	Parser_Init parser=new Parser_Init();
	public static void main(String[] args) throws Exception
	{
		Rule_Based_Relation rule=new Rule_Based_Relation();
		int index=1;
		BufferedReader br=new BufferedReader(new FileReader("trainfile/testflags.txt"));
		String line=br.readLine();
		while(line!=null&&index<=3)
		{
			System.out.println(line);		
			System.out.println(rule.solution(line));
			index++;
			line=br.readLine();
		}
	}
	public boolean solution(String input)
	{
		Relation_Extraction extraction=new Relation_Extraction(input);
		extraction.solution(parser);
		return extraction.relation;
	}
}
class Relation_Extraction
{
	int first=-1;
	int second=-1;
	relation rel=null;
	boolean relation=false;
	public Relation_Extraction(String input)
	{
		this.parserstring(input);
	}
	private void parserstring(String input)
	{
		String words[]=input.split("@@");
		String filename=words[1].trim();
		int hash_pair=Integer.parseInt(words[2].trim());
		this.first=Math.min(hash_pair/100000,hash_pair%100);
		this.second=Math.max(hash_pair/100000,hash_pair%100);
		this.rel=this.solution(filename);
	}	
	@SuppressWarnings("unchecked")
	private relation solution(String filename)
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
		
		Element pairs=root.element("pairs");
		Iterator<Element>pairs_it=pairs.elementIterator();
		HashSet<Integer>hash_pairs=new HashSet<Integer>();
		while(pairs_it.hasNext())
		{
			Element pair=pairs_it.next();
			hash_pairs.add(Integer.parseInt(pair.getText().trim()));
		}
		//add to sentence	
		rel.setGene_tagged_list(gene_tagged_list);
		rel.setHash_pairs(hash_pairs);
		rel.setHash_path(hash_path);
		rel.setSen_context(sentence.getText().trim());
		
		return rel;	
	}

	public void solution(Parser_Init parser)
    {
		String sen_context = rel.getSen_context();
		
		Init_String init_string = new Init_String();
		init_string.from_string_to_list(sen_context);

		parser.get_all_parments(init_string.list);

		Matrix matrix = new Matrix();
		matrix.getmatrix(parser);
		com.pattern.match.PattherMatch pattern = new PattherMatch();
		boolean flags = pattern.all_pattern_together(parser, first, second);
		PathFilter filter = new PathFilter();
		DFS_For_Parents dfs = new DFS_For_Parents();
		if (flags != true)
		{
			for (int k = 0; k < matrix.matrix.length; k++)
			{
				dfs = new DFS_For_Parents();
				dfs.init_and_run(matrix.matrix, first, second, k);
				// System.out.println(dfs.legal);
				if (dfs.legal == true) 
				{
					filter = new PathFilter();
					filter.filter(dfs, matrix, parser);
					if (filter.is_a_relation == true) 
					{
						break;
					}
				}
			}
			flags = filter.is_a_relation;
		}
		
		this.relation=flags;
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
