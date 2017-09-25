package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.handlers.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXXMLHandler extends DefaultHandler {

	private StringBuffer elementBuffer = new StringBuffer();
	private HashMap<String, String> attributeMap;
	private Document doc;
	
	public SAXXMLHandler(){
		super();
	}
	
	public Document getDocument(InputStream is) {
		try{
			XMLReader xmlReader=XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.parse(new InputSource(is));
		}catch(SAXException se){
			System.out.println("Problem pri parsiranju XML-a");
		}catch(IOException ioe){
			System.out.println("Nemoguce je otvoriti fajl");
		}
		return doc;
	}
	
	//bice pozvana kad pocne parsing
	public void startDocument(){
		System.out.println("Start parsinga");
		doc=new Document();
	}
	
	//bice pozvana svaki put kad se pristupi novom XML elementu
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{
		System.out.println("Start elementa: "+qName);
		elementBuffer.setLength(0);
		if(attributeMap!=null){
			attributeMap.clear();
		}
		if(atts.getLength()>0){
			if(attributeMap==null)
				attributeMap=new HashMap<String,String>();
			for(int i=0;i<atts.getLength();i++){
				attributeMap.put(atts.getQName(i), atts.getValue(i));
			}
		}
	}
	
	//kada se stigne do cdata delova
	public void characters(char[] ch, int start, int length) throws SAXException {
		elementBuffer.append(ch,start, length);
	}
	
	//kada se dostigne kraj elementa
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if(qName.equals("bookstore")){
			return;
		}else if(qName.equals("book") ){
			Set<String> keySet=attributeMap.keySet();
			for(String key : keySet){
				String value=attributeMap.get(key);
				doc.add(new StringField(key, value, Store.YES));
			}
		}else if(qName.equals("title")){
			Set<String> keySet=attributeMap.keySet();
			for(String key : keySet){
				String value=attributeMap.get(key);
				doc.add(new StringField(key, value, Store.YES));
			}
			doc.add(new TextField(qName,elementBuffer.toString(), Store.YES));
		}else{
			doc.add(new TextField(qName,elementBuffer.toString(), Store.NO));
		}
		
	}
}
