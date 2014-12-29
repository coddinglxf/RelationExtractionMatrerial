package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Xml_solve {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Xml_solve xml=new Xml_solve();
		Document document=xml.read("my.xml");
		xml.formatwrite("tt.xml", document);
		
	}

	public Document read(String fileName)
	{
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(new File(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public Element getRootElement(Document document) {
		return document.getRootElement();
	}


	public Document createDocument() {

		Document document = DocumentHelper.createDocument();

		Element root = document.addElement("root");
		root.addAttribute("id", "1");
		
		Element root2=root.addElement("root2");
		Element root2_ele1=root2.addElement("root2_ele2");
		root2_ele1.setText("hua");
		root2_ele1.addAttribute("author", "hua");

		Element author1 = root.addElement("author");

		author1.addAttribute("name", "James");

		author1.addAttribute("location", "UK");

		author1.addText("James Strachan");

		Element author2 = root.addElement("author");

		author2.addAttribute("name", "Bob");

		author2.addAttribute("location", " US");

		author2.addText("Bob McWhirter");

		this.formatwrite("d://foo.xml", document);
		return document;

	}
	public void write(String path,Document document)
	{
		FileWriter out=null;
		try
		{
		    out=new FileWriter(path);
			document.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void formatwrite(String path,Document document)
	{
		XMLWriter writer=null;
		try 
		{    
			OutputFormat format=OutputFormat.createPrettyPrint();
			format.setIndentSize(3);
			
			//format=OutputFormat.createCompactFormat();
			writer=new XMLWriter(new FileOutputStream(new File(path)),format);
			
			writer.write(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
