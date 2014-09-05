/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.MethodInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class MethodInstructionFilter extends InstructionFilter {

	@Override
	public boolean accept(AbstractInstruction ai) {
		return ai instanceof MethodInstruction && accept((MethodInstruction) ai);
	}

	public abstract boolean accept(MethodInstruction mi);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param owner
	 *            The field's owner or null to match any
	 * @param name
	 *            The field's name or null to match any
	 * @param descriptor
	 *            The field's descriptor or null to match any
	 * @return a filter matching the givenr parameters.
	 */
	public static MethodInstructionFilter create(final String owner, final String name, final String descriptor) {
		return new MethodInstructionFilter() {
			public boolean accept(MethodInstruction fi) {
				if (owner == null || fi.owner().equals(owner)) {
					if (name == null || fi.name().equals(name))
						return descriptor == null || fi.descriptor().equals(descriptor);
				}
				return false;
			}
		};
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param owner
	 *            The field's owner or null to match any
	 * @param descriptor
	 *            The field' descriptor or null to match any
	 * @return a filter matching the given parameters.
	 */
	public static MethodInstructionFilter create(final String owner, final String descriptor) {
		return create(owner, null, descriptor);
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param descriptor
	 *            The field's descriptor or null to match any
	 * @return a filter matching the givenr parameters.
	 */
	public static MethodInstructionFilter create(final String descriptor) {
		return create(null, descriptor);
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param descriptor
	 *            A regex pattern matching the field's descriptor or null to
	 *            match any
	 * @return a filter matching the given parameters.
	 */
	public static MethodInstructionFilter createRegex(final String descriptor) {
		return new MethodInstructionFilter() {
			public boolean accept(MethodInstruction fi) {
				return descriptor == null || fi.descriptor().matches(descriptor);
			}
		};
	}

	/**
	 * Creates a filter matching a MethodInstruction.
	 * 
	 * @return a filter matching a MethodInstruction.
	 */
	public static MethodInstructionFilter create() {
		return new MethodInstructionFilter() {
			public boolean accept(MethodInstruction mi) {
				return true;
			}
		};
	}
}
