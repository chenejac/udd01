package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.search;


import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;

import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.analysers.SerbianAnalyzer;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model.SearchType;


public class QueryBuilder {
	
	private static SerbianAnalyzer analyzer = new SerbianAnalyzer();
	private static int maxEdits = 1;
	
	public static int getMaxEdits(){
		return maxEdits;
	}
	
	public static void setMaxEdits(int maxEdits){
		QueryBuilder.maxEdits = maxEdits;
	}
	
	public static Query buildQuery(SearchType queryType, String field, String value) throws IllegalArgumentException, ParseException{
		QueryParser parser = new QueryParser(field, analyzer);
		String errorMessage = "";
		if(field == null || field.equals("")){
			errorMessage += "Field not specified";
		}
		if(value == null){
			if(!errorMessage.equals("")) errorMessage += "\n";
			errorMessage += "Value not specified";
		}
		if(!errorMessage.equals("")){
			throw new IllegalArgumentException(errorMessage);
		}
		
		Query query = null;
		if(queryType.equals(SearchType.regular)){
			Term term = new Term(field, value);
			query = new TermQuery(term);
		}else if(queryType.equals(SearchType.fuzzy)){
			Term term = new Term(field, value);
			query = new FuzzyQuery(term, maxEdits);
		}else if(queryType.equals(SearchType.prefix)){
			Term term = new Term(field, value);
			query = new PrefixQuery(term);
		}else if(queryType.equals(SearchType.range)){
			String[] values = value.split(" ");
			query = new TermRangeQuery(field, new BytesRef(values[0]), new BytesRef(values[1]), true, true);
		}else{
			String[] values = value.split(" ");
			PhraseQuery.Builder builder = new PhraseQuery.Builder();
			for(String word : values){
				Term term = new Term(field, word);
				builder.add(term);
			}
			query = builder.build();
		}
		
		return parser.parse(query.toString(field));
	}

}
