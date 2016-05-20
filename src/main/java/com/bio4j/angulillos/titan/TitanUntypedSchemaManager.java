package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;
import static com.bio4j.angulillos.titan.TitanConversions.*;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

import org.apache.tinkerpop.gremlin.structure.Direction;


public class TitanUntypedSchemaManager
implements UntypedGraphSchema<SchemaManager> {

  // TODO: create if not exists?

  /* This method should take into account vertex label */
  public SchemaManager createVertexType(SchemaManager schemaManager, AnyVertexType vertexType) {
    schemaManager
      .makeVertexLabel(vertexType._label())
      // .setStatic() ?
      .make();
    return schemaManager;
  }

  /* This method should take into account edge label, source/target types and to/from-arities */
  public SchemaManager createEdgeType(SchemaManager schemaManager, AnyEdgeType edgeType) {
    schemaManager
      .makeEdgeLabel(edgeType._label())
      .directed()
      .multiplicity(
        Arities.asTitanMultiplicity(
          edgeType.fromArity(),
          edgeType.toArity()
        )
      )
      // TODO: for making signature, we have to create all edge's properties beforehand
      // .signature(edgeType.properties...)
      .make();
    return schemaManager;
  }

  /* This method should take into account property's element type and from-arity */
  public SchemaManager createProperty(SchemaManager schemaManager, AnyProperty property) {
    schemaManager
      .makePropertyKey(property._label())
      .cardinality( Cardinality.SINGLE )
      .dataType( property.valueClass() )
      .make();
    return schemaManager;
  }

  /*
    **IMPORTANT** before creating an index the property must be already created. Do not forget to commit the `titanManagement` instance *after* you have created everything you wanted. See the Titan docs for more.
  */
  public TitanManagement createUniqueIndex(TitanManagement titanManagement, TitanTypedVertexIndex.Unique<?,?,?,?> index) {

    titanManagement
      .buildIndex( index.name(), TitanVertex.class )
      .addKey( titanManagement.getPropertyKey( index.property()._label() ) )
      .unique()
      .buildCompositeIndex()
    ;

    return titanManagement;
  }

}
