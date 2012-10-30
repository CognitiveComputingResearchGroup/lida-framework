package edu.memphis.ccrg.lida.actionselection.behaviornetwork;

public class OrderingTest {

	public static void main(String[] args) {
		new OrderingTest().go();
	}

	private Long[] src = new Long[10];

	public void go() {
		for (long i = 0; i < src.length; i++) {
			src[(int) i] = i;
		}
		printIt(src);
		printIt(getRandomOrdering());
		printIt(getRandomOrdering());
		printIt(getRandomOrdering());
		printIt(getRandomOrdering());
	}

	private Long[] getRandomOrdering() {
		Long[] keys = src.clone();
		for (int i = 0; i < keys.length - 1; i++) {
			int swapPosition = (int) (Math.random() * (keys.length - i)) + i;
			Long stored = keys[i];
			keys[i] = keys[swapPosition];
			keys[swapPosition] = stored;
		}
		return keys;
	}

	private void printIt(Long[] a) {
		// for(int i = 0; i < a.length; i++){
		// System.out.print(a[i] + " ");
		// }
		for (Long l : a) {
			System.out.print(l + " ");
		}

		System.out.println();
	}

}
