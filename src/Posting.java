import java.util.ArrayList;

/**
 * The posting for each individual document for a given word.
 * 
 * @author Jack
 * 
 */
public class Posting {
	private String word;
	private int docID;
	private ArrayList<Integer> position = new ArrayList<Integer>();

	/**
	 * Constructor for a posting.
	 * 
	 * @param word
	 *            The word.
	 * @param docID
	 *            The document id.
	 * @param position
	 *            Position of word in posting.
	 */
	public Posting(String word, int docID, ArrayList<Integer> position) {
		this.word = word;
		this.docID = docID;
		this.position = position;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDocID() {
		return docID;
	}

	public ArrayList<Integer> getPosition() {
		return position;
	}

}
