package com.jdon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinTestSample {
	private SampleAppTest sampleAppTest;

	private final int ACCEPTABLE_SIZE;
	private final List<String> letters;
	private final char key;
	private final int start;
	private final int stop;

	public ForkJoinTestSample(SampleAppTest sampleAppTest, int ACCEPTABLE_SIZE,
			List<String> letters, char key, int start, int stop) {
		super();
		this.sampleAppTest = sampleAppTest;

		this.ACCEPTABLE_SIZE = ACCEPTABLE_SIZE;
		this.letters = letters;
		this.key = key;
		this.start = start;
		this.stop = stop;
	}

	public List<String> getLetters() {
		return letters;
	}

	public char getKey() {
		return key;
	}

	public int getStart() {
		return start;
	}

	public int getStop() {
		return stop;
	}

	public int getACCEPTABLE_SIZE() {
		return ACCEPTABLE_SIZE;
	}
	
	

	public SampleAppTest getSampleAppTest() {
		return sampleAppTest;
	}

	public void setSampleAppTest(SampleAppTest sampleAppTest) {
		this.sampleAppTest = sampleAppTest;
	}

	public void testJdon() {
//		sampleAppTest.testCommand();

		sampleAppTest.testCQRS();
//		sampleAppTest.testDomainEvent();
//		sampleAppTest.testDomainEventSimple();
//		sampleAppTest.testEvent();
//		sampleAppTest.testOnEvent();
//		sampleAppTest.testGetService();

	}

	public static void main(String[] args) {
		int ARRAY_SIZE = 800; // 8000000;
		int ACCEPTABLE_SIZE = 100; // 100000;
		List<String> letters = new ArrayList<String>(ARRAY_SIZE);
		final char key = 'A';
		// fill the big array with A-Z randomly
		for (int i = 0; i < ARRAY_SIZE; i++) {
			char t = (char) (Math.random() * 26 + 65);
			letters.add(String.valueOf(t)); // A-Z
		}

		SampleAppTest sampleAppTest = new SampleAppTest();
		try {
			sampleAppTest.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTestSample forkJoinTestSample = new ForkJoinTestSample(
				sampleAppTest, ACCEPTABLE_SIZE, letters, key, 0, letters.size());
		ForkJoinTestJdon ft = new ForkJoinTestJdon(forkJoinTestSample);
		int count = pool.invoke(ft);
		System.out.print("\n found count " + count);

		sampleAppTest.tearDown();
		pool.shutdown();

	}

}
