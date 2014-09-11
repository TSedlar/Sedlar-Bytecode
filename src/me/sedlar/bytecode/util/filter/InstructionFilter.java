/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util.filter;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.Opcode;
import me.sedlar.util.Filter;
import me.sedlar.util.Strings;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class InstructionFilter implements Filter<AbstractInstruction> {

	private List<InstructionFilter> chained = new LinkedList<>();

	private int distance = 10;
	private String label = null;
	private Opcode opcode;

	public InstructionFilter() {
		chained.add(this);
	}

	/**
	 * Creates a filter with the given opcode
	 *
	 * @param opcode the opcode to set
	 * @return a filter with the given opcode
	 */
	public InstructionFilter opcode(Opcode opcode) {
		this.opcode = opcode;
		return this;
	}

	/**
	 * Creates a filter with the given distance.
	 *
	 * @param distance
	 *            the distance to set.
	 * @return a filter with the given distance.
	 */
	public InstructionFilter distance(int distance) {
		this.distance = distance;
		return this;
	}

	/**
	 * Checks if the distance is acceptable.
	 *
	 * @param distance
	 *            the distance to check against.
	 * @return <t>true</t> if the distance is acceptable, otherwise
	 *         <t>false</t>.
	 */
	public boolean accept(int distance) {
		return this.distance == -1 || distance < this.distance;
	}

	private void addAll(InstructionFilter filter) {
		if (filter.chained.size() > 1) {
			for (InstructionFilter f : filter.chained) {
				chained.add(f);
				if (f.chained.size() > 1)
					addAll(f);
			}
		}
	}

	/**
	 * Creates a filter matching this and the given filter.
	 *
	 * @param filter
	 *            the filter to match with.
	 * @return a filter matching this and the given filter.
	 */
	public InstructionFilter or(InstructionFilter filter) {
		addAll(filter);
		return this;
	}

	/**
	 * Checks if the given instruction is valid against this filter.
	 *
	 * @param ai
	 *            the instruction to check against.
	 * @return <t>true</t> if the given instruction is valid against this
	 *         filter, otherwise <t>false</t>.
	 */
	public boolean validate(AbstractInstruction ai) {
		if (opcode != null && ai.opcode() != opcode)
			return false;
		for (InstructionFilter filter : chained) {
			if (filter.accept(ai))
				return true;
		}
		return false;
	}

	/**
	 * Creates a filter matching the given opcode.
	 *
	 * @param opcode
	 *            the opcode to check against.
	 * @return a filter matching the given opcode.
	 */
	public static InstructionFilter create(final Opcode opcode) {
		return new InstructionFilter() {
			public boolean accept(AbstractInstruction ai) {
				return ai.opcode() == opcode;
			}
		};
	}

	/**
	 * Creates a filter matching any instruction
	 *
	 * @return a filter matching any instruction
	 */
	public static InstructionFilter create() {
		return new InstructionFilter() {
			public boolean accept(AbstractInstruction ai) {
				return true;
			}
		};
	}

	/**
	 * Gets the label for this filter.
	 *
	 * @return the label for this filter.
	 */
	public String label() {
		return label;
	}

	/**
	 * Creates a filter with the given label.
	 *
	 * @param label
	 *            The label
	 * @return a filter with the given label.
	 */
	public InstructionFilter label(String label) {
		this.label = label;
		return this;
	}

	private static Object common(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			if (string.equals("null")) {
				return null;
			}
		}
		return string;
	}

	/**
	 * Creates a filter from the given string.
	 *
	 * @param filter the string to construct the filter from.
	 * @return a filter from the given string.
	 */
	public static InstructionFilter fromString(String filter) {
		String label = null;
		if (filter.contains(" :: ")) {
			label = filter.split(" :: ")[1];
			filter = filter.replace(" :: " + label, "");
		}
		String filterType = filter.split(" ")[0];
		String cleaned = filter.replaceFirst(filterType, "").trim();
		String[] strings = cleaned.split(" ");
		Object[] objects = new Object[strings.length];
		for (int i = 0; i < strings.length; i++)
			objects[i] = common(strings[i]);
		boolean single = strings.length == 0 || cleaned.isEmpty();
		Opcode opcode = null;
		if (!single) {
			opcode = Opcode.fromName(strings[0]);
			if (opcode != null) {
				if (strings.length > 1) {
					objects = Arrays.copyOfRange(objects, 1, objects.length - 1);
					strings = Arrays.copyOfRange(strings, 1, strings.length - 1);
				} else {
					objects = new Object[] {};
					strings = new String[] {};
					single = true;
				}
			}
		}
		try {
			InstructionFilter instance;
			switch (filterType) {
				case "BIF": {
					instance = BranchInstructionFilter.create();
					break;
				}
				case "CIF": {
					instance = single ? ConstantInstructionFilter.create()
							: ConstantInstructionFilter.create(objects[0]);
					break;
				}
				case "FIF": {
					boolean ignoreStatic = true;
					if (strings.length > 0 && strings[0].equals("STATIC")) {
						ignoreStatic = false;
						String[] newStrings = new String[strings.length - 1];
						Object[] newObjects = new Object[objects.length - 1];
						for (int i = 1; i < strings.length; i++) {
							newStrings[i - 1] = strings[i];
							newObjects[i - 1] = objects[i];
						}
						strings = newStrings;
						objects = newObjects;
					}
					switch (strings.length) {
						case 3: {
							instance = FieldInstructionFilter.create((String) objects[0], (String) objects[1],
									(String) objects[2]);
							break;
						}
						case 2: {
							instance = FieldInstructionFilter.create((String) objects[0], (String) objects[1]);
							break;
						}
						case 1: {
							instance = FieldInstructionFilter.create((String) objects[0]);
							break;
						}
						case 0: {
							instance = FieldInstructionFilter.create();
							break;
						}
						default: {
							throw new IllegalArgumentException("argument overflow");
						}
					}
					if (!ignoreStatic)
						((FieldInstructionFilter) instance).staticize();
					break;
				}
				case "IIF": {
					instance = single ? IncrementInstructionFilter.create()
							: IncrementInstructionFilter.create(Integer.parseInt(strings[0]));
					break;
				}
				case "IF": {
					instance = single ? InstructionFilter.create() : InstructionFilter.create(Opcode.fromName(strings[0]));
					break;
				}
				case "MIF": {
					boolean regex = strings.length > 0 && strings[0].equals("REGEX");
					if (regex) {
						String[] newStrings = new String[strings.length - 1];
						Object[] newObjects = new Object[objects.length - 1];
						for (int i = 1; i < strings.length; i++) {
							newStrings[i - 1] = strings[i];
							newObjects[i - 1] = objects[i];
						}
						strings = newStrings;
						objects = newObjects;
					}
					switch (strings.length) {
						case 3: {
							instance = MethodInstructionFilter.create((String) objects[0], (String) objects[1],
									(String) objects[2]);
							break;
						}
						case 2: {
							instance = MethodInstructionFilter.create((String) objects[0], (String) objects[1]);
							break;
						}
						case 1: {
							if (regex) {
								strings[0] = strings[0].replaceAll("\\{DUMMY\\}", "(?:(I|B|S|D|F|J))?");
								strings[0] = strings[0].replaceFirst("\\(", "\\\\(");
								strings[0] = Strings.replaceLast(strings[0], "\\)", "\\\\)");
							}
							instance = regex ? MethodInstructionFilter.createRegex(strings[0])
									: MethodInstructionFilter.create(strings[0]);
							break;
						}
						case 0: {
							instance = MethodInstructionFilter.create();
							break;
						}
						default: {
							throw new IllegalArgumentException("argument overflow");
						}
					}
					break;
				}
				case "PIF": {
					instance = single ? PushInstructionFilter.create()
							: PushInstructionFilter.create(Integer.parseInt(strings[0]));
					break;
				}
				case "TIF": {
					boolean regex = strings.length > 0 && strings[0].equals("REGEX");
					if (regex)
						strings = new String[]{strings[1]};
					instance = single ? TypeInstructionFilter.create()
							: (regex ? TypeInstructionFilter.createRegex(strings[0])
							: TypeInstructionFilter.create(strings[0]));
					break;
				}
				case "VIF": {
					instance = single ? VariableInstructionFilter.create()
							: VariableInstructionFilter.create(Integer.parseInt(strings[0]));
					break;
				}
				default: {
					throw new IllegalArgumentException("filter not found");
				}
			}
			if (opcode != null)
				instance = instance.opcode(opcode);
			if (label != null)
				instance = instance.label(label);
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates an array of filters from the given strings.
	 *
	 * @param filters
	 *            the strings to construct an array of filters from.
	 * @return an array of filters from the given strings.
	 */
	public static InstructionFilter[] filtersFor(String... filters) {
		InstructionFilter[] array = new InstructionFilter[filters.length];
		for (int i = 0; i < filters.length; i++)
			array[i] = fromString(filters[i]);
		return array;
	}
}