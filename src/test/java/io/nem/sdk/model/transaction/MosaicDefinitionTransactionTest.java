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

package io.nem.sdk.model.transaction;

import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicNonce;
import io.nem.sdk.model.mosaic.MosaicProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class MosaicDefinitionTransactionTest {

    @Test
    void createAMosaicCreationTransactionViaStaticConstructor() {
        MosaicDefinitionTransaction mosaicCreationTx = MosaicDefinitionTransaction.create(
                new Deadline(2, ChronoUnit.HOURS),
                MosaicNonce.createFromBigInteger(new BigInteger("0")),
                new MosaicId(new BigInteger("0")),
                new MosaicProperties(true, true, true, 3, BigInteger.valueOf(10)),
                NetworkType.MIJIN_TEST
        );

        assertEquals(NetworkType.MIJIN_TEST, mosaicCreationTx.getNetworkType());
        assertTrue(3 == mosaicCreationTx.getVersion());
        assertTrue(LocalDateTime.now().isBefore(mosaicCreationTx.getDeadline().getLocalDateTime()));
        assertEquals(BigInteger.valueOf(0), mosaicCreationTx.getFee());
        assertEquals(new BigInteger("0"), mosaicCreationTx.getMosaicId().getId());
        assertEquals(true, mosaicCreationTx.getMosaicProperties().isSupplyMutable());
        assertEquals(true, mosaicCreationTx.getMosaicProperties().isTransferable());
        assertEquals(true, mosaicCreationTx.getMosaicProperties().isLevyMutable());
        assertEquals(3, mosaicCreationTx.getMosaicProperties().getDivisibility());
        assertEquals(BigInteger.valueOf(10), mosaicCreationTx.getMosaicProperties().getDuration());
    }

    /* TODO after catbuffer integration
    @Test
    @DisplayName("Serialization")
    void serialization() {
        byte[] expected = new byte[]{(byte) 156, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                2, (byte) 144, 77, 65, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, (byte) 155, (byte) 138, 22, 28, (byte) 245, 9, 35, (byte) 144, 21, (byte) 153, 17, (byte) 174, (byte) 167, 46, (byte) 189, 60, 7, 1, 7, 4, 109, 111, 115, 97, 105, 99, 115, 2, 16, 39, 0, 0, 0, 0, 0, 0};

        MosaicDefinitionTransaction mosaicDefinitionTransaction = MosaicDefinitionTransaction.create(
                new FakeDeadline(),
                MosaicNonce.createFromBigInteger(new BigInteger("0")),
                new MosaicId(new BigInteger("0")),
                new MosaicProperties(true, true, true, 4, BigInteger.valueOf(10000)),
                NetworkType.MIJIN_TEST
        );

        byte[] actual = mosaicDefinitionTransaction.generateBytes();
        assertArrayEquals(expected, actual);
    }*/
}
