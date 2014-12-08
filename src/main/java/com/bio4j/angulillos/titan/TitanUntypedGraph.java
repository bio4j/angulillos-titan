package com.bio4j.angulillos.titan;

import java.util.stream.Stream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.bio4j.angulillos.*;
import static com.bio4j.angulillos.conversions.*;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;
import com.tinkerpop.blueprints.Edge;

public interface TitanUntypedGraph extends UntypedGraph<TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker> {

  TitanGraph titanGraph();
  default TitanManagement managementSystem() { return titanGraph().getManagementSystem(); }

  default void commit() { titanGraph().commit(); }

  default void shutdown() { titanGraph().commit(); titanGraph().shutdown(); }

  @Override
  default TitanEdge addEdge(TitanVertex from, EdgeLabelMaker edgeType, TitanVertex to) {

    return from.addEdge( edgeType.getName(), to );
  }

  @Override
  default TitanVertex addVertex(VertexLabelMaker type) {

    return (TitanVertex) titanGraph().addVertexWithLabel(type.getName()); 
  }

  @Override
  default <V> V getPropertyV(TitanVertex vertex, String property) {

    return vertex.<V>getProperty(property);
  }

  @Override
  default <V> void setPropertyV(TitanVertex vertex, String property, V value) {

    vertex.setProperty(property, value);
  }

  @Override
  default <V> V getPropertyE(TitanEdge edge, String property) {

    return edge.<V>getProperty(property);
  }

  @Override
  default <V> void setPropertyE(TitanEdge edge, String property, V value) {

    edge.setProperty(property, value);
  }

  @Override
  default TitanVertex source(TitanEdge edge) {

    return edge.getVertex(com.tinkerpop.blueprints.Direction.OUT);
  }

  @Override
  default TitanVertex target(TitanEdge edge) {

    return edge.getVertex(com.tinkerpop.blueprints.Direction.IN);
  }

  @Override
  default Optional<Stream<TitanEdge>> out(TitanVertex vertex, EdgeLabelMaker edgeType) {

    Iterable<Edge> itb = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT, edgeType.getName());

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map( e -> (TitanEdge) e) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanVertex>> outV(TitanVertex vertex, EdgeLabelMaker edgeType) {

    Iterable<Edge> itb = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT, edgeType.getName());

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map( e -> ( (TitanEdge) e).getVertex(com.tinkerpop.blueprints.Direction.IN) ) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanEdge>> in(TitanVertex vertex, EdgeLabelMaker edgeType) {

    Iterable<Edge> itb = vertex.getEdges(com.tinkerpop.blueprints.Direction.IN, edgeType.getName());

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map(e -> (TitanEdge) e) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanVertex>> inV(TitanVertex vertex, EdgeLabelMaker edgeType) {

    Iterable<Edge> itb = vertex.getEdges(com.tinkerpop.blueprints.Direction.IN, edgeType.getName());

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map( e -> ((TitanEdge) e).getVertex(com.tinkerpop.blueprints.Direction.OUT) ) );

    } else {

      return Optional.empty();
    }
  }


  // create types
  /*
    creates a key in the graph using the provided `KeyMaker` and `name` if there is no such `PropertyKey` with that `name`; otherwise it returns the existing `PropertyKey` with the provided `name`.

    The `TitanManagement` argument should be the one that was used to create `labelMaker`.
  */
  default VertexLabel createOrGet(TitanManagement mgmt, VertexLabelMaker labelMaker) {

    VertexLabel vertexLabel;
    String name = labelMaker.getName();

    if ( mgmt.containsVertexLabel(name) ) {

      vertexLabel = mgmt.getVertexLabel(name);
    }
    else {

      vertexLabel = labelMaker.make();
    }

    return vertexLabel;
  }

  /*
    creates a label in the graph using the provided `LabelMaker` and `name` if there is no such `EdgeLabel` with that `name`; otherwise it returns the existing `EdgeLabel` with the provided `name`.
  */
  default EdgeLabel createOrGet(TitanManagement mgmt, EdgeLabelMaker labelMaker) {

    EdgeLabel edgeLabel;
    String name = labelMaker.getName();

    if ( mgmt.containsEdgeLabel(name) ) {

      edgeLabel = mgmt.getEdgeLabel(name);
    }
    else {

      edgeLabel = labelMaker.make();
    }

    return edgeLabel;
  }

  default PropertyKey createOrGet(TitanManagement mgmt, PropertyKeyMaker propertyMaker) {

    PropertyKey propertyKey;
    String name = propertyMaker.getName();

    if ( mgmt.containsPropertyKey(name) ) {

      propertyKey = mgmt.getPropertyKey(name);
    }
    else {

      propertyKey = propertyMaker.make();
    }

    return propertyKey;
  }



  /*
  Get a `VertexLabelMaker` for a vertex type
  */
  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  >
  VertexLabelMaker titanLabelMakerForVertexType(TitanManagement mgmt, NT vertexType) {

    // TODO: evaluate partition() and setStatic()
    VertexLabelMaker labelMaker = mgmt.makeVertexLabel(vertexType.name());

    return labelMaker;
  }

  /*
  Create a `LabelMaker` with the minimum defaults (name, arity and directed) for an edge type. As this is a `LabelMaker`, you can further refine it and define its signature, indexing etc. 
  */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
  >
  EdgeLabelMaker titanLabelMakerForEdgeType(TitanManagement mgmt, RT relationshipType) {

    EdgeLabelMaker labelMaker = mgmt.makeEdgeLabel(relationshipType.name())
      .directed();

    // define the arity
    switch (relationshipType.arity()) {

      case oneToOne:    labelMaker.multiplicity(Multiplicity.ONE2ONE); 
      case oneToMany:   labelMaker.multiplicity(Multiplicity.ONE2MANY);
      case manyToOne:   labelMaker.multiplicity(Multiplicity.MANY2ONE);
      case manyToMany:  labelMaker.multiplicity(Multiplicity.MULTI);
    }

    return labelMaker;
  }

  // see http://s3.thinkaurelius.com/docs/titan/0.5.1/data-model.html#_individual_edge_layout for why you might want this
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    // graph
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
  >
  EdgeLabelMaker titanLabelMakerForEdgeTypeWithProperties(TitanManagement mgmt, RT edgeType, PropertyKey[] propertyKeys) {

    // get the EdgeLabelMaker
    EdgeLabelMaker lblmkr = titanLabelMakerForEdgeType(mgmt, edgeType);

    lblmkr.signature(propertyKeys);
    return lblmkr;
  }

  /*
    create an `EdgeLabel` for an type, using the default configuration. If a type with the same name is present it will be returned instead.
  */
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
  >
  EdgeLabel titanLabelForEdgeType(TitanManagement mgmt, RT relationshipType) {

    // TODO: check that arities etc are ok, throw if not
    return createOrGet(mgmt, titanLabelMakerForEdgeType(mgmt, relationshipType));
  }


  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  >
  PropertyKeyMaker titanPropertyMakerForVertexProperty(TitanManagement mgmt, P property) {

    PropertyKeyMaker pkm = mgmt.makePropertyKey(property.name()).dataType(property.valueClass());
    
    return pkm;
  }

  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    // graph
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>  
  >
  PropertyKeyMaker titanPropertyMakerForEdgeProperty(TitanManagement mgmt, P property) {

    PropertyKeyMaker pkm = mgmt.makePropertyKey(property.name()).dataType(property.valueClass());

    return pkm;
  }

  // default <
  //    // src
  //    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    // edge
  //    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
  //    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
  //    // property
  //    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
  //    // graph
  //    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, I extends TitanUntypedGraph,
  //    //tgt
  //    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
  //    >
  // PropertyKey titanKeyForEdgePropertySingle(P property) {

  //  return createOrGet(titanKeyMakerForEdgeProperty(property).single(), property.name());
  // }

  // /*
  //  create a `PropertyKey` for a single vertex property, using the default configuration. If a property with the same name is present it will be returned instead.
  //   */
  // default <
  //    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
  //    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //    I extends TitanUntypedGraph
  //    >
  // PropertyKey titanKeyForVertexPropertySingle(P property) {

  //  return createOrGet(titanKeyMakerForVertexProperty(property).single(), property.name());
  // }

}
