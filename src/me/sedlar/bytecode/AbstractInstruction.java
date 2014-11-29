/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import java.io.IOException;

import me.sedlar.bytecode.io.BytecodeInput;
import me.sedlar.bytecode.io.BytecodeOutput;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.util.Filter;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public abstract class AbstractInstruction {

	private MethodInfo methodInfo;
	private int offset;
	private Opcode opcode;

	private AbstractInstruction previous, next;

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	protected AbstractInstruction(MethodInfo methodInfo, Opcode opcode) {
		this.methodInfo = methodInfo;
		this.opcode = opcode;
	}

	/**
	 * Get the MethodInfo that this instruction is within.
	 *
	 * @return the MethodInfo that this instruction is within.
	 */
	public MethodInfo methodInfo() {
		return methodInfo;
	}

	/**
	 * Gets the index of this instruction.
	 *
	 * @return the index of this instruction.
	 */
	public int index() {
		return methodInfo.instructions().indexOf(this);
	}

	/**
	 * Get the ClassInfo that this instruction is within.
	 *
	 * @return the ClassInfo that this instruction is within.
	 */
	public ClassInfo classInfo() {
		return methodInfo.classInfo();
	}

	/**
	 * Set the previous instruction
	 *
	 * @param previous
	 *            the previous instruction
	 */
	public void setPrevious(AbstractInstruction previous) {
		this.previous = previous;
	}

	/**
	 * Get the previous instruction
	 *
	 * @return the previous instruction
	 */
	public AbstractInstruction previous() {
		return previous;
	}

	/**
	 * Set the next instruction
	 *
	 * @param next
	 *            the next instruction
	 */
	public void setNext(AbstractInstruction next) {
		this.next = next;
	}

	/**
	 * Get the next instruction
	 *
	 * @return the next instruction
	 */
	public AbstractInstruction next() {
		return next;
	}

	/**
	 * Get the size in bits of this instruction.
	 *
	 * @return the size in bits
	 */
	public int size() {
		return 1;
	}

	/**
	 * Get the opcode of this instruction.
	 *
	 * @return the opcode
	 */
	public Opcode opcode() {
		return opcode;
	}

	/**
	 * Set the opcode of this instruction.
	 *
	 * @param opcode
	 *            the opcode
	 */
	public void setOpcode(Opcode opcode) {
		this.opcode = opcode;
	}

	/**
	 * Get the offset of this instruction in its parent <tt>Code</tt> attribute.
	 *
	 * @return the offset
	 */
	public int offset() {
		return offset;
	}

	/**
	 * Set the offset of this instruction in its parent <tt>Code</tt> attribute.
	 *
	 * @param offset
	 *            the offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Read this instruction from the given <tt>BytecodeInput</tt>.
	 * <p>
	 * <p/>
	 * Expects <tt>BytecodeInput</tt> to be in JVM class file format and just
	 * before a instruction of this kind.
	 *
	 * @param in
	 *            the <tt>BytecodeInput</tt> from which to read
	 * @throws IOException
	 *             if an exception occurs with the <tt>BytecodeInput</tt>
	 */
	public void read(BytecodeInput in) throws IOException {
		// The opcode has already been read
		offset = in.count() - 1;
	}

	/**
	 * Write this instruction to the given <tt>BytecodeOutput</tt>.
	 *
	 * @param out
	 *            the <tt>BytecodeOutput</tt> to which to write
	 * @throws IOException
	 *             if an exception occurs with the <tt>BytecodeOutput</tt>
	 */
	public void write(BytecodeOutput out) throws IOException {
		out.writeByte(opcode.id());
	}

	/**
	 * Gets the next instruction matching the given filter within reach of the given radius
	 * 
	 * @param filter the filter to match
	 * @param radius the maximum distance to search
	 * @return the next instruction matching the given filter within reach of the given radius
	 */
	public AbstractInstruction next(Filter<AbstractInstruction> filter, int radius) {
		int iterated = 0;
		AbstractInstruction ai = this;
		while ((ai = ai.next()) != null && (radius == -1 || iterated++ < radius)) {
			if (filter.accept(ai))
				return ai;
		}
		return null;
	}

	/**
	 * Gets the next instruction matching the given filter
	 * 
	 * @param filter the filter to mtach
	 * @return the next instruction matching the given filter
	 */
	public AbstractInstruction next(Filter<AbstractInstruction> filter) {
		return next(filter, -1);
	}
	
	/**
	 * Gets the previous instruction matching the given filter within reach of the given radius
	 * 
	 * @param filter the filter to match
	 * @param radius the maximum distance to search
	 * @return the previous instruction matching the given filter within reach of the given radius
	 */
	public AbstractInstruction previous(Filter<AbstractInstruction> filter, int radius) {
		int iterated = 0;
		AbstractInstruction ai = this;
		while ((ai = ai.previous()) != null && (radius == -1 || iterated++ < radius)) {
			if (filter.accept(ai))
				return ai;
		}
		return null;
	}
	
	/**
	 * Gets the previous instruction matching the given filter
	 * 
	 * @param filter the filter to match
	 * @return the previous instruction matching the given filter
	 */
	public AbstractInstruction previous(Filter<AbstractInstruction> filter) {
		return previous(filter, -1);
	}
}