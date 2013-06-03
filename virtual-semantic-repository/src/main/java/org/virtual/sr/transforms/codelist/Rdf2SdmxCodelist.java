package org.virtual.sr.transforms.codelist;

import com.hp.hpl.jena.rdf.model.*;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.sr.transforms.Xml2Rdf;
import org.virtual.sr.transforms.XmlTransform;
import org.virtual.sr.utils.Constants;
import org.virtualrepository.Asset;
import org.virtualrepository.Properties;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Transform;

import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.virtual.sr.vocabularies.SKOS_CORE;

/**
 * A {@link Transform}s from arbitrary {@link Asset}s in given APIs to RDF. <p>
 * Bridges {@link XmlTransform}s with the {@link Xml2Rdf} transform to adapt the {@link RepositoryService}
 * to given asset types.
 *
 * @author Fabio Simeoni
 *
 */
public class Rdf2SdmxCodelist implements Transform<SdmxCodelist, Model, CodelistBean> {

    private static Logger log = LoggerFactory.getLogger(Rdf2SdmxCodelist.class);

    @Override
    public CodelistBean apply(SdmxCodelist asset, Model m) throws Exception {

        log.info("transforming codelist " + asset.id() + " to sdmx");

        CodelistMutableBean codelist = new CodelistMutableBeanImpl();

        Properties props = asset.properties();

        if (props.contains(Constants.ownerName)) {
            codelist.setAgencyId(props.lookup(Constants.ownerName).value(String.class));
        } else {
            codelist.setAgencyId("SDMX");
        }

        codelist.setId(asset.name());
        codelist.setUri(asset.id());
        codelist.addName("en", asset.name());

        ResIterator codes = m.listSubjectsWithProperty(SKOS_CORE.inScheme);

        while (codes.hasNext()) {

            Resource code_resource = codes.next();

            CodeMutableBean code = new CodeMutableBeanImpl();

            code.setId(adaptId(code_resource.getRequiredProperty(SKOS_CORE.notation).getLiteral().getLexicalForm()));
            code.setUri(code_resource.getURI());
            ResIterator subjectEntities = m.listSubjectsWithProperty(DCTerms.subject, code_resource);

            while (subjectEntities.hasNext()) {
                Resource subjectEntity = subjectEntities.next();
                NodeIterator labels = m.listObjectsOfProperty(subjectEntity, SKOS_CORE.prefLabel);
                while (labels.hasNext()) {
                    Literal label = labels.next().asLiteral();
                    code.addName(label.getLanguage(), label.getLexicalForm());
                }
            }

            NodeIterator definitions = m.listObjectsOfProperty(code_resource, SKOS_CORE.definition);
            while (definitions.hasNext()) {
                Literal def = definitions.next().asLiteral();
                code.addDescription(def.getLanguage(), def.getLexicalForm());
            }

            codelist.addItem(code);
        }

        return codelist.getImmutableInstance();
    }

    @Override
    public Class<Model> inputAPI() {
        return Model.class;
    }

    @Override
    public Class<CodelistBean> outputAPI() {
        return CodelistBean.class;
    }

    //helpers
    private String adaptId(String id) {

        //TODO add to this simple adaptation
        return id.replace(".", "_");
    }
}
