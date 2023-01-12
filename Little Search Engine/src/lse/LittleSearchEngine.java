package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		try 
		{
			HashMap<String, Occurrence> keyW = new HashMap<String, Occurrence>();
			Scanner scan = new Scanner(new File(docFile));
			int apple = 1;
			while (scan.hasNext()) 
			{
				String gkey = getKeyword(scan.next());
				if (gkey != null) 
				{
					if (!keyW.containsKey(gkey))
						keyW.put(gkey, new Occurrence(docFile, apple));
					else
						keyW.get(gkey).frequency += 1;
				}
			}
			scan.close();
			return keyW; 
		}
		catch (FileNotFoundException e) 
		{
			return null;
		}
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) 
	{
		for (String oKey : kws.keySet()) 
		{
			if (!keywordsIndex.containsKey(oKey)) 
			{
				ArrayList<Occurrence> oList = new ArrayList<Occurrence>();
				oList.add(kws.get(oKey));
				keywordsIndex.put(oKey, oList);
			} 
			else 
			{
				keywordsIndex.get(oKey).add(kws.get(oKey));
				keywordsIndex.put(oKey, keywordsIndex.get(oKey));
				insertLastOccurrence(keywordsIndex.get(oKey));
			}
		}
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) 
	{
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		word = word.toLowerCase();
		while (word.length() != 0) 
		{
			char endChar = word.charAt(word.length() - 1);
			if (endChar == '.' || endChar == ',' || endChar == '?' || endChar == ':' || endChar == ';' || endChar == '!') 
			{
				word = word.substring(0, word.length() - 1);
			} 
			else
			{
				break;
			}
		}
		for (int i = 0; word != null && i < word.length(); i++)
			if (!Character.isLetter(word.charAt(i))) 
			{
				return null;
			}
		if (word != null && ((word.length() == 0) || noiseWords.contains(word))) 
		{
			return null;
		}
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) 
	{
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if (occs.size() == 1) 
		{
			return null;
		}

		ArrayList<Integer> midV = new ArrayList<Integer>();

		int lab = 0;
		int ray = occs.size() - 2;
		int insT = occs.get(occs.size() - 1).frequency, frequency = 0, middle = 0;

		while (lab <= ray) 
		{
			middle = (lab + ray) / 2;
			midV.add(middle);

			frequency = occs.get(middle).frequency;
			if (frequency == insT) 
			{
				Occurrence objInsert = occs.get(occs.size() - 1);

				occs.add(middle + 1, objInsert);
				occs.remove(occs.size() - 1);
				break;
			}
			if (frequency > insT) 
			{
				lab = middle + 1;
			} else 
			{
				ray = middle - 1;
			}
		}

		if (frequency < insT) 
		{
			Occurrence object = occs.get(occs.size() - 1);
			occs.add(middle, object);
			occs.remove(occs.size() - 1);
		} else 
		{
			if (frequency > insT) 
			{
				Occurrence object = occs.get(occs.size() - 1);
				occs.add(middle + 1, object);
				occs.remove(occs.size() - 1);
			}
		}
		return midV;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException 
	{
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) 
		{
			String w = sc.next();
			noiseWords.add(w);
		}
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) 
		{
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) 
	{
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if (kw1 == null && kw2 == null) 
		{
			return null;
		}

		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();

		ArrayList<Occurrence> aList1 = new ArrayList<Occurrence>();
		if (keywordsIndex.get(kw1) != null) 
		{
			aList1 = new ArrayList<Occurrence>();
			aList1.addAll(keywordsIndex.get(kw1));
		}

		ArrayList<Occurrence> bList1 = new ArrayList<Occurrence>();
		if (keywordsIndex.get(kw2) != null) 
		{
			bList1 = keywordsIndex.get(kw2);

			for (int i = 0; i < bList1.size(); i++) 
			{
				for (int j = 0; j < aList1.size(); j++) 
				{
					if (bList1.get(i).frequency == aList1.get(j).frequency) 
					{
						aList1.add(j + 1, bList1.get(i));
						j++;
					} else if (bList1.get(i).frequency > aList1.get(j).frequency) 
					{
						aList1.add(j, bList1.get(i));
						j++;
					}
				}
			}
		}
		if (aList1.size() == 0)
		{
			return null;
		}
		ArrayList<String> cfList = new ArrayList<String>();
		for (int i = 0; i < aList1.size(); i++) 
		{
			Occurrence last = aList1.get(i);
			if (!cfList.contains(last.document) && cfList.size() < 5) 
			{
				cfList.add(last.document);
			}
		}
		return cfList;
	}
}
