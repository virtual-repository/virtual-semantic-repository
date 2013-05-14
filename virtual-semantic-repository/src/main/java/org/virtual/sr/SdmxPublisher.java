package org.virtual.sr;

import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Publisher;

public class SdmxPublisher implements Publisher<SdmxCodelist,Void> {

	@Override
	public Type<SdmxCodelist> type() {
		return SdmxCodelist.type;
	}

	@Override
	public Class<Void> api() {
		return Void.class;
	}

	@Override
	public void publish(SdmxCodelist asset, Void content) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
