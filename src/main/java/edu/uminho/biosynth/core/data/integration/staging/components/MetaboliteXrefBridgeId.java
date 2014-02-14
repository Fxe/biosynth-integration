package edu.uminho.biosynth.core.data.integration.staging.components;

// Generated 13-Feb-2014 16:16:06 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MetaboliteXrefBridgeId generated by hbm2java
 */
@Embeddable
public class MetaboliteXrefBridgeId implements java.io.Serializable {

	private int xrefGroupId;
	private int xrefId;

	public MetaboliteXrefBridgeId() {
	}

	public MetaboliteXrefBridgeId(int xrefGroupId, int xrefId) {
		this.xrefGroupId = xrefGroupId;
		this.xrefId = xrefId;
	}

	@Column(name = "xref_group_id", nullable = false)
	public int getXrefGroupId() {
		return this.xrefGroupId;
	}

	public void setXrefGroupId(int xrefGroupId) {
		this.xrefGroupId = xrefGroupId;
	}

	@Column(name = "xref_id", nullable = false)
	public int getXrefId() {
		return this.xrefId;
	}

	public void setXrefId(int xrefId) {
		this.xrefId = xrefId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MetaboliteXrefBridgeId))
			return false;
		MetaboliteXrefBridgeId castOther = (MetaboliteXrefBridgeId) other;

		return (this.getXrefGroupId() == castOther.getXrefGroupId())
				&& (this.getXrefId() == castOther.getXrefId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getXrefGroupId();
		result = 37 * result + this.getXrefId();
		return result;
	}

}