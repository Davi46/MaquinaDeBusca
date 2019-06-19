package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
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
		List<String> visaoList = Arrays.asList(text.split(" "));
		List<String> novoVisao = new ArrayList<>();

		for (String word : visaoList) {
			if (!verificaStopWord(word)) {
				novoVisao.add(word.trim());
			}
		}

		// Forma nova visao
		StringBuilder nv = new StringBuilder();
		for (String w : novoVisao) {
			nv.append(w + " ");
		}
		
		//Retira pontuacao
		for (char pontuacao : pontuacao()) {
			nv.toString().toLowerCase().replace(pontuacao, '\u0000');
		}

		return nv.toString().trim();
	}

	private char[] pontuacao() {
		String pontuacao; 
		char[] stopWordfs = null ;
		
 		try {
			FileReader fr = new FileReader("stopwords/pontuacaoStop.txt");
			BufferedReader br = new BufferedReader(fr);
			stopWordfs = new char[br.readLine().length()];
			
			int i = 0;
			
			while ((pontuacao = br.readLine()) != null) {
				stopWordfs[i] = pontuacao.toLowerCase().trim().toString().charAt(0);
				i++;
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stopWordfs;
	}

	private boolean verificaStopWord(String word) {
		List<String> stopWords = lerStopWords();
		for (String string : stopWords) {
			if (string.trim().toLowerCase().equals(word.trim().toLowerCase())) {
				return true;
			}
		}
		return false;
	} 
}
