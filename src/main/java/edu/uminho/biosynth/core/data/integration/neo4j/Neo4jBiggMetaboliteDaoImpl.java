package edu.uminho.biosynth.core.data.integration.neo4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.cypher.CypherExecutionException;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;

import pt.uminho.sysbio.biosynth.integration.etl.dictionary.BioDbDictionary;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.BiggMetaboliteCrossreferenceEntity;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.BiggMetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.io.MetaboliteDao;

public class Neo4jBiggMetaboliteDaoImpl extends AbstractNeo4jDao<BiggMetaboliteEntity> implements MetaboliteDao<BiggMetaboliteEntity> {

	private static Logger LOGGER = Logger.getLogger(Neo4jBiggMetaboliteDaoImpl.class);
	
	public Neo4jBiggMetaboliteDaoImpl(GraphDatabaseService graphdb) {
		super(graphdb);
	}
	
	public boolean hasProxy(Serializable id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("entry", id);
		
		String query = null;
		if (id instanceof Integer) {
			query = "MATCH (cpd:BiGG {id:{id}}) RETURN cpd";
		} else if (id instanceof String) {
			query = "MATCH (cpd:BiGG {entry:{entry}}) RETURN cpd";
		} else {
			return false;
		}
		
		ExecutionResult result = executionEngine.execute(query, params);
		Iterator<Node> iterator = result.columnAs("cpd");
		List<Node> nodes = IteratorUtil.asList(iterator);
		
		if (nodes.size() > 1) {
			LOGGER.error(String.format("Schema constraints error - %s", query));
		}
		
		return !nodes.isEmpty();
	}
	
//	@Override
	public BiggMetaboliteEntity find(Serializable id) {
//		ExecutionEngine engine = new ExecutionEngine(graphdb);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("entry", id);
		
		String query = null;
		if (id instanceof Integer) {
			query = "MATCH (cpd:BiGG {id:{id}}) RETURN cpd";
		} else if (id instanceof String) {
			query = "MATCH (cpd:BiGG {entry:{entry}}) RETURN cpd";
		} else {
			return null;
		}
		
		ExecutionResult result = executionEngine.execute(query, params);
//		System.out.println(result.dumpToString());
		Iterator<Node> iterator = result.columnAs("cpd");
		List<Node> nodes = IteratorUtil.asList(iterator);
		if (nodes.size() > 1) System.err.println("ERROR id not unique - integretity error");
//		System.out.println(nodes);
		BiggMetaboliteEntity cpd = null;
		for (Node node : nodes) {
//			System.out.println(node);
			cpd = new BiggMetaboliteEntity();
			cpd.setId( (Long) node.getProperty("id"));
			cpd.setEntry( (String) node.getProperty("entry"));
			cpd.setFormula( (String) node.getProperty("formula"));
			cpd.setCharge( (Integer) node.getProperty("charge"));
		}
		
		return cpd;
	}
	
	public void remove(Serializable id) {
//		ExecutionEngine engine = new ExecutionEngine(graphdb);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("entry", id);
		
		String query = null;
		if (id instanceof Integer) {
			query = "MATCH (cpd:BiGG {id:{id}}) DELETE cpd";
		} else if (id instanceof String) {
			query = "MATCH (cpd:BiGG {entry:{entry}}) DELETE cpd";
		} else {
			return;
		}
		
		executionEngine.execute(query, params);
	}
	
	public boolean hasMetabolite(Serializable id) {
//		ExecutionEngine engine = new ExecutionEngine(graphdb);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("entry", id);
		
		String query = null;
		if (id instanceof Integer) {
			query = "MATCH (cpd:BiGG {id:{id}}) RETURN cpd AS c1";
		} else if (id instanceof String) {
			query = "MATCH (cpd:BiGG {entry:{entry}}) RETURN cpd AS c1";
		} else {
			return false;
		}
		
		ExecutionResult result = executionEngine.execute(query, params);
		Iterator<Node> iterator = result.columnAs("c1");
		List<Node> nodes = IteratorUtil.asList(iterator);
		if (nodes.size() > 1) System.err.println("ERROR id not unique - integretity error");
		
		return false;
	}

//	@Override
	public List<BiggMetaboliteEntity> findAll() {
//		ExecutionEngine engine = new ExecutionEngine(graphdb);
		ExecutionResult result = executionEngine.execute("MATCH (cpd:BiGG) RETURN cpd");
		Iterator<Node> iterator = result.columnAs("cpd");
		List<Node> nodes = IteratorUtil.asList(iterator);
		List<BiggMetaboliteEntity> res = new ArrayList<> ();
		for (Node node : nodes) {
			res.add(this.nodeToObject(node));
		}
		return res;
	}

	@Override
	public Serializable save(BiggMetaboliteEntity cpd) {
		LOGGER.info(String.format("Saving BiGG node %d:%s", cpd.getId(), cpd.getEntry()));
		String cypherQuery = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", cpd.getId());
			params.put("entry", cpd.getEntry());
			params.put("name", cpd.getName());
			params.put("formula", cpd.getFormula());
			params.put("charge", cpd.getCharge());
			
			if (this.hasProxy(cpd.getId())) {
				LOGGER.debug(String.format("Merge BiGG node from id:%d", cpd.getId()));
				cypherQuery = "MERGE (cpd:BiGG:Compound {id:{id}}) ON CREATE SET "
					+ "cpd.created_at=timestamp(), cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
					+ "cpd.charge={charge}, cpd.proxy=false "
					+ "ON MATCH SET "
					+ "cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
					+ "cpd.charge={charge}, cpd.proxy=false, cpd.entry={entry}";
			} else if (this.hasProxy(cpd.getEntry())) {
				LOGGER.debug(String.format("Merge BiGG node from entry:\"%s\"", cpd.getEntry()));
				cypherQuery = "MERGE (cpd:BiGG:Compound {entry:{entry}}) ON CREATE SET "
					+ "cpd.created_at=timestamp(), cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
					+ "cpd.charge={charge}, cpd.proxy=false "
					+ "ON MATCH SET "
					+ "cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
					+ "cpd.charge={charge}, cpd.proxy=false, cpd.id={id}";
			} else {
				LOGGER.debug(String.format("Create new BiGG node id:%d, entry:\"%s\"", cpd.getId(), cpd.getEntry()));
				cypherQuery = "MERGE (cpd:BiGG:Compound {entry:{entry}, id:{id}}) ON CREATE SET "
					+ "cpd.created_at=timestamp(), cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
					+ "cpd.charge={charge}, cpd.proxy=false "
					+ "ON MATCH SET "
					+ "cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
					+ "cpd.charge={charge}, cpd.proxy=false";
			}
			
//			cypherQuery = cypherQuery.concat("ON CREATE SET "
//					+ "cpd.created_at=timestamp(), cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
//					+ "cpd.charge={charge}, cpd.proxy=false "
//					+ "ON MATCH SET "
//					+ "cpd.updated_at=timestamp(), cpd.name={name}, cpd.formula={formula}, "
//					+ "cpd.charge={charge}, cpd.proxy=false");
			
			executionEngine.execute(cypherQuery, params);
			
			executionEngine.execute("MERGE (m:Formula {formula:{formula}}) ", params);
			executionEngine.execute("MERGE (m:Charge {charge:{charge}}) ", params);
			executionEngine.execute("MERGE (m:Name {name:{name}}) ", params);
			executionEngine.execute("MATCH (cpd:BiGG {id:{id}}), (f:Formula {formula:{formula}}) MERGE (cpd)-[r:HasFormula]->(f)", params);
			executionEngine.execute("MATCH (cpd:BiGG {id:{id}}), (c:Charge {charge:{charge}}) MERGE (cpd)-[r:HasCharge]->(c)", params);
			executionEngine.execute("MATCH (cpd:BiGG {id:{id}}), (n:Name {name:{name}}) MERGE (cpd)-[r:HasName]->(n)", params);
			
			for (String cmp : cpd.getCompartments()) {
				params.put("compartment", cmp);
				executionEngine.execute("MERGE (c:Compartment {compartment:{compartment}})", params);
				executionEngine.execute("MATCH (cpd:BiGG {id:{id}}), (c:Compartment {compartment:{compartment}}) MERGE (cpd)-[r:FoundIn]->(c)", params);
			}
			
			for (BiggMetaboliteCrossreferenceEntity xref : cpd.getCrossreferences()) {
				switch (xref.getType()) {
					case DATABASE:
						String dbLabel = BioDbDictionary.translateDatabase(xref.getRef());
						String dbEntry = xref.getValue(); //Also need to translate if necessary
						params.put("dbEntry", dbEntry);
						executionEngine.execute("MERGE (cpd:" + dbLabel + ":Compound {entry:{dbEntry}}) ON CREATE SET cpd.proxy=true", params);
						executionEngine.execute("MATCH (cpd1:BiGG {id:{id}}), (cpd2:" + dbLabel + " {entry:{dbEntry}}) MERGE (cpd1)-[r:HasCrossreferenceTo]->(cpd2)", params);
						break;
					case MODEL:
						String modelId = xref.getValue();
						params.put("modelId", modelId);
						executionEngine.execute("MERGE (m:Model {id:{modelId}}) ", params);
						executionEngine.execute("MATCH (cpd:BiGG {id:{id}}), (m:Model {id:{modelId}}) MERGE (cpd)-[r:FoundIn]->(m)", params);
						break;
					default:
						throw new RuntimeException("unsupported type " + xref.getType());
				}
	
			}
		} catch (CypherExecutionException e) {
			LOGGER.error(String.format("Cypher Error - %s", cypherQuery));
			throw e;
		}
		return null;
	}

	@Override
	protected BiggMetaboliteEntity nodeToObject(Node node) {
		BiggMetaboliteEntity cpd = new BiggMetaboliteEntity();
		cpd.setId( (Long) node.getProperty("id"));
		cpd.setEntry( (String) node.getProperty("entry"));
		cpd.setFormula( (String) node.getProperty("formula"));
		cpd.setCharge( (Integer) node.getProperty("charge"));
		return cpd;
	}

	@Override
	public List<Serializable> getAllMetaboliteIds() {
		List<Serializable> res = new ArrayList<> ();
		Iterator<String> iterator = executionEngine.execute(
				"MATCH (cpd:BiGG) RETURN cpd.id AS ids").columnAs("ids");
		while (iterator.hasNext()) {
			res.add(iterator.next());
		}
		return res;
	}

	@Override
	public BiggMetaboliteEntity getMetaboliteById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BiggMetaboliteEntity saveMetabolite(
			BiggMetaboliteEntity metabolite) {
		this.save(metabolite);
		return null;
	}

	@Override
	public Serializable saveMetabolite(Object entity) {		
		return this.save(BiggMetaboliteEntity.class.cast(entity));
	}

	@Override
	public BiggMetaboliteEntity getMetaboliteByEntry(String entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllMetaboliteEntries() {
		List<String> res = new ArrayList<> ();
		Iterator<String> iterator = executionEngine.execute(
				"MATCH (cpd:BiGG {proxy:false}) RETURN cpd.entry AS entries").columnAs("entries");
		while (iterator.hasNext()) {
			res.add(iterator.next());
		}
		return res;
	}

}
