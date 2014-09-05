/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */

package me.sedlar.bytecode.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import me.sedlar.bytecode.AbstractInstruction;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class BytecodeWriter {

	/**
	 * Converts a list of instructions to code.
	 *
	 * @param instructions
	 *            the <tt>java.util.List</tt> with the instructions
	 * @return the code as an array of bits
	 * @throws IOException
	 *             if an exception occurs with the code
	 */
	public static byte[] writeBytecode(List<AbstractInstruction> instructions) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (BytecodeOutputStream out = new BytecodeOutputStream(baos)) {
				for (AbstractInstruction instruction : instructions)
					instruction.write(out);
				return baos.toByteArray();
			}
		} catch (IOException e) {
			return new byte[0];
		}
	}

	/**
	 * Writes a list of instructions to a
	 * <tt>me.sedlar.bytecode.io.BytecodeOutputStream</tt>.
	 * 
	 * @param instructions
	 *            the <tt>java.util.List</tt> with the instructions
	 * @param out
	 *            The <tt>me.sedlar.bytecode.io.BytecodeOutputStream</tt> to
	 *            write the instructions to.
	 * @return <t>true</t> if the instructions were successfully written,
	 *         otherwise <t>false</t>.
	 */
	public static boolean writeBytecode(List<AbstractInstruction> instructions, BytecodeOutputStream out) {
		try {
			for (AbstractInstruction ai : instructions)
				ai.write(out);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
