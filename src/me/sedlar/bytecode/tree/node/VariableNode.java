/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.VariableInstruction;
import me.sedlar.bytecode.tree.NodeTree;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class VariableNode extends AbstractNode {

	public VariableNode(NodeTree tree, AbstractInstruction insn, int consuming, int producing) {
		super(tree, insn, consuming, producing);
	}

	@Override
	public VariableInstruction insn() {
		return (VariableInstruction) insn;
	}
}
