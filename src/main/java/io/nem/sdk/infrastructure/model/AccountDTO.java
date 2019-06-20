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
import java.util.ArrayList;
import java.util.List;
import io.nem.sdk.infrastructure.model.MosaicDTO;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

/**
 * AccountDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2019-06-20T19:56:23.892+01:00[Europe/London]")
public class AccountDTO {
  public static final String SERIALIZED_NAME_ADDRESS = "address";
  @SerializedName(SERIALIZED_NAME_ADDRESS)
  private String address;

  public static final String SERIALIZED_NAME_ADDRESS_HEIGHT = "addressHeight";
  @SerializedName(SERIALIZED_NAME_ADDRESS_HEIGHT)
  private List<Integer> addressHeight = new ArrayList<Integer>();

  public static final String SERIALIZED_NAME_PUBLIC_KEY = "publicKey";
  @SerializedName(SERIALIZED_NAME_PUBLIC_KEY)
  private String publicKey;

  public static final String SERIALIZED_NAME_PUBLIC_KEY_HEIGHT = "publicKeyHeight";
  @SerializedName(SERIALIZED_NAME_PUBLIC_KEY_HEIGHT)
  private List<Integer> publicKeyHeight = new ArrayList<Integer>();

  public static final String SERIALIZED_NAME_MOSAICS = "mosaics";
  @SerializedName(SERIALIZED_NAME_MOSAICS)
  private List<MosaicDTO> mosaics = new ArrayList<MosaicDTO>();

  public static final String SERIALIZED_NAME_IMPORTANCE = "importance";
  @SerializedName(SERIALIZED_NAME_IMPORTANCE)
  private List<Integer> importance = new ArrayList<Integer>();

  public static final String SERIALIZED_NAME_IMPORTANCE_HEIGHT = "importanceHeight";
  @SerializedName(SERIALIZED_NAME_IMPORTANCE_HEIGHT)
  private List<Integer> importanceHeight = new ArrayList<Integer>();

  public AccountDTO address(String address) {
    this.address = address;
    return this;
  }

   /**
   * The account unique address in hexadecimal. 
   * @return address
  **/
  @ApiModelProperty(example = "9081FCCB41F8C8409A9B99E485E0E28D23BD6304EF7215E01A", required = true, value = "The account unique address in hexadecimal. ")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public AccountDTO addressHeight(List<Integer> addressHeight) {
    this.addressHeight = addressHeight;
    return this;
  }

  public AccountDTO addAddressHeightItem(Integer addressHeightItem) {
    this.addressHeight.add(addressHeightItem);
    return this;
  }

   /**
   * Get addressHeight
   * @return addressHeight
  **/
  @ApiModelProperty(example = "[lower, higher]", required = true, value = "")
  public List<Integer> getAddressHeight() {
    return addressHeight;
  }

  public void setAddressHeight(List<Integer> addressHeight) {
    this.addressHeight = addressHeight;
  }

  public AccountDTO publicKey(String publicKey) {
    this.publicKey = publicKey;
    return this;
  }

   /**
   * The public key of an account can be used to verify signatures of the account. Only accounts that have already published a transaction have a public key assigned to the account. Otherwise, the field is null. 
   * @return publicKey
  **/
  @ApiModelProperty(example = "AC1A6E1D8DE5B17D2C6B1293F1CAD3829EEACF38D09311BB3C8E5A880092DE26", required = true, value = "The public key of an account can be used to verify signatures of the account. Only accounts that have already published a transaction have a public key assigned to the account. Otherwise, the field is null. ")
  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public AccountDTO publicKeyHeight(List<Integer> publicKeyHeight) {
    this.publicKeyHeight = publicKeyHeight;
    return this;
  }

  public AccountDTO addPublicKeyHeightItem(Integer publicKeyHeightItem) {
    this.publicKeyHeight.add(publicKeyHeightItem);
    return this;
  }

   /**
   * Get publicKeyHeight
   * @return publicKeyHeight
  **/
  @ApiModelProperty(example = "[lower, higher]", required = true, value = "")
  public List<Integer> getPublicKeyHeight() {
    return publicKeyHeight;
  }

  public void setPublicKeyHeight(List<Integer> publicKeyHeight) {
    this.publicKeyHeight = publicKeyHeight;
  }

  public AccountDTO mosaics(List<MosaicDTO> mosaics) {
    this.mosaics = mosaics;
    return this;
  }

  public AccountDTO addMosaicsItem(MosaicDTO mosaicsItem) {
    this.mosaics.add(mosaicsItem);
    return this;
  }

   /**
   * The list of mosaics the account owns. The amount is represented in absolute amount. Thus a balance of 123456789 for a mosaic with divisibility 6 (absolute) means the account owns 123.456789 instead. 
   * @return mosaics
  **/
  @ApiModelProperty(required = true, value = "The list of mosaics the account owns. The amount is represented in absolute amount. Thus a balance of 123456789 for a mosaic with divisibility 6 (absolute) means the account owns 123.456789 instead. ")
  public List<MosaicDTO> getMosaics() {
    return mosaics;
  }

  public void setMosaics(List<MosaicDTO> mosaics) {
    this.mosaics = mosaics;
  }

  public AccountDTO importance(List<Integer> importance) {
    this.importance = importance;
    return this;
  }

  public AccountDTO addImportanceItem(Integer importanceItem) {
    this.importance.add(importanceItem);
    return this;
  }

   /**
   * Get importance
   * @return importance
  **/
  @ApiModelProperty(example = "[lower, higher]", required = true, value = "")
  public List<Integer> getImportance() {
    return importance;
  }

  public void setImportance(List<Integer> importance) {
    this.importance = importance;
  }

  public AccountDTO importanceHeight(List<Integer> importanceHeight) {
    this.importanceHeight = importanceHeight;
    return this;
  }

  public AccountDTO addImportanceHeightItem(Integer importanceHeightItem) {
    this.importanceHeight.add(importanceHeightItem);
    return this;
  }

   /**
   * Get importanceHeight
   * @return importanceHeight
  **/
  @ApiModelProperty(example = "[lower, higher]", required = true, value = "")
  public List<Integer> getImportanceHeight() {
    return importanceHeight;
  }

  public void setImportanceHeight(List<Integer> importanceHeight) {
    this.importanceHeight = importanceHeight;
  }

  public String getAddressEncoded() throws DecoderException {
    return new String(new Base32().encode(Hex.decodeHex(address)));
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountDTO accountDTO = (AccountDTO) o;
    return Objects.equals(this.address, accountDTO.address) &&
        Objects.equals(this.addressHeight, accountDTO.addressHeight) &&
        Objects.equals(this.publicKey, accountDTO.publicKey) &&
        Objects.equals(this.publicKeyHeight, accountDTO.publicKeyHeight) &&
        Objects.equals(this.mosaics, accountDTO.mosaics) &&
        Objects.equals(this.importance, accountDTO.importance) &&
        Objects.equals(this.importanceHeight, accountDTO.importanceHeight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, addressHeight, publicKey, publicKeyHeight, mosaics, importance, importanceHeight);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountDTO {\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    addressHeight: ").append(toIndentedString(addressHeight)).append("\n");
    sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
    sb.append("    publicKeyHeight: ").append(toIndentedString(publicKeyHeight)).append("\n");
    sb.append("    mosaics: ").append(toIndentedString(mosaics)).append("\n");
    sb.append("    importance: ").append(toIndentedString(importance)).append("\n");
    sb.append("    importanceHeight: ").append(toIndentedString(importanceHeight)).append("\n");
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

