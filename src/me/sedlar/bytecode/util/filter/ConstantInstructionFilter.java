/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.ConstantInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class ConstantInstructionFilter extends InstructionFilter {

	@Override
	public boolean accept(AbstractInstruction ai) {
		return ai instanceof ConstantInstruction && accept((ConstantInstruction) ai);
	}

	public abstract boolean accept(ConstantInstruction ci);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param constant
	 *            the constant object to match against.
	 * @return a filter matching the given parameters.
	 */
	public static ConstantInstructionFilter create(final Object object) {
		return new ConstantInstructionFilter() {
			public boolean accept(ConstantInstruction ci) {
				return ci.constant() == object;
			}
		};
	}

	/**
	 * Creates a filter matching a ConstantInstruction.
	 * 
	 * @return a filter matching a ConstantInstruction.
	 */
	public static ConstantInstructionFilter create() {
		return new ConstantInstructionFilter() {
			public boolean accept(ConstantInstruction ci) {
				return true;
			}
		};
	}
}
