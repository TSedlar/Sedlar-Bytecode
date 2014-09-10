/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.util;

import static me.sedlar.bytecode.Opcode.ATHROW;
import static me.sedlar.bytecode.Opcode.INVOKEDYNAMIC;
import static me.sedlar.bytecode.Opcode.INVOKESTATIC;
import static me.sedlar.bytecode.Opcode.MONITOREXIT;

import java.util.LinkedList;
import java.util.List;

import me.sedlar.bytecode.*;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.structure.flow.BasicBlock;
import me.sedlar.bytecode.tree.NodeTree;
import me.sedlar.bytecode.tree.node.*;
import me.sedlar.bytecode.util.Assembly;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class TreeBuilder {

	public static final int[] CONSUMING_SIZES, PRODUCING_SIZES;

	static {
		CONSUMING_SIZES = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 3, 4, 3, 3, 3, 3, 1, 2, 1, 2,
				3, 2, 3, 4, 2, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 1, 2, 1, 2, 2, 3, 2, 3,
				2, 3, 2, 4, 2, 4, 2, 4, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2, 1, 1, 1, 4, 2, 2, 4, 4, 1, 1, 1, 1,
				1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1,
				1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0 };
		PRODUCING_SIZES = new int[] { 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 0, 0, 1, 2, 1, 2,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 2, 1, 1, 1, 1, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3,
				4, 4, 5, 6, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
				1, 2, 1, 2, 1, 2, 1, 2, 0, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
				1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 };
	}

	/**
	 * Gets the <tt>TreeSize</tt> for the given instruction.
	 * 
	 * @param ain
	 *            The instruction to get the <tt>TreeSize</tt> for.
	 * @return the <tt>TreeSize</tt> for the given instructions.
	 */
	public static TreeSize getTreeSize(AbstractInstruction ain) {
		int c = 0, p = 0;
		if (ain instanceof SimpleInstruction || ain instanceof PushInstruction
				|| ain instanceof VariableInstruction || ain instanceof BranchInstruction
				|| ain instanceof TableSwitchInstruction || ain instanceof LookupSwitchInstruction) {
			c = CONSUMING_SIZES[ain.opcode().id()];
			p = PRODUCING_SIZES[ain.opcode().id()];
		} else if (ain instanceof FieldInstruction) {
			FieldInstruction field = (FieldInstruction) ain;
			char d = field.descriptor().charAt(0);
			switch (field.opcode()) {
				case GETFIELD: {
					c = 1;
					p = d == 'D' || d == 'J' ? 2 : 1;
					break;
				}
				case GETSTATIC: {
					c = 0;
					p = d == 'D' || d == 'J' ? 2 : 1;
					break;
				}
				case PUTFIELD: {
					c = d == 'D' || d == 'J' ? 3 : 2;
					p = 0;
					break;
				}
				case PUTSTATIC: {
					c = d == 'D' || d == 'J' ? 2 : 1;
					p = 0;
					break;
				}
				default: {
					c = 0;
					p = 0;
					break;
				}
			}
		} else if (ain instanceof MethodInstruction) {
			MethodInstruction method = (MethodInstruction) ain;
			int as = Assembly.getArgumentsAndReturnSizes(method.descriptor());
			c = (as >> 2) - (method.opcode() == INVOKEDYNAMIC || method.opcode() == INVOKESTATIC ? 1 : 0);
			p = as & 0x03;
		} else if (ain instanceof ConstantInstruction) {
			Object constant = ((ConstantInstruction) ain).constant();
			p = constant instanceof Double || constant instanceof Long ? 2 : 1;
		} else if (ain instanceof MultianewarrayInstruction) {
			c = ((MultianewarrayInstruction) ain).dimensions();
			p = 1;
		}
		return new TreeSize(c, p);
	}

	private static AbstractNode createNode(AbstractInstruction instruction, NodeTree tree, TreeSize size) {
		if (instruction instanceof PushInstruction) {
			return new PushNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof VariableInstruction) {
			return new VariableNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof FieldInstruction) {
			return new FieldNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof MethodInstruction) {
			return new MethodNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof ConstantInstruction) {
			return new ConstantNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof IncrementInstruction) {
			return new IncrementNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof TypeInstruction) {
			return new TypeNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof InvokeDynamicInstruction) {
			return new InvokeDynamicNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof InvokeInterfaceInstruction) {
			return new InvokeInterfaceNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof MultianewarrayInstruction) {
			return new MultianewArrayNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof BranchInstruction) {
			return new BranchNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof WideBranchInstruction) {
			return new WideBranchNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof LookupSwitchInstruction) {
			return new LookupSwitchNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof TableSwitchInstruction) {
			return new TableSwitchNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof PaddedInstruction) {
			return new PaddedNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof ImmediateByteInstruction) {
			return new ImmediateByteNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof ImmediateShortInstruction) {
			return new ImmediateShortNode(tree, instruction, size.consuming, size.producing);
		} else if (instruction instanceof SimpleInstruction) {
			return new SimpleNode(tree, instruction, size.consuming, size.producing);
		}
		return new AbstractNode(tree, instruction, size.consuming, size.producing);
	}

	private static int treeIndex = -1;

	private static AbstractNode iterate(List<AbstractNode> nodes) {
		if (treeIndex < 0)
			return null;
		AbstractNode node = nodes.get(treeIndex--);
		if (node.consuming == 0)
			return node;
		int c = node.consuming;
		while (c != 0) {
			AbstractNode n = iterate(nodes);
			if (n == null)
				break;
			Opcode op = n.opcode();
			if (op == MONITOREXIT && node.opcode() == ATHROW)
				n.producing = 1;
			node.addFirst(n);
			int cr = c - n.producing;
			if (cr < 0) {
				node.producing += -cr;
				n.producing = 0;
				break;
			}
			c -= n.producing;
			n.producing = 0;
		}
		return node;
	}

	/**
	 * Gets the NodeTree for the list of given instructions.
	 * 
	 * @param instructions
	 *            the instructions to construct a NodeTree for.
	 * @return the NodeTree for the given instructions.
	 */
	public static NodeTree build(List<AbstractInstruction> instructions) {
		if (instructions.isEmpty())
			return null;
		NodeTree tree = new NodeTree(instructions.get(0).methodInfo());
		List<AbstractNode> nodes = new LinkedList<>();
		instructions.forEach(ai -> nodes.add(createNode(ai, tree, getTreeSize(ai))));
		treeIndex = nodes.size() - 1;
		AbstractNode node;
		while ((node = iterate(nodes)) != null)
			tree.addFirst(node);
		return tree;
	}

	/**
	 * Gets the NodeTree for the given method.
	 *
	 * @param method
	 *            The method to construct a NodeTree for.
	 * @return the NodeTree for the given method.
	 */
	public static NodeTree build(MethodInfo method) {
		return build(method.instructions());
	}

	/**
	 * Gets the NodeTree for the given block.
	 *
	 * @param block
	 *            The block to construct a NodeTree for.
	 * @return the NodeTree for the given method.
	 */
	public static NodeTree build(BasicBlock block) {
		return build(block.instructions());
	}
}