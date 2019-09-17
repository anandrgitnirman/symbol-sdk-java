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

package io.nem.sdk.infrastructure.okhttp.mappers;

import static io.nem.core.utils.MapperUtils.toAddressFromUnresolved;

import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.transaction.LockHashAlgorithmType;
import io.nem.sdk.model.transaction.JsonHelper;
import io.nem.sdk.model.transaction.SecretProofTransaction;
import io.nem.sdk.model.transaction.SecretProofTransactionFactory;
import io.nem.sdk.model.transaction.TransactionFactory;
import io.nem.sdk.model.transaction.TransactionType;
import io.nem.sdk.openapi.okhttp_gson.model.SecretProofTransactionDTO;

class SecretProofTransactionMapper extends
    AbstractTransactionMapper<SecretProofTransactionDTO, SecretProofTransaction> {

    public SecretProofTransactionMapper(JsonHelper jsonHelper) {
        super(jsonHelper, TransactionType.SECRET_PROOF, SecretProofTransactionDTO.class);
    }

    @Override
    protected TransactionFactory<SecretProofTransaction> createFactory(NetworkType networkType,
        SecretProofTransactionDTO transaction) {
        return new SecretProofTransactionFactory(
            networkType,
            LockHashAlgorithmType.rawValueOf(transaction.getHashAlgorithm().getValue()),
            toAddressFromUnresolved(transaction.getRecipientAddress()),
            transaction.getSecret(),
            transaction.getProof());
    }

}
