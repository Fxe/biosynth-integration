package edu.uminho.biosynth.core.data.integration.staging.components;

// Generated 12-Feb-2014 16:59:46 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * MetaboliteXrefDim generated by hbm2java
 */
@Entity
@Table(name = "metabolite_xref_dim")
public class MetaboliteXrefDim implements java.io.Serializable {

	private int id;
	private String value;
	private String source;
	private Set<MetaboliteXrefGroup> metaboliteXrefGroups = new HashSet<MetaboliteXrefGroup>(
			0);

	public MetaboliteXrefDim() {
	}

	public MetaboliteXrefDim(int id) {
		this.id = id;
	}

	public MetaboliteXrefDim(int id, String value, String source,
			Set<MetaboliteXrefGroup> metaboliteXrefGroups) {
		this.id = id;
		this.value = value;
		this.source = source;
		this.metaboliteXrefGroups = metaboliteXrefGroups;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "source")
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "metaboliteXrefDim")
	public Set<MetaboliteXrefGroup> getMetaboliteXrefGroups() {
		return this.metaboliteXrefGroups;
	}

	public void setMetaboliteXrefGroups(
			Set<MetaboliteXrefGroup> metaboliteXrefGroups) {
		this.metaboliteXrefGroups = metaboliteXrefGroups;
	}

}