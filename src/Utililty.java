import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Class that allows comparisons of bitarrays for finding intersections between documents.
 * @author Jack
 *
 */
public class Utililty {

	static JenkinsHash jenkinsHash = new JenkinsHash();
	
	
	/**
	 * Gets the list of possible candidates documents using bloom filter and the word in question.
	 * @param docIDsForWord The document IDs to check.
	 * @param bloomFilterToCheckAgainst The bloom filter to check IDs against.
	 * @return The list of all candidate documents.
	 */
	public static BitSet getListOfCandidates(BitSet docIDsForWord, BitSet bloomFilterToCheckAgainst){
		
		BitSet candidateDocIDList = new BitSet();
		
		if(bloomFilterToCheckAgainst == null || docIDsForWord == null){
			return candidateDocIDList;
		}
		
		if(docIDsForWord.isEmpty()){
			return candidateDocIDList;
		}
		BigInteger bigInt;
		int hashValue;
		int hashValue2;
		int hashValue3;
		int hashValue4;
		for (int i = docIDsForWord.nextSetBit(0); i >= 0; i = docIDsForWord.nextSetBit(i+1)) {
			 bigInt = BigInteger.valueOf(i);
			 
			 hashValue = (int) jenkinsHash.hash(bigInt.toByteArray()) % Term.HASH_SIZE;
			 
//			if(bloomFilterToCheckAgainst.get(hashValue)){
//				candidateDocIDList.set(i);
//			}
			
			//UNCOMMENT FOR k=2
			 hashValue2= (int) jenkinsHash.hash(bigInt.toByteArray(), 500) % Term.HASH_SIZE;
			 hashValue3= (int) jenkinsHash.hash(bigInt.toByteArray(), 1000) % Term.HASH_SIZE;
			 hashValue4= (int) jenkinsHash.hash(bigInt.toByteArray(), 1500) % Term.HASH_SIZE;
			if(bloomFilterToCheckAgainst.get(hashValue) && bloomFilterToCheckAgainst.get(hashValue2) && bloomFilterToCheckAgainst.get(hashValue3) && bloomFilterToCheckAgainst.get(hashValue4)){
				candidateDocIDList.set(i);
			}
			
			if (i == Integer.MAX_VALUE) {
				break; // or (i+1) would overflow
			}
		}

		
		
		return candidateDocIDList;
		
	}
	

	
	
	/**
	 * Checks the result of the bloom filter if there is a valid hit.
	 * @param candidateList The candidate list to check.
	 * @param wordListToCheckAgainst The real intersection values to check against.
	 * @return the list of valid document IDs.
	 */
	public static ArrayList<Integer> checkListOfCandidatesForReal(BitSet candidateList, BitSet wordListToCheckAgainst){
		BitSet docIDList = new BitSet();
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		if(candidateList.isEmpty()){
			return ret;
		}
		
		docIDList = (BitSet) candidateList.clone();
		docIDList.and(wordListToCheckAgainst);
		
		
		for (int i = docIDList.nextSetBit(0); i >= 0; i = docIDList.nextSetBit(i+1)) {
			ret.add(i);
			if (i == Integer.MAX_VALUE) {
				break; // or (i+1) would overflow
			}
		}
		
		return ret;
	}
	
	/**
	 * Performs a bitwise and of two bitarrays of two words to find intersection.
	 * @param word1List 1st word's bit array
	 * @param word2List 2nd word's bit array
	 * @return A list of intersection DocIDs.
	 */
	public static ArrayList<Integer> getListOfRealDocuments(BitSet word1List, BitSet word2List){
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		if(word2List == null){
			return docIDList;
		}
		BitSet result = new BitSet();
		result = (BitSet) word1List.clone();
		result.and(word2List);	
		
		for (int i = result.nextSetBit(0); i >= 0; i = result.nextSetBit(i+1)) {
			docIDList.add(i);
			
		     if (i == Integer.MAX_VALUE) {
		         break; // or (i+1) would overflow
		     }
		 }
		
		
		return docIDList;
	}
	

	/**
	 * Performs a linear search of the bit array and finds the intersection.
	 * @param word1List
	 * @param word2List
	 * @return
	 */
	public static ArrayList<Integer> getNaiveListOfRealDocuments(ArrayList<Integer> word1List, ArrayList<Integer> word2List) {
		ArrayList<Integer> docIDList = new ArrayList<Integer>();
		
		if(word2List == null || word1List == null){
			return docIDList;
		}
		
		for(int i=0; i<word1List.size(); i++){
			if(word2List.contains(word1List.get(i))){
				docIDList.add(word1List.get(i));
			}
		}
		return docIDList;
	}
	
	/**
	 * Compares the size of two postings list, returns true if first posting list is smaller than the second
	 * posting list. Else, return false
	 * @param postingWord1 The first postings list.
	 * @param postingWord2 The second postings list.
	 * @return True if word1 is smaller, false otherwise
	 */
	public static boolean smallerThan(ArrayList<Integer> postingWord1, ArrayList<Integer> postingWord2) {
		if(postingWord1.size() < postingWord2.size()){
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
