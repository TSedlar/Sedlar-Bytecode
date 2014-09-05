/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.PushInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class PushInstructionFilter extends InstructionFilter {

	@Override
	public boolean accept(AbstractInstruction ai) {
		return ai instanceof PushInstruction && accept((PushInstruction) ai);
	}

	public abstract boolean accept(PushInstruction pi);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param value
	 *            The value to match against
	 * @return a filter matching the given parameters.
	 */
	public static PushInstructionFilter create(final int value) {
		return new PushInstructionFilter() {
			public boolean accept(PushInstruction pi) {
				return pi.value() == value;
			}
		};
	}

	/**
	 * Creates a filter matching a PushInstruction.
	 * 
	 * @return a filter matching a PushInstruction.
	 */
	public static PushInstructionFilter create() {
		return new PushInstructionFilter() {
			public boolean accept(PushInstruction pi) {
				return true;
			}
		};
	}
}
