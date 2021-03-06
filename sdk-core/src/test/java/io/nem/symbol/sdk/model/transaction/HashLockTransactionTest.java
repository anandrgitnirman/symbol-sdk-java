/*
 * Copyright 2020 NEM
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

package io.nem.symbol.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.nem.symbol.sdk.model.account.Account;
import io.nem.symbol.sdk.model.account.PublicAccount;
import io.nem.symbol.sdk.model.mosaic.NetworkCurrency;
import io.nem.symbol.sdk.model.network.NetworkType;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashLockTransactionTest extends AbstractTransactionTester {

    private static NetworkType networkType = NetworkType.MIJIN_TEST;

    static Account account = new Account(
        "787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d", networkType);
    static String generationHash = "57F7DA205008026C776CB6AED843393F04CD458E0AA2D9F1D5F31A402072B2D6";


    @Test
    @DisplayName("Serialization")
    void serialization() {
        String expected =
            "b800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002134e47aee6f2392a5b3d1238cd7714eabeb739361b7ccf24bae127f10df17f200000000019048410000000000000000010000000000000044b262c46ceabb85809698000000000064000000000000008498b38d89c1dc8a448ea5824938ff828926cd9f7747b1844b59b4b6807e878b";
        HashLockTransaction transaction =
            HashLockTransactionFactory.create(
                networkType,
                NetworkCurrency.CAT_CURRENCY.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B")
                .signer(account.getPublicAccount()).deadline(new FakeDeadline())
                .build();

        assertSerialization(expected, transaction);
    }

    @Test
    @DisplayName("To aggregate")
    void toAggregate() {
        String expected =
            "68000000000000009a49366406aca952b88badf5f1e9be6ce4968141035a60be503273ea65456b24000000000190484144b262c46ceabb85809698000000000064000000000000008498b38d89c1dc8a448ea5824938ff828926cd9f7747b1844b59b4b6807e878b";

        HashLockTransaction transaction =
            HashLockTransactionFactory.create(
                networkType,
                NetworkCurrency.CAT_CURRENCY.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B")
                .deadline(new FakeDeadline()).build();

        transaction
            .toAggregate(
                new PublicAccount(
                    "9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24",
                    networkType));

        assertEmbeddedSerialization(expected, transaction);
    }

    @Test
    void serializeAndSignTransaction() {
        SignedTransaction signedTransaction =
            new SignedTransaction(
                Account.generateNewAccount(networkType).getPublicAccount(),
                "payload",
                "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B",
                TransactionType.AGGREGATE_BONDED);
        HashLockTransaction transaction =
            HashLockTransactionFactory.create(
                networkType,
                NetworkCurrency.CAT_CURRENCY.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction).deadline(new FakeDeadline()).build();
        SignedTransaction lockFundsTransactionSigned = transaction
            .signWith(account, generationHash);

        String payload = lockFundsTransactionSigned.getPayload();
        assertEquals(
            "010000000000000044B262C46CEABB85809698000000000064000000000000008498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B",
            payload.substring(240)
        );
        assertEquals(
            "C29536DFA2B6FFBE112965F7695ADCB9D67F9984139709ED5A6A51BF8700BBDB",
            lockFundsTransactionSigned.getHash());
    }

    @Test
    void shouldThrowExceptionWhenSignedTransactionIsNotTypeAggregateBonded() {

        SignedTransaction signedTransaction =
            new SignedTransaction(
                Account.generateNewAccount(networkType).getPublicAccount(),
                "payload",
                "8498B38D89C1DC8A448EA5824938FF828926CD9F7747B1844B59B4B6807E878B",
                TransactionType.TRANSFER);
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                HashLockTransactionFactory.create(
                    networkType,
                    NetworkCurrency.CAT_CURRENCY.createRelative(BigInteger.valueOf(10)),
                    BigInteger.valueOf(100),
                    signedTransaction).deadline(
                    new FakeDeadline()).build();
            },
            "Signed transaction must be Aggregate Bonded Transaction");
    }
}
