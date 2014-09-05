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

import me.sedlar.bytecode.structure.AttributeInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class RuntimeTypeAnnotationsAttribute extends AttributeInfo implements AnnotationHolder {

	private static final int INITIAL_LENGTH = 2;

	protected TypeAnnotation[] runtimeTypeAnnotations;

	/**
	 * Get the list of runtime annotations associations of the parent structure
	 * as an array of <tt>Annotation</tt> structure.
	 *
	 * @return the array
	 */
	public TypeAnnotation[] annotations() {
		return runtimeTypeAnnotations;
	}

	/**
	 * Set the list of runtime annotations associations of the parent structure
	 * as an array of <tt>Annotation</tt> structure.
	 *
	 * @param runtimeAnnotations
	 *            the array
	 */
	public void setAnnotations(TypeAnnotation[] runtimeAnnotations) {
		this.runtimeTypeAnnotations = runtimeAnnotations;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int runtimeVisibleAnnotationsLength = in.readUnsignedShort();
		runtimeTypeAnnotations = new TypeAnnotation[runtimeVisibleAnnotationsLength];
		for (int i = 0; i < runtimeVisibleAnnotationsLength; i++) {
			runtimeTypeAnnotations[i] = new TypeAnnotation();
			runtimeTypeAnnotations[i].setClassInfo(classInfo);
			runtimeTypeAnnotations[i].read(in);
		}
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int runtimeVisibleAnnotationsLength = length(runtimeTypeAnnotations);
		out.writeShort(runtimeVisibleAnnotationsLength);
		for (int i = 0; i < runtimeVisibleAnnotationsLength; i++)
			runtimeTypeAnnotations[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		int length = INITIAL_LENGTH;
		for (TypeAnnotation runtimeTypeAnnotation : runtimeTypeAnnotations)
			length += runtimeTypeAnnotation.length();
		return length;
	}

	@Override
	public int annotationLength() {
		return runtimeTypeAnnotations.length;
	}
}
