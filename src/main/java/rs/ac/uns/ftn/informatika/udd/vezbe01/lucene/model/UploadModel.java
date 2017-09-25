package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class UploadModel {

    private String title;
    
    private String keywords;

    private MultipartFile[] files;

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "UploadModel{" +
                "title='" + title + '\'' +
                ", files=" + Arrays.toString(files) +
                '}';
    }
}
