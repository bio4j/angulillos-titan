package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.QueryPredicate;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.attribute.Contain;
import com.thinkaurelius.titan.core.Multiplicity;


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

  public static final class Arity {

    public static final Multiplicity asTitanMultiplicity(com.bio4j.angulillos.Arity arity) {
      // one/atMostOne -> ONE
      // atLeastOne/any -> MANY
      switch(arity) {

        case oneToOne:               return Multiplicity.ONE2ONE;
        case oneToAtMostOne:         return Multiplicity.ONE2ONE;
        case oneToAtLeastOne:        return Multiplicity.ONE2MANY;
        case oneToAny:               return Multiplicity.ONE2MANY;

        case atMostOneToOne:         return Multiplicity.ONE2ONE;
        case atMostOneToAtMostOne:   return Multiplicity.ONE2ONE;
        case atMostOneToAtLeastOne:  return Multiplicity.ONE2MANY;
        case atMostOneToAny:         return Multiplicity.ONE2MANY;

        case atLeastOneToOne:        return Multiplicity.MANY2ONE;
        case atLeastOneToAtMostOne:  return Multiplicity.MANY2ONE;
        case atLeastOneToAtLeastOne: return Multiplicity.MULTI;
        case atLeastOneToAny:        return Multiplicity.MULTI;

        case anyToOne:               return Multiplicity.MANY2ONE;
        case anyToAtMostOne:         return Multiplicity.MANY2ONE;
        case anyToAtLeastOne:        return Multiplicity.MULTI;
        case anyToAny:               return Multiplicity.MULTI;
        // NOTE: this shouldn't happen, because we pattern match on all cases of a sealed enum
        default:                     return Multiplicity.MULTI;
      }
    }

  }

}
