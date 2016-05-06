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
  public TitanEdge addEdge(TitanVertex source, String edgeLabel, TitanVertex target) {

    return source.addEdge( edgeLabel, target );
  }

  @Override
  public TitanVertex addVertex(String vertexLabel) {

    return titanGraph().addVertex(vertexLabel);
  }

  @Override
  public <V> V getPropertyV(TitanVertex vertex, String property) {

    return vertex.<V>property(property).value();
  }

  @Override
  public <V> TitanVertex setPropertyV(TitanVertex vertex, String property, V value) {

    vertex.property(property, value);
    return vertex;
  }

  @Override
  public <V> V getPropertyE(TitanEdge edge, String property) {

    return edge.<V>property(property).value();
  }

  @Override
  public <V> TitanEdge setPropertyE(TitanEdge edge, String property, V value) {

    edge.property(property, value);
    return edge;
  }

  @Override
  public TitanVertex source(TitanEdge edge) { return edge.outVertex(); }

  @Override
  public TitanVertex target(TitanEdge edge) { return edge.inVertex(); }

  @Override
  public Stream<TitanEdge> outE(TitanVertex vertex, String edgeLabel) {

    return stream(
      vertex.query()
        .labels(edgeLabel)
        .direction(Direction.OUT)
        .edges()
    );
  }

  @Override
  public Stream<TitanVertex> outV(TitanVertex vertex, String edgeLabel) {

    return stream(
      vertex.query()
        .labels(edgeLabel)
        .direction(Direction.OUT)
        .vertices()
    );
  }

  @Override
  public Stream<TitanEdge> inE(TitanVertex vertex, String edgeLabel) {

    return stream(
      vertex.query()
        .labels(edgeLabel)
        .direction(Direction.IN)
        .edges()
    );
  }

  @Override
  public Stream<TitanVertex> inV(TitanVertex vertex, String edgeLabel) {

    return stream(
      vertex.query()
        .labels(edgeLabel)
        .direction(Direction.IN)
        .vertices()
    );
  }

}
