import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Model that stores the posting for each individual word/term. Used to create a
 * list of all terms as a inverted index.
 * 
 * @author Jack
 * 
 */
public class Term implements Comparable<Term>{
	static int HASH_SIZE = 5000;

	private String word;
	private ArrayList<Posting> postingList = new ArrayList<Posting>();
	private BitSet bloomFilter = new BitSet();
	private BitSet bitArray = new BitSet();


	/**
	 * Creates a new term for the postings list.
	 * 
	 * @param word
	 */
	public Term(String word) {
		this.word = word;
	}

	/**
	 * Adds a postings to the given word.
	 * 
	 * @param document
	 */
	public void addPosting(Posting document) {
		postingList.add(document);

	}

	/**
	 * Creates corresponding bit arrays and bloom filters.
	 */
	public void processPosting() {
		createBitArray();
		createBloomFilter();
	}

	/**
	 * Private method to create bit Array.
	 */
	private void createBitArray() {
		for (Posting posting : postingList) {
			bitArray.set(posting.getDocID());
		}
	}

	/**
	 * Private method to create a bloom filter for the posting.
	 */
	private void createBloomFilter() {
		JenkinsHash jenkinsHash = new JenkinsHash();

		for (Posting posting : postingList) {


			BigInteger bigInt = BigInteger.valueOf(posting.getDocID());
			
			int hashValue =  (int) jenkinsHash.hash(bigInt.toByteArray());
			hashValue = hashValue%HASH_SIZE;
			
			bloomFilter.set(hashValue, true);
			
			//UNCOMMENT FOR k=2 
			int hashValue2 =  (int) jenkinsHash.hash(bigInt.toByteArray(), 500);
			hashValue2 = hashValue2%HASH_SIZE;
			bloomFilter.set(hashValue2, true);
			
			int hashValue3 =  (int) jenkinsHash.hash(bigInt.toByteArray(), 1000);
			hashValue3 = hashValue3%HASH_SIZE;
			bloomFilter.set(hashValue3, true);
			
			int hashValue4 =  (int) jenkinsHash.hash(bigInt.toByteArray(), 1500);
			hashValue4 = hashValue4%HASH_SIZE;
			bloomFilter.set(hashValue4, true);

		}
		
		
	}
	
	
	public String getWord() {
		return word;
	}

	public ArrayList<Posting> getPostingList() {
		return postingList;
	}

	public BitSet getBitArray() {
		return bitArray;
	}

	public BitSet getBloomFilter() {
		return bloomFilter;
	}

	@Override
	public int compareTo(Term o) {
		if(this.postingList.size()<o.getPostingList().size()){
			return -1;
		}
		else{
			return 1;
		}
	}

	
}
