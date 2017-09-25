package rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.indexing.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.document.DateTools;

import rs.ac.uns.ftn.informatika.udd.vezbe01.lucene.model.IndexUnit;

public class TextDocHandler extends DocumentHandler {

	@Override
	public IndexUnit getIndexUnit(File file) {
		IndexUnit retVal = new IndexUnit();
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, "UTF8"));

			String firstLine = reader.readLine(); // u prvoj liniji svake
													// tekstualne datoteke se
													// nalazi naslov rada

			retVal.setTitle(firstLine);
			
			/*
			 * add other custom metadata
			 */

			String secondLine = reader.readLine();
			String[] keywords = secondLine.split(";");
			retVal.setKeywords(new ArrayList<String>(Arrays.asList(keywords)));

			String fullText = "";
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText += " " + secondLine;
			}
			retVal.setText(fullText);
			
			retVal.setFilename(file.getCanonicalPath());
			
			String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
			retVal.setFiledate(modificationDate);

			return retVal;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
	}

	@Override
	public String getText(File file) {
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, "UTF8"));
			String secondLine;
			String fullText = "";
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText += " " + secondLine;
			}
			return fullText;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
	}

}
