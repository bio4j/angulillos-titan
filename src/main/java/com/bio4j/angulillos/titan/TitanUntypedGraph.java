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


  /* ### Creating types in the TitanGraph Management System

     These methods create a key in the graph using the provided label or just return it if it already exists.
  */
  public VertexLabel createOrGetVertexType(TitanManagement mgmt, String vertexLabel) {

    return Optional.ofNullable(
      mgmt.getVertexLabel(vertexLabel)
    ).orElse(
      // TODO: evaluate partition() and setStatic()
      mgmt.makeVertexLabel(vertexLabel).make()
    );
  }

  public EdgeLabel createOrGetEdgeType(TitanManagement mgmt, String edgeLabel) {

    return Optional.ofNullable(
      mgmt.getEdgeLabel(edgeLabel)
    ).orElse(
      mgmt.makeEdgeLabel(edgeLabel).directed().make()
    );
  }

  public PropertyKey createOrGetPropertyType(TitanManagement mgmt, String propertyLabel) {

    return Optional.ofNullable(
      mgmt.getPropertyKey(propertyLabel)
    ).orElse(
      mgmt.makePropertyKey(propertyLabel).make()
    );
  }



  // /*
  // Get a `VertexLabelMaker` for a vertex type
  // */
  // VertexLabelMaker titanLabelMakerForVertexType(TitanManagement mgmt, NT vertexType) {
  //
  //   return mgmt.makeVertexLabel(vertexType.name());
  // }

  // /*
  // Create a `LabelMaker` with the minimum defaults (name, arity and directed) for an edge type. As this is a `LabelMaker`, you can further refine it and define its signature, indexing etc.
  // */
  // EdgeLabelMaker titanLabelMakerForEdgeType(TitanManagement mgmt, RT relationshipType) {
  //
  //   return mgmt.makeEdgeLabel(relationshipType.name()).directed().multiplicity(...);;
  // }
  //
  // // see http://s3.thinkaurelius.com/docs/titan/0.5.1/data-model.html#_individual_edge_layout for why you might want this
  // default <
  // EdgeLabelMaker titanLabelMakerForEdgeTypeWithProperties(TitanManagement mgmt, RT edgeType, PropertyKey... propertyKeys) {
  //
  //   return titanLabelMakerForEdgeType(mgmt, edgeType).signature(propertyKeys);
  // }


  // /* create an `EdgeLabel` for an type, using the default configuration. If a type with the same name is present it will be returned instead. */
  // EdgeLabel titanLabelForEdgeType(TitanManagement mgmt, RT relationshipType) {
  //
  //   // TODO: check that arities etc are ok, throw if not
  //   return createOrGet(mgmt, titanLabelMakerForEdgeType(mgmt, relationshipType));
  // }


  // PropertyKeyMaker titanPropertyMakerForVertexProperty(TitanManagement mgmt, P property) {
  //
  //   return mgmt.makePropertyKey(property.name()).dataType(property.valueClass());
  // }

  // PropertyKeyMaker titanPropertyMakerForEdgeProperty(TitanManagement mgmt, P property) {
  //
  //   return mgmt.makePropertyKey(property.name()).dataType(property.valueClass());
  // }

}
