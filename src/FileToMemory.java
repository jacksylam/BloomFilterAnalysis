import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Utility class to store inverted index file into memory.
 * 
 * @author Jack
 * 
 */
public class FileToMemory {

	public FileToMemory() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Store inverted index file into memory.
	 * 
	 * @param file
	 *            The file location
	 * @return The inverted index.
	 */
	public static ArrayList<Term> fileToMemory(Path file) {
		ArrayList<Term> ret = new ArrayList<Term>();
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
			
				String word = line.substring(0, line.indexOf("|"));
				Term term = new Term(word);

				line = line.substring(line.indexOf("|") + 1);
				String[] entries = line.split(";");
				for (int i = 0; i < entries.length; i++) {
					
					int docID = Integer.parseInt(entries[i].substring(0,
							entries[i].indexOf(":")));

					entries[i] = entries[i]
							.substring(entries[i].indexOf(":") + 1);
					String[] position = entries[i].split(",");
					ArrayList<Integer> positionInt = new ArrayList<>();
					for (String number : position) {
						positionInt.add(Integer.parseInt(number));
					}

					term.addPosting(new Posting(word, docID, positionInt));
				}
				ret.add(term);
				term.processPosting();

			}
			reader.close();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		
		return ret;
	}

}
