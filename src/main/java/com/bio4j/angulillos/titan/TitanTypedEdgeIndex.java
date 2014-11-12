package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;

import java.util.stream.Stream;
import java.util.Optional;
import java.util.Iterator;
import com.tinkerpop.blueprints.Edge;

public interface TitanTypedEdgeIndex <
  // src
  S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
  ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
  SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  // edge
  R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
  RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
  // property
  P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
  G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  //tgt
  T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
  TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
  I extends TitanUntypedGraph
> 
extends 
  TypedEdgeIndex<
    S,ST,SG,
    R,RT, P,V, G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,
    T,TT,TG
  >
{

  // TODO: add this in angulillos at the level of typed element index
  RT edgeType();
  TitanGraphIndex raw();
  P property();
  String name();

  public static interface Unique <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  >
  extends 
    TypedEdgeIndex.Unique<
      S,ST,SG,
      R,RT, P,V, G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,
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
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    I extends TitanUntypedGraph
  > 
  extends 
    TypedEdgeIndex.List<
      S,ST,SG,
      R,RT, P,V, G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,
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
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
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

      if( graph == null ) {

        throw new IllegalArgumentException("trying to create an index with a null graph");
      }

      this.graph = graph;

      if( property == null ) {

        throw new IllegalArgumentException("trying to create an index with a null property");
      }

      this.property = property;

    }

    protected TitanGraphIndex raw;
    protected G graph;
    protected P property;

    public P property() { return this.property; }

    @Override
    public TitanGraphIndex raw() { return raw; }

    public RT edgeType() { return property().elementType(); }

    @Override
    public G graph() { return graph; }

    @Override public Stream<R> query(com.tinkerpop.blueprints.Compare predicate, V value) {

      // uh oh could be null
      RT elmt = property.elementType();

      Stream<R> strm = stream( graph().raw().titanGraph()
        .query().has(
          property.name(),
          predicate,
          value
        )
        .edges()
      )
      .flatMap( 

        e -> {

          Stream<R> es;

          if ( e != null ) {

            es = Stream.of( elmt.from( (TitanEdge) e ) );
          }
          else {

            es = Stream.empty();
          }

          return es;
        }
      );

      return strm;
    }
  }


  /* Default implementation of a relationship unique index */
  public final class DefaultUnique <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
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

    public DefaultUnique(G graph, P property) {

      super(graph,property);

      // TODO: all interaction with this should be done by the graph; or not?
      I tgrph = graph().raw();

      // create the index
      // open a new tx
      TitanManagement mgmt = tgrph.managementSystem();

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

        PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForEdgeProperty(property).cardinality(Cardinality.SINGLE);

        pky = tgrph.createOrGet(pkmkr);
      }
      else {

        pky = null;

        throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements:");
      }

      TitanGraphIndex alreadyThere = mgmt.getGraphIndex(name());

      if( alreadyThere != null && isKeyThere != null ) {
        
        // uh oh the index is there, checking times
        Boolean theExistingIndexIsOk =  alreadyThere.isCompositeIndex()                 &&
                                        alreadyThere.isUnique()                         &&
                                        alreadyThere.getFieldKeys().length == 1         &&
                                        alreadyThere.getFieldKeys()[0] == pky           &&
                                        alreadyThere.getIndexedElement() == Edge.class;

        if ( theExistingIndexIsOk ) {

          this.raw = alreadyThere;
        }
        else {

          throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
        }
      }

      TitanManagement.IndexBuilder indxbldr = mgmt.buildIndex( name(), Edge.class )
        .addKey(pky)
        .indexOnly( property.elementType().raw() )
        .unique();

      indxbldr.buildCompositeIndex();

      mgmt.commit();
    }

    @Override
    public String name() { 

      return edgeType().name() +":"+ property().name() +":"+ "UNIQUE";
    }
  }

  final class DefaultList <
    // src
    S extends TypedVertex<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    ST extends TypedVertex.Type<S,ST,SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    SG extends TypedGraph<SG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    // edge
    R extends TypedEdge<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>, 
    RT extends TypedEdge.Type<S,ST,SG,R,RT,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel,T,TT,TG>,
    // property
    P extends Property<R,RT,P,V,G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    //tgt
    T extends TypedVertex<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>, 
    TT extends TypedVertex.Type<T,TT,TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
    TG extends TypedGraph<TG,I,TitanVertex,VertexLabel,TitanEdge,EdgeLabel>,
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

    public DefaultList(G graph, P property) {

      super(graph,property);

      // TODO: all interaction with this should be done by the graph; or not?
      I tgrph = graph().raw();

      // create the index
      // open a new tx
      TitanManagement mgmt = tgrph.managementSystem();

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

        PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForEdgeProperty(property);

        pky = tgrph.createOrGet(pkmkr);
      }
      else {

        pky = null;

        throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements:");
      }

      TitanGraphIndex alreadyThere = mgmt.getGraphIndex(name());

      if( alreadyThere != null ) {
        
        // uh oh the index is there, checking times
        Boolean theExistingIndexIsOk =  alreadyThere.isCompositeIndex()                 &&
                                        alreadyThere.getFieldKeys().length == 1         &&
                                        alreadyThere.getFieldKeys()[0] == pky           &&
                                        ( ! alreadyThere.isUnique() )                   &&
                                        alreadyThere.getIndexedElement() == Edge.class;

        if ( theExistingIndexIsOk ) {

          this.raw = alreadyThere;
        }
        else {

          throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
        }
      }

      TitanManagement.IndexBuilder indxbldr = mgmt.buildIndex( name(), Edge.class )
        .addKey(pky)
        .indexOnly( property.elementType().raw() );

      indxbldr.buildCompositeIndex();

      mgmt.commit();

    }
  }

}