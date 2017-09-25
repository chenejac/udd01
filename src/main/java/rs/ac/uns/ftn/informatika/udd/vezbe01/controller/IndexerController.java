package rs.ac.uns.ftn.informatika.udd.vezbe01.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.Indexer;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model.IndexUnit;
import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model.UploadModel;

@RestController
public class IndexerController {

		private static String DATA_DIR_PATH;
	
		static {
			ResourceBundle rb=ResourceBundle.getBundle("application");
			DATA_DIR_PATH=rb.getString("dataDir");
		}
		
		@GetMapping("/reindex")
		public ResponseEntity<String> index() throws IOException {
			File dataDir = getResourceFilePath(DATA_DIR_PATH);
			long start = new Date().getTime();
			int numIndexed = Indexer.getInstance().index(dataDir);
			long end = new Date().getTime();
			String text = "Indexing " + numIndexed + " files took "
					+ (end - start) + " milliseconds";
			return new ResponseEntity<String>(text, HttpStatus.OK);
		}
		
		private File getResourceFilePath(String path) {
		    URL url = this.getClass().getClassLoader().getResource(path);
		    File file = null;
		    try {
		        file = new File(url.toURI());
		    } catch (URISyntaxException e) {
		        file = new File(url.getPath());
		    }   
		    return file;
		}
		


	    @PostMapping("/index/add")
	    public ResponseEntity<String> multiUploadFileModel(@ModelAttribute UploadModel model) {


	        try {

	        	indexUploadedFile(model);

	        } catch (IOException e) {
	            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity<String>("Successfully uploaded!", HttpStatus.OK);

	    }
		    
		    
	    //save file
	    private String saveUploadedFile(MultipartFile file) throws IOException {
	    	String retVal = null;
            if (! file.isEmpty()) {
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(getResourceFilePath(DATA_DIR_PATH).getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.write(path, bytes);
	            retVal = path.toString();
            }
            return retVal;
	    }
	    
	    private void indexUploadedFile(UploadModel model) throws IOException{
	    	
	    	for (MultipartFile file : model.getFiles()) {

	            if (file.isEmpty()) {
	                continue; //next please
	            }
	            String fileName = saveUploadedFile(file);
	            if(fileName != null){
	            	IndexUnit indexUnit = Indexer.getInstance().getHandler(fileName).getIndexUnit(new File(fileName));
	            	indexUnit.setTitle(model.getTitle());
	            	indexUnit.setKeywords(new ArrayList<String>(Arrays.asList(model.getKeywords().split(" "))));
	            	Indexer.getInstance().add(indexUnit.getLuceneDocument());
	            }
	    	}
	    }

	
}
