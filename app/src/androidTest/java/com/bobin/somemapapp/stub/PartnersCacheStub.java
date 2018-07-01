package com.bobin.somemapapp.stub;

import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.storage.PartnersCache;

import java.util.HashMap;
import java.util.List;

public class PartnersCacheStub implements PartnersCache {
    private HashMap<String, DepositionPartner> hash;
    private boolean expired;

    public PartnersCacheStub() {
        hash = new HashMap<>();
    }

    @Override
    public DepositionPartner getPartnerByIdOrNull(String id) {
        if (!hash.containsKey(id))
            return null;
        return hash.get(id);
    }

    @Override
    public HashMap<String, DepositionPartner> getPartnersByIdsOrNull(String[] ids) {
        HashMap<String, DepositionPartner> result = new HashMap<>();
        for (String id : ids) {
            if (hash.containsKey(id))
                result.put(id, hash.get(id));
        }

        return result;
    }

    @Override
    public void savePartners(List<DepositionPartner> partners) {
        for (DepositionPartner partner : partners) {
            hash.put(partner.getId(), partner);
        }
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
