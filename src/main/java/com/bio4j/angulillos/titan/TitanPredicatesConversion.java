package com.bio4j.angulillos.titan;

import com.bio4j.angulillos.QueryPredicate;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.attribute.Contain;


public class TitanPredicatesConversion {

  public static Cmp toTitanCmp(QueryPredicate.Compare predicate) {
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

  public static Contain toTitanContain(QueryPredicate.Contain predicate) {
    switch(predicate) {
      case IN:              return Contain.IN;
      case NOT_IN:          return Contain.NOT_IN;
      // NOTE: this shouldn't happen, because we pattern match on all cases of a sealed enum
      default:                 return Contain.IN;
    }
  }
}
