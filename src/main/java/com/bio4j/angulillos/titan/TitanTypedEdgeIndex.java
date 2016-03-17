package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;
import static com.bio4j.angulillos.titan.TitanPredicatesConversion.*;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

import java.util.stream.Stream;
import java.util.Objects;
import java.util.Optional;
import java.util.Iterator;
import java.util.Collection;

// FIXME: is this really needed?
import com.tinkerpop.blueprints.Edge;


public interface TitanTypedEdgeIndex <
  // src
  S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  // edge
  R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
  RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
  // property
  P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
  G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  //tgt
  T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  I extends TitanUntypedGraph
>
extends
  TypedEdgeIndex<
    S,ST,SG,
    R,RT, P,V, G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,
    T,TT,TG
  >
{

  TitanGraphIndex raw();

  public static interface Unique <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  >
  extends
    TypedEdgeIndex.Unique<
      S,ST,SG,
      R,RT, P,V, G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,
      T,TT,TG
    >,
    TitanTypedEdgeIndex<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,
      I
    >
  {

    default String name() {

      return this.edgeType().name() +":"+ this.property().name() +":"+ "UNIQUE";
    }
  }

  public static interface List <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  >
  extends
    TypedEdgeIndex.List<
      S,ST,SG,
      R,RT, P,V, G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,
      T,TT,TG
    >,
    TitanTypedEdgeIndex<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,
      I
    >
  {

    default String name() {

      return edgeType().name() +":"+ property().name() +":"+ "LIST";
    }

  }

  public static abstract class Default <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  >
  implements
    TitanTypedEdgeIndex<
      S,ST,SG,
      R,RT, P,V,G,
      T,TT,TG,I
    >
  {

    protected Default(G graph, P property) {

      this.graph    = Objects.requireNonNull(graph,    "trying to create an index with a null graph");
      this.property = Objects.requireNonNull(property, "trying to create an index with a null property");
    }

    protected TitanGraphIndex raw;
    protected G graph;
    protected P property;

    @Override
    public P property() { return this.property; }

    @Override
    public TitanGraphIndex raw() { return raw; }

    @Override
    public G graph() { return graph; }

    @Override
    public Stream<R> query(QueryPredicate.Compare predicate, V value) {

      return stream(
        graph().raw().titanGraph()
          .query()
          .has("label", edgeType().name())
          .has(property.name(), toTitanCmp(predicate), value)
          .edges()
      ).map( e ->
        edgeType().from( (TitanEdge) e )
      );
    }

    @Override
    public Stream<R> query(QueryPredicate.Contain predicate, Collection<V> values) {

      return stream(
        graph().raw().titanGraph()
          .query()
          .has("label", edgeType().name())
          .has(property.name(), toTitanContain(predicate), values)
          .edges()
      ).map( e ->
        edgeType().from( (TitanEdge) e )
      );
    }
  }


  /* Default implementation of a relationship unique index */
  public final class DefaultUnique <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  >
  extends
    Default<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    >
  implements
    Unique<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    >
  {

    private TitanManagement.IndexBuilder indxbldr;
    private TitanManagement mgmt;

    // TODO: review this constructor
    public DefaultUnique(TitanManagement mgmt, G graph, P property) {

      super(graph,property);

      this.mgmt = mgmt;

      // TODO: all interaction with this should be done by the graph; or not?
      I tgrph = graph().raw();

      // the key we're going to use to create the index
      PropertyKey pky;


      Boolean isKeyThere;
      // get the property, check for unique etc. if not create it
      if ( mgmt.containsPropertyKey(property.name()) ) {

        isKeyThere = true;
        PropertyKey existingKey = mgmt.getPropertyKey( property.name() );

        if(
          (existingKey.getDataType() == property.valueClass()) &&
          (existingKey.getCardinality() == Cardinality.SINGLE)
        ){

          pky = existingKey;
        }
        else {

          throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements:");
        }
      } else {

        isKeyThere = false;
      }

      if ( ! isKeyThere ) {

        PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForEdgeProperty(mgmt, property).cardinality(Cardinality.SINGLE);

        pky = tgrph.createOrGet(mgmt, pkmkr);
      }
      else {

        pky = null;

        throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements:");
      }

      TitanGraphIndex alreadyThere = mgmt.getGraphIndex(name());

      if( alreadyThere != null && isKeyThere != null ) {

        Boolean theExistingIndexIsOk = true;
        // // uh oh the index is there, checking times
        // Boolean theExistingIndexIsOk =  alreadyThere.isUnique()                         &&
        //                                 alreadyThere.getFieldKeys().length == 1         &&
        //                                 // alreadyThere.getFieldKeys()[0] == pky           &&
        //                                 alreadyThere.getIndexedElement() == Edge.class;

        if ( theExistingIndexIsOk ) {

          this.raw = alreadyThere;
        }
        else {

          throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
        }
      }

      TitanManagement.IndexBuilder indxbldr = mgmt.buildIndex( name(), Edge.class )
        .addKey(pky)
        .unique();
    }

    private final void make(EdgeLabel edgeLabel) {

      this.raw = indxbldr.indexOnly( edgeLabel ).buildCompositeIndex();
    }

    public final void makeIfNotThere(EdgeLabel edgeLabel) {

      if ( ! mgmt.containsGraphIndex(name()) ) {

        make(edgeLabel);
      }
    }
  }

  final class DefaultList <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  >
  extends
    Default<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    >
  implements
    List<
      S,ST,SG,
      R,RT, P,V, G,
      T,TT,TG,I
    >
  {

    private TitanManagement.IndexBuilder indxbldr;
    private TitanManagement mgmt;

    // TODO: review this constructor
    public DefaultList(TitanManagement mgmt, G graph, P property) {

      super(graph,property);

      this.mgmt = mgmt;

      // TODO: all interaction with this should be done by the graph; or not?
      I tgrph = graph().raw();

      // the key we're going to use to create the index
      PropertyKey pky;

      Boolean isKeyThere;
      // get the property, check for unique etc. if not create it
      if ( mgmt.containsPropertyKey(property.name()) ) {

        isKeyThere = true;
        PropertyKey existingKey = mgmt.getPropertyKey( property.name() );

        if( existingKey.getDataType() == property.valueClass() ) {

          pky = existingKey;
        }
        else {

          throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements:");
        }
      }
      else {

        isKeyThere = false;
      }

      if ( ! isKeyThere ) {

        PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForEdgeProperty(mgmt, property);

        pky = tgrph.createOrGet(mgmt, pkmkr);
      }
      else {

        pky = null;

        throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements:");
      }

      TitanGraphIndex alreadyThere = mgmt.getGraphIndex(name());

      if( alreadyThere != null ) {

        Boolean theExistingIndexIsOk = true;
        // // uh oh the index is there, checking times
        // Boolean theExistingIndexIsOk =  alreadyThere.getFieldKeys().length == 1         &&
        //                                 // alreadyThere.getFieldKeys()[0] == pky           &&
        //                                 ( ! alreadyThere.isUnique() )                   &&
        //                                 alreadyThere.getIndexedElement() == Edge.class;

        if ( theExistingIndexIsOk ) {

          this.raw = alreadyThere;
        }
        else {

          throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
        }
      }

      this.indxbldr = mgmt.buildIndex( name(), Edge.class )
        .addKey(pky);
    }

    private final void make(EdgeLabel edgeLabel) {

      this.raw = indxbldr.indexOnly( edgeLabel ).buildCompositeIndex();
    }

    public final void makeIfNotThere(EdgeLabel edgeLabel) {

      if ( ! mgmt.containsGraphIndex(name()) ) {

        make(edgeLabel);
      }
    }
  }

}
