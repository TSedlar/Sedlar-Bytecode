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
public class LocalVariableTargetInfo extends TargetInfo {

	private LocalVariableTarget[] localVariableTargets;

	public LocalVariableTarget[] targets() {
		return localVariableTargets;
	}

	public void setTargets(LocalVariableTarget[] localVariableTargets) {
		this.localVariableTargets = localVariableTargets;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		int count = in.readUnsignedShort();
		localVariableTargets = new LocalVariableTarget[count];
		for (int i = 0; i < count; i++) {
			localVariableTargets[i] = new LocalVariableTarget();
			localVariableTargets[i].read(in);
		}
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int count = length(localVariableTargets);
		out.writeShort(count);
		for (int i = 0; i < count; i++)
			localVariableTargets[i].write(out);
	}

	@Override
	public int length() {
		return 2 + localVariableTargets.length * 6;
	}

	@Override
	public String verbose() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < localVariableTargets.length; i++) {
			LocalVariableTarget target = localVariableTargets[i];
			buffer.append("[").append(i).append("] start: ").append(target.startPc);
			buffer.append(", length: ").append(target.length);
			buffer.append(", <a href=\"L").append(target.index).append("\">local variable with index ");
			buffer.append(target.index).append("</a>");
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
