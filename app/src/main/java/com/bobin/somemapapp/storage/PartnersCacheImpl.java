package com.bobin.somemapapp.storage;

import com.bobin.somemapapp.model.tables.DepositionPartner;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PartnersCacheImpl implements PartnersCache {

    private KeyValueStorage keyValueStorage;

    private static final String PARTNERS_UPDATE_KEY = "partnersUpdateTime";

    public PartnersCacheImpl(KeyValueStorage keyValueStorage) {
        this.keyValueStorage = keyValueStorage;
    }

    @Override
    public DepositionPartner getPartnerByIdOrNull(String id) {
        if (isExpired())
            return null;

        Realm realm = Realm.getDefaultInstance();
        RealmResults<DepositionPartner> partners = realm.where(DepositionPartner.class)
                .equalTo("id", id)
                .findAll();

        if (partners == null || partners.size() == 0)
            return null;
        //noinspection ConstantConditions
        return realm.copyFromRealm(partners.get(0));
    }

    @Override
    public List<DepositionPartner> getPartnersByIdsOrNull(String[] ids) {
        if (isExpired())
            return null;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DepositionPartner> partners = realm.where(DepositionPartner.class)
                .in("id", ids)
                .findAll();

        if (partners == null)
            return null;

        return realm.copyFromRealm(partners);
    }

    @Override
    public void savePartners(List<DepositionPartner> partners) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            r.delete(DepositionPartner.class);
            r.insert(partners);
        });
        keyValueStorage.save(PARTNERS_UPDATE_KEY, System.currentTimeMillis());
    }

    @Override
    public boolean isExpired() {
        long now = System.currentTimeMillis();
        long lastUpdate = keyValueStorage.getLong(PARTNERS_UPDATE_KEY);
        return now - lastUpdate > 1000 * 60 * 10;
    }
}
