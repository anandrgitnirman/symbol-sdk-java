/*
 * Copyright 2019 NEM
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
/*
 * Catapult REST API Reference
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.7.15
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package io.nem.sdk.infrastructure.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import io.nem.sdk.infrastructure.model.MultisigDTO;

/**
 * MultisigAccountInfoDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-06-20T19:56:23.892+01:00[Europe/London]")
public class MultisigAccountInfoDTO {
  public static final String SERIALIZED_NAME_MULTISIG = "multisig";
  @SerializedName(SERIALIZED_NAME_MULTISIG)
  private MultisigDTO multisig = null;

  public MultisigAccountInfoDTO multisig(MultisigDTO multisig) {
    this.multisig = multisig;
    return this;
  }

   /**
   * Get multisig
   * @return multisig
  **/
  @ApiModelProperty(required = true, value = "")
  public MultisigDTO getMultisig() {
    return multisig;
  }

  public void setMultisig(MultisigDTO multisig) {
    this.multisig = multisig;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultisigAccountInfoDTO multisigAccountInfoDTO = (MultisigAccountInfoDTO) o;
    return Objects.equals(this.multisig, multisigAccountInfoDTO.multisig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(multisig);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultisigAccountInfoDTO {\n");
    sb.append("    multisig: ").append(toIndentedString(multisig)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

