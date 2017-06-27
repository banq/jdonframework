package com.jdon;

import java.util.concurrent.RecursiveTask;

public class ForkJoinTestJdon extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;

	private final ForkJoinTestSample forkJoinTestSample;

	public ForkJoinTestJdon(ForkJoinTestSample forkJoinTestSample) {
		this.forkJoinTestSample = forkJoinTestSample;
	}

	@Override
	protected Integer compute() {
		int start = forkJoinTestSample.getStart();
		int stop = forkJoinTestSample.getStop();
		int workLoadSize = stop - start;
		int ACCEPTABLE_SIZE = forkJoinTestSample.getACCEPTABLE_SIZE();
		if (workLoadSize <= ACCEPTABLE_SIZE) {
			int count = 0;
			for (int i = start; i < stop; i++) {
//				String s = (String) forkJoinTestSample.getLetters().get(i);
//				if (String.valueOf(forkJoinTestSample.getKey()).equals(s)) {
					count++;
					forkJoinTestSample.testJdon();
					System.out.print("\n ################# ok= " + count);
//				}
			}
			return count;

		} else {
			int mid = start + workLoadSize / 2;

			ForkJoinTestJdon left = new ForkJoinTestJdon(
					new ForkJoinTestSample(
							forkJoinTestSample.getSampleAppTest(),
							forkJoinTestSample.getACCEPTABLE_SIZE(),
							forkJoinTestSample.getLetters(),
							forkJoinTestSample.getKey(), start, mid));
			ForkJoinTestJdon right = new ForkJoinTestJdon(
					new ForkJoinTestSample(
							forkJoinTestSample.getSampleAppTest(),
							forkJoinTestSample.getACCEPTABLE_SIZE(),
							forkJoinTestSample.getLetters(),
							forkJoinTestSample.getKey(), mid, stop));

			// fork (push to queue)-> compute -> join
			invokeAll(left, right);
			int countr = right.join();
			int countl = left.join();
			return countr + countl;

		}

	}

}
