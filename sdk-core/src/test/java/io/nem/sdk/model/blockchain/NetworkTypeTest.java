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

package io.nem.sdk.model.blockchain;

import static junit.framework.TestCase.assertTrue;

import org.junit.jupiter.api.Test;

public class NetworkTypeTest {

    @Test
    void MAIN_NETIs0x68() {
        assertTrue(0x68 == NetworkType.MAIN_NET.getValue());
        assertTrue(104 == NetworkType.MAIN_NET.getValue());
    }

    @Test
    void TEST_NETIs0x96() {
        assertTrue(0x98 == NetworkType.TEST_NET.getValue());
        assertTrue(152 == NetworkType.TEST_NET.getValue());
    }

    @Test
    void MIJINIs0x60() {
        assertTrue(0x60 == NetworkType.MIJIN.getValue());
        assertTrue(96 == NetworkType.MIJIN.getValue());
    }

    @Test
    void MIJIN_TESTIs0x90() {
        assertTrue(0x90 == NetworkType.MIJIN_TEST.getValue());
        assertTrue(144 == NetworkType.MIJIN_TEST.getValue());
    }
}
