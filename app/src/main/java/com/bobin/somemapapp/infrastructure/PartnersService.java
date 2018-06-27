package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.tables.DepositionPartner;

import java.util.List;

import io.reactivex.Single;

public interface PartnersService {
    Single<DepositionPartner> getPartnerById(String id);

    Single<List<DepositionPartner>> getPartnersByIds(List<String> ids);
}
