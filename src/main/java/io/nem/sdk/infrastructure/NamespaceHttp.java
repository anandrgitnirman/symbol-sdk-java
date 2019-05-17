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
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.namespace.*;
import io.nem.sdk.model.transaction.UInt64;
import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Namespace http repository.
 *
 * @since 1.0
 */
public class NamespaceHttp extends Http implements NamespaceRepository {

    public NamespaceHttp(String host) throws MalformedURLException {
        this(host, new NetworkHttp(host));
    }

    public NamespaceHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host, networkHttp);
    }

    @Override
    public Observable<NamespaceInfo> getNamespace(NamespaceId namespaceId) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/namespace/" + UInt64.bigIntegerToHex(namespaceId.getId()))
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), NamespaceInfoDTO.class))
                        .map(namespaceInfoDTO -> this.createNamespaceInfo(namespaceInfoDTO, networkType)));
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address, QueryParams queryParams) {
        return this.getNamespacesFromAccount(address, Optional.of(queryParams));
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address) {
        return this.getNamespacesFromAccount(address, Optional.empty());
    }

    private Observable<List<NamespaceInfo>> getNamespacesFromAccount(Address address, Optional<QueryParams> queryParams) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/account/" + address.plain() + "/namespaces" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                        .as(BodyCodec.jsonArray())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<NamespaceInfoDTO>>readValue(json.toString(), new TypeReference<List<NamespaceInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> this.createNamespaceInfo(namespaceInfoDTO, networkType))
                        .toList()
                        .toObservable());
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses, QueryParams queryParams) {
        return this.getNamespacesFromAccounts(addresses, Optional.of(queryParams));
    }

    @Override
    public Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses) {
        return this.getNamespacesFromAccounts(addresses, Optional.empty());
    }

    private Observable<List<NamespaceInfo>> getNamespacesFromAccounts(List<Address> addresses, Optional<QueryParams> queryParams) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("addresses", addresses.stream().map((address -> address.plain())).collect(Collectors.toList()));
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .postAbs(this.url + "/account/namespaces" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                        .as(BodyCodec.jsonArray())
                        .rxSendJson(requestBody)
                        .toObservable()
                        .map(Http::mapJsonArrayOrError)
                        .map(json -> objectMapper.<List<NamespaceInfoDTO>>readValue(json.toString(), new TypeReference<List<NamespaceInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(namespaceInfoDTO -> this.createNamespaceInfo(namespaceInfoDTO, networkType))
                        .toList()
                        .toObservable());
    }

    @Override
    public Observable<List<NamespaceName>> getNamespaceNames(List<NamespaceId> namespaceIds) {
        JsonObject requestBody = new JsonObject();
        requestBody.put("namespaceIds", namespaceIds.stream().map(id -> UInt64.bigIntegerToHex(id.getId())).collect(Collectors.toList()));
        return this.client
                .postAbs(this.url + "/namespace/names")
                .as(BodyCodec.jsonArray())
                .rxSendJson(requestBody)
                .toObservable()
                .map(Http::mapJsonArrayOrError)
                .map(json -> objectMapper.<List<NamespaceNameDTO>>readValue(json.toString(), new TypeReference<List<NamespaceNameDTO>>() {
                }))
                .flatMapIterable(item -> item)
                .map(namespaceNameDTO -> {
                    if (namespaceNameDTO.getParentId() != null) {
                        return new NamespaceName(
                                new NamespaceId(namespaceNameDTO.getNamespaceId().extractIntArray()),
                                namespaceNameDTO.getName(),
                                new NamespaceId(namespaceNameDTO.getParentId().extractIntArray()));
                    } else {
                        return new NamespaceName(
                                new NamespaceId(namespaceNameDTO.getNamespaceId().extractIntArray()),
                                namespaceNameDTO.getName());
                    }
                })
                .toList()
                .toObservable();
    }

    /**
     * Gets the MosaicId from a MosaicAlias
     * @param namespaceId - the namespaceId of the namespace
     * @return Observable of <{@link MosaicId}>
     */
    @Override
    public Observable<MosaicId> getLinkedMosaicId(NamespaceId namespaceId) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/namespace/" + UInt64.bigIntegerToHex(namespaceId.getId()))
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), NamespaceInfoDTO.class))
                        .map(namespaceInfoDTO -> this.createMosaicId(namespaceInfoDTO.getNamespace())));
    }

    /**
     * Gets the Address from a AddressAlias
     * @param namespaceId - the namespaceId of the namespace
     * @return Observable of <{@link MosaicId}>
     */
    @Override
    public Observable<Address> getLinkedAddress(NamespaceId namespaceId) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/namespace/" + UInt64.bigIntegerToHex(namespaceId.getId()))
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(Http::mapJsonObjectOrError)
                        .map(json -> objectMapper.readValue(json.toString(), NamespaceInfoDTO.class))
                        .map(namespaceInfoDTO -> this.createAddress(namespaceInfoDTO.getNamespace())));
    }

    /**
     * Create a NamespaceInfo from a NamespaceInfoDTO and a NetworkType
     *
     * @internal
     * @access private
     * @param namespaceInfoDTO, networkType
     */
    private NamespaceInfo createNamespaceInfo(NamespaceInfoDTO namespaceInfoDTO, NetworkType networkType) {
        return new NamespaceInfo(namespaceInfoDTO.getMeta().isActive(),
                namespaceInfoDTO.getMeta().getIndex(),
                namespaceInfoDTO.getMeta().getId(),
                NamespaceType.rawValueOf(namespaceInfoDTO.getNamespace().getType()),
                namespaceInfoDTO.getNamespace().getDepth(),
                this.extractLevels(namespaceInfoDTO),
                new NamespaceId(namespaceInfoDTO.getNamespace().getParentId().extractIntArray()),
                new PublicAccount(namespaceInfoDTO.getNamespace().getOwner(), networkType),
                namespaceInfoDTO.getNamespace().getStartHeight().extractIntArray(),
                namespaceInfoDTO.getNamespace().getEndHeight().extractIntArray(),
                this.extractAlias(namespaceInfoDTO.getNamespace()));
    }

    /**
     * Create a MosaicId from a NamespaceDTO
     *
     * @internal
     * @access private
     * @param namespaceDTO
     */
    private MosaicId createMosaicId(NamespaceDTO namespaceDTO) {
        MosaicId mosaicId = null;
        if (namespaceDTO.getAlias() != null) {
            if (namespaceDTO.getAlias().getType() == AliasType.Mosaic.getValue()) {
                mosaicId = new MosaicId(namespaceDTO.getAlias().getMosaicId().extractIntArray());
            }
        }
        return mosaicId;
    }

    /**
     * Create a Address from a NamespaceDTO
     *
     * @internal
     * @access private
     * @param namespaceDTO
     */
    private Address createAddress(NamespaceDTO namespaceDTO) {
        Address address = null;
        if (namespaceDTO.getAlias() != null) {
            if (namespaceDTO.getAlias().getType() == AliasType.Address.getValue()) {
                String rawAddress = namespaceDTO.getAlias().getAddress();
                address = Address.createFromRawAddress(rawAddress);
            }
        }
        return address;
    }

    /**
     * Extract a list of NamespaceId levels from a NamespaceInfoDTO
     *
     * @internal
     * @access private
     * @param namespaceInfoDTO
     */
    private List<NamespaceId> extractLevels(NamespaceInfoDTO namespaceInfoDTO) {
        List<NamespaceId> levels = new ArrayList<NamespaceId>();
        if (namespaceInfoDTO.getNamespace().getLevel0() != null) {
            levels.add(new NamespaceId(namespaceInfoDTO.getNamespace().getLevel0().extractIntArray()));
        }

        if (namespaceInfoDTO.getNamespace().getLevel1() != null) {
            levels.add(new NamespaceId(namespaceInfoDTO.getNamespace().getLevel1().extractIntArray()));
        }

        if (namespaceInfoDTO.getNamespace().getLevel2() != null) {
            levels.add(new NamespaceId(namespaceInfoDTO.getNamespace().getLevel2().extractIntArray()));
        }

        return levels;
    }

    /**
     * Extract the alias from a NamespaceDTO
     *
     * @internal
     * @access private
     * @param namespaceDTO
     */
    private Alias extractAlias(NamespaceDTO namespaceDTO) {

        Alias alias = new EmptyAlias();
        if (namespaceDTO.getAlias() != null) {
            if (namespaceDTO.getAlias().getType() == AliasType.Mosaic.getValue()) {
                BigInteger mosaicId = namespaceDTO.getAlias().getMosaicId().extractIntArray();
                return new MosaicAlias(new MosaicId(mosaicId));
            } else if (namespaceDTO.getAlias().getType() == AliasType.Address.getValue()) {
                String rawAddress = namespaceDTO.getAlias().getAddress();
                return new AddressAlias(Address.createFromRawAddress(rawAddress));
            }
        }

        return alias;
    }
}
