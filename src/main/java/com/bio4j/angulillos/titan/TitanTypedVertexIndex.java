// package com.bio4j.angulillos.titan;
//
// import com.bio4j.angulillos.*;
// import static com.bio4j.angulillos.conversions.*;
//
// import com.thinkaurelius.titan.core.TitanVertex;
// import com.thinkaurelius.titan.core.TitanEdge;
//
// import java.util.stream.Stream;
// import java.util.Collection;
//
// public class TitanTypedVertexIndex {
//
//   public static class Unique<
//     V  extends      TypedVertex<V,VT, ?, TitanVertex,TitanEdge>,
//     VT extends TypedVertex.Type<V,VT, ?, TitanVertex,TitanEdge>,
//     P extends Property<VT,X> & Arity.FromAtMostOne, X
//   >
//   implements TypedVertexIndex.Unique<V,VT,P,X>
//   {
//
//     private final P property;
//     private final TitanUntypedGraph titanUntypedGraph;
//
//     public Unique(P property, TitanUntypedGraph titanUntypedGraph) {
//
//       this.property           = property;
//       this.titanUntypedGraph  = titanUntypedGraph;
//     }
//
//     public P property() { return property; }
//     public TitanUntypedGraph titanUntypedGraph() { return titanUntypedGraph; }
//
//     @Override
//     public final String name() { return "index.unique"+property._label(); }
//
//     @Override
//     public Stream<V> query(QueryPredicate.Compare predicate, X value) {
//
//       return stream(
//         titanUntypedGraph().titanGraph()
//           .query()
//           .has( "label", property().elementType()._label() )
//           .has( property()._label(), TitanConversions.Predicate.asTitanCmp(predicate), value )
//           .vertices()
//       ).map( v ->
//         property().elementType().fromRaw( (TitanVertex) v )
//       );
//     }
//
//     @Override
//     public Stream<V> query(QueryPredicate.Contain predicate, Collection<X> values) {
//
//       return stream(
//         titanUntypedGraph().titanGraph()
//           .query()
//           .has( "label", property().elementType()._label() )
//           .has( property()._label(), TitanConversions.Predicate.asTitanContain(predicate), values )
//           .vertices()
//       ).map( v ->
//         property().elementType().fromRaw( (TitanVertex) v )
//       );
//     }
//   }
//
//   public static class NonUnique<
//     V  extends      TypedVertex<V,VT, ?, TitanVertex,TitanEdge>,
//     VT extends TypedVertex.Type<V,VT, ?, TitanVertex,TitanEdge>,
//     P extends Property<VT,X>, X
//   >
//   implements TypedVertexIndex.NonUnique<V,VT,P,X>
//   {
//
//     private final P property;
//     private final TitanUntypedGraph titanUntypedGraph;
//
//     public NonUnique(P property, TitanUntypedGraph titanUntypedGraph) {
//
//       this.property           = property;
//       this.titanUntypedGraph  = titanUntypedGraph;
//     }
//
//     public P property() { return property; }
//     public TitanUntypedGraph titanUntypedGraph() { return titanUntypedGraph; }
//
//     @Override
//     public final String name() { return "index.unique"+property._label(); }
//
//     @Override
//     public Stream<V> query(QueryPredicate.Compare predicate, X value) {
//
//       return stream(
//         titanUntypedGraph().titanGraph()
//           .query()
//           .has( "label", property().elementType()._label() )
//           .has( property()._label(), TitanConversions.Predicate.asTitanCmp(predicate), value )
//           .vertices()
//       ).map( v ->
//         property().elementType().fromRaw( (TitanVertex) v )
//       );
//     }
//
//     @Override
//     public Stream<V> query(QueryPredicate.Contain predicate, Collection<X> values) {
//
//       return stream(
//         titanUntypedGraph().titanGraph()
//           .query()
//           .has( "label", property().elementType()._label() )
//           .has( property()._label(), TitanConversions.Predicate.asTitanContain(predicate), values )
//           .vertices()
//       ).map( v ->
//         property().elementType().fromRaw( (TitanVertex) v )
//       );
//     }
//   }
// }
