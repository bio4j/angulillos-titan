package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.Collection;

public class TitanTypedVertexIndex {

  public static abstract class Unique<
    V  extends      TypedVertex<V,VT, ?, TitanVertex,TitanEdge>,
    VT extends TypedVertex.Type<V,VT, ?, TitanVertex,TitanEdge>,
    P extends Property<VT,X> & Arity.FromAtMostOne, X
  >
  implements TypedVertexIndex.Unique<V,VT,P,X>
  {

    public abstract P property();
    public abstract TitanUntypedGraph titanUntypedGraph();


    @Override
    public Stream<V> query(QueryPredicate.Compare predicate, X value) {

      return stream(
        titanUntypedGraph().titanGraph()
          .query()
          .has( "label", property().elementType()._label() )
          .has( property()._label(), TitanConversions.Predicate.asTitanCmp(predicate), value )
          .vertices()
      ).map( v ->
        property().elementType().fromRaw( (TitanVertex) v )
      );
    }
  }
}

//
// public interface TitanTypedVertexIndex <
//   N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//   NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//   P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
//   G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//   I extends TitanUntypedGraph
// >
// extends
//   TypedVertexIndex<N,NT,P,V, G, I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
// {
//
//   TitanGraphIndex raw();
//
//   abstract class Default <
//     N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
//     G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     I extends TitanUntypedGraph
//   >
//   implements
//     TitanTypedVertexIndex<N,NT,P,V,G,I>
//   {
//
//     public Default(G graph, P property) {
//
//       this.graph    = Objects.requireNonNull(graph,    "trying to create an index with a null graph");
//       this.property = Objects.requireNonNull(property, "trying to create an index with a null property");
//     }
//
//     protected TitanGraphIndex raw;
//     protected G graph;
//     protected P property;
//
//     @Override
//     public P property() { return this.property; }
//
//     @Override
//     public TitanGraphIndex raw() { return raw; }
//
//     @Override
//     public G graph() { return graph; }
//
//     @Override
//     public Stream<N> query(QueryPredicate.Compare predicate, V value) {
//
//       return stream(
//         graph().raw().titanGraph()
//           .query()
//           .has("label", vertexType().name())
//           .has(property.name(), toTitanCmp(predicate), value)
//           .vertices()
//       ).map( v ->
//         vertexType().from( (TitanVertex) v )
//       );
//     }
//
//     @Override
//     public Stream<N> query(QueryPredicate.Contain predicate, Collection<V> values) {
//
//       return stream(
//         graph().raw().titanGraph()
//           .query()
//           .has("label", vertexType().name())
//           .has(property.name(), toTitanContain(predicate), values)
//           .vertices()
//       ).map( v ->
//         vertexType().from( (TitanVertex) v )
//       );
//     }
//   }
//
//   interface Unique <
//     N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
//     G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     I extends TitanUntypedGraph
//   >
//   extends
//     TitanTypedVertexIndex<N,NT,P,V,G,I>,
//     TypedVertexIndex.Unique<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
//   {
//
//     default String name() {
//
//       return vertexType().name() +":"+ property().name() +":"+ "UNIQUE";
//     }
//   }
//
//   /* Default implementation of a node unique index */
//   final class DefaultUnique <
//     N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
//     G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     I extends TitanUntypedGraph
//   >
//   extends
//     Default<N,NT,P,V,G,I>
//   implements
//     TitanTypedVertexIndex.Unique<N,NT,P,V,G,I>
//   {
//
//     private final TitanManagement.IndexBuilder indxbldr;
//     private final TitanManagement mgmt;
//
//     // TODO: review this constructor
//     public DefaultUnique(TitanManagement mgmt, G graph, P property) {
//
//       super(graph,property);
//
//       this.property = property;
//
//
//       this.mgmt = mgmt;
//
//       // TODO: all interaction with this should be done by the graph; or not?
//       I tgrph = graph().raw();
//
//       // the key we're going to use to create the index
//       PropertyKey pky = null;
//
//       Boolean isKeyThere;
//       // get the property, check for unique etc. if not create it
//       if ( mgmt.containsPropertyKey(property.name()) ) {
//
//         isKeyThere = true;
//         PropertyKey existingKey = mgmt.getPropertyKey( property.name() );
//
//         if(
//           (existingKey.dataType() == property.valueClass()) &&
//           (existingKey.cardinality() == Cardinality.SINGLE)
//         ){
//
//           pky = existingKey;
//         }
//         else {
//
//           throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements");
//         }
//       } else {
//
//         isKeyThere = false;
//       }
//
//       if ( ! isKeyThere ) {
//
//         PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForVertexProperty(mgmt, property).cardinality(Cardinality.SINGLE);
//
//         pky = tgrph.createOrGet(mgmt, pkmkr);
//       }
//
//       TitanGraphIndex alreadyThere = mgmt.getGraphIndex(this.name());
//
//       if( alreadyThere != null && isKeyThere != null ) {
//
//         Boolean theExistingIndexIsOk = true;
//         // uh oh the index is there, checking times
//         // Boolean theExistingIndexIsOk =  alreadyThere.isUnique()                           &&
//         //                                 alreadyThere.getFieldKeys().length == 1           &&
//         //                                 // alreadyThere.getFieldKeys()[0] == pky             &&
//         //                                 alreadyThere.getIndexedElement() == Vertex.class;
//
//         if ( theExistingIndexIsOk ) {
//
//           this.raw = alreadyThere;
//         }
//         else {
//
//           throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
//         }
//       }
//
//       // at this point pky is correct whatever it is; let's retrieve it to make Titan happy
//       PropertyKey freshpky = mgmt.getPropertyKey(pky.name());
//       this.indxbldr = mgmt.buildIndex(
//           name(),
//           org.apache.tinkerpop.gremlin.structure.Vertex.class
//         ).addKey(freshpky).unique();
//     }
//
//     public final void make(VertexLabel vl) {
//
//       this.raw = indxbldr.indexOnly( vl ).buildCompositeIndex();
//     }
//
//     public final void makeOrGet(VertexLabel vertexLabel) {
//
//       if ( ! mgmt.containsGraphIndex(name()) ) {
//
//         make(vertexLabel);
//       }
//     }
//   }
//
//   interface List <
//     N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
//     G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     I extends TitanUntypedGraph
//   >
//   extends
//     TitanTypedVertexIndex<N,NT,P,V,G,I>,
//     TypedVertexIndex.List<N,NT,P,V,G, I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
//   {
//
//     default String name() {
//
//       return this.vertexType().name() +":"+ this.property().name() +":"+ "LIST";
//     }
//   }
//
//   final class DefaultList <
//     N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
//     G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
//     I extends TitanUntypedGraph
//   >
//   extends
//     Default<N,NT,P,V,G,I>
//   implements
//     TitanTypedVertexIndex.List<N,NT,P,V,G,I>
//   {
//
//     private final TitanManagement.IndexBuilder indxbldr;
//     private final TitanManagement mgmt;
//
//     // TODO: review this constructor
//     public DefaultList(TitanManagement mgmt, G graph, P property) {
//
//       super(graph,property);
//
//       this.mgmt = mgmt;
//
//       // TODO: all interaction with this should be done by the graph; or not?
//       I tgrph = graph().raw();
//
//       // the key we're going to use to create the index
//       PropertyKey pky;
//
//
//       Boolean isKeyThere;
//       // get the property, check for unique etc. if not create it
//       if ( mgmt.containsPropertyKey(property.name()) ) {
//
//         isKeyThere = true;
//         PropertyKey existingKey = mgmt.getPropertyKey( property.name() );
//
//         if( existingKey.dataType() == property.valueClass() ) {
//
//           pky = existingKey;
//         }
//         else {
//
//           throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements");
//         }
//       }
//       else {
//
//         isKeyThere = false;
//       }
//
//       if ( ! isKeyThere ) {
//
//         PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForVertexProperty(mgmt, property);
//
//         pky = tgrph.createOrGet(mgmt, pkmkr);
//       }
//       else {
//
//         pky = null;
//
//         throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements");
//       }
//
//       TitanGraphIndex alreadyThere = mgmt.getGraphIndex(name());
//
//       if( alreadyThere != null ) {
//
//         Boolean theExistingIndexIsOk = true;
//         // uh oh the index is there, checking times
//         // Boolean theExistingIndexIsOk =  alreadyThere.getFieldKeys().length == 1         &&
//         //                                 // alreadyThere.getFieldKeys()[0] == pky           &&
//         //                                 ( ! alreadyThere.isUnique() )                   &&
//         //                                 alreadyThere.getIndexedElement() == Vertex.class;
//
//         if ( theExistingIndexIsOk ) {
//
//           this.raw = alreadyThere;
//         }
//         else {
//
//           throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
//         }
//       }
//
//       indxbldr = mgmt.buildIndex(
//           name(),
//           org.apache.tinkerpop.gremlin.structure.Vertex.class
//         ).addKey(pky);
//     }
//
//     private final void make(VertexLabel vl) {
//
//       this.raw = indxbldr.indexOnly( vl ).buildCompositeIndex();
//     }
//
//     public final void makeIfNotThere(VertexLabel vertexLabel) {
//
//       if ( ! mgmt.containsGraphIndex(name()) ) {
//
//         make(vertexLabel);
//       }
//     }
//   }
//
// }
