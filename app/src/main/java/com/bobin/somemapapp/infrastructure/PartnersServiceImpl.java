package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.response.TinkoffApiResponse;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.storage.PartnersCache;
import com.bobin.somemapapp.utils.CollectionUtils;
import com.bobin.somemapapp.utils.StorageUtils;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class PartnersServiceImpl implements PartnersService {
    private TinkoffApi tinkoffApi;
    private PartnersCache partnersCache;

    public PartnersServiceImpl(TinkoffApi tinkoffApi, PartnersCache partnersCache) {
        this.tinkoffApi = tinkoffApi;
        this.partnersCache = partnersCache;
    }

    @Override
    public Single<DepositionPartner> getPartnerById(String id) {
        return cachePartnersIfNeed()
                .map(f -> partnersCache.getPartnerByIdOrNull(id));
    }

    @Override
    public Single<HashMap<String, DepositionPartner>> getPartnersByIds(List<String> ids) {
        String[] idsArray = CollectionUtils.toArray(ids);
        return cachePartnersIfNeed()
                .map(f -> partnersCache.getPartnersByIdsOrNull(idsArray));
    }

    private Single<Boolean> cachePartnersIfNeed() {
        if (!partnersCache.isExpired())
            return Single.just(true);

        return tinkoffApi.getDepositionPartners("Credit")
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMapIterable(TinkoffApiResponse::getPayload)
                .map(StorageUtils::convert)
                .toList()
                .doOnSuccess(x -> partnersCache.savePartners(x))
                .map(x -> true);
    }
}
