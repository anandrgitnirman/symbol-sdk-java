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
package io.nem.sdk.model.transaction;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.NetworkType;
import java.util.List;
import org.apache.commons.lang3.Validate;

/**
 * Factory of {@link AccountAddressRestrictionModificationTransaction}
 */
public class AccountAddressRestrictionModificationTransactionFactory extends
    TransactionFactory<AccountAddressRestrictionModificationTransaction> {

    private final AccountRestrictionType restrictionType;

    private final List<AccountRestrictionModification<Address>> modifications;

    AccountAddressRestrictionModificationTransactionFactory(
        final NetworkType networkType,
        final AccountRestrictionType restrictionType,
        final List<AccountRestrictionModification<Address>> modifications) {
        super(TransactionType.ACCOUNT_PROPERTIES_ADDRESS, networkType);
        Validate.notNull(restrictionType, "RestrictionType must not be null");
        Validate.notNull(modifications, "Modifications must not be null");
        this.restrictionType = restrictionType;
        this.modifications = modifications;
    }


    /**
     * Get account restriction type
     *
     * @return {@link AccountRestrictionType}
     */
    public AccountRestrictionType getRestrictionType() {
        return this.restrictionType;
    }

    /**
     * Get account address restriction modifications
     *
     * @return List of {@link AccountRestrictionModification}
     */
    public List<AccountRestrictionModification<Address>> getModifications() {
        return this.modifications;
    }

    @Override
    public AccountAddressRestrictionModificationTransaction build() {
        return new AccountAddressRestrictionModificationTransaction(this);
    }
}
