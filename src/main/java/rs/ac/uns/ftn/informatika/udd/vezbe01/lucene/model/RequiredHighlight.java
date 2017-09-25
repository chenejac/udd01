package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model;

public final class RequiredHighlight {
	
	private String fieldName;
	private String value;
	
	public RequiredHighlight() {
		super();
	}

	public RequiredHighlight(String fieldName, String value) {
		super();
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getValue() {
		return value;
	}

}
