package com.bobin.somemapapp.storage;

import java.util.List;

import io.realm.RealmObject;

public interface DbRepository {
    <T extends RealmObject> List<T> getAll(Class<T> clazz);

    <T extends RealmObject> void saveOrUpdate(T entity);

    <T extends RealmObject> void saveOrUpdate(List<T> entities);
}
