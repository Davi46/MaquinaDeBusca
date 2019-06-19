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
		char[] pontuacao = pontuacao();

		for (String word : visaoList) {
			if (!verificaStopWord(word)) {
				List<String> wo = new ArrayList<>();
				char[] letras = word.toCharArray();
				for (char l : letras) { 
					if (!verificaPontuacao(l)) {
						wo.add(String.valueOf(l));
					} 
				}

				StringBuilder nw = new StringBuilder();
				for (String w : wo) {
					nw.append(w + "");
				}

				novoVisao.add(nw.toString());
			}
		}

		// Forma nova visao
		StringBuilder nv = new StringBuilder();
		for (String w : novoVisao) {
			if(w != "") {
				nv.append(w + " ");
			} 
		}

		return nv.toString().trim();
	}

	private char[] pontuacao() {
		String pontuacao;
		char[] stopWordfs = null;

		try {
			FileReader fr = new FileReader("stopwords/pontuacaoStop.txt");
			BufferedReader br = new BufferedReader(fr);
			stopWordfs = new char[29];

			int i = 0;

			while ((pontuacao = br.readLine()) != null) {
				char p = pontuacao.toLowerCase().trim().toString().charAt(0);
				stopWordfs[i] = p;
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

	private boolean verificaPontuacao(char letra) {
		char[] pontuacao = pontuacao();
		for (char ponto : pontuacao) {
			if (ponto == letra) {
				return true;
			}
		}

		return false;
	}
}
