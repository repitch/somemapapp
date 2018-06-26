package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.tables.DepositionPartner;

import io.reactivex.Single;

public interface PartnersService {
    Single<DepositionPartner> getPartnerById(String id);
}
