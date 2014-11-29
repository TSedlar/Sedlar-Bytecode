/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util.graph;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class CyclomaticComplexity {

	private final int edges, nodes, connections;

	private ComplexityType type = ComplexityType.LINEAR;

	public enum ComplexityType {
		STANDARD {
			public int evaluate(int edges, int nodes, int connections) {
				return edges - nodes + (connections * 2);
			}
		},
		LINEAR {
			public int evaluate(int edges, int nodes, int connections) {
				return edges - nodes + connections;
			}
		},
		SIMPLE {
			public int evaluate(int edges, int nodes, int connections) {
				return edges - nodes + 2;
			}
		};

		public int evaluate(int edges, int nodes, int connections) {
			throw new IllegalArgumentException("No type specified");
		}
	}

	public CyclomaticComplexity(int edges, int nodes, int connections) {
		this.edges = edges;
		this.nodes = nodes;
		this.connections = connections;
	}

	/**
	 * Sets the complexity to standard.
	 * 
	 * @return the complexity with standard evaluation.
	 */
	public CyclomaticComplexity standard() {
		type = ComplexityType.STANDARD;
		return this;
	}

	/**
	 * Sets the complexity to linear.
	 * 
	 * @return the complexity with linear evaluation.
	 */
	public CyclomaticComplexity linear() {
		type = ComplexityType.LINEAR;
		return this;
	}

	/**
	 * Sets the complexity to simple.
	 * 
	 * @return the complexity with simple evaluation.
	 */
	public CyclomaticComplexity simple() {
		type = ComplexityType.SIMPLE;
		return this;
	}

	/**
	 * Gets the edge count.
	 * 
	 * @return the edge count.
	 */
	public int edges() {
		return edges;
	}

	/**
	 * Gets the node count.
	 * 
	 * @return the node count.
	 */
	public int nodes() {
		return nodes;
	}

	/**
	 * Gets the connection count.
	 * 
	 * @return the connection count.
	 */
	public int connections() {
		return connections;
	}

	/**
	 * Gets the complexity evaluation.
	 * 
	 * @return the complexity evaluation.
	 */
	public int complexity() {
		return type.evaluate(edges, nodes, connections);
	}

	/**
	 * Checks if the complexity is within the two given values.
	 * 
	 * @param low
	 *            The lowest range value.
	 * @param high
	 *            The highest range value.
	 * @return <t>true</t> if the complexity is within the two given values,
	 *         otherwise <t>false</t>.
	 */
	public boolean within(int low, int high) {
		int complexity = complexity();
		return complexity >= low && complexity <= high;
	}

	/**
	 * Checks if the complexity is less than the given value.
	 * 
	 * @param x
	 *            the value to check against.
	 * @return <t>true</t> if the complexity is less than the given value,
	 *         otherwise <t>false</t>.
	 */
	public boolean less(int x) {
		return complexity() < x;
	}

	/**
	 * Checks if the complexity is greater than the given value.
	 * 
	 * @param x
	 *            the value to check against.
	 * @return <t>true</t> if the complexity is greater than the given value,
	 *         otherwise <t>false</t>.
	 */
	public boolean greater(int x) {
		return complexity() > x;
	}

	/**
	 * Checks if the complexity equals the given value.
	 * 
	 * @param x
	 *            the value to check against.
	 * @return <t>true</t> if the complexity equals the given value, otherwise
	 *         <t>false</t>.
	 */
	public boolean is(int x) {
		return complexity() == x;
	}

	@Override
	public String toString() {
		return String.format("cyclomatic[edges=%s][nodes=%s][connections=%s][complexity=%s/%s]", edges, nodes,
				connections, type.toString(), complexity());
	}
}