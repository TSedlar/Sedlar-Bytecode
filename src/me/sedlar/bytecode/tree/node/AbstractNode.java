/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.Opcode;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.tree.NodeTree;
import me.sedlar.bytecode.tree.Tree;
import me.sedlar.bytecode.tree.node.filter.AbstractNodeFilter;
import me.sedlar.bytecode.util.Assembly;
import me.sedlar.util.collection.QueryableList;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class AbstractNode extends Tree<AbstractNode> {

	public int consuming, producing;

	protected NodeTree tree;
	protected AbstractInstruction insn;
	protected int produceCount;

	private AbstractInstruction[] instructions;

	public AbstractNode(NodeTree tree, AbstractInstruction insn, int consuming, int producing) {
		this.tree = tree;
		this.insn = insn;
		this.consuming = consuming;
		this.producing = (produceCount = producing);
	}

	/**
	 * Gets the instruction paired with this node.
	 *
	 * @return the instruction paired with this node.
	 */
	public AbstractInstruction insn() {
		return insn;
	}

	/**
	 * Gets the opcode for this node's instruction
	 *
	 * @return the opcode for this node.
	 */
	public Opcode opcode() {
		return insn != null ? insn.opcode() : null;
	}

	/**
	 * Gets the consuming instructions.
	 *
	 * @return the consuming instructions.
	 */
	public AbstractInstruction[] consuming() {
		if (instructions != null)
			return instructions;
		instructions = new AbstractInstruction[total()];
		int i = 0;
		for (AbstractNode n : this) {
			AbstractInstruction[] nodes = n.consuming();
			System.arraycopy(nodes, 0, instructions, i, nodes.length);
			i += nodes.length;
		}
		if (instructions.length - i != 1)
			throw new RuntimeException();
		instructions[i] = insn();
		return instructions;
	}

	/**
	 * Gets the producing nodes.
	 *
	 * @return the producing nodes.
	 */
	public AbstractNode[] producing() {
		AbstractNode[] nodes = new AbstractNode[size()];
		int i = 0;
		for (AbstractNode n : this) {
			if (n.produceCount > 0)
				nodes[i++] = n;
		}
		return Arrays.copyOf(nodes, i);
	}

	@Override
	public String toString() {
		return toString(1);
	}

	private String toString(int tab) {
		StringBuilder sb = new StringBuilder();
		sb.append(Assembly.toString(insn));
		for (AbstractNode n : this) {
			sb.append('\n');
			for (int i = 0; i < tab; i++)
				sb.append('\t');
			sb.append(n.toString(tab + 1));
		}
		return sb.toString();
	}

	/**
	 * Gets the total amount nodes within this subtree.
	 *
	 * @return the total amount of nodes within this subtree.
	 */
	public int total() {
		int size = 1;
		for (AbstractNode n : this)
			size += n.total();
		return size;
	}

	/**
	 * Gets the amount of children within this subtree.
	 *
	 * @return the amount of children within this subtree.
	 */
	public int children() {
		return producing().length;
	}

	/**
	 * Gets the tree this node is within.
	 *
	 * @return the tree this node is within.
	 */
	public NodeTree tree() {
		return tree;
	}

	/**
	 * Gets the method this node is in.
	 *
	 * @return the method this node is in.
	 */
	public MethodInfo method() {
		return insn.methodInfo();
	}

	/**
	 * Gets the index of this node's instruction.
	 *
	 * @return the index of this node's instruction.
	 */
	public int index() {
		return method().instructions().indexOf(insn);
	}

	/**
	 * Checks if this node is valid against the given filter.
	 *
	 * @param filter
	 *            The filter to check this node against.
	 */
	public boolean accept(AbstractNodeFilter filter) {
		return filter.validate(this);
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof AbstractNode && insn.equals(((AbstractNode) object).insn);
	}

	public AbstractNode first() {
		return first((Opcode) null);
	}

	public AbstractNode first(Opcode opcode) {
		for (AbstractNode n : this) {
			if (opcode == null || n.opcode() == opcode)
				return n;
		}
		return null;
	}

	public AbstractNode find(Opcode opcode, int index) {
		int i = 0;
		for (AbstractNode n : this) {
			if (n.opcode() == opcode) {
				if (i++ == index)
					return n;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractNode> T first(Class<? extends AbstractNode> clazz) {
		for (AbstractNode n : this) {
			if (n.getClass().equals(clazz))
				return (T) n;
		}
		return null;
	}

	/**
	 * Gets the first BranchNode in this node's tree.
	 * 
	 * @return the first <tt>BranchNode</tt> in this node's tree.
	 */
	public BranchNode firstBranch() {
		return first(BranchNode.class);
	}

	/**
	 * Gets the first ConstantNode in this node's tree.
	 * 
	 * @return the first <tt>ConstantNode</tt> in this node's tree.
	 */
	public ConstantNode firstConstant() {
		return first(ConstantNode.class);
	}

	/**
	 * Gets the first FieldNode in this node's tree.
	 * 
	 * @return the first <tt>FieldNode</tt> in this node's tree.
	 */
	public FieldNode firstField() {
		return first(FieldNode.class);
	}

	/**
	 * Gets the first ImmediateByteNode in this node's tree.
	 * 
	 * @return the first <tt>ImmediateByteNode</tt> in this node's tree.
	 */
	public ImmediateByteNode firstImmediateByte() {
		return first(ImmediateByteNode.class);
	}

	/**
	 * Gets the first ImmediateShortNode in this node's tree.
	 * 
	 * @return the first <tt>ImmediateShortNode</tt> in this node's tree.
	 */
	public ImmediateShortNode firstImmediateShort() {
		return first(ImmediateShortNode.class);
	}

	/**
	 * Gets the first IncrementNode in this node's tree.
	 * 
	 * @return the first <tt>IncrementNode</tt> in this node's tree.
	 */
	public IncrementNode firstIncrement() {
		return first(IncrementNode.class);
	}

	/**
	 * Gets the first InvokeDynamicNode in this node's tree.
	 * 
	 * @return the first <tt>InvokeDynamicNode</tt> in this node's tree.
	 */
	public InvokeDynamicNode firstInvokeDynamic() {
		return first(InvokeDynamicNode.class);
	}

	/**
	 * Gets the first InvokeInterfaceNode in this node's tree.
	 * 
	 * @return the first <tt>InvokeInterfaceNode</tt> in this node's tree.
	 */
	public InvokeInterfaceNode firstInvokeInterface() {
		return first(InvokeInterfaceNode.class);
	}

	/**
	 * Gets the first LookupSwitchNode in this node's tree.
	 * 
	 * @return the first <tt>LookupSwitchNode</tt> in this node's tree.
	 */
	public LookupSwitchNode firstLookupSwitch() {
		return first(LookupSwitchNode.class);
	}

	/**
	 * Gets the first MethodNode in this node's tree.
	 * 
	 * @return the first <tt>MethodNode</tt> in this node's tree.
	 */
	public MethodNode firstMethod() {
		return first(MethodNode.class);
	}

	/**
	 * Gets the first MultianewArrayNode in this node's tree.
	 * 
	 * @return the first <tt>MultianewArrayNode</tt> in this node's tree.
	 */
	public MultianewArrayNode firstMultianewArray() {
		return first(MultianewArrayNode.class);
	}

	/**
	 * Gets the first PaddedNode in this node's tree.
	 * 
	 * @return the first <tt>PaddedNode</tt> in this node's tree.
	 */
	public PaddedNode firstPad() {
		return first(PaddedNode.class);
	}

	/**
	 * Gets the first PushNode in this node's tree.
	 * 
	 * @return the first <tt>PushNode</tt> in this node's tree.
	 */
	public PushNode firstPush() {
		return first(PushNode.class);
	}

	/**
	 * Gets the first SimpleNode in this node's tree.
	 * 
	 * @return the first <tt>SimpleNode</tt> in this node's tree.
	 */
	public SimpleNode firstSimple() {
		return first(SimpleNode.class);
	}

	/**
	 * Gets the first TableSwitchNode in this node's tree.
	 * 
	 * @return the first <tt>TableSwitchNode</tt> in this node's tree.
	 */
	public TableSwitchNode firstTableSwitch() {
		return first(TableSwitchNode.class);
	}

	/**
	 * Gets the first TypeNode in this node's tree.
	 * 
	 * @return the first <tt>TypeNode</tt> in this node's tree.
	 */
	public TypeNode firstType() {
		return first(TypeNode.class);
	}

	/**
	 * Gets the first VariableNode in this node's tree.
	 * 
	 * @return the first <tt>VariableNode</tt> in this node's tree.
	 */
	public VariableNode firstVariable() {
		return first(VariableNode.class);
	}

	/**
	 * Gets the first WideBranchNode in this node's tree.
	 * 
	 * @return the first <tt>WideBranchNode</tt> in this node's tree.
	 */
	public WideBranchNode firstWideBranch() {
		return first(WideBranchNode.class);
	}

	/**
	 * Gets the next node matching the given class and max distance.
	 * 
	 * @param clazz
	 *            the class to match
	 * @param max
	 *            the maximum distance to search within
	 * @return the next node matching the given class and max distance
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractNode> T next(Class<? extends AbstractNode> clazz, int max) {
		int i = 0;
		AbstractNode next = this;
		while ((next = next.next()) != null && (max == -1 || i++ < max)) {
			if (next.getClass().equals(clazz))
				return (T) next;
		}
		return null;
	}

	/**
	 * Gets the next node matching the given class.
	 * 
	 * @param clazz
	 *            the class to match
	 * @return the next node matching the given class
	 */
	public <T extends AbstractNode> T next(Class<? extends AbstractNode> clazz) {
		return next(clazz, -1);
	}

	/**
	 * Gets the next BranchNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>BranchNode<tt> within the given max distance.
	 */
	public BranchNode nextBranch(int max) {
		return next(BranchNode.class, max);
	}

	/**
	 * Gets the next BranchNode.
	 * 
	 * @return the next <tt>BranchNode<tt>.
	 */
	public BranchNode nextBranch() {
		return next(BranchNode.class);
	}

	/**
	 * Gets the next ConstantNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>ConstantNode<tt> within the given max distance.
	 */
	public ConstantNode nextConstant(int max) {
		return next(ConstantNode.class, max);
	}

	/**
	 * Gets the next ConstantNode.
	 * 
	 * @return the next <tt>ConstantNode<tt>.
	 */
	public ConstantNode nextConstant() {
		return next(ConstantNode.class);
	}

	/**
	 * Gets the next FieldNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>FieldNode<tt> within the given max distance.
	 */
	public FieldNode nextField(int max) {
		return next(FieldNode.class, max);
	}

	/**
	 * Gets the next FieldNode.
	 * 
	 * @return the next <tt>FieldNode<tt>.
	 */
	public FieldNode nextField() {
		return next(FieldNode.class);
	}

	/**
	 * Gets the next ImmediateByteNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>ImmediateByteNode<tt> within the given max distance.
	 */
	public ImmediateByteNode nextImmediateByte(int max) {
		return next(ImmediateByteNode.class, max);
	}

	/**
	 * Gets the next ImmediateByteNode.
	 * 
	 * @return the next <tt>ImmediateByteNode<tt>.
	 */
	public ImmediateByteNode nextImmediateByte() {
		return next(ImmediateByteNode.class);
	}

	/**
	 * Gets the next ImmediateShortNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next
	 *         <tt>ImmediateShortNode<tt> within the given max distance.
	 */
	public ImmediateShortNode nextImmediateShort(int max) {
		return next(ImmediateShortNode.class, max);
	}

	/**
	 * Gets the next ImmediateShortNode.
	 * 
	 * @return the next <tt>ImmediateShortNode<tt>.
	 */
	public ImmediateShortNode nextImmediateShort() {
		return next(ImmediateShortNode.class);
	}

	/**
	 * Gets the next IncrementNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>IncrementNode<tt> within the given max distance.
	 */
	public IncrementNode nextIncrement(int max) {
		return next(IncrementNode.class, max);
	}

	/**
	 * Gets the next IncrementNode.
	 * 
	 * @return the next <tt>IncrementNode<tt>.
	 */
	public IncrementNode nextIncrement() {
		return next(IncrementNode.class);
	}

	/**
	 * Gets the next InvokeDynamicNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>InvokeDynamicNode<tt> within the given max distance.
	 */
	public InvokeDynamicNode nextInvokeDynamic(int max) {
		return next(InvokeDynamicNode.class, max);
	}

	/**
	 * Gets the next InvokeDynamicNode.
	 * 
	 * @return the next <tt>InvokeDynamicNode<tt>.
	 */
	public InvokeDynamicNode nextInvokeDynamic() {
		return next(InvokeDynamicNode.class);
	}

	/**
	 * Gets the next InvokeInterfaceNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next
	 *         <tt>InvokeInterfaceNode<tt> within the given max distance.
	 */
	public InvokeInterfaceNode nextInvokeInterface(int max) {
		return next(InvokeInterfaceNode.class, max);
	}

	/**
	 * Gets the next InvokeInterfaceNode.
	 * 
	 * @return the next <tt>InvokeInterfaceNode<tt>.
	 */
	public InvokeInterfaceNode nextInvokeInterface() {
		return next(InvokeInterfaceNode.class);
	}

	/**
	 * Gets the next LookupSwitchNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>LookupSwitchNode<tt> within the given max distance.
	 */
	public LookupSwitchNode nextLookupSwitch(int max) {
		return next(LookupSwitchNode.class, max);
	}

	/**
	 * Gets the next LookupSwitchNode.
	 * 
	 * @return the next <tt>LookupSwitchNode<tt>.
	 */
	public LookupSwitchNode nextLookupSwitch() {
		return next(LookupSwitchNode.class);
	}

	/**
	 * Gets the next MethodNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>MethodNode<tt> within the given max distance.
	 */
	public MethodNode nextMethod(int max) {
		return next(MethodNode.class, max);
	}

	/**
	 * Gets the next MethodNode.
	 * 
	 * @return the next <tt>MethodNode<tt>.
	 */
	public MethodNode nextMethod() {
		return next(MethodNode.class);
	}

	/**
	 * Gets the next MultianewArrayNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next
	 *         <tt>MultianewArrayNode<tt> within the given max distance.
	 */
	public MultianewArrayNode nextMultianewArray(int max) {
		return next(MultianewArrayNode.class, max);
	}

	/**
	 * Gets the next MultianewArrayNode.
	 * 
	 * @return the next <tt>MultianewArrayNode<tt>.
	 */
	public MultianewArrayNode nextMultianewArray() {
		return next(MultianewArrayNode.class);
	}

	/**
	 * Gets the next PaddedNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>PaddedNode<tt> within the given max distance.
	 */
	public PaddedNode nextPad(int max) {
		return next(PaddedNode.class, max);
	}

	/**
	 * Gets the next PaddedNode.
	 * 
	 * @return the next <tt>PaddedNode<tt>.
	 */
	public PaddedNode nextPad() {
		return next(PaddedNode.class);
	}

	/**
	 * Gets the next PushNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>PushNode<tt> within the given max distance.
	 */
	public PushNode nextPush(int max) {
		return next(PushNode.class, max);
	}

	/**
	 * Gets the next PushNode.
	 * 
	 * @return the next <tt>PushNode<tt>.
	 */
	public PushNode nextPush() {
		return next(PushNode.class);
	}

	/**
	 * Gets the next SimpleNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>SimpleNode<tt> within the given max distance.
	 */
	public SimpleNode nextSimple(int max) {
		return next(SimpleNode.class, max);
	}

	/**
	 * Gets the next SimpleNode.
	 * 
	 * @return the next <tt>SimpleNode<tt>.
	 */
	public SimpleNode nextSimple() {
		return next(SimpleNode.class);
	}

	/**
	 * Gets the next TableSwitchNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>TableSwitchNode<tt> within the given max distance.
	 */
	public TableSwitchNode nextTableSwitch(int max) {
		return next(TableSwitchNode.class, max);
	}

	/**
	 * Gets the next TableSwitchNode.
	 * 
	 * @return the next <tt>TableSwitchNode<tt>.
	 */
	public TableSwitchNode nextTableSwitch() {
		return next(TableSwitchNode.class);
	}

	/**
	 * Gets the next TypeNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>TypeNode<tt> within the given max distance.
	 */
	public TypeNode nextType(int max) {
		return next(TypeNode.class, max);
	}

	/**
	 * Gets the next TypeNode.
	 * 
	 * @return the next <tt>TypeNode<tt>.
	 */
	public TypeNode nextType() {
		return next(TypeNode.class);
	}

	/**
	 * Gets the next VariableNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>VariableNode<tt> within the given max distance.
	 */
	public VariableNode nextVariable(int max) {
		return next(VariableNode.class, max);
	}

	/**
	 * Gets the next VariableNode.
	 * 
	 * @return the next <tt>VariableNode<tt>.
	 */
	public VariableNode nextVariable() {
		return next(VariableNode.class);
	}

	/**
	 * Gets the next WideBranchNode within the given max distance.
	 * 
	 * @param max
	 *            the maximum distance to search within
	 * @return the next <tt>WideBranchNode<tt> within the given max distance.
	 */
	public WideBranchNode nextWideBranch(int max) {
		return next(WideBranchNode.class, max);
	}

	/**
	 * Gets the next WideBranchNode.
	 * 
	 * @return the next <tt>WideBranchNode<tt>.
	 */
	public WideBranchNode nextWideBranch() {
		return next(WideBranchNode.class);
	}

	/**
	 * Gets all the children within this node's tree.
	 * 
	 * @return all the children within this node's tree.
	 */
	public QueryableList<AbstractNode> allChildren() {
		return allChildren((AbstractNodeFilter) null);
	}

	/**
	 * Gets all the children within this node's tree matching the given filter.
	 * 
	 * @param filter
	 *            the filter to match against.
	 * @return all the children within this node's tree matching the given
	 *         filter.
	 */
	public QueryableList<AbstractNode> allChildren(AbstractNodeFilter filter) {
		return allChildren((QueryableList<AbstractNode>) null).all(filter);
	}

	private QueryableList<AbstractNode> allChildren(QueryableList<AbstractNode> list) {
		if (list == null)
			list = new QueryableList<>();
		for (AbstractNode n : this) {
			list.add(n);
			list.addAll(n.allChildren(list));
		}
		return list;
	}

	/**
	 * Gets all the children matching the given opcode.
	 * 
	 * @param opcode
	 *            the opcode to search for.
	 * @return all the children matching the given opcode.
	 */
	public List<AbstractNode> findChildren(Opcode opcode) {
		List<AbstractNode> children = new ArrayList<>();
		for (AbstractNode n : this) {
			if (n.opcode() == opcode)
				children.add(n);
		}
		return !children.isEmpty() ? children : null;
	}

	/**
	 * Gets all the children matching the given opcodes in order.
	 * 
	 * @param opcodes
	 *            the opcodes to match in order.
	 * @return all the children matching the given opcodes in order.
	 */
	public List<AbstractNode> layerAll(Opcode... opcodes) {
		List<AbstractNode> children = findChildren(opcodes[0]);
		if (children == null)
			return null;
		if (opcodes.length == 1)
			return children;
		for (int i = 1; i < opcodes.length; i++) {
			List<AbstractNode> next = new ArrayList<>();
			for (AbstractNode n : children) {
				List<AbstractNode> match = n.findChildren(opcodes[i]);
				if (match == null)
					continue;
				next.addAll(match);
			}
			if (next.isEmpty()) {
				return null;
			} else {
				children.clear();
				children.addAll(next);
			}
		}
		return children;
	}

	/**
	 * Gets the children matching the given opcodes in order.
	 * 
	 * @param opcodes
	 *            the opcodes to match in order.
	 * @return the children matching the given opcodes in order.
	 */
	public AbstractNode layer(Opcode... opcodes) {
		List<AbstractNode> nodes = layerAll(opcodes);
		return nodes != null ? nodes.get(0) : null;
	}

	/**
	 * Gets the first opcode matching any of the given opcodes.
	 * 
	 * @param opcodes
	 *            the opcode to search for.
	 * @return the first opcode matching any of the given opcodes.
	 */
	public AbstractNode firstOf(Opcode... opcodes) {
		for (Opcode opcode : opcodes) {
			AbstractNode an = first(opcode);
			if (an != null)
				return an;
		}
		return null;
	}
}