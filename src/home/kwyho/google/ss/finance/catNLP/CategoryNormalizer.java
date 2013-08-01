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

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class CategoryNormalizer {
	private HashMap<String, String> crosswalkHash;
	private HashMap<String, List<String>> stemmedCategoriesHash;
	private MaxentTagger tagger;
	
	public CategoryNormalizer(String filename) throws IOException, ClassNotFoundException {
		File crosswalkFile = new File(filename);
		importCrosswalk(crosswalkFile);
		tagger = new MaxentTagger("english-left3words-distsim.tagger");
	}
	
	public CategoryNormalizer() throws IOException, ClassNotFoundException {
		this("SSSpendCatCrosswalk.csv");
	}
	
	public static String stemWords(String word) {
		String[] words = word.split(" ");
		StringBuffer stemmedWord = new StringBuffer("");
		for (String singleWord: words) {
			stemmedWord.append(PorterStemmerTokenizerFactory.stem(singleWord)+" ");
		}
		return stemmedWord.toString().trim();
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
			String stemmedCategory = CategoryNormalizer.stemWords(category);
			if (stemmedCategoriesHash.containsKey(stemmedCategory)) {
				stemmedCategoriesHash.get(stemmedCategory).add(category);
			} else {
				List<String> list = new ArrayList<String>();
				list.add(category);
				stemmedCategoriesHash.put(stemmedCategory, list);
			}
		}
	}
	
	private List<String> getTagLabels(String word) {
		List<String> tagLabels = new ArrayList<String>();
		String[] tokenizedWord = word.split(" ");
		for (String token: tokenizedWord) {
			String tagged_token = tagger.tagString(token);
			int underscorePos = tagged_token.lastIndexOf('_');
			tagLabels.add(tagged_token.substring(underscorePos+1));
		}
		return tagLabels;
	}
	
	protected String chooseBestWord(List<String> words) {
		if (words.size()==1) {
			return words.get(0);
		} else {
			List<Integer> scores = new ArrayList<Integer>();
			for (int i=0; i<words.size(); i++) {
				scores.add(0);
			}
			for (int i=0; i<words.size(); i++) {
				String word = words.get(i);
				List<String> tagLabels = getTagLabels(word);
				// Rule 1: prefer '-ing' ending
				if (tagLabels.contains("VBG")) {
					scores.set(i, scores.get(i)+1);
				}
				// Rule 2: prefer capitalized start
				String[] tokens = word.split(" ");
				boolean isCapitalized = true;
				for (String token: tokens) {
					isCapitalized = isCapitalized && (Character.isUpperCase(token.charAt(0)));
				}
				if (isCapitalized) {
					scores.set(i, scores.get(i)+1);
				}
				// Rule 3: prefer singular
				if (tagLabels.contains("NN") || tagLabels.contains("NNP")) {
					scores.set(i, scores.get(i)+1);
				}
			}
			int maxScore = Integer.MIN_VALUE;
			int maxScorePos = -1;
			for (int i=0; i<words.size(); i++) {
				if (scores.get(i) > maxScore) {
					maxScore = scores.get(i);
					maxScorePos = i;
				}
			}
			return words.get(maxScorePos);
		}
	}
	
	public String normalize(String category) {
		String stemmedCategory = CategoryNormalizer.stemWords(category);
		if (stemmedCategoriesHash.containsKey(stemmedCategory)) {
			return chooseBestWord(stemmedCategoriesHash.get(stemmedCategory));
		} else {
			return category;
		}
	}
}
