/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class UninitializedVerificationTypeEntry extends VerificationTypeInfoEntry {

	public UninitializedVerificationTypeEntry() {
		super(VerificationType.UNINITIALIZED);
	}

	private int offset;

	public int offset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	protected void readExtra(DataInput in) throws InvalidByteCodeException, IOException {
		super.readExtra(in);
		offset = in.readUnsignedShort();
	}

	@Override
	public void writeExtra(DataOutput out) throws InvalidByteCodeException, IOException {
		super.writeExtra(out);
		out.writeShort(offset);
	}

	@Override
	public void appendTo(StringBuilder buffer) {
		super.appendTo(buffer);
		buffer.append(" (offset: ").append(offset).append(")");
	}

	@Override
	public int length() {
		return super.length() + 2;
	}
}
