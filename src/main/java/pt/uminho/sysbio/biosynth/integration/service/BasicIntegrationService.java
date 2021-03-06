package pt.uminho.sysbio.biosynth.integration.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pt.uminho.sysbio.biosynth.integration.io.dao.IntegrationMetadataDao;
import edu.uminho.biosynth.core.data.integration.chimera.domain.IntegratedCluster;
import edu.uminho.biosynth.core.data.integration.chimera.domain.IntegrationSet;

public class BasicIntegrationService implements IntegrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicIntegrationService.class);
	
	@Autowired
	protected IntegrationMetadataDao meta;
	
	public IntegrationMetadataDao getMeta() { return meta;}
	public void setMeta(IntegrationMetadataDao meta) { this.meta = meta;}

	@Override
	public IntegrationSet getIntegrationSetByEntry(String entry) {
		IntegrationSet integrationSet = this.meta.getIntegrationSet(entry);
		
		if (integrationSet == null) {
			LOGGER.warn(String.format("Integration Set [%s] not found", entry));
			return null;
		}
		
		return integrationSet;
	}
	@Override
	public IntegrationSet getIntegrationSetById(Long id) {
		IntegrationSet integrationSet = this.meta.getIntegrationSet(id);
		
		if (integrationSet == null) {
			LOGGER.warn(String.format("Integration Set [%d] not found", id));
			return null;
		}
		
		return integrationSet;
	}
	@Override
	public IntegrationSet createIntegrationSet(String name, String description) {
		IntegrationSet integrationSet = new IntegrationSet();
		integrationSet.setName(name);
		integrationSet.setDescription(description);
		meta.saveIntegrationSet(integrationSet);
		return integrationSet;
	}

	@Override
	public void resetIntegrationSet(IntegrationSet integrationSet) {
		LOGGER.info(String.format("Reset atempt to Integration Set %s", integrationSet));
		
		List<Long> clusters = new ArrayList<> (integrationSet.getIntegratedClustersMap().keySet());
		for (Long clusterId : clusters){
			IntegratedCluster cluster = integrationSet.getIntegratedClustersMap().remove(clusterId);
			LOGGER.info(String.format("Removing cluster %s", cluster));
			this.meta.deleteCluster(cluster);
		}
		
//		this.meta.saveIntegrationSet(currentIntegrationSet);
	}

	@Override
	public void deleteIntegrationSet(IntegrationSet integrationSet) {
		this.meta.deleteIntegrationSet(integrationSet);
	}

	@Override
	public List<IntegrationSet> getAllIntegrationSets() {
		List<IntegrationSet> res = new ArrayList<> ();
		for (Long id : this.meta.getAllIntegrationSetsId()) {
			res.add(this.meta.getIntegrationSet(id));
		}
		return res;
	}

}
