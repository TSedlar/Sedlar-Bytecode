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

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.Annotation;
import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.attributes.target.TargetInfo;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class TypeAnnotation extends AbstractStructure {

	private static final int INITIAL_LENGTH = 2;

	private TypeAnnotationTargetType targetType;
	private TargetInfo targetInfo;
	private TypePathEntry typePathEntries[];
	private Annotation annotation;

	public TypeAnnotationTargetType targetType() {
		return targetType;
	}

	public void setTargetType(TypeAnnotationTargetType targetType) {
		this.targetType = targetType;
	}

	public TargetInfo targetInfo() {
		return targetInfo;
	}

	public void setTargetInfo(TargetInfo targetInfo) {
		this.targetInfo = targetInfo;
	}

	public TypePathEntry[] typePathEntries() {
		return typePathEntries;
	}

	public void setTypePathEntries(TypePathEntry[] typePathEntries) {
		this.typePathEntries = typePathEntries;
	}

	public Annotation annotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		targetType = TypeAnnotationTargetType.getFromTag(in.readUnsignedByte());
		targetInfo = targetType.createTargetInfo();
		targetInfo.read(in);
		int typePathLength = in.readUnsignedByte();
		typePathEntries = new TypePathEntry[typePathLength];
		for (int i = 0; i < typePathLength; i++) {
			typePathEntries[i] = new TypePathEntry();
			typePathEntries[i].read(in);
		}
		annotation = new Annotation();
		annotation.read(in);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeByte(targetType.tag());
		targetInfo.write(out);
		int typePathLength = length(typePathEntries);
		out.writeByte(typePathLength);
		for (int i = 0; i < typePathLength; i++)
			typePathEntries[i].write(out);
		annotation.write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "TypeAnnotation entry");
	}

	public int length() {
		return INITIAL_LENGTH + targetInfo.length() + (typePathEntries.length * 2) + annotation.length();
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return "";
	}
}
