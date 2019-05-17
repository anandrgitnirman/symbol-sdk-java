package io.nem.sdk.model.account;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountPropertiesInfoTest {

    @Test
    void shouldCreateAccountPropertiesInfoViaConstructor() {
        String metaId = "12345";
        Address address = Address.createFromEncoded("9050B9837EFAB4BBE8A4B9BB32D812F9885C00D8FC1650E142");
        AccountProperty accountProperty = new AccountProperty(PropertyType.AllowAddress, Arrays.asList("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"));
        AccountProperties accountProperties = new AccountProperties(address, Arrays.asList(accountProperty));
        AccountPropertiesInfo accountPropertiesInfo = new AccountPropertiesInfo(metaId, accountProperties);

        assertEquals(metaId, accountPropertiesInfo.getMetaId());
        assertEquals(address, accountPropertiesInfo.getAccountProperties().getAddress());
        assertEquals(1, accountPropertiesInfo.getAccountProperties().getProperties().size());
        assertEquals(PropertyType.AllowAddress, accountPropertiesInfo.getAccountProperties().getProperties().get(0).getPropertyType());
    }
}
