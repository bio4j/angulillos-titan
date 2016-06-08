
```java
package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

import org.apache.tinkerpop.gremlin.structure.Direction;

import java.util.stream.Stream;

public class TitanUntypedGraph
implements
  UntypedGraph.Transactional<TitanVertex, TitanEdge>,
  UntypedGraph.Transaction<TitanVertex, TitanEdge>,
  AutoCloseable
{
```

The property used by Titan to store vertex labels

```java
  private static final String LABEL = "label";

  private final TitanGraph titanGraph;
  public  final TitanGraph titanGraph() { return this.titanGraph; }

  public TitanUntypedGraph(TitanGraph titanGraph) {

    this.titanGraph = titanGraph;
  }

  public TitanManagement managementSystem() { return titanGraph.openManagement(); }
```


The transaction/Graph returned by this method is a thread-independent transaction; it can safely be used from different threads. See Titan transaction docs.


```java
  @Override
  public ConcurrentTransaction beginTx() { return new ConcurrentTransaction( titanGraph.newTransaction() ); }
```


This method will (try to) commit the implictly opened thread-local transaction.


```java
  @Override
  public void commit() { titanGraph.tx().commit(); }

  @Override
  public void shutdown() { titanGraph.close(); }

  @Override
  public void close() { titanGraph.tx().close(); }

  @Override
  public TitanUntypedGraph graph() { return this; }
```


This method will (try to) rollback the implictly opened thread-local transaction.


```java
  @Override
  public void rollback() { titanGraph.tx().rollback(); }
```


This class wraps a global threadsafe Titan transaction; it is a TitanUntypedGraph too, as this is the Titan API design. See the Titan docs for details.


```java
  public class ConcurrentTransaction extends TitanUntypedGraph {

    private final TitanTransaction rawTx;
    ConcurrentTransaction(TitanTransaction rawTx) {

      super(TitanUntypedGraph.this.titanGraph);
      this.rawTx = rawTx;
    }

    @Override
    public final ConcurrentTransaction graph() { return this; }
```


All methods here will work with *this* transaction, not the implicit one.


```java
    @Override
    public final void commit()    { rawTx.commit();   }
    @Override
    public final void rollback()  { rawTx.rollback(); }
    @Override
    public final void close()     { rawTx.close();    }
  }

  @Override
  public TitanEdge addEdge(TitanVertex source, AnyEdgeType edgeType, TitanVertex target) {

    return source.addEdge( edgeType._label(), target );
  }

  @Override
  public TitanVertex addVertex(AnyVertexType vertexType) {

    return titanGraph().addVertex(vertexType._label());
  }

  @Override
  public <V> V getPropertyV(TitanVertex vertex, AnyProperty property) {

    return vertex.<V>property(property._label()).value();
  }

  @Override
  public <V> TitanVertex setPropertyV(TitanVertex vertex, AnyProperty property, V value) {

    vertex.property(property._label(), value);
    return vertex;
  }

  @Override
  public <V> V getPropertyE(TitanEdge edge, AnyProperty property) {

    return edge.<V>property(property._label()).value();
  }

  @Override
  public <V> TitanEdge setPropertyE(TitanEdge edge, AnyProperty property, V value) {

    edge.property(property._label(), value);
    return edge;
  }

  @Override
  public TitanVertex source(TitanEdge edge) { return edge.outVertex(); }

  @Override
  public TitanVertex target(TitanEdge edge) { return edge.inVertex(); }

  @Override
  public Stream<TitanEdge> outE(TitanVertex vertex, AnyEdgeType edgeType) {

    return stream(
      vertex.query()
        .labels(edgeType._label())
        .direction(Direction.OUT)
        .edges()
    );
  }

  @Override
  public Stream<TitanVertex> outV(TitanVertex vertex, AnyEdgeType edgeType) {

    return stream(
      vertex.query()
        .labels(edgeType._label())
        .direction(Direction.OUT)
        .vertices()
    );
  }

  @Override
  public Stream<TitanEdge> inE(TitanVertex vertex, AnyEdgeType edgeType) {

    return stream(
      vertex.query()
        .labels(edgeType._label())
        .direction(Direction.IN)
        .edges()
    );
  }

  @Override
  public Stream<TitanVertex> inV(TitanVertex vertex, AnyEdgeType edgeType) {

    return stream(
      vertex.query()
        .labels(edgeType._label())
        .direction(Direction.IN)
        .vertices()
    );
  }

  @Override
  public <X> Stream<TitanVertex> queryVertices(AnyProperty p, QueryPredicate.Contain predicate, java.util.Collection<X> values) {

    return stream(
      titanGraph()
        .query()
        .has( LABEL, p.elementType()._label() )
        .has( p._label(), TitanConversions.Predicate.asTitanContain(predicate), values )
        .vertices()
    );
  }

  @Override
  public Stream<TitanVertex> vertices(AnyVertexType vertexType) {

    return stream(
      titanGraph()
        .query()
        .has( LABEL, vertexType._label() )
        .vertices()
    );
  }

  @Override
  public <X> Stream<TitanVertex> queryVertices(AnyProperty p, QueryPredicate.Compare predicate, X value) {

    if( predicate.equals(QueryPredicate.Compare.EQUAL) ) {

      Iterable<TitanVertex> vs = titanGraph()
        .query()
        .has( LABEL, p.elementType()._label() )
        .has( p._label(), value )
        .vertices()
      ;

      return stream(vs);
    }
    else {

      Iterable<TitanVertex> vs = titanGraph()
        .query()
        .has( LABEL, p.elementType()._label() )
        .has( p._label(), TitanConversions.Predicate.asTitanCmp(predicate), value )
        .vertices()
      ;

      return stream(vs);
    }
  }

  @Override
  public <X> Stream<TitanEdge> queryEdges(AnyProperty p, QueryPredicate.Contain predicate, java.util.Collection<X> values) {

    return stream(
      titanGraph()
        .query()
        .has( LABEL, p.elementType()._label() )
        .has( p._label(), TitanConversions.Predicate.asTitanContain(predicate), values )
        .edges()
    );
  }

  @Override
  public <X> Stream<TitanEdge> queryEdges(AnyProperty p, QueryPredicate.Compare predicate, X value) {

    if( predicate.equals(QueryPredicate.Compare.EQUAL) ) {

      Iterable<TitanEdge> es = titanGraph()
        .query()
        .has( LABEL, p.elementType()._label() )
        .has( p._label(), value )
        .edges()
      ;

      return stream(es);
    }
    else {

      Iterable<TitanEdge> es = titanGraph()
        .query()
        .has( LABEL, p.elementType()._label() )
        .has( p._label(), TitanConversions.Predicate.asTitanCmp(predicate), value )
        .edges()
      ;

      return stream(es);
    }
  }
}

```




[main/java/com/bio4j/angulillos/titan/TitanConversions.java]: TitanConversions.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraphSchema.java]: TitanUntypedGraphSchema.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]: TitanUntypedGraph.java.md