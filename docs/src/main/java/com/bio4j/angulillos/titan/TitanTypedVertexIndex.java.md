
```java
package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.*;

import static com.bio4j.angulillos.conversions.*;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.*;
import com.thinkaurelius.titan.core.schema.*;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.Iterator;
import com.tinkerpop.blueprints.Vertex;

public interface TitanTypedVertexIndex <
  N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
  G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
  I extends TitanUntypedGraph
> 
extends 
  TypedVertexIndex<N,NT,P,V, G, I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
{

  // TODO: add this in angulillos at the level of typed element index
  NT vertexType();
  TitanGraphIndex raw();
  P property();
  String name();

  public static abstract class Default <
    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
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

    protected TitanGraphIndex raw;
    protected G graph;
    protected P property;

    public P property() { return this.property; }

    @Override
    public TitanGraphIndex raw() { return raw; }

    public NT vertexType() { return property().elementType(); }

    @Override
    public G graph() { return graph; }

    @Override public Stream<N> query(com.tinkerpop.blueprints.Compare predicate, V value) {

      NT elmt = property.elementType();

      Stream<N> strm = stream( graph().raw().titanGraph()
        .query().has(
          property.name(),
          predicate,
          value
        )
        .has("label", vertexType().name())
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
    }
  }

  public interface Unique <
    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  > 
  extends
    TitanTypedVertexIndex<N,NT,P,V,G,I>,
    TypedVertexIndex.Unique<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
  {

    default String name() { 

      return this.vertexType().name() +":"+ this.property().name() +":"+ "UNIQUE";
    }
  }
```

Default implementation of a node unique index

```java
  public static final class DefaultUnique <
    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  > 
  extends
    Default<N,NT,P,V,G,I> 
  implements 
    TitanTypedVertexIndex.Unique<N,NT,P,V,G,I> 
  {

    private TitanManagement.IndexBuilder indxbldr;
    private TitanManagement mgmt;

    public DefaultUnique(TitanManagement mgmt, G graph, P property) {

      super(graph,property);

      this.mgmt = mgmt;

      // TODO: all interaction with this should be done by the graph; or not?
      I tgrph = graph().raw();

      // the key we're going to use to create the index
      PropertyKey pky = null;

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

          throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements");
        } 
      } else {

        isKeyThere = false;
      }

      if ( ! isKeyThere ) {

        PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForVertexProperty(mgmt, property).cardinality(Cardinality.SINGLE);

        pky = tgrph.createOrGet(mgmt, pkmkr);
      }

      TitanGraphIndex alreadyThere = mgmt.getGraphIndex(name());

      if( alreadyThere != null && isKeyThere != null ) {
        
        // uh oh the index is there, checking times
        Boolean theExistingIndexIsOk =  alreadyThere.isCompositeIndex()                   &&
                                        alreadyThere.isUnique()                           &&
                                        alreadyThere.getFieldKeys().length == 1           &&
                                        alreadyThere.getFieldKeys()[0] == pky             &&
                                        alreadyThere.getIndexedElement() == Vertex.class;

        if ( theExistingIndexIsOk ) {

          this.raw = alreadyThere;
        }
        else {

          throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
        }
      }

      // at this point pky is correct whatever it is; let's retrieve it to make Titan happy
      PropertyKey freshpky = mgmt.getPropertyKey(pky.getName());
      this.indxbldr = mgmt.buildIndex( name(), Vertex.class )
        .addKey(freshpky)
        .unique();
    }

    public final void make(VertexLabel vl) {

      this.raw = indxbldr.indexOnly( vl ).buildCompositeIndex();
    }
  }

  public static interface List <
    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  > 
  extends
    TitanTypedVertexIndex<N,NT,P,V,G,I>,
    TypedVertexIndex.List<N,NT,P,V,G, I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>
  {

    default String name() { 

      return this.vertexType().name() +":"+ this.property().name() +":"+ "LIST";
    }
  }

  public static final class DefaultList <
    N extends TypedVertex<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    NT extends TypedVertex.Type<N,NT,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    P extends Property<N,NT,P,V,G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>, V,
    G extends TypedGraph<G,I,TitanVertex,VertexLabelMaker,TitanEdge,EdgeLabelMaker>,
    I extends TitanUntypedGraph
  > 
  extends
    Default<N,NT,P,V,G,I>
  implements 
    TitanTypedVertexIndex.List<N,NT,P,V,G,I> 
  {

    private TitanManagement.IndexBuilder indxbldr;
    private TitanManagement mgmt;

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

          throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements");
        }
      }
      else {

        isKeyThere = false;
      }

      if ( ! isKeyThere ) {

        PropertyKeyMaker pkmkr = tgrph.titanPropertyMakerForVertexProperty(mgmt, property);

        pky = tgrph.createOrGet(mgmt, pkmkr);
      }
      else {

        pky = null;

        throw new IllegalArgumentException("The property key already exists and does not satisfy the requirements");
      }

      TitanGraphIndex alreadyThere = mgmt.getGraphIndex(name());

      if( alreadyThere != null ) {
        
        // uh oh the index is there, checking times
        Boolean theExistingIndexIsOk =  alreadyThere.isCompositeIndex()                 &&
                                        alreadyThere.getFieldKeys().length == 1         &&
                                        alreadyThere.getFieldKeys()[0] == pky           &&
                                        ( ! alreadyThere.isUnique() )                   &&
                                        alreadyThere.getIndexedElement() == Vertex.class;

        if ( theExistingIndexIsOk ) {

          this.raw = alreadyThere;
        }
        else {

          throw new IllegalArgumentException("The property key is already indexed with the same index name and incompatible characteristics");
        }
      }

      indxbldr = mgmt.buildIndex( name(), Vertex.class )
        .addKey(pky);
    }

    public final void make(VertexLabel vl) {

      this.raw = indxbldr.indexOnly( vl ).buildCompositeIndex();
    }
  }

}
```


------

### Index

+ src
  + test
    + java
      + com
        + bio4j
          + angulillos
            + titan
              + [TitanGoGraph.java][test/java/com/bio4j/angulillos/titan/TitanGoGraph.java]
  + main
    + java
      + com
        + bio4j
          + angulillos
            + titan
              + [TitanTypedEdgeIndex.java][main/java/com/bio4j/angulillos/titan/TitanTypedEdgeIndex.java]
              + [TitanTypedVertexIndex.java][main/java/com/bio4j/angulillos/titan/TitanTypedVertexIndex.java]
              + [TitanUntypedGraph.java][main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]

[test/java/com/bio4j/angulillos/titan/TitanGoGraph.java]: ../../../../../../test/java/com/bio4j/angulillos/titan/TitanGoGraph.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedEdgeIndex.java]: TitanTypedEdgeIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedVertexIndex.java]: TitanTypedVertexIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]: TitanUntypedGraph.java.md