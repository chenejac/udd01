package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.search;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.analysers.SerbianAnalyzer;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.handlers.DocumentHandler;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.handlers.PDFHandler;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.handlers.TextDocHandler;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.handlers.Word2007Handler;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.handlers.WordHandler;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model.RequiredHighlight;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model.ResultData;


public class ResultRetriever {
	
	private TopScoreDocCollector collector;
	private static int maxHits = 10;
	
	public ResultRetriever(){
		collector=TopScoreDocCollector.create(10);
	}
	
	public static void setMaxHits(int maxHits) {
		ResultRetriever.maxHits = maxHits;
	}

	public static int getMaxHits() {
		return ResultRetriever.maxHits;
	}

	public static List<ResultData> getResults(Query query,
			List<RequiredHighlight> requiredHighlights) {
		if (query == null) {
			return null;
		}
		try {
			Directory indexDir = new SimpleFSDirectory(FileSystems.getDefault().getPath(ResourceBundle
					.getBundle("application").getString("index")));
			DirectoryReader reader = DirectoryReader.open(indexDir);
			IndexSearcher is = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					maxHits);

			List<ResultData> results = new ArrayList<ResultData>();

			is.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			ResultData rd;
			Document doc;
			Highlighter hl;
			SerbianAnalyzer sa = new SerbianAnalyzer();
			
			for (ScoreDoc sd : hits) {
				doc = is.doc(sd.doc);
				String[] allKeywords = doc.getValues("keyword");
				String keywords = "";
				for (String keyword : allKeywords) {
					keywords += keyword.trim() + " ";
				}
				keywords = keywords.trim();
				String title = doc.get("title");
				String location = doc.get("filename");
				String highlight = "";
				for (RequiredHighlight rh : requiredHighlights) {
					hl = new Highlighter(new QueryScorer(query, reader, rh.getFieldName()));
					try{
						highlight += hl.getBestFragment(sa, rh.getFieldName(),
								"" + getDocumentText(location));
					}catch (InvalidTokenOffsetsException e) {
						//throw new IllegalArgumentException("Unable to make highlight");
					}
				}
				rd = new ResultData(title, keywords, location,
						highlight);
				results.add(rd);
			}
			reader.close();
			return results;

		} catch (IOException e) {
			throw new IllegalArgumentException(
					"U prosledjenom direktorijumu ne postoje indeksi ili je direktorijum zakljucan");
		} 
	}
	
	public String printSearchResults(Query query, File indexDir){
		StringBuilder retVal = new StringBuilder();
		try{
			Directory fsDir=new SimpleFSDirectory(FileSystems.getDefault().getPath(indexDir.getAbsolutePath()));
			DirectoryReader ireader = DirectoryReader.open(fsDir);
			IndexSearcher is = new IndexSearcher(ireader);
			is.search(query, collector);
			
			ScoreDoc[] hits=collector.topDocs().scoreDocs;
			System.err.println("Found " + hits.length + " document(s) that matched query '" + query + "':");
			for (int i = 0; i < collector.getTotalHits(); i++) {
				int docId=hits[i].doc;
				Document doc = is.doc(docId);
				retVal.append("\t"+doc.get("title")+" ("+doc.get("filedate")+")\n");
				retVal.append("\t"+doc.get("filename")+"\n\n");
			}
		}catch(IOException ioe){
			retVal.append(ioe.getMessage() + "\n");
		}
		return retVal.toString();
	}
	
	public String printHTMLSearchResults(Query query, File indexDir){
		StringBuilder retVal = new StringBuilder();
		retVal.append("<html><body>");
		try{
			Directory fsDir=new SimpleFSDirectory(FileSystems.getDefault().getPath(indexDir.getAbsolutePath()));
			DirectoryReader ireader = DirectoryReader.open(fsDir);
			IndexSearcher is = new IndexSearcher(ireader);
			is.search(query, collector);
			
			ScoreDoc[] hits=collector.topDocs().scoreDocs;
			System.err.println("Found " + hits.length + " document(s) that matched query '" + query + "':");
			for (int i = 0; i < collector.getTotalHits(); i++) {
				int docId=hits[i].doc;
				Document doc = is.doc(docId);
				retVal.append("<p><h3>"+doc.get("title")+" ("+doc.get("filedate")+")</h3><br/>");
				retVal.append(doc.get("filename")+"<br/><br/></p>");
			}
		}catch(IOException ioe){
			retVal.append(ioe.getMessage() + "<br/>");
		}
		retVal.append("</body></html>");
		return retVal.toString();
	}
	
	private static String getDocumentText(String location){
		File file = new File(location);
		DocumentHandler handler = getHandler(location);
		return handler.getText(file);
	}
	
	protected static DocumentHandler getHandler(String fileName){
		if(fileName.endsWith(".txt")){
			return new TextDocHandler();
		}else if(fileName.endsWith(".pdf")){
			return new PDFHandler();
		}else if(fileName.endsWith(".doc")){
			return new WordHandler();
		}else if(fileName.endsWith(".docx")){
			return new Word2007Handler();
		}else{
			return null;
		}
	}
}
