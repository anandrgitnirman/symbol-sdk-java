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
import static io.nem.core.utils.MapperUtils.toMosaicId;

import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.transaction.JsonHelper;
import io.nem.sdk.model.transaction.Message;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.TransactionFactory;
import io.nem.sdk.model.transaction.TransactionType;
import io.nem.sdk.model.transaction.TransferTransaction;
import io.nem.sdk.model.transaction.TransferTransactionFactory;
import io.nem.sdk.openapi.okhttp_gson.model.TransferTransactionDTO;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bouncycastle.util.encoders.Hex;

class TransferTransactionMapper extends
    AbstractTransactionMapper<TransferTransactionDTO, TransferTransaction> {

    public TransferTransactionMapper(JsonHelper jsonHelper) {
        super(jsonHelper, TransactionType.TRANSFER, TransferTransactionDTO.class);
    }

    @Override
    protected TransactionFactory<TransferTransaction> createFactory(NetworkType networkType,
        TransferTransactionDTO transaction) {
        List<Mosaic> mosaics = new ArrayList<>();
        if (transaction.getMosaics() != null) {
            mosaics =
                transaction.getMosaics().stream()
                    .map(
                        mosaic ->
                            new Mosaic(
                                toMosaicId(mosaic.getId()),
                                mosaic.getAmount()))
                    .collect(Collectors.toList());
        }

        Message message = PlainMessage.Empty;
        if (transaction.getMessage() != null) {
            message =
                new PlainMessage(
                    new String(
                        Hex.decode(transaction.getMessage().getPayload()),
                        StandardCharsets.UTF_8));
        }

        return new TransferTransactionFactory(networkType,
            Optional.of(toAddressFromUnresolved(transaction.getRecipientAddress())),
            Optional.empty(),
            mosaics,
            message);
    }

}