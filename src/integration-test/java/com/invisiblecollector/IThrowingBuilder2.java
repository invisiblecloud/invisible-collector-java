package com.invisiblecollector;

import com.invisiblecollector.exceptions.IcException;

public interface IThrowingBuilder2<ReturnT, Arg1T, Arg2T> {
  ReturnT build(Arg1T arg1, Arg2T arg2) throws IcException;
}
