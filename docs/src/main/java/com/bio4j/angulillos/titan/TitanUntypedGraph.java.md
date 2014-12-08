
```java
package com.bio4j.angulillos.titan;

import java.util.stream.Stream;
import java.util.Iterator;
import java.util.Optional;

import com.bio4j.angulillos.*;
import static com.bio4j.angulillos.conversions.*;
import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.core.TitanEdge;
import com.thinkaurelius.titan.core.EdgeLabel;
import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

public interface TitanUntypedGraph extends UntypedGraph<TitanVertex,PropertyKey,TitanEdge,EdgeLabel> {

  TitanGraph titanGraph();

  default void commit() { titanGraph().commit(); }

  default void shutdown() { titanGraph().shutdown(); }

  @Override
  default TitanEdge addEdge(TitanVertex from, EdgeLabel edgeType, TitanVertex to) {

    return from.addEdge( edgeType, to );
  }

  @Override
  default TitanVertex addVertex(PropertyKey type) {

    return (TitanVertex) titanGraph().addVertex(null); 
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
  default Optional<Stream<TitanEdge>> out(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.OUT, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanVertex>> outV(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.OUT, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map( e -> e.getVertex(com.tinkerpop.blueprints.Direction.IN) ) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanEdge>> in(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.IN, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ) );

    } else {

      return Optional.empty();
    }
  }

  @Override
  default Optional<Stream<TitanVertex>> inV(TitanVertex vertex, EdgeLabel edgeType) {

    Iterable<TitanEdge> itb = vertex.getTitanEdges(com.tinkerpop.blueprints.Direction.IN, edgeType);

    if ( itb.iterator().hasNext() ) {

      return Optional.of( stream( itb ).map( e -> e.getVertex(com.tinkerpop.blueprints.Direction.OUT) ) );

    } else {

      return Optional.empty();
    }
  }


  // create types

```


creates a key in the graph using the provided `KeyMaker` and `name` if there is no such `PropertyKey` with that `name`; otherwise it returns the existing `PropertyKey` with the provided `name`.


```java
  default PropertyKey createOrGet(KeyMaker keyMaker, String name) {

    Boolean isNotDefined = true;

    PropertyKey key = null;
    // first see if there's such a thing there
    Iterator<PropertyKey> definedKeys = titanGraph().getTypes(PropertyKey.class).iterator();

    while( definedKeys.hasNext() ) {

      PropertyKey someKey = definedKeys.next();

      if ( someKey.getName().equals(name) ) { 
        
        isNotDefined = false;
        key = someKey;
      }
    }

    if( isNotDefined ) {

      key = keyMaker.make();
    }

    return key;
  }
```


creates a label in the graph using the provided `LabelMaker` and `name` if there is no such `EdgeLabel` with that `name`; otherwise it returns the existing `EdgeLabel` with the provided `name`.


```java
  public default EdgeLabel createOrGet(LabelMaker labelMaker, String name) {

    Boolean isNotDefined = true;

    EdgeLabel label = null;

    // first see if there's such a thing there
    Iterator<EdgeLabel> definedLabels = titanGraph().getTypes(EdgeLabel.class).iterator();

    while( definedLabels.hasNext() ) {

      EdgeLabel someLabel = definedLabels.next();

      if ( someLabel.getName().equals(name) ) { 
        
        isNotDefined = false;
        label = someLabel;
      }
    }

    if( isNotDefined ) {

      label = labelMaker.make();
    }

    return label;
  }
```


Get a `KeyMaker` already configured for creating the key corresponding to a node type. You can use this for defining the corresponding `...`.


```java
  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  KeyMaker titanKeyMakerForVertexType(P property) {

    // note how here we take the full name so that this is scoped by the node type; see `Property`.
    KeyMaker keyMaker = titanGraph().makeKey(property.name())
      .dataType(property.valueClass())
      .indexed(com.tinkerpop.blueprints.Vertex.class)
      .unique()
	  .single();

    return keyMaker;
  }
```


create a `PropertyKey` for a node type, using the default configuration. If a type with the same name is present it will be returned instead.


```java
  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  > 
  PropertyKey titanKeyForVertexType(P property) {

    return createOrGet(titanKeyMakerForVertexType(property), property.name());
  }
```


Create a LabelMaker with the minimum default for a relationship type; you should use this for defining the corresponding `TitanTitanTypedEdge.Type`. This is a `LabelMaker` so that you can define any custom signature, indexing etc.


```java
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    SG extends TypedGraph<SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
    G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>
  >
  LabelMaker titanLabelMakerForEdgeType(RT relationshipType) {

    LabelMaker labelMaker = titanGraph().makeLabel(relationshipType.name())
      .directed();

    // define the arity
    switch (relationshipType.arity()) {

      case oneToOne:    labelMaker.oneToOne(); 
      case oneToMany:   labelMaker.oneToMany();
      case manyToOne:   labelMaker.manyToOne();
      case manyToMany:  labelMaker.manyToMany();
    }

    return labelMaker;
  }
```


create a `EdgeLabel` for a relationship type, using the default configuration. If a type with the same name is present it will be returned instead.


```java
  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    SG extends TypedGraph<SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
    G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>
  >
  EdgeLabel titanLabelForEdgeType(RT relationshipType) {

    return createOrGet(titanLabelMakerForEdgeType(relationshipType), relationshipType.name());
  }


  default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  KeyMaker titanKeyMakerForVertexProperty(P property) {

    return titanGraph().makeKey(property.name())
      // .indexed(com.tinkerpop.blueprints.Edge.class)
      .dataType(property.valueClass());
  }

  default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    SG extends TypedGraph<SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, V,
    // graph
    G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, I extends TitanUntypedGraph,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>  
  >
  KeyMaker titanKeyMakerForEdgeProperty(P property) {

    return titanGraph().makeKey(property.name())
      // .indexed(com.tinkerpop.blueprints.Edge.class)
      .dataType(property.valueClass());
  }

	default <
			// src
			S extends TypedVertex<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			SG extends TypedGraph<SG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			// edge
			R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
			RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel,T,TT,TG>,
			// property
			P extends Property<R,RT,P,V,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, V,
			// graph
			G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, I extends TitanUntypedGraph,
			//tgt
			T extends TypedVertex<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			TG extends TypedGraph<TG,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>
			>
	PropertyKey titanKeyForEdgePropertySingle(P property) {

		return createOrGet(titanKeyMakerForEdgeProperty(property).single(), property.name());
	}
```


		create a `PropertyKey` for a single vertex property, using the default configuration. If a property with the same name is present it will be returned instead.


```java
	default <
			N extends TypedVertex<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			P extends Property<N,NT,P,V,G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>, V,
			G extends TypedGraph<G,I,TitanVertex,PropertyKey,TitanEdge,EdgeLabel>,
			I extends TitanUntypedGraph
			>
	PropertyKey titanKeyForVertexPropertySingle(P property) {

		return createOrGet(titanKeyMakerForVertexProperty(property).single(), property.name());
	}

}

```


------

### Index

+ src
  + test
    + java
      + com
        + bio4j
          + angulillos
            + titan
              + [TitanGoGraph.java][test/java/com/bio4j/angulillos/titan/TitanGoGraph.java]
  + main
    + java
      + com
        + bio4j
          + angulillos
            + titan
              + [TitanTypedEdgeIndex.java][main/java/com/bio4j/angulillos/titan/TitanTypedEdgeIndex.java]
              + [TitanTypedVertexIndex.java][main/java/com/bio4j/angulillos/titan/TitanTypedVertexIndex.java]
              + [TitanUntypedGraph.java][main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]

[test/java/com/bio4j/angulillos/titan/TitanGoGraph.java]: ../../../../../../test/java/com/bio4j/angulillos/titan/TitanGoGraph.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedEdgeIndex.java]: TitanTypedEdgeIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedVertexIndex.java]: TitanTypedVertexIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]: TitanUntypedGraph.java.md