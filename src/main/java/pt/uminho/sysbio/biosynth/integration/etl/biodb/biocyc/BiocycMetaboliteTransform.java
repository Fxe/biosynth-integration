package pt.uminho.sysbio.biosynth.integration.etl.biodb.biocyc;

import java.util.ArrayList;
import java.util.List;

import edu.uminho.biosynth.core.components.biodb.biocyc.BioCycMetaboliteEntity;
import edu.uminho.biosynth.core.components.biodb.biocyc.components.BioCycMetaboliteCrossreferenceEntity;
import edu.uminho.biosynth.core.data.integration.dictionary.BioDbDictionary;
import pt.uminho.sysbio.biosynth.integration.CentralMetaboliteEntity;
import pt.uminho.sysbio.biosynth.integration.CentralMetaboliteProxyEntity;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.AbstractMetaboliteTransform;

public class BiocycMetaboliteTransform
extends AbstractMetaboliteTransform<BioCycMetaboliteEntity>{

//	private final String BIOCYC_P_COMPOUND_METABOLITE_LABEL;
	
	public BiocycMetaboliteTransform(String majorLabel) {
		super(majorLabel);
//		this.BIOCYC_P_COMPOUND_METABOLITE_LABEL = majorLabel;
	}

	@Override
	protected void configureAdditionalPropertyLinks(
			CentralMetaboliteEntity centralMetaboliteEntity,
			BioCycMetaboliteEntity entity) {
		
		centralMetaboliteEntity.addPropertyEntity(
				this.buildPropertyLinkPair(
						PROPERTY_UNIQUE_KEY, 
						entity.getInchi(), 
						METABOLITE_INCHI_LABEL, 
						METABOLITE_INCHI_RELATIONSHIP_TYPE));
		centralMetaboliteEntity.addPropertyEntity(
				this.buildPropertyLinkPair(
						PROPERTY_UNIQUE_KEY, 
						entity.getSmiles(), 
						METABOLITE_SMILE_LABEL, 
						METABOLITE_SMILE_RELATIONSHIP_TYPE));
		for (String parent : entity.getParents()) {
			centralMetaboliteEntity.addPropertyEntity(
					this.buildPropertyLinkPair(
							PROPERTY_UNIQUE_KEY, 
							parent, 
							SUPER_METABOLITE_LABEL, 
							METABOLITE_INSTANCE_RELATIONSHIP_TYPE));
		}
	}
	
	@Override
	protected void configureNameLink(
			CentralMetaboliteEntity centralMetaboliteEntity,
			BioCycMetaboliteEntity entity) {
		
		for (String name : entity.getSynonyms()) {
			centralMetaboliteEntity.addPropertyEntity(
					this.buildPropertyLinkPair(
							PROPERTY_UNIQUE_KEY, 
							name, 
							METABOLITE_NAME_LABEL, 
							METABOLITE_NAME_RELATIONSHIP_TYPE));
		}
		
		super.configureNameLink(centralMetaboliteEntity, entity);
	}

	@Override
	protected void configureCrossreferences(
			CentralMetaboliteEntity centralMetaboliteEntity,
			BioCycMetaboliteEntity entity) {

		List<CentralMetaboliteProxyEntity> crossreferences = new ArrayList<> ();
		
		for (BioCycMetaboliteCrossreferenceEntity xref : entity.getCrossreferences()) {
			String dbLabel = BioDbDictionary.translateDatabase(xref.getRef());
			String dbEntry = xref.getValue(); //Also need to translate if necessary
			CentralMetaboliteProxyEntity proxy = new CentralMetaboliteProxyEntity();
			proxy.setEntry(dbEntry);
			proxy.setMajorLabel(dbLabel);
			proxy.putProperty("reference", xref.getRef());
			proxy.addLabel(METABOLITE_LABEL);
			crossreferences.add(proxy);
		}
		
		centralMetaboliteEntity.setCrossreferences(crossreferences);
	}

}