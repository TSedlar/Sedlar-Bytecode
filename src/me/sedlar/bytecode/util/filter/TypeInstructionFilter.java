/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.TypeInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class TypeInstructionFilter extends InstructionFilter {

	@Override
	public boolean accept(AbstractInstruction ai) {
		return ai instanceof TypeInstruction && accept((TypeInstruction) ai);
	}

	public abstract boolean accept(TypeInstruction ti);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param type
	 *            The type descriptor
	 * @return a filter matching the given parameters.
	 */
	public static TypeInstructionFilter create(final String type) {
		return new TypeInstructionFilter() {
			public boolean accept(TypeInstruction ti) {
				return ti.type().equals(type);
			}
		};
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param type
	 *            A regex pattern matching the type's descriptor
	 * @return a filter matching the given parameters.
	 */
	public static TypeInstructionFilter createRegex(final String regex) {
		return new TypeInstructionFilter() {
			public boolean accept(TypeInstruction ti) {
				return ti.type().matches(regex);
			}
		};
	}

	/**
	 * Creates a filter matching a TypeInstruction.
	 * 
	 * @return a filter matching a TypeInstruction.
	 */
	public static TypeInstructionFilter create() {
		return new TypeInstructionFilter() {
			public boolean accept(TypeInstruction ti) {
				return true;
			}
		};
	}
}
