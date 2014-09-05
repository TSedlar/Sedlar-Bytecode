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

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class VariableInstruction extends AbstractInstruction {

	private AbstractInstruction instruction;

	/**
	 * Constructor.
	 *
	 * @param ibi
	 *            an ImmediateByteInstruction for namesake an LDC* that pushes a
	 *            byte.
	 */
	public VariableInstruction(ImmediateByteInstruction ibi) {
		super(ibi.methodInfo(), ibi.opcode());
		this.instruction = ibi;
	}

	/**
	 * Constructor.
	 *
	 * @param si
	 *            a SimpleInstruction that loads or stores.
	 */
	public VariableInstruction(SimpleInstruction si) {
		super(si.methodInfo(), si.opcode());
		this.instruction = si;
	}

	public int variable() {
		if (instruction instanceof ImmediateByteInstruction) {
			return ((ImmediateByteInstruction) instruction).value();
		} else {
			String verbose = opcode().verbose();
			if (verbose.contains("m1")) {
				return -1;
			} else if (!verbose.contains("_")) {
				return 0;
			} else {
				return Integer.parseInt(verbose.split("_")[1]);
			}
		}
	}

	@Override
	public void read(BytecodeInput input) throws IOException {
		instruction.read(input);
	}

	@Override
	public void write(BytecodeOutput output) throws IOException {
		instruction.write(output);
	}
}
