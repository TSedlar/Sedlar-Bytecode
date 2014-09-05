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
public class TypeParameterBoundTargetInfo extends TargetInfo {

	private int typeParameterIndex;
	private int boundIndex;

	public int parameterIndex() {
		return typeParameterIndex;
	}

	public void setParameterIndex(int typeParameterIndex) {
		this.typeParameterIndex = typeParameterIndex;
	}

	public int boundIndex() {
		return boundIndex;
	}

	public void setBoundIndex(int boundIndex) {
		this.boundIndex = boundIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		typeParameterIndex = in.readUnsignedByte();
		boundIndex = in.readUnsignedByte();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeByte(typeParameterIndex);
		out.writeByte(boundIndex);
	}

	@Override
	public int length() {
		return 2;
	}

	@Override
	public String verbose() {
		return "parameter index " + typeParameterIndex + ", bound index " + boundIndex;
	}
}
