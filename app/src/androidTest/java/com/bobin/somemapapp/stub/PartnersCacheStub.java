package com.bobin.somemapapp.stub;

import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.storage.PartnersCache;

import java.util.HashMap;
import java.util.List;

public class PartnersCacheStub implements PartnersCache {
    @Override
    public DepositionPartner getPartnerByIdOrNull(String id) {
        return null;
    }

    @Override
    public HashMap<String, DepositionPartner> getPartnersByIdsOrNull(String[] ids) {
        return null;
    }

    @Override
    public void savePartners(List<DepositionPartner> partners) {

    }

    @Override
    public boolean isExpired() {
        return false;
    }
}
