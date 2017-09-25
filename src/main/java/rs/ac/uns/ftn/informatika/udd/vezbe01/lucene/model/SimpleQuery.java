package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model;

public class SimpleQuery {
	
	private String field;
	private String value;
	
	public SimpleQuery() {
		super();
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
