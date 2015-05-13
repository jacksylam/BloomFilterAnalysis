import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Class to perform all project experiments.
 * 
 * @author Jack
 *
 */
public class Experiment {
	private ListTerm postingsList;
	private int numberOfIterations = 10000;
	private int DEBUG_MODE = 0;

	public Experiment() {
		Path file = Paths.get("testIndex.dat");
		postingsList = new ListTerm(FileToMemory.fileToMemory(file));
	}

	/**
	 * Compares the time it takes for finding the intersection between two
	 * words.
	 * 
	 * @param word1
	 *            The first word.
	 * @param word2
	 *            The second word.
	 */
	public void compareTimeForIntersection(String word1, String word2) {

		System.out.println("STARTING " + word1 + " " + word2);

		//DEBUG PURPOSES
		//		System.out.println("Size of postings list for " + word1+ " " + postingsList.getPostingListBitArrayForWord(word1).cardinality());
		//		System.out.println("Size of bloomfilter list for " + word1+ " " + postingsList.getPostingListBloomFilterForWord(word1).length());
		//		System.out.println(postingsList.getPostingListBloomFilterForWord(word1).cardinality());
		//		System.out.println("Size of postings list for " + word2+ " " + postingsList.getPostingListBitArrayForWord(word2).cardinality());
		//		System.out.println("Size of bloomfilter list for " + word2+ " " + postingsList.getPostingListBloomFilterForWord(word2).length());
		//		System.out.println(postingsList.getPostingListBloomFilterForWord(word2).cardinality());


		long startTime = System.nanoTime();
		for (int i = 0; i < numberOfIterations; i++) {
			//ArrayList<Integer> candidateList = Utililty.getListOfCandidates(
			//					BitSet candidateList = Utililty.getListOfCandidates(
			//					postingsList.getDocIDs(word1),
			//					postingsList.getPostingListBloomFilterForWord(word2));

			ArrayList<Integer> checkedList = null;

			BitSet candidateList = Utililty.getListOfCandidates(
					postingsList.getPostingListBitArrayForWord(word1),
					postingsList.getPostingListBloomFilterForWord(word2));

			if (i == 0)
				System.out.println("LIST WITH BLOOM FILTER: "
						+ candidateList.toString());

			if (!candidateList.isEmpty()) {
				checkedList = Utililty.checkListOfCandidatesForReal(
						candidateList,
						postingsList.getPostingListBitArrayForWord(word2));
			}
			if (i == 0)
				System.out.println("LIST WITH CHECKED BLOOM FILTER: "
						+ checkedList.toString());
		}
		long endTime = System.nanoTime();

		System.out.format("Took %.8f ms%n", ((endTime - startTime) / 1000000.0)
				/ numberOfIterations);



		ArrayList<Integer> docIDList;
		//BitSet docIDList;

		long startTime2 = System.nanoTime();
		for (int i = 0; i < numberOfIterations; i++) {
			BitSet temp = postingsList.getPostingListBitArrayForWord(word1);
			BitSet temp2 = postingsList.getPostingListBitArrayForWord(word2);
			docIDList = Utililty.getListOfRealDocuments(
					temp, temp2);
			if (i == 0)
				System.out.println("LIST WITH REAL DOCID: " + docIDList);
		}
		long endTime2 = System.nanoTime();
		System.out.format("Took %.8f ms%n", ((endTime2 - startTime2) / 1000000.0)
				/ numberOfIterations);

		System.out.println();
	}

	/**
	 * Compares the time it takes for finding the intersection between two words
	 * using the smaller one has hash and checking the second word's bloom
	 * filter.
	 * 
	 * @param word1
	 *            The first word.
	 * @param word2
	 *            The second word.
	 */
	public void compareTimeForIntersectionLeastFirst(String word1, String word2) {
		System.out.println("STARTING " + word1 + " " + word2);


		//DEBUG PURPOSES
		System.out.println("Size of postings list for " + word1+ " " + postingsList.getPostingListBitArrayForWord(word1).cardinality());
		//		System.out.println("Size of bloomfilter list for " + word1+ " " + postingsList.getPostingListBloomFilterForWord(word1).length());
		//		System.out.println("Size of bloomfilter set true for " + word1 + " " + postingsList.getPostingListBloomFilterForWord(word1).cardinality());
		System.out.println("Size of postings list for " + word2+ " " + postingsList.getPostingListBitArrayForWord(word2).cardinality());
		//		System.out.println("Size of bloomfilter list for " + word2+ " " + postingsList.getPostingListBloomFilterForWord(word2).length());
		//		System.out.println("Size of bloomfilter set true for " + word2 + " " +postingsList.getPostingListBloomFilterForWord(word2).cardinality());

		int size1 = postingsList.getDocIDs(word1).size();
		int size2 = postingsList.getDocIDs(word2).size();
		int tempSize = 0;
		String smaller = word1;
		String bigger = word2;


		long startTime = System.nanoTime();
		for (int i = 0; i < numberOfIterations; i++) {
			if (size2< size1) {
				smaller = word2;
				tempSize = size1;
				size1 = size2;
				size2 = tempSize;
				bigger = word1;
			}

			//ArrayList<Integer> candidateList = Utililty.getListOfCandidates(
			//					BitSet candidateList = Utililty.getListOfCandidates(
			//					postingsList.getDocIDs(smaller),
			//					postingsList.getPostingListBloomFilterForWord(bigger));

			ArrayList<Integer> checkedList = null;

			BitSet candidateList = Utililty.getListOfCandidates(
					postingsList.getPostingListBitArrayForWord(smaller),
					postingsList.getPostingListBloomFilterForWord(bigger));

			if (i == 0)
				System.out.println("LIST WITH BLOOM FILTER: "
						+ candidateList.toString());

			if (!candidateList.isEmpty()) {
				checkedList = Utililty.checkListOfCandidatesForReal(
						candidateList,
						postingsList.getPostingListBitArrayForWord(bigger));
			}
			if (i == 0)
				System.out.println("LIST WITH CHECKED BLOOM FILTER: "
						+ checkedList.toString());
		}
		long endTime = System.nanoTime();

		System.out.format("Took %.8f ms%n", ((endTime - startTime) / 1000000.0)
				/ numberOfIterations);

		ArrayList<Integer> docIDList;
		//BitSet docIDList;

		long startTime2 = System.nanoTime();
		for (int i = 0; i < numberOfIterations; i++) {
			BitSet temp = postingsList.getPostingListBitArrayForWord(word1);
			BitSet temp2 = postingsList.getPostingListBitArrayForWord(word2);
			docIDList = Utililty.getListOfRealDocuments(
					temp, temp2);
			if (i == 0)
				System.out.println("LIST WITH REAL DOCID: " + docIDList);
		}
		long endTime2 = System.nanoTime();
		System.out.format("Took %.8f ms%n", ((endTime2 - startTime2) / 1000000.0)
				/ numberOfIterations);

		System.out.println();
	}

	/**
	 * Compares the time it takes for finding the intersection between two words
	 * using the smaller one has hash and checking the second word's bloom
	 * filter.
	 * 
	 * Optimized method. Will use bitwise AND instead of bloom filter if there are too many hashes.
	 * 
	 * @param word1
	 *            The first word.
	 * @param word2
	 *            The second word.
	 */
	public void compareTimeForIntersectionLeastFirstOptimize(String word1, String word2) {
		System.out.println("STARTING " + word1 + " " + word2);

		if(postingsList.getPostingListBitArrayForWord(word1) != null || postingsList.getPostingListBitArrayForWord(word2) != null){
			//DEBUG PURPOSES
			//System.out.println("Size of postings list for " + word1+ " " + postingsList.getPostingListBitArrayForWord(word1).cardinality());
			//		System.out.println("Size of bloomfilter list for " + word1+ " " + postingsList.getPostingListBloomFilterForWord(word1).length());
			//		System.out.println("Size of bloomfilter set true for " + word1 + " " + postingsList.getPostingListBloomFilterForWord(word1).cardinality());
			//System.out.println("Size of postings list for " + word2+ " " + postingsList.getPostingListBitArrayForWord(word2).cardinality());
			//		System.out.println("Size of bloomfilter list for " + word2+ " " + postingsList.getPostingListBloomFilterForWord(word2).length());
			//		System.out.println("Size of bloomfilter set true for " + word2 + " " +postingsList.getPostingListBloomFilterForWord(word2).cardinality());

		}


		int size1 = postingsList.getDocIDs(word1).size();
		int size2 = postingsList.getDocIDs(word2).size();
		int tempSize = 0;
		String smaller = word1;
		String bigger = word2;
		
		ArrayList<Integer> checkedList;
		BitSet candidateList = null;
		
		checkedList = new ArrayList<Integer>();

		candidateList = new BitSet();

		if (size2< size1) {
			smaller = word2;
			tempSize = size1;
			size1 = size2;
			size2 = tempSize;
			bigger = word1;
		}

		long startTime = System.nanoTime();
		for (int i = 0; i < numberOfIterations; i++) {


//			//if(size1 > 100){
//			if(false){
//				//Too big. Don't use bloom filter and do straight bitwise AND with the bit array.
//				if(i==0) System.out.println("Too big, doing bitwise AND.");
//
//				BitSet temp =  postingsList.getPostingListBitArrayForWord(word1);
//				BitSet temp2 = postingsList.getPostingListBitArrayForWord(word2);
//
//				ArrayList<Integer> docIDList;
//				//BitSet docIDList = new BitSet();
//				docIDList = Utililty.getListOfRealDocuments(
//						temp, temp2);
//				if (i == 0)
//					System.out.println("LIST WITH REAL DOCID: " + docIDList.toString());
//			}

//			else{
				//ArrayList<Integer> candidateList = Utililty.getListOfCandidates(
				//						BitSet candidateList = Utililty.getListOfCandidates(
				//						postingsList.getDocIDs(smaller),
				//						postingsList.getPostingListBloomFilterForWord(bigger));


				candidateList = Utililty.getListOfCandidates(
						postingsList.getPostingListBitArrayForWord(smaller),
						postingsList.getPostingListBloomFilterForWord(bigger));

				if (i == DEBUG_MODE)
					System.out.println("LIST WITH BLOOM FILTER: "
							+ candidateList.toString());

				if (!candidateList.isEmpty()) {
					
					checkedList = Utililty.checkListOfCandidatesForReal(
							candidateList,
							postingsList.getPostingListBitArrayForWord(bigger));
				}
				if (i == DEBUG_MODE)
					System.out.println("LIST WITH CHECKED BLOOM FILTER: "
							+ checkedList.toString());
			//}
		}
		long endTime = System.nanoTime();

		System.out.format("Took %.8f ms%n", ((endTime - startTime) / 1000000.0)
				/ numberOfIterations);


		ArrayList<Integer> docIDList = null;
		//BitSet docIDList;


		long startTime2 = System.nanoTime();
		for (int i = 0; i < numberOfIterations; i++) {
			BitSet temp = postingsList.getPostingListBitArrayForWord(word1);
			BitSet temp2 = postingsList.getPostingListBitArrayForWord(word2);
			docIDList = Utililty.getListOfRealDocuments(
					temp, temp2);
			if (i == DEBUG_MODE)
				System.out.println("LIST WITH REAL DOCID: " + docIDList.toString());
		}
		long endTime2 = System.nanoTime();
		System.out.format("Took %.8f ms%n", ((endTime2 - startTime2) / 1000000.0)
				/ numberOfIterations);

		float correctRate = (float) docIDList.size() /candidateList.cardinality() ;
		System.out.println("Correct positive rate is: "+ correctRate);
		System.out.println("Correct positive rate is: "+ docIDList.size()+ " / " + candidateList.cardinality());
		
		ArrayList<Integer> naiveDocIDList = null;
		
		long startTime3 = System.nanoTime();
		for (int i = 0; i < numberOfIterations; i++) {
			ArrayList<Integer> temp = postingsList.getDocIDs(word1);
			ArrayList<Integer> temp2 = postingsList.getDocIDs(word2);
			naiveDocIDList = Utililty.getNaiveListOfRealDocuments(temp, temp2);
			if (i == DEBUG_MODE)
				System.out.println("LIST WITH NAIVE DOCID: " + naiveDocIDList.toString());
		}
		long endTime3 = System.nanoTime();
		System.out.format("Took %.8f ms%n", ((endTime3 - startTime3) / 1000000.0)
				/ numberOfIterations);
		System.out.println();
	}





	public void getPostitiveRate(String word1, String word2) {
		System.out.println("STARTING correct positive rate " + word1 + " " + word2);

		if(postingsList.getPostingListBitArrayForWord(word1) != null || postingsList.getPostingListBitArrayForWord(word2) != null){
			//DEBUG PURPOSES
			//System.out.println("Size of postings list for " + word1+ " " + postingsList.getPostingListBitArrayForWord(word1).cardinality());
			//		System.out.println("Size of bloomfilter list for " + word1+ " " + postingsList.getPostingListBloomFilterForWord(word1).length());
			//		System.out.println("Size of bloomfilter set true for " + word1 + " " + postingsList.getPostingListBloomFilterForWord(word1).cardinality());
			//System.out.println("Size of postings list for " + word2+ " " + postingsList.getPostingListBitArrayForWord(word2).cardinality());
			//		System.out.println("Size of bloomfilter list for " + word2+ " " + postingsList.getPostingListBloomFilterForWord(word2).length());
			//		System.out.println("Size of bloomfilter set true for " + word2 + " " +postingsList.getPostingListBloomFilterForWord(word2).cardinality())
		}

		int size1 = postingsList.getDocIDs(word1).size();
		int size2 = postingsList.getDocIDs(word2).size();
		int tempSize = 0;
		String smaller = word1;
		String bigger = word2;

		if (size2< size1) {
			smaller = word2;
			tempSize = size1;
			size1 = size2;
			size2 = tempSize;
			bigger = word1;
		}

		if(size1 > 100){
			//Too big. Don't use bloom filter and do straight bitwise AND with the bit array.
			System.out.println("Too big, doing bitwise AND.");

			BitSet temp = postingsList.getPostingListBitArrayForWord(word1);
			BitSet temp2 = postingsList.getPostingListBitArrayForWord(word2);

			ArrayList<Integer> docIDList;
			//BitSet docIDList;
			docIDList = Utililty.getListOfRealDocuments(
					temp, temp2);

			//System.out.println("LIST WITH REAL DOCID: " + docIDList.toString());
		}

		else{

			BitSet candidateList = Utililty.getListOfCandidates(
					postingsList.getPostingListBitArrayForWord(smaller),
					postingsList.getPostingListBloomFilterForWord(bigger));
			ArrayList<Integer> checkedList = new ArrayList<Integer>();

//			System.out.println("LIST WITH BLOOM FILTER: "
//					+ candidateList.toString());

			BitSet oldCandidateList = (BitSet) candidateList.clone();

			if (!candidateList.isEmpty()) {
				checkedList = Utililty.checkListOfCandidatesForReal(
						candidateList,
						postingsList.getPostingListBitArrayForWord(bigger));
			}

//			System.out.println("LIST WITH CHECKED BLOOM FILTER: "
//					+ checkedList.toString());

			float correctRate = (float) candidateList.cardinality() / oldCandidateList.cardinality();

			System.out.println("Correct positive rate is: "+ correctRate);
		}


		System.out.println();
	}
}
