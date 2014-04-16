package org.acme;

import static org.sdmx.SdmxServiceFactory.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.acme.comet.Term;
import org.fao.fi.comet.vr.model.Element;
import org.fao.fi.comet.vr.model.ElementIdentifier;
import org.fao.fi.comet.vr.model.Mapping;
import org.fao.fi.comet.vr.model.MappingData;
import org.fao.fi.comet.vr.model.MappingDetail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.util.io.ReadableDataLocationTmp;
import org.virtual.sr.RepositoryPlugin;
import org.virtual.sr.transforms.Comet2Xml;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.fmf.CometAsset;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;

public class PublishIntegrationTests {

    @BeforeClass
    public static void setup() {

        System.setProperty("org.slf4j.simpleLogger.log.org.virtual.sr ", "trace");
    }

//    @Test
    public void publishSdmxCodelist() {

        InputStream stream = getClass().getClassLoader().getResourceAsStream("mini-asfis.xml");

        CodelistBean list = parser().parseStructures(new ReadableDataLocationTmp(stream)).
                getStructureBeans(false).getCodelists().iterator().next();

        VirtualRepository repo = new Repository();

        RepositoryService service = repo.services().lookup(RepositoryPlugin.name);

        SdmxCodelist asset = new SdmxCodelist("test", service);

        repo.publish(asset, list);

    }

    @Test
    public void publishFromRTMS() {

        SdmxServiceFactory.init();

        //start vr
        VirtualRepository repo = new Repository();

        //discover codelists in sdmx
        repo.discover(SdmxCodelist.type);

        //get first discovered codelist
        Iterator<Asset> it = repo.iterator();
        int idx = 0;
        while (it.hasNext() & idx<=5) {
            Asset discoveredAsset = it.next();

            try {
                //retrieve its content
                CodelistBean sdmx = repo.retrieve(discoveredAsset, CodelistBean.class);

                //get virtual semantic repo 
                RepositoryService service = repo.services().lookup(RepositoryPlugin.name);

                //create empty asset for publication
                SdmxCodelist publishAsset = new SdmxCodelist(discoveredAsset.name(), service);

                //public sdmx with virtual repo
                repo.publish(publishAsset, sdmx);
                idx++;
            } catch (Exception e) {
                continue;
            }
        }

    }

    @Test
    public void publishMapping() {

        VirtualRepository repo = new Repository();

        RepositoryService service = repo.services().lookup(RepositoryPlugin.name);

        CometAsset asset = new CometAsset(CometAsset.type, "test", service);
        
        MappingData<Term, Term> mappingData = this.getFakeMappingData();
        
        try {
        	System.out.println(new Comet2Xml(Term.class).emitXml(mappingData, asset));
        } catch(Throwable t) {
        	t.printStackTrace();
        }
        
        repo.publish(asset, mappingData);
    }
    
    private MappingData<Term, Term> getFakeMappingData() {
    	Term left1 = new Term("over-exploited");
        Term right11 = new Term("overexploited");
        Term right12 = new Term("ov-erexploited");

        Term left2 = new Term("under-exploited");
        Term right21 = new Term("underexploited");
        Term right22 = new Term("und-erexploited");
        Term right23 = new Term("un-derexploited");

        Element<Term> eLeft1 = new Element<Term>();
        eLeft1.setId(new ElementIdentifier("fooResourceStatus", "1"));
        eLeft1.setData(left1);
        
        Element<Term> eRight11 = new Element<Term>();
        eRight11.setId(new ElementIdentifier("barResourceStatus", "69"));
        eRight11.setData(right11);

        Element<Term> eRight12 = new Element<Term>();
        eRight12.setId(new ElementIdentifier("barResourceStatus", "96"));
        eRight12.setData(right12);
        
        Element<Term> eLeft2 = new Element<Term>();
        eLeft2.setId(new ElementIdentifier("fooResourceStatus", "2"));
        eLeft2.setData(left2);
        
        Element<Term> eRight21 = new Element<Term>();
        eRight21.setId(new ElementIdentifier("barResourceStatus", "70"));
        eRight21.setData(right21);

        Element<Term> eRight22 = new Element<Term>();
        eRight22.setId(new ElementIdentifier("barResourceStatus", "97"));
        eRight22.setData(right22);
        
        Element<Term> eRight23 = new Element<Term>();
        eRight23.setId(new ElementIdentifier("barResourceStatus", "98"));
        eRight23.setData(right23);

        Collection<MappingDetail<Term>> leftMappings1 = new ArrayList<MappingDetail<Term>>();
        leftMappings1.add(new MappingDetail<Term>(0.99, eRight11));
        leftMappings1.add(new MappingDetail<Term>(0.69, eRight12));

        Collection<MappingDetail<Term>> leftMappings2 = new ArrayList<MappingDetail<Term>>();
        leftMappings2.add(new MappingDetail<Term>(0.99, eRight21));
        leftMappings2.add(new MappingDetail<Term>(0.69, eRight22));
        leftMappings2.add(new MappingDetail<Term>(0.39, eRight23));

        Mapping<Term, Term> mapping1 = new Mapping<Term, Term>(eLeft1, leftMappings1);
        Mapping<Term, Term> mapping2 = new Mapping<Term, Term>(eLeft2, leftMappings2);
        
        Collection<Mapping<Term, Term>> mappings = new ArrayList<Mapping<Term, Term>>();
        mappings.add(mapping1);
        mappings.add(mapping2);
        
        MappingData<Term, Term> mappingData = new MappingData<Term, Term>();
        mappingData.setMappings(mappings);
        
        return mappingData;
    }
}
