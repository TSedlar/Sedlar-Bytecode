/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.BranchInstruction;
import me.sedlar.bytecode.io.BytecodeOutputStream;
import me.sedlar.bytecode.io.BytecodeReader;
import me.sedlar.bytecode.io.BytecodeWriter;
import me.sedlar.bytecode.structure.attributes.CodeAttribute;
import me.sedlar.bytecode.structure.flow.BasicBlock;
import me.sedlar.bytecode.structure.flow.BlockType;
import me.sedlar.bytecode.structure.flow.graph.FlowGraph;
import me.sedlar.bytecode.transform.TransformableBlock;
import me.sedlar.bytecode.tree.NodeTree;
import me.sedlar.bytecode.tree.util.TreeBuilder;
import me.sedlar.bytecode.util.QueryableInstructionList;
import me.sedlar.util.AlphaLabel;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class MethodInfo extends ClassMember {

	private final QueryableInstructionList instructions = new QueryableInstructionList();
	private NodeTree tree;
	private final FlowGraph graph = new FlowGraph(this);
	private final List<BasicBlock> blocks = new LinkedList<>();

	/**
	 * Factory method for creating <tt>MethodInfo</tt> structure from a
	 * <tt>DataInput</tt>.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>MethodInfo</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>MethodInfo</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static MethodInfo create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setClassInfo(classInfo);
		methodInfo.read(in);
		return methodInfo;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		for (AttributeInfo attr : attributes()) {
			if (attr instanceof CodeAttribute) {
				try {
					instructions.addAll(BytecodeReader.readBytecode(this, ((CodeAttribute) attr).code()));
				} catch (IOException ignored) {
				}
			}
		}
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeShort(accessFlags);
		out.writeShort(nameIndex);
		out.writeShort(descriptorIndex);
		int attributesCount = length(attributes);
		out.writeShort(attributesCount);
		for (int i = 0; i < attributesCount; i++) {
			if (attributes[i] == null)
				throw new InvalidByteCodeException("attribute " + i + " is null");
			if (attributes[i] instanceof CodeAttribute) {
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
					try (BytecodeOutputStream bos = new BytecodeOutputStream(baos)) {
						BytecodeWriter.writeBytecode(instructions, bos);
					}
					((CodeAttribute) attributes[i]).setCode(baos.toByteArray());
				} catch (IOException e) {
					throw new RuntimeException("CodeAttribute failed to write");
				}
			}
			attributes[i].write(out);
		}
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "method with access flags " + accessFlags(accessFlags) + ", name_index " + nameIndex
				+ ", descriptor_index " + descriptorIndex + ", " + length(attributes) + " attributes");
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return verboseAccessFlags(AccessFlag.METHOD_ACCESS_FLAGS, accessFlags);
	}

	/**
	 * Get the instruction list for this method
	 *
	 * @return the instruction list for this method
	 */
	public QueryableInstructionList instructions() {
		return instructions;
	}

	/**
	 * Gets the NodeTree for this method.
	 *
	 * @return the NodeTree for this method.
	 */
	public NodeTree tree() {
		if (tree == null)
			tree = TreeBuilder.build(this);
		return tree;
	}

	/**
	 * Constructs a control flow graph for this method.
	 *
	 * @return a control flow graph for this method.
	 */
	public FlowGraph graph() {
		if (graph.size() == 0)
			blocks();
		return graph;
	}

	private BasicBlock targetOf(BasicBlock block) {
		int targetIndex = block.targetIndex();
		for (BasicBlock bb : blocks) {
			if (targetIndex >= bb.low() && targetIndex <= bb.high())
				return bb;
		}
		return null;
	}

	/**
	 * Gets the basic blocks for this method
	 *
	 * @return The basic blocks for this method
	 */
	private List<BasicBlock> blocks() {
		if (!blocks.isEmpty())
			return blocks;
		QueryableInstructionList list = new QueryableInstructionList();
		int low = -1;
		int index = -1;
		for (AbstractInstruction ai : instructions()) {
			int offset = ai.offset();
			index = (offset > index ? offset : index + 1);
			if (low == -1)
				low = index;
			list.add(ai);
			if (ai instanceof BranchInstruction || ai.opcode().verbose().endsWith("return")) {
				String label = AlphaLabel.get(blocks.size() + 1);
				QueryableInstructionList clone = (QueryableInstructionList) list.clone();
				blocks.add(new BasicBlock(label, this, clone, low, index));
				list.clear();
				low = -1;
			}
		}
		List<BasicBlock> empty = new LinkedList<>();
		for (BasicBlock block : blocks) {
			if (block.type() == BlockType.EMPTY)
				empty.add(block);
		}
		blocks.removeAll(empty);
		BasicBlock previous = null;
		for (BasicBlock block : blocks) {
			if (previous != null) {
				previous.setNext(block);
				block.setPrevious(previous);
			}
			BasicBlock target = targetOf(block);
			if (target != null) {
				block.setTarget(target);
				block.addSuccessor(target);
				target.addPredecessor(block);
			}
			previous = block;
		}
		for (BasicBlock block : blocks) {
			if (!graph.containsVertex(block))
				graph.addVertex(block);
			BasicBlock target = block.target();
			BasicBlock next = block.next();
			if (target != null && target != block) {
				if (!graph.containsVertex(target))
					graph.addVertex(target);
				graph.addEdge(block, target);
			}
			if (next != null) {
				if (!graph.containsVertex(next))
					graph.addVertex(next);
				graph.addEdge(block, next);
			}
		}
		return blocks;
	}
	
	/**
	 * Accepts a TransformableBlock
	 * 
	 * @param transform the transform to accept
	 */
	public void accept(TransformableBlock transform) {
		blocks().forEach(bb -> transform.transform(bb));
	}
}
