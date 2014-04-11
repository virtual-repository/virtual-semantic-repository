package org.acme;

import org.junit.Test;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;

public class BrowserIntegrationTests {

    @Test
    public void discoverSdmxCodelists() {

        VirtualRepository repo = new Repository();

        repo.discover(SdmxCodelist.type);
        for (Asset asset : repo) {
            System.out.println("asset id: " + asset.id());
            System.out.println("asset name: " + asset.name());
            System.out.println("asset name: " + asset.properties().lookup("name").value().toString());
            System.out.println("#");
        }

    }
}
