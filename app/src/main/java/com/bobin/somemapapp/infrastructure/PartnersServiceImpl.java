package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.response.TinkoffApiResponse;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.storage.PartnersCache;
import com.bobin.somemapapp.utils.StorageUtils;

import java.util.List;

import io.reactivex.Single;

public class PartnersServiceImpl implements PartnersService {
    private TinkoffApi tinkoffApi;
    private PartnersCache partnersCache;

    public PartnersServiceImpl(TinkoffApi tinkoffApi, PartnersCache partnersCache) {
        this.tinkoffApi = tinkoffApi;
        this.partnersCache = partnersCache;
    }

    @Override
    public Single<DepositionPartner> getPartnerById(String id) {
        DepositionPartner partner = partnersCache.getPartnerByIdOrNull(id);
        if (partner != null)
            return Single.just(partner);

        return cachePartners().map(x -> partnersCache.getPartnerByIdOrNull(id));
    }

    @Override
    public Single<List<DepositionPartner>> getPartnersByIds(List<String> ids) {
        String[] idsArray = new String[ids.size()];
        for (int i = 0; i < ids.size(); ++i)
            idsArray[i] = ids.get(i);

        if (partnersCache.isExpired())
            return cachePartners().map(x -> partnersCache.getPartnersByIdsOrNull(idsArray));

        return Single.just(partnersCache.getPartnersByIdsOrNull(idsArray));
    }

    private Single<Boolean> cachePartners() {
        return tinkoffApi.getDepositionPartners("Credit")
                .toObservable()
                .flatMapIterable(TinkoffApiResponse::getPayload)
                .map(StorageUtils::convert)
                .toList()
                .map(x -> {
                    partnersCache.savePartners(x);
                    return true;
                });
    }
}
