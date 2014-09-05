/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.BranchInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class BranchInstructionFilter extends InstructionFilter {

	@Override
	public boolean accept(AbstractInstruction ai) {
		return ai instanceof BranchInstruction && accept((BranchInstruction) ai);
	}

	public abstract boolean accept(BranchInstruction bi);

	/**
	 * Creates a filter matching a BranchInstruction.
	 * 
	 * @return a filter matching a BranchInstruction.
	 */
	public static BranchInstructionFilter create() {
		return new BranchInstructionFilter() {
			public boolean accept(BranchInstruction bi) {
				return true;
			}
		};
	}
}
