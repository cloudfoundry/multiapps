package com.sap.cloud.lm.sl.common.util;

/**
 * @deprecated, Use objects with proper names and purpose instead
 * 
 * @param T1 something's type
 * @param T2 probably something else's
 */
@Deprecated
public class Pair<T1, T2> {
    public final T1 _1;
    public final T2 _2;

    public Pair(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }
}
