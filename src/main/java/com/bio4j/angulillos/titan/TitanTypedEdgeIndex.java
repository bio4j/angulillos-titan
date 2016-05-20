package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;
import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.core.TitanEdge;

import java.util.stream.Stream;
import java.util.Collection;

public class TitanTypedEdgeIndex {

  public static class Unique<
    E  extends      TypedEdge<?,?, E,ET, ?, ?,?, TitanVertex,TitanEdge>,
    ET extends TypedEdge.Type<?,?, E,ET, ?, ?,?, TitanVertex,TitanEdge>,
    P extends Property<ET,X> & Arity.FromAtMostOne, X
  >
  implements TypedEdgeIndex.Unique<E,ET,P,X>
  {

    private final P property;
    private final TitanUntypedGraph titanUntypedGraph;

    public Unique(P property, TitanUntypedGraph titanUntypedGraph) {

      this.property           = property;
      this.titanUntypedGraph  = titanUntypedGraph;
    }

    public P property() { return property; }
    public TitanUntypedGraph titanUntypedGraph() { return titanUntypedGraph; }

    @Override
    public final String name() { return "index.unique"+property._label(); }

    @Override
    public Stream<E> query(QueryPredicate.Compare predicate, X value) {

      return stream(
        titanUntypedGraph().titanGraph()
          .query()
          .has( "label", property().elementType()._label() )
          .has( property()._label(), TitanConversions.Predicate.asTitanCmp(predicate), value )
          .vertices()
      ).map( v ->
        property().elementType().fromRaw( (TitanEdge) v )
      );
    }

    @Override
    public Stream<E> query(QueryPredicate.Contain predicate, Collection<X> values) {

      return stream(
        titanUntypedGraph().titanGraph()
          .query()
          .has( "label", property().elementType()._label() )
          .has( property()._label(), TitanConversions.Predicate.asTitanContain(predicate), values )
          .vertices()
      ).map( v ->
        property().elementType().fromRaw( (TitanEdge) v )
      );
    }
  }

  public static class NonUnique<
    E  extends      TypedEdge<?,?, E,ET, ?, ?,?, TitanVertex,TitanEdge>,
    ET extends TypedEdge.Type<?,?, E,ET, ?, ?,?, TitanVertex,TitanEdge>,
    P extends Property<ET,X>, X
  >
  implements TypedEdgeIndex.NonUnique<E,ET,P,X>
  {

    private final P property;
    private final TitanUntypedGraph titanUntypedGraph;

    public NonUnique(P property, TitanUntypedGraph titanUntypedGraph) {

      this.property           = property;
      this.titanUntypedGraph  = titanUntypedGraph;
    }

    public P property() { return property; }
    public TitanUntypedGraph titanUntypedGraph() { return titanUntypedGraph; }

    @Override
    public final String name() { return "index.unique"+property._label(); }

    @Override
    public Stream<E> query(QueryPredicate.Compare predicate, X value) {

      return stream(
        titanUntypedGraph().titanGraph()
          .query()
          .has( "label", property().elementType()._label() )
          .has( property()._label(), TitanConversions.Predicate.asTitanCmp(predicate), value )
          .vertices()
      ).map( v ->
        property().elementType().fromRaw( (TitanEdge) v )
      );
    }

    @Override
    public Stream<E> query(QueryPredicate.Contain predicate, Collection<X> values) {

      return stream(
        titanUntypedGraph().titanGraph()
          .query()
          .has( "label", property().elementType()._label() )
          .has( property()._label(), TitanConversions.Predicate.asTitanContain(predicate), values )
          .vertices()
      ).map( v ->
        property().elementType().fromRaw( (TitanEdge) v )
      );
    }
  }
}
