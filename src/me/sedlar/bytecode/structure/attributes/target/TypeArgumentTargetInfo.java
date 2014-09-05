/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes.target;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class TypeArgumentTargetInfo extends TargetInfo {

	private int offset;
	private int typeArgumentIndex;

	public int offset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int index() {
		return typeArgumentIndex;
	}

	public void setIndex(int typeArgumentIndex) {
		this.typeArgumentIndex = typeArgumentIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		offset = in.readUnsignedShort();
		typeArgumentIndex = in.readByte();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(offset);
		out.writeByte(typeArgumentIndex);
	}

	@Override
	public int length() {
		return 3;
	}

	@Override
	public String verbose() {
		return "offset " + offset + ", type argument index " + typeArgumentIndex;
	}
}
