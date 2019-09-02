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

package io.nem.sdk.api;

import io.nem.sdk.model.mosaic.MosaicInfo;
import io.nem.sdk.model.mosaic.MosaicNames;
import io.nem.sdk.model.transaction.UInt64Id;
import io.reactivex.Observable;
import java.util.List;

/**
 * Mosaic interface repository.
 *
 * @since 1.0
 */
public interface MosaicRepository {

    /**
     * Gets a MosaicInfo for a given mosaicId
     *
     * @param mosaicId {@link java.math.BigInteger}
     * @return Observable of {@link MosaicInfo}
     */
    Observable<MosaicInfo> getMosaic(UInt64Id mosaicId);

    /**
     * Gets MosaicInfo for different mosaicIds.
     *
     * @param mosaicIds {@link List} of {@link java.math.BigInteger}
     * @return Observable of List<{@link MosaicInfo}>
     */
    Observable<List<MosaicInfo>> getMosaics(List<UInt64Id> mosaicIds);

    /**
     * Gets MosaicNames for different accounts. The names are namespaces linked using mosaic
     * aliases.
     *
     * @param mosaicIds {@link List} of {@link java.math.BigInteger}
     * @return Observable of List<{@link MosaicNames}>
     */

    Observable<List<MosaicNames>> getMosaicsNames(List<UInt64Id> mosaicIds);

    /**
     * Gets list of MosaicInfo from mosaics created with provided namespace.
     *
     * @param namespaceId BigInteger
     * @param queryParams QueryParams
     * @return Observable of List<{@link MosaicInfo}>
     */
    // Observable<List<MosaicInfo>> getMosaicsFromNamespace(NamespaceId namespaceId, QueryParams
    // queryParams);

    /**
     * Gets list of MosaicInfo from mosaics created with provided namespace. With pagination.
     *
     * @param namespaceId BigInteger
     * @return Observable of List<{@link MosaicInfo}>
     */
    // Observable<List<MosaicInfo>> getMosaicsFromNamespace(NamespaceId namespaceId);

}