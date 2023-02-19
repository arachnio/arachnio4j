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
package io.arachn.arachnio4j.client;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import io.aleph0.skein.spi.model.DomainName;
import io.aleph0.skein.spi.model.DomainNameBatch;
import io.aleph0.skein.spi.model.ExtractedLink;
import io.aleph0.skein.spi.model.Link;
import io.aleph0.skein.spi.model.LinkBatch;
import io.aleph0.skein.spi.model.ParsedDomainName;
import io.aleph0.skein.spi.model.ParsedDomainNameBatch;
import io.aleph0.skein.spi.model.ParsedLink;
import io.aleph0.skein.spi.model.ParsedLinkBatch;
import io.aleph0.skein.spi.model.UnwoundLink;
import io.aleph0.skein.spi.model.UnwoundLinkBatch;
import io.arachn.arachnio4j.ArachnioClient;
import io.arachn.arachnio4j.exception.ForbiddenArachnioException;
import io.arachn.arachnio4j.exception.InternalErrorArachnioException;
import io.arachn.arachnio4j.util.Jackson;

/**
 * You can subscribe at <a href="https://developer.arachn.io/">https://developer.arachn.io/</a>.
 */
public class DefaultArachnioClient implements ArachnioClient {
  /**
   * For example. Each product has its own base URL.
   * 
   * @see <a href=
   *      "https://developer.arachn.io/catalog/prd_2jlyolt6e0gkaur4">https://developer.arachn.io/catalog/prd_2jlyolt6e0gkaur4</a>
   */
  public static final String FREE_PLAN_BASE_URL = "https://api.arachn.io/booh1cxg5suxjets";

  /**
   * The header used to perform authentication for the API
   */
  private static final String BLOBR_API_KEY_HEADER_NAME = "BLOBR-API-KEY";

  public static HttpClient defaultClient() {
    return HttpClient.newHttpClient();
  }

  private final HttpClient client;
  private final String baseUrl;
  private final String key;

  public DefaultArachnioClient(String baseUrl, String key) {
    this(defaultClient(), baseUrl, key);
  }

  public DefaultArachnioClient(HttpClient client, String baseUrl, String key) {
    if (baseUrl == null)
      throw new NullPointerException();
    if (baseUrl.endsWith("/"))
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    this.client = requireNonNull(client);
    this.baseUrl = baseUrl;
    this.key = requireNonNull(key);
  }


  @Override
  public ParsedDomainName parseDomainName(DomainName domainName) {
    HttpResponse<String> response;
    try {
      response =
          getClient().send(
              HttpRequest.newBuilder(URI.create(format("%s/domains/parse", getBaseUrl())))
                  .header(BLOBR_API_KEY_HEADER_NAME, getKey())
                  .POST(BodyPublishers.ofString(Jackson.serialize(domainName),
                      StandardCharsets.UTF_8))
                  .build(),
              BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedIOException("interrupted", new InterruptedIOException());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN)
      throw new UncheckedIOException(new ForbiddenArachnioException());
    if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST || response.statusCode() == 422)
      throw new IllegalArgumentException("invalid domain name");
    if (response.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
      throw new UncheckedIOException(new InternalErrorArachnioException());
    if (response.statusCode() != HttpURLConnection.HTTP_OK)
      throw new UncheckedIOException(
          new IOException("unrecognized failure " + response.statusCode()));

    return Jackson.deserialize(ParsedDomainName.class, response.body());
  }


  @Override
  public ParsedDomainNameBatch parseDomainNameBatch(DomainNameBatch domainNameBatch) {
    HttpResponse<String> response;
    try {
      response = getClient().send(
          HttpRequest.newBuilder(URI.create(format("%s/domains/parse/batch", getBaseUrl())))
              .header(BLOBR_API_KEY_HEADER_NAME, getKey()).POST(BodyPublishers
                  .ofString(Jackson.serialize(domainNameBatch), StandardCharsets.UTF_8))
              .build(),
          BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedIOException("interrupted", new InterruptedIOException());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN)
      throw new UncheckedIOException(new ForbiddenArachnioException());
    if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST || response.statusCode() == 422)
      throw new IllegalArgumentException("invalid domain name batch");
    if (response.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
      throw new UncheckedIOException(new InternalErrorArachnioException());
    if (response.statusCode() != HttpURLConnection.HTTP_OK)
      throw new UncheckedIOException(
          new IOException("unrecognized failure " + response.statusCode()));

    return Jackson.deserialize(ParsedDomainNameBatch.class, response.body());
  }


  @Override
  public ExtractedLink extractLink(Link link) {
    HttpResponse<String> response;
    try {
      response = getClient().send(HttpRequest
          .newBuilder(URI.create(format("%s/links/extract", getBaseUrl())))
          .header(BLOBR_API_KEY_HEADER_NAME, getKey())
          .POST(BodyPublishers.ofString(Jackson.serialize(link), StandardCharsets.UTF_8)).build(),
          BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedIOException("interrupted", new InterruptedIOException());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN)
      throw new UncheckedIOException(new ForbiddenArachnioException());
    if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST || response.statusCode() == 422)
      throw new IllegalArgumentException("invalid link");
    if (response.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
      throw new UncheckedIOException(new InternalErrorArachnioException());
    if (response.statusCode() != HttpURLConnection.HTTP_OK)
      throw new UncheckedIOException(
          new IOException("unrecognized failure " + response.statusCode()));

    return Jackson.deserialize(ExtractedLink.class, response.body());
  }


  @Override
  public ParsedLink parseLink(Link link) {
    HttpResponse<String> response;
    try {
      response = getClient().send(HttpRequest
          .newBuilder(URI.create(format("%s/links/parse", getBaseUrl())))
          .header(BLOBR_API_KEY_HEADER_NAME, getKey())
          .POST(BodyPublishers.ofString(Jackson.serialize(link), StandardCharsets.UTF_8)).build(),
          BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedIOException("interrupted", new InterruptedIOException());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN)
      throw new UncheckedIOException(new ForbiddenArachnioException());
    if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST || response.statusCode() == 422)
      throw new IllegalArgumentException("invalid link");
    if (response.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
      throw new UncheckedIOException(new InternalErrorArachnioException());
    if (response.statusCode() != HttpURLConnection.HTTP_OK)
      throw new UncheckedIOException(
          new IOException("unrecognized failure " + response.statusCode()));

    return Jackson.deserialize(ParsedLink.class, response.body());
  }


  @Override
  public ParsedLinkBatch parseLinkBatch(LinkBatch linkBatch) {
    HttpResponse<String> response;
    try {
      response =
          getClient()
              .send(
                  HttpRequest.newBuilder(URI.create(format("%s/links/parse/batch", getBaseUrl())))
                      .header(BLOBR_API_KEY_HEADER_NAME, getKey())
                      .POST(BodyPublishers.ofString(Jackson.serialize(linkBatch),
                          StandardCharsets.UTF_8))
                      .build(),
                  BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedIOException("interrupted", new InterruptedIOException());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN)
      throw new UncheckedIOException(new ForbiddenArachnioException());
    if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST || response.statusCode() == 422)
      throw new IllegalArgumentException("invalid link batch");
    if (response.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
      throw new UncheckedIOException(new InternalErrorArachnioException());
    if (response.statusCode() != HttpURLConnection.HTTP_OK)
      throw new UncheckedIOException(
          new IOException("unrecognized failure " + response.statusCode()));

    return Jackson.deserialize(ParsedLinkBatch.class, response.body());
  }


  @Override
  public UnwoundLink unwindLink(Link link) {
    HttpResponse<String> response;
    try {
      response = getClient().send(HttpRequest
          .newBuilder(URI.create(format("%s/links/unwind", getBaseUrl())))
          .header(BLOBR_API_KEY_HEADER_NAME, getKey())
          .POST(BodyPublishers.ofString(Jackson.serialize(link), StandardCharsets.UTF_8)).build(),
          BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedIOException("interrupted", new InterruptedIOException());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN)
      throw new UncheckedIOException(new ForbiddenArachnioException());
    if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST || response.statusCode() == 422)
      throw new IllegalArgumentException("invalid domain name");
    if (response.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
      throw new UncheckedIOException(new InternalErrorArachnioException());
    if (response.statusCode() != HttpURLConnection.HTTP_OK)
      throw new UncheckedIOException(
          new IOException("unrecognized failure " + response.statusCode()));

    return Jackson.deserialize(UnwoundLink.class, response.body());
  }


  @Override
  public UnwoundLinkBatch unwindLinkBatch(LinkBatch linkBatch) {
    HttpResponse<String> response;
    try {
      response =
          getClient()
              .send(
                  HttpRequest.newBuilder(URI.create(format("%s/links/unwind/batch", getBaseUrl())))
                      .header(BLOBR_API_KEY_HEADER_NAME, getKey())
                      .POST(BodyPublishers.ofString(Jackson.serialize(linkBatch),
                          StandardCharsets.UTF_8))
                      .build(),
                  BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedIOException("interrupted", new InterruptedIOException());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN)
      throw new UncheckedIOException(new ForbiddenArachnioException());
    if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST || response.statusCode() == 422)
      throw new IllegalArgumentException("invalid link batch");
    if (response.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
      throw new UncheckedIOException(new InternalErrorArachnioException());
    if (response.statusCode() != HttpURLConnection.HTTP_OK)
      throw new UncheckedIOException(
          new IOException("unrecognized failure " + response.statusCode()));

    return Jackson.deserialize(UnwoundLinkBatch.class, response.body());
  }

  /**
   * @return the client
   */
  private HttpClient getClient() {
    return client;
  }

  /**
   * @return the baseUrl
   */
  private String getBaseUrl() {
    return baseUrl;
  }

  /**
   * @return the key
   */
  private String getKey() {
    return key;
  }
}
