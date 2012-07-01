package code;
import java.util.*;
import java.io.*;
import code.*;


public class WordChain {
	
	List allWords;  // All words of same length to be contained in this List
	List parentWords; 
	Map<String,String> visitedWords; 
	int count; // Count after which searching stops and program exists
		
	private void readWordsFrmDictionary(int wordLength) throws IOException { //Reads all the word from dictionary with same length
		allWords = new ArrayList();
		BufferedReader r = new BufferedReader(new FileReader("data/wordsEn.txt"));
		try {
			String line;
			while ((line = r.readLine()) != null) {
				if(line.length() == wordLength)
				allWords.add(line);
			}
		} finally {
			r.close();
		}
	}

	private void checkIsWordInDictionary(String word) throws WordNotInDictionaryException { //to check if words are valid and present in dictionary
		if (! allWords.contains(word)) {
			throw new WordNotInDictionaryException();
		}
	}

	public String findChain(String start, String end)  //returns the word chain with minimum length
		throws WordNotInDictionaryException, IOException,WordLengthMismatchException {
		
		if (start.length() != end.length())
			throw new WordLengthMismatchException();

		if(start.equals(end)) 
			return start;
		
		readWordsFrmDictionary(start.length());
		
		checkIsWordInDictionary(start);
		checkIsWordInDictionary(end);

		parentWords = new LinkedList();
		visitedWords = new HashMap();
		count = 0;
		
		parentWords.add(start);
		visitedWords.put(start, null);
		String chain=searchForEndWord(end);
		if (chain !=null)  {
			return chain;
		}else return "no chain exists";
	}

	private String searchForEndWord(String end) {
		while (parentWords.size() > 0) {
			if (count++ > 15000)
				throw new RuntimeException("exceeded search limit");

			String parentWord = (String) parentWords.remove(0);
			//System.out.println(parentWord + " : "+"in remove");
			Iterator iter = allWords.iterator();
			while (iter.hasNext()) {
				String childWord = (String) iter.next();
				if (!visitedWords.containsKey(childWord) && adjacentWord(parentWord, childWord)) {
					visitedWords.put(childWord, parentWord);
					//System.out.println(visitedWords.get(childWord) +" : "+visitedWords.get(parentWord) +" : " +childWord + " : "+parentWord);
					if (end.equals(childWord)){
					return createWordChain(childWord);
					}
					parentWords.add(childWord);
				}
			}
		}
		return null;
	}
  
	private String createWordChain(String word) { //formats the shortest path once the end word is found
		String result = word;
		word = (String) visitedWords.get(word);
		//System.out.println(word);
		while (word != null) {
			result = word + "," + result;
			word = (String) visitedWords.get(word);
			//System.out.println(word +" as in format function "+result);
		}
		return result;
	}
	public boolean adjacentWord(String a, String b) {
		int nDifferent = 0;
		int length = a.length();
		for (int i = 0; i < length; i++) {
			if (a.charAt(i) != b.charAt(i)) {
				nDifferent++;
			}
			if(nDifferent > 1)
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		WordChain cf = new WordChain();
		boolean isCorrectInput=false;
		System.out.println("Please provide two Input words of same length separted by space");
		while (!isCorrectInput) { 
			try {
				InputStreamReader inp = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(inp);
				String str = br.readLine();
				String words[]=str.split(" ");
				System.out.println(cf.findChain(words[0],words[1]));
				isCorrectInput=true;
				} catch (Exception e) {
				System.out.println("Invalid Input");
				e.printStackTrace();
				System.out.println("Please provide two Input words of same length separted by space");
			}
		}
	}
}
