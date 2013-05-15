package org.virtual.sr;

import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Importer;

public class SdmxImporter implements Importer<SdmxCodelist,CodelistBean> {

	private final RepositoryConfiguration configuration;

	public SdmxImporter(RepositoryConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Type<SdmxCodelist> type() {
		return SdmxCodelist.type;
	}

	@Override
	public Class<CodelistBean> api() {
		return CodelistBean.class;
	}

	@Override
	public CodelistBean retrieve(SdmxCodelist asset) throws Exception {
		
		// TODO Auto-generated method stub
		System.out.println(configuration);
		return null;
	}
}
