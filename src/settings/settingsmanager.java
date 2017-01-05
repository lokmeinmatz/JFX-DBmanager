package settings;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class settingsmanager {

	private File settingsfile;
	private Document doc;

	private List<String> savelocations, categories, serien;

	public void loadSettingsData(){
		try {
			settingsfile = new File("res/settings.xml");
			DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(settingsfile);
			Element root = doc.getDocumentElement();
			savelocations = new ArrayList<>();
			categories = new ArrayList<>();
			serien = new ArrayList<>();
			print(root.getNodeName());
			NodeList nList = doc.getElementsByTagName("s");
			 
	        for (int temp = 0; temp < nList.getLength(); temp++) {
	         	Node nNode = nList.item(temp);
	         	
	         	if(nNode.getParentNode().getNodeName().equals("savelocations")){
	         		savelocations.add(nNode.getTextContent());
	         	}
	         	else if(nNode.getParentNode().getNodeName().equals("categories")){
	         		categories.add(nNode.getTextContent());
	         	}
	         	else if(nNode.getParentNode().getNodeName().equals("serien")){
	         		serien.add(nNode.getTextContent());
	         	}
	         }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void print(String s){
		System.out.println(s);
	}
	
	public List<String> getSaveLocations(){
		return savelocations;
	}
	public List<String> getCategorys(){
		return categories;
	}
	public List<String> getSeries(){
		return serien;
	}
	
	public void addSavelocation(String loc){
		Element root = doc.getDocumentElement();
		NodeList allnodes = root.getChildNodes();
		for(int i = 0; i < allnodes.getLength(); i++){
			Node n = allnodes.item(i);
			
			if(n.getNodeName().equals("savelocations")){
				print(n.getNodeName());
				Element s = doc.createElement("s");
				s.appendChild(doc.createTextNode(loc));
				n.appendChild(s);
			}
		}
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(settingsfile);
	         transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadSettingsData();
	}
	
	public void addSerie(String serie){
		Element root = doc.getDocumentElement();
		NodeList allnodes = root.getChildNodes();
		for(int i = 0; i < allnodes.getLength(); i++){
			Node n = allnodes.item(i);
			
			if(n.getNodeName().equals("serien")){
				print(n.getNodeName());
				Element s = doc.createElement("s");
				s.appendChild(doc.createTextNode(serie));
				n.appendChild(s);
			}
		}
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(settingsfile);
	         transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadSettingsData();
	}
	
	public void addCategory(String cat){
		Element root = doc.getDocumentElement();
		NodeList allnodes = root.getChildNodes();
		for(int i = 0; i < allnodes.getLength(); i++){
			Node n = allnodes.item(i);
			
			if(n.getNodeName().equals("categories")){
				print(n.getNodeName());
				Element s = doc.createElement("s");
				s.appendChild(doc.createTextNode(cat));
				n.appendChild(s);
			}
		}
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(settingsfile);
	         transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadSettingsData();
	}
}
