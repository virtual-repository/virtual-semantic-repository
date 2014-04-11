package org.virtual.sr.transforms;

import javax.xml.transform.Source;

import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Transform;
import com.hp.hpl.jena.rdf.model.Model;
import org.virtual.sr.utils.Constants;

/**
 * A {@link Transform}s from arbitrary {@link Asset}s in given APIs to RDF.
 * <p>
 * Bridges {@link XmlTransform}s with the {@link Xml2Rdf} transform to adapt the
 * {@link RepositoryService} to given asset types.
 *
 * @author Fabio Simeoni
 *
 */
public class Asset2Rdf<A extends Asset, T> implements Transform<A, T, Model> {

    private final XmlTransform<T> transform;

    /**
     * Creates a new instance with an {@link XmlTransform}
     *
     * @param transform the transform
     */
    public Asset2Rdf(XmlTransform<T> transform) {
        this.transform = transform;
    }

    @Override
    public Model apply(A asset, T content) throws Exception {

        Xml2Rdf converter = new Xml2Rdf();

        Source source = null;

        try {
            source = transform.toXml(content, asset);
        } catch (Exception e) {
            throw new Exception("cannot transform " + asset.type() + " to XML (see cause)", e);
        }
        Model triplifiedModel = converter.triplify(source);
        return enrich(triplifiedModel, asset.id());
    }

    @Override
    public Class<T> inputAPI() {
        return transform.api();
    }

    @Override
    public Class<Model> outputAPI() {
        return Model.class;
    }

    private Model enrich(Model m, String id) {
        m.add(m.createResource(Constants.pseudoNS+"content"), m.createProperty(Constants.pseudoNS+"content"), m.getResource(id));
        return m;
    }
}
