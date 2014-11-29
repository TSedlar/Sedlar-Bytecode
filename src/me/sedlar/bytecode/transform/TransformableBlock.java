/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.transform;

import java.util.Map;

import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.flow.BasicBlock;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public interface TransformableBlock extends Transformable {

	public void transform(BasicBlock block);

	@Override
	default void transform(Map<String, ClassInfo> classes) {
		classes.values().forEach(ci -> ci.methods().forEach(mi -> mi.graph().forEach(this::transform)));
	}
}
