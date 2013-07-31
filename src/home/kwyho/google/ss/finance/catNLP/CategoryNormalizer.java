package home.kwyho.google.ss.finance.catNLP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;

public class CategoryNormalizer {
	private HashMap<String, String> crosswalkHash;
	private HashMap<String, List<String>> stemmedCategoriesHash;
	
	public CategoryNormalizer(String filename) {
		File crosswalkFile = new File(filename);
		importCrosswalk(crosswalkFile);
	}
	
	public CategoryNormalizer() {
		this("SSSpendCatCrosswalk.csv");
	}

	public void importCrosswalk(File crosswalkFile) {
		crosswalkHash = new HashMap<String, String>();
		
		try {
			CSVReader reader = new CSVReader(new FileReader(crosswalkFile));
			List<String[]> crosswalks = reader.readAll();
			reader.close();
			
			for (String[] crosswalk: crosswalks) {
				crosswalkHash.put(crosswalk[0], crosswalk[1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importAllCategories(List<String> categories) {
		stemmedCategoriesHash = new HashMap<String, List<String>>();
		
		for (String category: categories) {
			String stemmedCategory = PorterStemmerTokenizerFactory.stem(category);
			if (stemmedCategoriesHash.containsKey(stemmedCategory)) {
				stemmedCategoriesHash.get(stemmedCategory).add(category);
			} else {
				List<String> list = new ArrayList<String>();
				list.add(category);
				stemmedCategoriesHash.put(stemmedCategory, list);
			}
		}
	}
}
