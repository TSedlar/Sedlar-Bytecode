/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.attributes.target.*;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public enum TypeAnnotationTargetType {

	GENERIC_PARAMETER_CLASS(0x00, ParameterTargetInfo.class),
	GENERIC_PARAMETER_METHOD(0x01, ParameterTargetInfo.class),
	SUPERTYPE(0x10, SupertypeTargetInfo.class),
	BOUND_GENERIC_PARAMETER_CLASS(0x11, TypeParameterBoundTargetInfo.class),
	BOUND_GENERIC_PARAMETER_METHOD(0x12, TypeParameterBoundTargetInfo.class),
	FIELD(0x13, EmptyTargetInfo.class),
	RETURN_TYPE_METHOD(0x14, EmptyTargetInfo.class),
	RECEIVER_TYPE_METHOD(0x15, EmptyTargetInfo.class),
	FORMAL_PARAMETER_METHOD(0x16, ParameterTargetInfo.class),
	THROWS(0x17, ExceptionTargetInfo.class),
	LOCAL_VARIABLE(0x40, LocalVariableTargetInfo.class),
	LOCAL_RESOURCE(0x41, LocalVariableTargetInfo.class),
	CATCH(0x42, ExceptionTargetInfo.class),
	INSTANCEOF(0x43, OffsetTargetInfo.class),
	NEW(0x44, OffsetTargetInfo.class),
	METHODREF_NEW(0x45, OffsetTargetInfo.class),
	METHODREF_IDENTIFIER_NEW(0x46, OffsetTargetInfo.class),
	CAST(0x47, TypeArgumentTargetInfo.class),
	TYPE_ARGUMENT_CONSTRUCTOR_INVOCATION(0x48, TypeArgumentTargetInfo.class),
	TYPE_ARGUMENT_METHOD_INVOCATION(0x49, TypeArgumentTargetInfo.class),
	TYPE_ARGUMENT_METHODREF_NEW(0x4A, TypeArgumentTargetInfo.class),
	TYPE_ARGUMENT_METHODREF_IDENTIFIER(0x4B, TypeArgumentTargetInfo.class);

	protected static final TypeAnnotationTargetType[] TARGET_TYPES = new TypeAnnotationTargetType[values()[values().length - 1].tag() + 1];

	static {
		for (TypeAnnotationTargetType typeAnnotationTargetType : values())
			TARGET_TYPES[typeAnnotationTargetType.tag()] = typeAnnotationTargetType;
	}

	public static TypeAnnotationTargetType getFromTag(int tag) throws InvalidByteCodeException {
		if (tag < 0 || tag >= TARGET_TYPES.length)
			throw new InvalidByteCodeException("Invalid type annotation target type: " + tag);
		return TARGET_TYPES[tag];
	}

	private int tag;
	private Class<? extends TargetInfo> targetInfoClass;

	TypeAnnotationTargetType(int tag, Class<? extends TargetInfo> targetInfoClass) {
		this.tag = tag;
		this.targetInfoClass = targetInfoClass;
	}

	public int tag() {
		return tag;
	}

	public TargetInfo createTargetInfo() {
		try {
			return targetInfoClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
