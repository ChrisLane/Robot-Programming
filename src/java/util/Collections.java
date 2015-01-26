package java.util;

public class Collections {
	private static final Random RND = new Random();

	private static <E extends Comparable<? super E>> void swap(List<E> l, int i, int j) {
		E tmp = l.get(i);
		l.set(i, l.get(j));
		l.set(j, tmp);
	}
	private static <E extends Comparable<? super E>> int partition(List<E> l, int begin, int end) {
		int index = begin + RND.nextInt(end - begin + 1);
		E pivot = l.get(index);
		swap(l, index, end);
		for (int i = index = begin; i < end; ++i) {
			if (l.get(i).compareTo(pivot) <= 0) {
				swap(l, index++, i);
			}
		}
		swap(l, index, end);
		return (index);
	}
	private static <E extends Comparable<? super E>> void qsort(List<E> l, int begin, int end) {
		if (end > begin) {
			int index = partition(l, begin, end);
			qsort(l, begin, index - 1);
			qsort(l, index + 1, end);
		}
	}
	public static <E extends Comparable<? super E>> void sort(List<E> l) {
		qsort(l, 0, l.size() - 1);
	}
}
