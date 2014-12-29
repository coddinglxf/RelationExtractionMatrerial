package FmeasureCalculate;


import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.xml.solve.Xml_solve;

public class Calculate {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String tempname="";
		double f_max=Double.MIN_VALUE;
		Calculate cal=new Calculate();
		String path="fight_for_freedom_withiepa_consine2//";
		File f[]=new File(path).listFiles();
		for(int i=0;i<f.length;i++)
		{
			String filename=f[i].getName();
			measure temp=cal.calculate(path+filename);
			System.out.println(filename);
			System.out.println(temp.pre);
			System.out.println(temp.recall);
			System.out.println(temp.f_measure);
			System.out.println("....................................");
			if(temp.f_measure>f_max)
			{
				tempname=path+filename;
				f_max=temp.f_measure;
			}
		}
		System.out.println(tempname);
		System.out.println(f_max);
	}
	@SuppressWarnings("unchecked")
	public measure calculate(String filenmae)
	{
		measure m=new measure();
		double p=0;
		double r=0;
		double f=0;
		Xml_solve xml=new Xml_solve();
		Document document=xml.read(filenmae);
		Element root=xml.getRootElement(document);
		Element measures=root.element("measures");
		Iterator<Element>it=measures.elementIterator();
		int index=0;
		while(it.hasNext())
		{
			index++;
			Element measure=it.next();
			Element precision=measure.element("precision");
			Element recall=measure.element("recall");
			Element f_measure=measure.element("f_measure");
			p=p+Double.valueOf(precision.getText());
			r=r+Double.valueOf(recall.getText());
			f=f+Double.valueOf(f_measure.getText());
		}
		m.pre=p/index;
		m.recall=r/index;
		m.f_measure=f/index;
		return m;
	}
}
class measure
{
	double pre=0;
	double recall=0;
	double f_measure=0;

}
