/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nem.sdk.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicInfo;
import io.nem.sdk.model.mosaic.MosaicProperties;
import io.nem.sdk.model.transaction.UInt64;
import io.nem.sdk.model.transaction.UInt64Id;
import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mosaic http repository.
 *
 * @since 1.0
 */
public class MosaicHttp extends Http implements MosaicRepository {

    public MosaicHttp(String host) throws MalformedURLException {
        this(host, new NetworkHttp(host));
    }

    public MosaicHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host, networkHttp);
    }

    @Override
    public Observable<MosaicInfo> getMosaic(UInt64Id mosaicId) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/mosaic/" + UInt64.bigIntegerToHex(mosaicId.getId()))
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), MosaicInfoDTO.class))
                        .map(mosaicInfoDTO -> this.createMosaicInfo(mosaicInfoDTO, networkType)));
    }

    @Override
    public Observable<List<MosaicInfo>> getMosaics(List<UInt64Id> mosaicIds) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("mosaicIds", mosaicIds.stream().map(id -> UInt64.bigIntegerToHex(id.getId())).collect(Collectors.toList()));

        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .postAbs(this.url + "/mosaic")
                        .as(BodyCodec.jsonArray())
                        .rxSendJson(requestBody)
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<MosaicInfoDTO>>readValue(json.toString(), new TypeReference<List<MosaicInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(mosaicInfoDTO -> this.createMosaicInfo(mosaicInfoDTO, networkType))
                        .toList()
                        .toObservable());
    }

    private MosaicInfo createMosaicInfo(MosaicInfoDTO mosaicInfoDTO, NetworkType networkType) {
        return new MosaicInfo(mosaicInfoDTO.getMeta().getId(),
                new MosaicId(mosaicInfoDTO.getMosaic().getMosaicId().extractIntArray()),
                mosaicInfoDTO.getMosaic().getSupply().extractIntArray(),
                mosaicInfoDTO.getMosaic().getHeight().extractIntArray(),
                new PublicAccount(mosaicInfoDTO.getMosaic().getOwner(), networkType),
                mosaicInfoDTO.getMosaic().getRevision(),
                extractMosaicProperties(mosaicInfoDTO.getMosaic().getProperties()),
                mosaicInfoDTO.getMosaic().getLevy());
    }

    private MosaicProperties extractMosaicProperties(MosaicPropertiesDTO mosaicPropertiesDTO) {
        String flags = "00" + Integer.toBinaryString(mosaicPropertiesDTO.get(0).extractIntArray().intValue());
        String bitMapFlags = flags.substring(flags.length() - 3, flags.length());
        return new MosaicProperties(bitMapFlags.charAt(2) == '1',
                bitMapFlags.charAt(1) == '1',
                bitMapFlags.charAt(0) == '1',
                mosaicPropertiesDTO.get(1).extractIntArray().intValue(),
                mosaicPropertiesDTO.get(2).extractIntArray());
    }
}