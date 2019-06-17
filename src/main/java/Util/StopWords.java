package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class StopWords {
	
	public StopWords() {
		
	}
	
	public List<String> lerStopWords() {
		String palavra;
		List<String> stopWords = new LinkedList<String>();
		try {
			FileReader fr = new FileReader("stopwords/stopwords.txt");
			BufferedReader br = new BufferedReader(fr);
			while ((palavra = br.readLine()) != null) {
				stopWords.add(palavra.toLowerCase().trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stopWords;
	}

	public String retiraStopWords(String text) {
		List<String> stopWords = lerStopWords();
		for (String stopWord : stopWords) {
			text.toLowerCase().replace(stopWord, "");
		}
		return text.toLowerCase();
	}
}
