package edu.uminho.biosynth.core.data.integration.chimera.strategy;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluators;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uminho.biosynth.core.data.integration.neo4j.CompoundPropertyLabel;
import edu.uminho.biosynth.core.data.integration.neo4j.CompoundRelationshipType;

public class InchiClusterStrategy implements ClusteringStrategy {
	
	@Autowired
	private GraphDatabaseService db;
	
	private Node initialNode;
	
	public Node getInitialNode() { return initialNode;}
	public void setInitialNode(Node initialNode) { this.initialNode = initialNode;}

	public GraphDatabaseService getDb() { return db;}
	public void setDb(GraphDatabaseService db) { this.db = db;}
	
	@Override
	public void setInitialNode(Long id) {
		this.initialNode = db.getNodeById(id);
		if (!this.initialNode.hasLabel(CompoundPropertyLabel.InChI)) {
			throw new RuntimeException("Invalid Node - not a inchi node");
		}
	}

	@Override
	public List<Long> execute() {
		if (!this.initialNode.hasLabel(CompoundPropertyLabel.InChI)) {
			throw new RuntimeException("Invalid Node - not a inchi node");
		}

		List<Long> nodes = new ArrayList<> ();
		for (Path position: db.traversalDescription()
				.depthFirst()
				.relationships(CompoundRelationshipType.HasInChI)
				.evaluator(Evaluators.all()).traverse(initialNode)) {
			
			nodes.add(position.endNode().getId());
		}
		
		return nodes;
	}
	
	@Override
	public String toString() {
		if (!this.initialNode.hasLabel(CompoundPropertyLabel.InChI)) {
			throw new RuntimeException("Invalid Node - not a inchi node");
		}
		return "InchiClusterStrategy " + initialNode.getProperty("inchi");
	}

}