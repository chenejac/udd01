package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model;

public class AdvancedQuery {
	
	private String field1;
	private String value1;
	private String field2;
	private String value2;
	private String operation;
	
	public AdvancedQuery() {
		super();
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}

}
