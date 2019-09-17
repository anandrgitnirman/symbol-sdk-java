/*
 * Copyright 2019. NEM
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.nem.sdk.infrastructure.vertx.mappers;

import static io.nem.core.utils.MapperUtils.toNamespaceId;

import io.nem.core.utils.MapperUtils;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.namespace.NamespaceRegistrationType;
import io.nem.sdk.model.transaction.JsonHelper;
import io.nem.sdk.model.transaction.NamespaceRegistrationTransaction;
import io.nem.sdk.model.transaction.NamespaceRegistrationTransactionFactory;
import io.nem.sdk.model.transaction.TransactionFactory;
import io.nem.sdk.model.transaction.TransactionType;
import io.nem.sdk.openapi.vertx.model.NamespaceRegistrationTransactionDTO;
import java.util.Optional;

class NamespaceRegistrationTransactionMapper extends
    AbstractTransactionMapper<NamespaceRegistrationTransactionDTO, NamespaceRegistrationTransaction> {

    public NamespaceRegistrationTransactionMapper(JsonHelper jsonHelper) {
        super(jsonHelper, TransactionType.REGISTER_NAMESPACE,
            NamespaceRegistrationTransactionDTO.class);
    }

    @Override
    protected TransactionFactory<NamespaceRegistrationTransaction> createFactory(
        NetworkType networkType, NamespaceRegistrationTransactionDTO transaction) {

        NamespaceRegistrationType namespaceRegistrationType = NamespaceRegistrationType
            .rawValueOf(transaction.getRegistrationType().getValue());

        return new NamespaceRegistrationTransactionFactory(networkType,
            transaction.getName(),
            toNamespaceId(transaction.getId()),
            namespaceRegistrationType,
            namespaceRegistrationType == NamespaceRegistrationType.ROOT_NAMESPACE
                ? Optional.of(transaction.getDuration())
                : Optional.empty(),
            namespaceRegistrationType == NamespaceRegistrationType.SUB_NAMESPACE
                ? Optional
                .of(MapperUtils.toNamespaceId(transaction.getParentId()))
                : Optional.empty());
    }

}
