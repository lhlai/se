package se.extractor.algorithm;

import java.util.Comparator;

public class SelectedSort {
//	public static void main(String[] args) {
//	    String[] a = {"aba","bd","123","sgeg","ggs"};
//		int[] b = {12,3,4,5,7};
//        SelectedSort.sort(a,b);
//        show(a,b);
//	}
	public static String[] sort(String[] trimpath,int[] trimcount) {
        int N = trimcount.length;
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i+1; j < N; j++) {
                if (less(trimcount[j], trimcount[min])) min = j;
            }
            exch(trimcount, i, min);
            exch(trimpath, i, min);
            assert isSorted(trimcount, 0, i);
        }
        assert isSorted(trimcount);
		return trimpath;
    }
	public static int[] sort(int[] trimcount) {
        int N = trimcount.length;
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i+1; j < N; j++) {
                if (less(trimcount[j], trimcount[min])) min = j;
            }
            exch(trimcount, i, min);
            assert isSorted(trimcount, 0, i);
        }
        assert isSorted(trimcount);
		return trimcount;
    }
	/***********************************************************************
	  *  Helper sorting functions
	 ***********************************************************************/
	    
	// is v < w ?
	private static boolean less(Comparable v, Comparable w) {
	    return (v.compareTo(w) < 0);
	}

	// is v < w ?
	 private static boolean less(Comparator c, Object v, Object w) {
	    return (c.compare(v, w) < 0);
	 }

	 // exchange a[i] and a[j]
	 private static void exch(int[] a, int i, int j) {
	     int swap = a[i];
	     a[i] = a[j];
	     a[j] = swap;
	 }
	 private static void exch(String[] a, int i, int j) {
	     String swap = a[i];
	     a[i] = a[j];
	     a[j] = swap;
	 }
	 /***********************************************************************
	 *  Check if array is sorted - useful for debugging
	 ***********************************************************************/

	  // is the array a[] sorted?
	  private static boolean isSorted(int[] a) {
	     return isSorted(a, 0, a.length - 1);
	  }      
	  // is the array sorted from a[lo] to a[hi]
	  private static boolean isSorted(int[] a, int lo, int hi) {
	      for (int i = lo + 1; i <= hi; i++)
	          if (less(a[i], a[i-1])) return false;
	      return true;
	  }    
	  // print array to standard output
//	  private static void show(String[] sortpath,int[] sortcount) {
//	      for (int i = 0; i < sortcount.length; i++) {
//	          System.out.println("sortpath"+i+"为:"+sortpath[i]+";其个数为:"+sortcount[i]);
//	      }
//	  }
}
