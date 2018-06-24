package com.bobin.somemapapp.storage;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

public class DbRepositoryImpl implements DbRepository {

    @Override
    public <T extends RealmObject> List<T> getAll(Class<T> clazz) {
        return Realm.getDefaultInstance().where(clazz).findAll();
    }

    @Override
    public <T extends RealmObject> void saveOrUpdate(T entity) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> r.insertOrUpdate(entity));
    }

    @Override
    public <T extends RealmObject> void saveOrUpdate(List<T> entities) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> r.insertOrUpdate(entities));
    }
}
