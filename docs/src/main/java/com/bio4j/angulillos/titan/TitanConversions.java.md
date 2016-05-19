
```java
package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.QueryPredicate;
import com.bio4j.angulillos.Arity;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.attribute.Contain;
import com.thinkaurelius.titan.core.Multiplicity;
import com.thinkaurelius.titan.core.Cardinality;


public final class TitanConversions {

  public static final class Predicate {

    public static final Cmp asTitanCmp(QueryPredicate.Compare predicate) {
      switch(predicate) {
        case EQUAL:              return Cmp.EQUAL;
        case NOT_EQUAL:          return Cmp.NOT_EQUAL;
        case GREATER_THAN:       return Cmp.GREATER_THAN;
        case GREATER_THAN_EQUAL: return Cmp.GREATER_THAN_EQUAL;
        case LESS_THAN:          return Cmp.LESS_THAN;
        case LESS_THAN_EQUAL:    return Cmp.LESS_THAN_EQUAL;
        // NOTE: this shouldn't happen, because we pattern match on all cases of a sealed enum
        default:                 return Cmp.EQUAL;
      }
    }

    public static final Contain asTitanContain(QueryPredicate.Contain predicate) {
      switch(predicate) {
        case IN:              return Contain.IN;
        case NOT_IN:          return Contain.NOT_IN;
        // NOTE: this shouldn't happen, because we pattern match on all cases of a sealed enum
        default:              return Contain.IN;
      }
    }

  }

  public static final class Arities {
    // One/AtMostOne -> ONE
    // AtLeastOne/Any -> MANY
    // default case represents arity Any

    // NOTE: we don't support non-single cardinality in the angulillos API
    // public static final Cardinality asTitanCardinality(Arity arity) {
    //   switch(arity) {
    //     case One:       return Cardinality.SINGLE;
    //     case AtMostOne: return Cardinality.SINGLE;
    //     default:        return Cardinality.LIST;
    //   }
    // }

    public static final Multiplicity asTitanMultiplicity(Arity fromArity, Arity toArity) {
      switch(fromArity) {

        case One: switch (toArity) {
          case One:        return Multiplicity.ONE2ONE;
          case AtMostOne:  return Multiplicity.ONE2ONE;
          case AtLeastOne: return Multiplicity.ONE2MANY;
          default:         return Multiplicity.ONE2MANY;
        }

        case AtMostOne: switch (toArity) {
          case One:        return Multiplicity.ONE2ONE;
          case AtMostOne:  return Multiplicity.ONE2ONE;
          case AtLeastOne: return Multiplicity.ONE2MANY;
          default:         return Multiplicity.ONE2MANY;
        }

        case AtLeastOne: switch (toArity) {
          case One:        return Multiplicity.MANY2ONE;
          case AtMostOne:  return Multiplicity.MANY2ONE;
          case AtLeastOne: return Multiplicity.MULTI;
          default:         return Multiplicity.MULTI;
        }

        default: switch(toArity) {
          case One:        return Multiplicity.MANY2ONE;
          case AtMostOne:  return Multiplicity.MANY2ONE;
          case AtLeastOne: return Multiplicity.MULTI;
          default:         return Multiplicity.MULTI;
        }
      }
    }

  }

}

```




[test/java/com/bio4j/angulillos/titan/TitanGoGraph.java]: ../../../../../../test/java/com/bio4j/angulillos/titan/TitanGoGraph.java.md
[main/java/com/bio4j/angulillos/titan/TitanConversions.java]: TitanConversions.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedVertexIndex.java]: TitanTypedVertexIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedSchemaManager.java]: TitanUntypedSchemaManager.java.md
[main/java/com/bio4j/angulillos/titan/TitanTypedEdgeIndex.java]: TitanTypedEdgeIndex.java.md
[main/java/com/bio4j/angulillos/titan/TitanUntypedGraph.java]: TitanUntypedGraph.java.md