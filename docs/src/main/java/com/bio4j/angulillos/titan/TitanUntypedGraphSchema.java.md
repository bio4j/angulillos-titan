
```java
package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;
import static com.bio4j.angulillos.titan.TitanConversions.*;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

public class TitanUntypedGraphSchema
implements UntypedGraphSchema<TitanManagement> {

  // TODO: create if not exists?

```

This method should take into account vertex label

```java
  public TitanManagement createVertexType(TitanManagement titanManagement, AnyVertexType vertexType) {
    titanManagement
      .makeVertexLabel(vertexType._label())
      // .setStatic() ?
      .make();
    return titanManagement;
  }
```

This method should take into account edge label, source/target types and to/from-arities

```java
  public TitanManagement createEdgeType(TitanManagement titanManagement, AnyEdgeType edgeType) {
    titanManagement
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
    return titanManagement;
  }
```

This method should take into account property's element type and from-arity

```java
  public TitanManagement createProperty(TitanManagement titanManagement, AnyProperty property) {
    titanManagement
      .makePropertyKey(property._label())
      .cardinality( Cardinality.SINGLE )
      .dataType( property.valueClass() )
      .make();
    return titanManagement;
  }
```


**IMPORTANT** before creating an index the property must be already created. Do not forget to commit the `titanManagement` instance *after* you have created everything you wanted. See the Titan docs for more.


```java
  @Override
  public TitanManagement createUniqueVertexIndex(
    TitanManagement titanManagement,
    TypedVertexIndex.Unique<?,?,?,?,?,?> index
  )
  {

    titanManagement
      .buildIndex( index._label(), TitanVertex.class )
      .addKey( titanManagement.getPropertyKey( index.property()._label() ) )
      .unique()
      .buildCompositeIndex()
    ;

    return titanManagement;
  }

  @Override
  public TitanManagement createNonUniqueVertexIndex(
    TitanManagement titanManagement,
    TypedVertexIndex.NonUnique<?,?,?,?,?,?> index
  )
  {

    titanManagement
      .buildIndex( index._label(), TitanVertex.class )
      .addKey( titanManagement.getPropertyKey( index.property()._label() ) )
      .buildCompositeIndex()
    ;

    return titanManagement;
  }

  @Override
  public TitanManagement createUniqueEdgeIndex(
    TitanManagement titanManagement,
    TypedEdgeIndex.Unique<?,?,?,?,?,?> index
  )
  {

    titanManagement
      .buildIndex( index._label(), TitanEdge.class )
      .addKey( titanManagement.getPropertyKey( index.property()._label() ) )
      .unique()
      .buildCompositeIndex()
    ;

    return titanManagement;
  }

  @Override
  public TitanManagement createNonUniqueEdgeIndex(
    TitanManagement titanManagement,
    TypedEdgeIndex.NonUnique<?,?,?,?,?,?> index
  )
  {

    titanManagement
      .buildIndex( index._label(), TitanEdge.class )
      .addKey( titanManagement.getPropertyKey( index.property()._label() ) )
      .buildCompositeIndex()
    ;

    return titanManagement;
  }



}

```




[main/java/com/bio4j/angulillos/titan/TitanConversions.java]: TitanConversions.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraphSchema.java]: TitanUntypedGraphSchema.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]: TitanUntypedGraph.java.md