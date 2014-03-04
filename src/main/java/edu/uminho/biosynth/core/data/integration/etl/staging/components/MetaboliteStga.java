package edu.uminho.biosynth.core.data.integration.etl.staging.components;

// Generated 14-Feb-2014 20:21:31 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * MetaboliteStga generated by hbm2java
 */
@Entity
@Table(name = "metabolite_stga")
public class MetaboliteStga implements java.io.Serializable {

	private int id;
	private MetaboliteXrefGroupDim metaboliteXrefGroupDim;
	private MetaboliteNameGroupDim metaboliteNameGroupDim;
	private MetaboliteInchiDim metaboliteInchiDim;
	private MetaboliteSmilesDim metaboliteSmilesDim;
	private MetaboliteFormulaDim metaboliteFormulaDim;
	private MetaboliteServiceDim metaboliteServiceDim;
	private Integer numeryKey;
	private String textKey;
	private String comment;
	private String remark;
	
	@Column(name = "charge", length=255)
	private String charge;
	public String getCharge() { return charge;}
	public void setCharge(String charge) { this.charge = charge;}

	private String description;
	private String formula;
	private String gibbs;
	private String mass;
	private String class_;

	public MetaboliteStga() {
	}

	public MetaboliteStga(int id) {
		this.id = id;
	}

	public MetaboliteStga(int id,
			MetaboliteXrefGroupDim metaboliteXrefGroupDim,
			MetaboliteNameGroupDim metaboliteNameGroupDim,
			MetaboliteInchiDim metaboliteInchiDim,
			MetaboliteSmilesDim metaboliteSmilesDim,
			MetaboliteFormulaDim metaboliteFormulaDim,
			MetaboliteServiceDim metaboliteServiceDim, Integer numeryKey,
			String textKey, String comment, String remark, String description,
			String formula, String gibbs, String mass, String class_) {
		this.id = id;
		this.metaboliteXrefGroupDim = metaboliteXrefGroupDim;
		this.metaboliteNameGroupDim = metaboliteNameGroupDim;
		this.metaboliteInchiDim = metaboliteInchiDim;
		this.metaboliteSmilesDim = metaboliteSmilesDim;
		this.metaboliteFormulaDim = metaboliteFormulaDim;
		this.metaboliteServiceDim = metaboliteServiceDim;
		this.numeryKey = numeryKey;
		this.textKey = textKey;
		this.comment = comment;
		this.remark = remark;
		this.description = description;
		this.formula = formula;
		this.gibbs = gibbs;
		this.mass = mass;
		this.class_ = class_;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "xref_group_id")
	public MetaboliteXrefGroupDim getMetaboliteXrefGroupDim() {
		return this.metaboliteXrefGroupDim;
	}

	public void setMetaboliteXrefGroupDim(
			MetaboliteXrefGroupDim metaboliteXrefGroupDim) {
		this.metaboliteXrefGroupDim = metaboliteXrefGroupDim;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "name_group_id")
	public MetaboliteNameGroupDim getMetaboliteNameGroupDim() {
		return this.metaboliteNameGroupDim;
	}

	public void setMetaboliteNameGroupDim(
			MetaboliteNameGroupDim metaboliteNameGroupDim) {
		this.metaboliteNameGroupDim = metaboliteNameGroupDim;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inchi_id")
	public MetaboliteInchiDim getMetaboliteInchiDim() {
		return this.metaboliteInchiDim;
	}

	public void setMetaboliteInchiDim(MetaboliteInchiDim metaboliteInchiDim) {
		this.metaboliteInchiDim = metaboliteInchiDim;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "smiles_id")
	public MetaboliteSmilesDim getMetaboliteSmilesDim() {
		return this.metaboliteSmilesDim;
	}

	public void setMetaboliteSmilesDim(MetaboliteSmilesDim metaboliteSmilesDim) {
		this.metaboliteSmilesDim = metaboliteSmilesDim;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "formula_id")
	public MetaboliteFormulaDim getMetaboliteFormulaDim() {
		return this.metaboliteFormulaDim;
	}

	public void setMetaboliteFormulaDim(
			MetaboliteFormulaDim metaboliteFormulaDim) {
		this.metaboliteFormulaDim = metaboliteFormulaDim;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	public MetaboliteServiceDim getMetaboliteServiceDim() {
		return this.metaboliteServiceDim;
	}

	public void setMetaboliteServiceDim(
			MetaboliteServiceDim metaboliteServiceDim) {
		this.metaboliteServiceDim = metaboliteServiceDim;
	}

	@Column(name = "numery_key")
	public Integer getNumeryKey() {
		return this.numeryKey;
	}

	public void setNumeryKey(Integer numeryKey) {
		this.numeryKey = numeryKey;
	}

	@Column(name = "text_key")
	public String getTextKey() {
		return this.textKey;
	}

	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}

	@Column(name = "comment", length = 65534)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "remark", length = 65534)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "description", length = 65534)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "formula")
	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Column(name = "gibbs")
	public String getGibbs() {
		return this.gibbs;
	}

	public void setGibbs(String gibbs) {
		this.gibbs = gibbs;
	}

	@Column(name = "mass")
	public String getMass() {
		return this.mass;
	}

	public void setMass(String mass) {
		this.mass = mass;
	}

	@Column(name = "class")
	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

}