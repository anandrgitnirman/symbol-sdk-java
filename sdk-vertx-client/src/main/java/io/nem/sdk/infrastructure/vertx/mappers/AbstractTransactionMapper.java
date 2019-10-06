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

import io.nem.core.crypto.PublicKey;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.JsonHelper;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransactionFactory;
import io.nem.sdk.model.transaction.TransactionInfo;
import io.nem.sdk.model.transaction.TransactionType;
import io.nem.sdk.openapi.vertx.model.EmbeddedTransactionInfoDTO;
import io.nem.sdk.openapi.vertx.model.EmbeddedTransactionMetaDTO;
import io.nem.sdk.openapi.vertx.model.TransactionDTO;
import io.nem.sdk.openapi.vertx.model.TransactionInfoDTO;
import io.nem.sdk.openapi.vertx.model.TransactionMetaDTO;

/**
 * Abstract transaction mapper for the transaction mappers that support a specific type of
 * transaction (Account Link, Mosaic Alias, etc.).
 *
 * @param <T> the dto type of the transaction object.
 */
public abstract class AbstractTransactionMapper<D, T extends Transaction> implements
    TransactionMapper {

    private final TransactionType transactionType;

    private final JsonHelper jsonHelper;

    private Class<D> transactionDtoClass;

    public AbstractTransactionMapper(JsonHelper jsonHelper, TransactionType transactionType,
        Class<D> transactionDtoClass) {
        this.jsonHelper = jsonHelper;
        this.transactionType = transactionType;
        this.transactionDtoClass = transactionDtoClass;
    }


    @Override
    public Transaction map(EmbeddedTransactionInfoDTO transactionInfoDTO) {
        TransactionInfo transactionInfo = createTransactionInfo(transactionInfoDTO.getMeta());
        return createModel(transactionInfo, transactionInfoDTO.getTransaction());
    }

    @Override
    public Transaction map(TransactionInfoDTO transactionInfoDTO) {
        TransactionInfo transactionInfo = createTransactionInfo(transactionInfoDTO.getMeta());
        return createModel(transactionInfo, transactionInfoDTO.getTransaction());
    }

    protected final T createModel(TransactionInfo transactionInfo, Object transactionDto) {
        D transaction = getJsonHelper().convert(transactionDto, transactionDtoClass);
        TransactionDTO transactionDTO = getJsonHelper()
            .convert(transactionDto, TransactionDTO.class);
        NetworkType networkType = extractNetworkType(transactionDTO.getVersion());
        TransactionFactory<T> factory = createFactory(networkType, transaction);
        factory.version(extractTransactionVersion(transactionDTO.getVersion()));
        factory.deadline(new Deadline(transactionDTO.getDeadline()));
        if (transactionDTO.getSignerPublicKey() != null) {
            factory.signer(
                PublicAccount
                    .createFromPublicKey(transactionDTO.getSignerPublicKey(), networkType));
        }
        if (transactionDTO.getSignature() != null) {
            factory.signature(transactionDTO.getSignature());
        }
        if (transactionDTO.getMaxFee() != null) {
            factory.maxFee(transactionDTO.getMaxFee());
        }
        if (transactionInfo != null) {
            factory.transactionInfo(transactionInfo);
        }
        T transactionModel = factory.build();
        if (transactionModel.getType() != getTransactionType()) {
            throw new IllegalStateException(
                "Expected transaction to be " + getTransactionType() + " but got "
                    + transactionModel.getType());
        }
        return transactionModel;
    }

    protected abstract TransactionFactory<T> createFactory(NetworkType networkType, D transaction);

    protected TransactionInfo createTransactionInfo(TransactionMetaDTO meta) {
        return meta == null ? null : TransactionInfo.create(meta.getHeight(),
            meta.getIndex(),
            meta.getId(),
            meta.getHash(),
            meta.getMerkleComponentHash());
    }

    protected TransactionInfo createTransactionInfo(EmbeddedTransactionMetaDTO meta) {
        return meta == null ? null : TransactionInfo.createAggregate(
            meta.getHeight(),
            meta.getIndex(),
            meta.getId(),
            meta.getAggregateHash(),
            meta.getAggregateId());
    }

    @Override
    public EmbeddedTransactionInfoDTO mapToEmbedded(Transaction transaction) {
        EmbeddedTransactionInfoDTO dto = new EmbeddedTransactionInfoDTO();
        dto.setMeta(createTransactionInfoEmbedded(transaction));
        dto.setTransaction(mapTransaction(transaction, true));
        return dto;
    }


    private EmbeddedTransactionMetaDTO createTransactionInfoEmbedded(Transaction transaction) {
        return transaction.getTransactionInfo().map(i -> {
            EmbeddedTransactionMetaDTO dto = new EmbeddedTransactionMetaDTO();
            dto.setHeight(i.getHeight());
            dto.setAggregateHash(i.getAggregateHash().orElse(null));
            dto.setId(i.getId().orElse(null));
            dto.setIndex(i.getIndex().orElse(null));
            dto.setAggregateId(i.getAggregateId().orElse(null));
            return dto;
        }).orElse(null);
    }

    private TransactionMetaDTO createTransactionInfo(Transaction transaction) {
        return transaction.getTransactionInfo().map(i -> {
            TransactionMetaDTO dto = new TransactionMetaDTO();
            dto.setHeight(i.getHeight());
            dto.setHash(i.getHash().orElse(null));
            dto.setId(i.getId().orElse(null));
            dto.setIndex(i.getIndex().orElse(null));
            dto.setMerkleComponentHash(i.getMerkleComponentHash().orElse(null));
            return dto;
        }).orElse(null);
    }


    @Override
    public TransactionInfoDTO map(Transaction transaction) {
        TransactionInfoDTO dto = new TransactionInfoDTO();
        dto.setMeta(createTransactionInfo(transaction));
        dto.setTransaction(mapTransaction(transaction, false));
        return dto;
    }

    private D mapTransaction(Transaction transaction, boolean embedded) {

        TransactionDTO dto = new TransactionDTO();
        dto.setSignerPublicKey(
            transaction.getSigner().map(PublicAccount::getPublicKey).map(PublicKey::toString)
                .orElse(null));

        dto.setVersion(transaction.getTransactionVersion());
        dto.setType(transaction.getType().getValue());

        if (!embedded) {
            dto.setMaxFee(transaction.getMaxFee());
            dto.setDeadline(transaction.getDeadline().toBigInteger());
            dto.setSignature(transaction.getSignature().orElse(null));
        }

        D specificDto = getJsonHelper().parse(getJsonHelper().print(dto), transactionDtoClass);
        copyToDto((T) transaction, specificDto);
        return specificDto;
    }


    protected abstract void copyToDto(T transaction, D dto);

    protected Integer extractTransactionVersion(int version) {
        return (int) Long.parseLong(Integer.toHexString(version).substring(2, 4), 16);
    }

    protected NetworkType extractNetworkType(int version) {
        int networkType = (int) Long.parseLong(Integer.toHexString(version).substring(0, 2), 16);
        return NetworkType.rawValueOf(networkType);
    }


    public JsonHelper getJsonHelper() {
        return jsonHelper;
    }

    @Override
    public TransactionType getTransactionType() {
        return transactionType;
    }

}
