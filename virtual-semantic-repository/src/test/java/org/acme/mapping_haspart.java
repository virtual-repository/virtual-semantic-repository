/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme;

import static org.fao.fi.comet.mapping.dsl.DataProviderDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDetailDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementIdentifierDSL.*;
import static org.fao.fi.comet.mapping.model.utils.jaxb.JAXB2DOMUtils.*;

import java.net.URI;
import java.util.Date;

import org.apache.jena.web.DatasetGraphAccessorHTTP;
import org.fao.fi.comet.mapping.model.Mapping;
import org.fao.fi.comet.mapping.model.MappingData;
import org.fao.fi.comet.mapping.model.MappingElement;
import org.virtual.sr.RepositoryPlugin;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.comet.CometAsset;
import org.virtualrepository.impl.Repository;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DCTerms;

/**
 *
 * @author Claudio Baldassarre <c.baldassarre@me.com>
 */
public class mapping_haspart {

    public static void main(String[] args) {
        MappingData md = makeMapping();
        stageMapping(md);
    }

    private static MappingData makeMapping() {
        DatasetGraphAccessorHTTP accessor = new DatasetGraphAccessorHTTP("http://168.202.3.223:3030/sr_public/data");
        Graph fao_major_areas = accessor.httpGet(NodeFactory.createURI("http://semanticrepository/public/graph/CL_FAO_MAJOR_AREA"));
        Node area21 = fao_major_areas.find(Node.ANY, Node.ANY, NodeFactory.createURI("http://www.fao.org/figis/flod/entities/statarea/21")).next().getSubject();
        Graph sub_areas_graph = accessor.httpGet(NodeFactory.createURI("http://semanticrepository/public/graph/CL_SUB_AREA"));
        ExtendedIterator<Triple> sub_areas = sub_areas_graph.find(Node.ANY, DCTerms.subject.asNode(), Node.ANY);

        MappingData mappingData = new MappingData();
        mappingData.
                linking(provider(URI.create("urn:provider:source"), URI.create("urn:source:type"), URI.create("urn:source:data:bar"))).
                to(provider(URI.create("urn:provider:target"), URI.create("urn:target:type"), URI.create("urn:target:data:bar"))).
                version("1.0").
		producedBy("Semantic Repository Plugin").
		on(new Date());

        Mapping mappingEntry;

        MappingElement target, source = wrap(asElement(GenericTerm.describing(area21.getLocalName()))).with(identifierFor(area21.toString()));

        mappingEntry = map(source);
        
        while (sub_areas.hasNext()) {
            Triple triple = sub_areas.next();
            target = wrap(asElement(GenericTerm.describing(triple.getObject().getLocalName()))).with(identifierFor(triple.getSubject().toString()));
            mappingEntry = mappingEntry.to(target(target));

        }
        mappingData.include(mappingEntry);
//        System.out.println(JAXBDeSerializationUtils.toXML(mappingData));

        return mappingData;
    }

    private static void stageMapping(MappingData md) {
        VirtualRepository repo = new Repository();

        RepositoryService service = repo.services().lookup(RepositoryPlugin.name);

        CometAsset asset = new CometAsset("area_haspart_subarea", service);
        
        repo.publish(asset, md);
        
        repo.shutdown();
    }

}
