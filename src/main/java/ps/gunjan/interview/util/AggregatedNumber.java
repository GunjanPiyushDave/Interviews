package ps.gunjan.interview.util;

/**
 * aggregated numbers these are types of numbers where if a number if divided
 * into multiple parts it follows a particular rule i.e. sum of previous parts
 * is equal to later part e.g.1 112358 = 1+1=2; 1+2=3; 2+3=5; 3+5=8 e.g.2
 * 122436: 12+24=36 e.g.3 1299111210: 12+99=111; 99+111=210 e.g.4 112112224:
 * 112+112=224 so can you provide a function which can decide whether an input
 * number if aggregate number
 * 
 * @author DipsJix
 * 
 */
public class AggregatedNumber {

	public static String check(int n) {

		System.out.println("Start Passed Number to check: "+ n);
		int l = MathUtils.getDigits10(n);

		for (int i = 0; i < (double) l / 2 - 1; i++) {

			for (int j = i + 1; j < l && l - j > i + 1 && l - j > (j - i); j++) {

				int n1 = getNumber(n, l, 0, i);
				int n2 = getNumber(n, l, i, j);
				System.out.println("[" + i + "] " + getNumber(n, l, 0, i) + " "
						+ getNumber(n, l, i, j));

				int ll = MathUtils.getDigits10(n1 + n2);
				int p = j;
				while (ll + p <= l) {
					ll = MathUtils.getDigits10(n1 + n2);
					int nextInt = getNumber(n, l, p, p + ll);
					if (n1 + n2 == nextInt) {
						System.out
								.println((n1 + n2) + " match with " + nextInt);
						n1 = n2;
						n2 = nextInt;
						p += ll;
						if (p == l - 1) {
							System.out.println(" match! ");
							System.out.println("End check for : "+ n+ " Result is MATCH");
							return "match";
						}
					} else {
						System.out.println((n1 + n2) + " mismatch with "
								+ nextInt);
						break;
					}
				}

			}
		}
		System.out.println("End check for : "+ n+ " Result is MIS-MATCH");
		return "mismatch";
	}

	public static  int getNumber(int n, int l, int i, int j) {
		int n1Digits = (int) Math.pow(10, (l - i - 1));
		int n1 = n / n1Digits;
		if (i == j) {
			return n1;
		} else {
			return (n - n1 * n1Digits) / (int) Math.pow(10, (l - j - 1));
		}
	}
}
