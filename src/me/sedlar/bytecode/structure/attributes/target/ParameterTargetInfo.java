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
public class ParameterTargetInfo extends TargetInfo {

	private int typeParameterIndex;

	public int index() {
		return typeParameterIndex;
	}

	public void setIndex(int typeParameterIndex) {
		this.typeParameterIndex = typeParameterIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		typeParameterIndex = in.readUnsignedByte();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeByte(typeParameterIndex);
	}

	@Override
	public int length() {
		return 1;
	}

	@Override
	public String verbose() {
		return "parameter index " + typeParameterIndex;
	}
}
