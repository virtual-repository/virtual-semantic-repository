package org.virtual.sr;

import java.util.Collection;

import org.virtualrepository.AssetType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

public class RepositoryBrowser implements Browser {

	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
