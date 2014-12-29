package com.stanford.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Parser_Init {

	/**
	 * @param args
	 */
    String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
    String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
    LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
    TreebankLanguagePack tlp = lp.getOp().langpack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	public static void main(String[] args) {
		Parser_Init p=new Parser_Init();
		String s="A tree kernel calculates the similarity between two input trees by counting the number of common sub-structures";

		//p.get_all_parments(s);
		System.out.println("....................");
		List<String>list=new ArrayList<String>();
		String words[] = s.split(" ");
		for (int i = 0; i < words.length; i++) {
			list.add(words[i]);
		}
		
//		list.add("This package ");
//		list.add("is");
//		list.add("a Java implementation");
//		list.add("of");
//		list.add("probabilistic natural language parsers");

		p.get_all_parments(list);
		p.get_path_from_two_node(1, 14, s);
		p.get_path_from_two_node(2, 4, s);
	}
	public Tree tree;
	public ArrayList<TaggedWord>taggedword=new ArrayList<TaggedWord>();
	public List<TypedDependency>dependencies=new ArrayList<TypedDependency>();
	public Collection<TypedDependency>dependencies2=new ArrayList<TypedDependency>();
	public Tree common;
	public int min=Integer.MAX_VALUE;
	public ArrayList<Tree>path=new ArrayList<Tree>();
	public void get_all_parments(List<String>list)
	{
		this.tree=lp.parseStrings(list);
		
		//List<Tree>treelist=tree.getLeaves();
//		
//		for(Tree t:treelist)
//		{
//			System.out.println(t.value()+" [][");
//		}

		
		
		this.taggedword=this.tree.taggedYield();
		
//		for(TaggedWord tw:taggedword)
//		{
//			System.out.println(tw.value()+tw.tag());
//		}
//		
		GrammaticalStructure gs=this.gsf.newGrammaticalStructure(this.tree);
		this.dependencies=gs.typedDependenciesCCprocessed();
		
		
		this.dependencies2=gs.typedDependenciesCollapsed();
//		System.out.println("...............dependencies as follow..............");
//		for(int i=0;i<this.dependencies.size();i++)
//		{
//			TypedDependency td=dependencies.get(i);
//			System.out.println(td.dep()+"\t"+td.gov()+"\t"+td.reln());
//		}
//		System.out.println();
//		for(TypedDependency td:this.dependencies2)
//		{
//			System.out.println(td.dep().value()+"\t"+td.gov().value()+"\t"+td.reln());
//		}
	}
	public void get_all_parments(String s)
	{

		this.tree=lp.parse(s);
		this.taggedword=this.tree.taggedYield();
		
		
		
		for(TaggedWord tw:taggedword)
		{
			System.out.println(tw.value());
		}
		
		GrammaticalStructure gs=this.gsf.newGrammaticalStructure(this.tree);
		this.dependencies=gs.typedDependenciesCCprocessed();
		
		
		this.dependencies2=gs.typedDependenciesCollapsed();
		System.out.println("...............dependencies as follow..............");
		for(int i=0;i<this.dependencies.size();i++)
		{
			TypedDependency td=dependencies.get(i);
			System.out.println(td.dep()+"\t"+td.gov()+"\t"+td.reln());
		}
		System.out.println();
		for(TypedDependency td:this.dependencies2)
		{
			System.out.println(td.dep().value()+"\t"+td.gov().value()+"\t"+td.reln());
		}
	
	}
	public ArrayList<String> get_path_from_two_node(int first,int second,String sentence)
	{
		String words[]=sentence.split("@");
		words[first-1]="PROXT1";
		words[second-1]="PROXT2";
		
		List<String>list=new ArrayList<String>();
		for(int i=0;i<words.length;i++)
		{
			list.add(words[i]);
		}
		Tree root=this.lp.parseStrings(list);
		this.dfs(root);
		
		ArrayList<Tree>temp=new ArrayList<Tree>();
		
		ArrayList<String>pathlist=new ArrayList<String>();
		this.dfs_for_path(common, "PROXT1",temp);
        
		for(int i=this.path.size()-1;i>=0;i--)
		{
			pathlist.add(this.path.get(i).value());
		}
		temp.clear();
		this.path.clear();
		this.dfs_for_path(common, "PROXT2",temp);
		for(int i=1;i<this.path.size();i++)
		{
			pathlist.add(this.path.get(i).value());
		}
		this.min=Integer.MAX_VALUE;
		
		//System.out.println(this.common.value()+"\t"+this.common.depth()+"\t"+pathlist);
		return pathlist;
	}

	private void dfs(Tree root)
	{
		if(istrue(root))
		{
			if(root.depth()<this.min)
			{
				min=root.depth();
				this.common=root;
			}
		}
		Tree child[]=root.children();
		for(int i=0;i<child.length;i++)
		{
			this.dfs(child[i]);
		}
	}
	private boolean istrue(Tree node)
	{
		if(node.depth()==0)
		{
			return false;
		}
		List<Tree>list=node.getLeaves();
		//System.out.println("node value="+node.value()+"\t"+list);
		boolean first=false;;
		boolean second=false;
		for(int i=0;i<list.size();i++)
		{
			Tree temp=list.get(i);
			if(temp.value().equals("PROXT1"))
			{
				first=true;
			}
			if(temp.value().equals("PROXT2"))
			{
				second=true;
			}
		}
		return first&&second;
	}
	
	private void dfs_for_path(Tree common,String value,ArrayList<Tree>temp)
	{
		if(common.value().equals(value))
		{
			temp.add(common);
			//
			path.clear();
			for(int i=0;i<temp.size();i++)
			{
				path.add(temp.get(i));
			}
			temp.remove(temp.size()-1);
			return;
		}
		Tree child[]=common.children();
		for(int i=0;i<child.length;i++)
		{
			temp.add(common);
			this.dfs_for_path(child[i], value,temp);
			temp.remove(temp.size()-1);
		}
	}
}
