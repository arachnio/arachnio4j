/*-
 * =================================LICENSE_START==================================
 * arachnio4j
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Arachnio
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package io.arachn.arachnio4j.util;

import java.io.UncheckedIOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.arachn.spi.model.serialization.ArachnioClientModule;

public final class Jackson {
  private Jackson() {}

  public static final ObjectMapper MAPPER =
      new ObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          .registerModule(new ArachnioClientModule()).registerModule(new JavaTimeModule());

  public static <T> String serialize(T value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException("Failed to serialize value", e);
    }
  }

  public static <T> T deserialize(Class<T> type, String s) {
    try {
      return MAPPER.readValue(s, type);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException("Failed to deserialize value", e);
    }
  }
}
