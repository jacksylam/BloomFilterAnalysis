

import junit.framework.TestCase;

public class JenkinsHashTest extends TestCase {
	
	/**
	 * Test data to validate the hash.
	 */
	private static class TestData {
		String str;
		byte[] bytes;
		long initial;
		long expected;
		public TestData (byte[] bytes, long initial, long expected) {
			this.str = "byte[] { ";
			for (byte one : bytes) {
				this.str += one + ", ";
			}
			this.str += "}";
			this.bytes = bytes;
			this.initial = initial;
			this.expected = expected;
		}
		public TestData (String str, long initial, long expected) {
			this.str = str; 
			this.bytes = new byte[str.length()];
			for (int charCount = 0; charCount < str.length(); charCount++) {
				this.bytes[charCount] = (byte)str.codePointAt(charCount);
			}
			this.initial = initial;
			this.expected = expected;
		}
	}
	
	private static TestData[] tests = new TestData[] {
			new TestData ("hello", 0L, 3070638494L),
			new TestData ("wow", 0L, 627410295L),
			new TestData (new byte[] { 0 }, 0L, 1843378377L),
			new TestData (new byte[] { (byte)255, (byte)128, 64, 1 }, 0L, 3359486273L),
			new TestData ("this is 11c", 0L, 3384459500L),
			new TestData ("this is 12ch", 0L, 313177311L),
			new TestData ("this is >12ch", 0L, 2321813933L),
			new TestData ("this is much large than 12 characters", 0L, 2771373033L),
			
			new TestData ("hello", 3070638494L, 1535955511L),
			new TestData ("wow", 627410295L, 320141986L),
			new TestData (new byte[] { 0 }, 1843378377L, 341630388L),
			new TestData (new byte[] { (byte)255, (byte)128, 64, 1 }, 3359486273L, 2916354366L),
			new TestData ("this is 11c", 3384459500L, 1497460513L),
			new TestData ("this is 12ch", 313177311L, 1671722359L),
			new TestData ("this is >12ch", 2321813933L, 4197822112L),
			new TestData ("this is much large than 12 characters", 2771373033L, 1338302094L),
	};
	
	public void testHashes() {
		JenkinsHash jenkinsHash = new JenkinsHash ();
		for (TestData test : tests) {
			long got = jenkinsHash.hash(test.bytes, test.initial);
			if (got != test.expected) {
				fail("Buffer '" + test.str + "' (len " + test.bytes.length
						+ ") with initial " + test.initial + " expected " + test.expected + " but got " + got);
			}
		}
	}

}