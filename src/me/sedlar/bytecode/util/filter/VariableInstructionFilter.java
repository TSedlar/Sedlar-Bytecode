/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.VariableInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class VariableInstructionFilter extends InstructionFilter {

	@Override
	public boolean accept(AbstractInstruction ai) {
		return ai instanceof VariableInstruction && accept((VariableInstruction) ai);
	}

	public abstract boolean accept(VariableInstruction vi);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param variable
	 *            The variable number
	 * @return a filter matching the given parameters.
	 */
	public static VariableInstructionFilter create(final int variable) {
		return new VariableInstructionFilter() {
			public boolean accept(VariableInstruction vi) {
				return vi.variable() == variable;
			}
		};
	}

	/**
	 * Creates a filter matching a VariableInstruction
	 * 
	 * @return a filter matching a VariableInstruction
	 */
	public static VariableInstructionFilter create() {
		return new VariableInstructionFilter() {
			public boolean accept(VariableInstruction vi) {
				return true;
			}
		};
	}
}
