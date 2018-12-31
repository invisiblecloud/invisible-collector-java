package com.invisiblecollector.connection.builders;

public interface ThrowingSupplier<Ret, Ex extends Exception> {
    Ret get() throws Ex;
}
