package me.sedlar.util;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @since Sep 9, 2014 - 3:20:58 PM
 **/
public class MeasurableInteger {

	private final int i;

	public MeasurableInteger(int i) {
		this.i = i;
	}
	
	public int value() {
		return i;
	}

	public boolean greater(int x) {
		return i > x;
	}

	public boolean less(int x) {
		return i < x;
	}

	public boolean equals(int x) {
		return i == x;
	}

	public boolean within(int min, int max) {
		return i >= min && i <= max;
	}
	
	@Override
	public String toString() {
		return Integer.toString(i);
	}
}