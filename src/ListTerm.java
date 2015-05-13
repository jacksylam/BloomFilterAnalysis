import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The object that holds the postings list for each individual word/term. This
 * is an inverted index. Hierarchy: ListTerm contains Terms where each
 * individual term contains a posting.
 * 
 * 
 * @author Jack
 * 
 */
public class ListTerm {

	private List<Term> list = new ArrayList<Term>();
	private HashMap<String, Integer> hashMapID = new HashMap<>();

	public ListTerm(ArrayList<Term> list) {
		this.list = list;
		for(int i=0; i<list.size(); i++){
			hashMapID.put(list.get(i).getWord(), i);
		}
	}

	/**
	 * Gets the posting list for a given word.
	 * 
	 * @param word
	 *            The word.
	 * @return The arraylist of all the postings.
	 */
	public ArrayList<Posting> getPostingListForWord(String word) {
		ArrayList<Posting> ret = null;

		if(!hashMapID.containsKey(word)){
			return ret;
		}
		ret = list.get(hashMapID.get(word)).getPostingList();
		return ret;
	}

	/**
	 * Gets the postings list as a bitarray for a given word.
	 * 
	 * @param word
	 *            The word.
	 * @return The bit array for all the postings.
	 */
	public BitSet getPostingListBitArrayForWord(String word) {
		BitSet ret = null;

		if(!hashMapID.containsKey(word)){
			return ret;
		}
		ret = list.get(hashMapID.get(word)).getBitArray();
		return ret;
	}

	
	/**
	 * Gets the bloom filter as a bitarray that contains marked docIDs for the given word.
	 * Value in bitarray is only marked if the docID contains that word.
	 * 
	 * @param word The word.
	 * @return The bloom filter for the given word.
	 */
	public BitSet getPostingListBloomFilterForWord(String word) {
		BitSet ret = null;

		if(!hashMapID.containsKey(word)){
			return ret;
		}
		ret = list.get(hashMapID.get(word)).getBloomFilter();
		return ret;
	}
	
	
	/**
	 * Gets all the docIDs from a given word.
	 * @param word
	 * @return
	 */
	public ArrayList<Integer> getDocIDs(String word) {
		ArrayList<Posting> list = getPostingListForWord(word);
		ArrayList<Integer> listOfDocIDs = new ArrayList<Integer>();
		
		if(list == null){
			return listOfDocIDs;
		}
		
		for(Posting posting : list) {
			if(!listOfDocIDs.contains(posting.getDocID())){
				listOfDocIDs.add(posting.getDocID());
			}
		}
		return listOfDocIDs;
		
	}
	
	/**
	 * Sorts the postings list in order of size
	 */
	public void sort(){
		Collections.sort(this.list);
	}

}
