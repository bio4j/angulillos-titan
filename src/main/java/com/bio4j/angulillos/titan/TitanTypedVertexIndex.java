package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.*;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.Iterator;
import com.tinkerpop.blueprints.Vertex;

public interface TitanTypedVertexIndex <
  N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
  NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
  P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>, V,
  G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
  I extends TitanUntypedGraph
> 
extends 
  TypedVertexIndex<N,NT,P,V, G, I,TitanVertex,TitanKey,TitanEdge,TitanLabel>
{

  public static abstract class Default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    I extends TitanUntypedGraph
  > 
  implements 
    TitanTypedVertexIndex<N,NT,P,V,G,I>
  {

    public Default(G graph, P property) {

      if( graph == null ) {

        throw new IllegalArgumentException("trying to create an index with a null graph");
      }

      this.graph = graph;

      if( property == null ) {

        throw new IllegalArgumentException("trying to create an index with a null property");
      }

      this.property = property;
    }

    protected G graph;
    protected P property;

    public P property() { return this.property; }

    @Override
    public G graph() {

      return graph;
    }

    @Override public Stream<N> query(com.tinkerpop.blueprints.Compare predicate, V value) {

      NT elmt = property.elementType();

      Stream<N> strm = stream( graph().raw().titanGraph()
        .query().has(
          property.name(),
          predicate,
          value
        )
        .vertices()
      )
      .flatMap( v -> {

          Stream<N> vs;

          if ( v != null ) {

            vs = Stream.of( elmt.from( (TitanVertex) v ) );
          }
          else {

            vs = Stream.empty();
          }

            return vs;
          }
      );
          

      return strm;

      // java.util.List<N> list = new LinkedList<>();

      // Iterator<Vertex> iterator = graph().raw().titanGraph()
      //   .query().has(
      //     property.name(),
      //     predicate,
      //     value
      //   )
      //   .vertices().iterator();
      
      // Boolean someResult = iterator.hasNext();

      // while ( iterator.hasNext() ) {

      //   Vertex vrtx = iterator.next();
      //   NT elmt = property.elementType();

      //   if ( elmt != null && vrtx != null ) {

      //     list.add( elmt.from( (TitanVertex) vrtx ) );
      //   }
      // }

      // if (someResult ) {

      //   return Optional.of(list);

      // } else {

      //   return Optional.empty();
      // }
    }
  }

  public interface Unique <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    I extends TitanUntypedGraph
  > 
  extends
    TitanTypedVertexIndex<N,NT,P,V,G,I>,
    TypedVertexIndex.Unique<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>
  {}

  /* Default implementation of a node unique index */
  public static final class DefaultUnique <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    I extends TitanUntypedGraph
  > 
  extends
    Default<N,NT,P,V,G,I> 
  implements 
    TitanTypedVertexIndex.Unique<N,NT,P,V,G,I> 
  {

    public DefaultUnique(G graph, P property) {

      super(graph,property);
    }
  }

  public static interface List <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    I extends TitanUntypedGraph
  > 
  extends
    TitanTypedVertexIndex<N,NT,P,V,G,I>,
    TypedVertexIndex.List<N,NT,P,V,G, I,TitanVertex,TitanKey,TitanEdge,TitanLabel>
  {

  }

  public static final class DefaultList <
    N extends TypedVertex<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>, V,
    G extends TypedGraph<G,I,TitanVertex,TitanKey,TitanEdge,TitanLabel>,
    I extends TitanUntypedGraph
  > 
  extends
    Default<N,NT,P,V,G,I>
  implements 
    TitanTypedVertexIndex.List<N,NT,P,V,G,I> 
  {

    public DefaultList(G graph, P property) {

      super(graph,property);
    }
  }

}