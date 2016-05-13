
```java
package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

import org.apache.tinkerpop.gremlin.structure.Direction;

import java.util.stream.Stream;
import java.util.Optional;


public class TitanUntypedGraph
implements
  UntypedGraph<TitanVertex, TitanEdge>
{
  private final TitanGraph titanGraph;
  public  final TitanGraph titanGraph() { return this.titanGraph; }

  public TitanUntypedGraph(TitanGraph titanGraph) { this.titanGraph = titanGraph; }


  public TitanManagement managementSystem() { return titanGraph().openManagement(); }


  @Override
  public void commit() { titanGraph().tx().commit(); }

  @Override
  public void shutdown() { titanGraph().close(); }

  @Override
  public void rollback() {
    // titanGraph().rollback();
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

}

```




[main/java/com/bio4j/angulillos/titan/TitanConversions.java]: TitanConversions.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedEdgeIndex.java]: TitanTypedEdgeIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedVertexIndex.java]: TitanTypedVertexIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]: TitanUntypedGraph.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedSchemaManager.java]: TitanUntypedSchemaManager.java.md
[test/java/com/bio4j/angulillos/titan/TitanGoGraph.java]: ../../../../../../test/java/com/bio4j/angulillos/titan/TitanGoGraph.java.md