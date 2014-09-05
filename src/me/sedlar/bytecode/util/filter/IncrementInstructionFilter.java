/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.IncrementInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class IncrementInstructionFilter extends InstructionFilter {

	@Override
	public boolean accept(AbstractInstruction ai) {
		return ai instanceof IncrementInstruction && accept((IncrementInstruction) ai);
	}

	public abstract boolean accept(IncrementInstruction ii);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param increment
	 *            The increment number
	 * @return a filter matching the given parameters.
	 */
	public static IncrementInstructionFilter create(final int increment) {
		return new IncrementInstructionFilter() {
			public boolean accept(IncrementInstruction ii) {
				return ii.increment() == increment;
			}
		};
	}

	/**
	 * Creates a filter matching an IncrementInstruction
	 * 
	 * @return a filter matching an IncrementInstruction
	 */
	public static IncrementInstructionFilter create() {
		return new IncrementInstructionFilter() {
			public boolean accept(IncrementInstruction ii) {
				return true;
			}
		};
	}
}
