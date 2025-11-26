/*------------------------------------------------------------------------
Name: Parser.java
Author: Lior shalom
Reviewer:
Date: 24/08/2024
------------------------------------------------------------------------*/

package simple_rps;

import il.co.ilrd.pair.Pair;

public interface Parser<T> {
    public Pair<String, Params> parse(T request);
}
