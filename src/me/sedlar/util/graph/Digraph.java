/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class Digraph<V, E> implements Iterable<V> {

	private final Map<V, Set<E>> graph = new HashMap<>();
	private CyclomaticComplexity cyclomatic = null;

	@Override
	public Iterator<V> iterator() {
		return graph.keySet().iterator();
	}

	/**
	 * Gets the edge at the given index.
	 * 
	 * @param index
	 *            the index to fetch at.
	 * @return the edge at the given index.
	 */
	@SuppressWarnings("unchecked")
	public Set<E> edgeAt(int index) {
		return (Set<E>) graph.values().toArray()[index];
	}

	/**
	 * Gets the size of this graph.
	 * 
	 * @return the size of this graph.
	 */
	public int size() {
		return graph.size();
	}

	/**
	 * Checks if this graph contains the given vertex.
	 * 
	 * @param vertex
	 *            the vertex to check for.
	 * @return <t>true</t> if the graph contains the vertex, otherwise
	 *         <t>false</t>.
	 */
	public boolean containsVertex(V vertex) {
		return graph.containsKey(vertex);
	}

	/**
	 * Checks if this graph contains the given edge.
	 * 
	 * @param vertex
	 *            the vertex to check within.
	 * @param edge
	 *            the edge to check for.
	 * @return <t>true</t> if the graph contains the edge, otherwise
	 *         <t>false</t>.
	 */
	public boolean containsEdge(V vertex, E edge) {
		return graph.containsKey(vertex) && graph.get(vertex).contains(edge);
	}

	/**
	 * Adds the vertex to the graph.
	 * 
	 * @param vertex
	 *            the vertex to add.
	 * @return <t>true</t> if the vertex was added, otherwise <t>false</t>.
	 */
	public boolean addVertex(V vertex) {
		if (graph.containsKey(vertex))
			return false;
		graph.put(vertex, new HashSet<E>());
		return true;
	}

	/**
	 * Adds the edge to the graph.
	 * 
	 * @param start
	 *            the vertex to add to.
	 * @param dest
	 *            the edge to add.
	 */
	public void addEdge(V start, E dest) {
		if (!graph.containsKey(start))
			return;
		graph.get(start).add(dest);
	}

	/**
	 * Removes the edge from the graph.
	 * 
	 * @param start
	 *            the vertex to remove from.
	 * @param dest
	 *            the edge to add.
	 */
	public void removeEdge(V start, E dest) {
		if (!graph.containsKey(start))
			return;
		graph.get(start).remove(dest);
	}

	/**
	 * Gets the edges for the given vertex.
	 * 
	 * @param vertex
	 *            the vertex to get the edges from.
	 * @return the edges for the given vertex.
	 */
	public Set<E> edgesFrom(V vertex) {
		return Collections.unmodifiableSet(graph.get(vertex));
	}

	/**
	 * Gets the complexity for this graph.
	 * 
	 * @param cached
	 *            <t>true</t> to get the cached complexity.
	 * @return the complexity for this graph.
	 */
	public CyclomaticComplexity cyclomatic(boolean cached) {
		if (cached && cyclomatic != null)
			return cyclomatic;
		int edges = 0;
		for (Set<E> set : graph.values())
			edges += set.size();
		int connections = 0;
		for (Set<E> set : graph.values()) {
			if (!set.isEmpty())
				connections++;
		}
		return (cyclomatic = new CyclomaticComplexity(edges, size(), connections));
	}

	/**
	 * Gets the complexity for this graph.
	 * 
	 * @return the complexity for this graph.
	 */
	public CyclomaticComplexity cyclomatic() {
		return cyclomatic(true);
	}

	/**
	 * Adds the given graph to this graph.
	 * 
	 * @param graph
	 *            the graph to add.
	 */
	public void graph(Digraph<V, E> graph) {
		this.graph.putAll(graph.graph);
	}

	/**
	 * Clears this graph of its values.
	 */
	public void clear() {
		graph.clear();
	}
}