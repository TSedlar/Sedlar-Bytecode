/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree;

import java.util.Arrays;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.BranchInstruction;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.filter.AbstractNodeFilter;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class NodeTree extends AbstractNode {

	private final MethodInfo method;

	public NodeTree(MethodInfo method) {
		super(null, null, -1, -1);
		this.method = method;
	}

	/**
	 * Gets this tree's method (parent)
	 *
	 * @return this tree's method (parent)
	 */
	public MethodInfo method() {
		return method;
	}

	/**
	 * Gets the consuming instructions.
	 *
	 * @return the consuming instructions.
	 */
	public AbstractInstruction[] consuming() {
		AbstractInstruction[] instructions = super.consuming();
		int i = instructions.length > 1 && instructions[instructions.length - 2] instanceof BranchInstruction ? 2
				: 1;
		return Arrays.copyOf(instructions, instructions.length - i);
	}

	/**
	 * Checks if the given filter matches this tree.
	 *
	 * @param filter
	 *            The filter to check this tree against.
	 */
	@Override
	public boolean accept(AbstractNodeFilter filter) {
		for (AbstractNode node : this) {
			if (accept(filter, node))
				return true;
		}
		return false;
	}

	private boolean accept(AbstractNodeFilter filter, AbstractNode an) {
		if (an.accept(filter))
			return true;
		for (AbstractNode node : an) {
			if (accept(filter, node))
				return true;
		}
		return false;
	}
}
