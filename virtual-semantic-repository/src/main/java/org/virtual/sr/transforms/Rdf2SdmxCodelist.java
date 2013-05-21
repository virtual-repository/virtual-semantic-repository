package org.virtual.sr.transforms;

import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Transform;

import com.hp.hpl.jena.query.ResultSet;

/**
 * A {@link Transform}s from arbitrary {@link Asset}s in given APIs to RDF.
 * <p>
 * Bridges {@link XmlTransform}s with the {@link Xml2Rdf} transform to adapt the {@link RepositoryService} to given asset types.
 * 
 * @author Fabio Simeoni
 *
 */
public class Rdf2SdmxCodelist implements Transform<SdmxCodelist,ResultSet,CodelistBean> {

	@SuppressWarnings("null")
	@Override
	public CodelistBean apply(SdmxCodelist asset, ResultSet content) throws Exception {
		
		CodelistMutableBean codelist = null;
		//TODO transform to SDMX CodelistBean;
		return codelist.getImmutableInstance(); 
	}

	@Override
	public Class<ResultSet> inputAPI() {
		return ResultSet.class;
	}

	@Override
	public Class<CodelistBean> outputAPI() {
		return CodelistBean.class;
	}
}
