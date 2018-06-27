package com.bobin.somemapapp.storage;

import com.bobin.somemapapp.model.tables.DepositionPartner;

import java.util.List;

public interface PartnersCache {
    DepositionPartner getPartnerByIdOrNull(String id);

    List<DepositionPartner> getPartnersByIdsOrNull(String[] ids);

    void savePartners(List<DepositionPartner> partners);

    boolean isExpired();
}
