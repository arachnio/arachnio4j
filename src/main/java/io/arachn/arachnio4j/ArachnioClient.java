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
package io.arachn.arachnio4j;

import java.util.List;
import io.arachn.spi.model.DomainName;
import io.arachn.spi.model.DomainNameBatch;
import io.arachn.spi.model.DomainNameBatchEntry;
import io.arachn.spi.model.ExtractedLink;
import io.arachn.spi.model.Link;
import io.arachn.spi.model.LinkBatch;
import io.arachn.spi.model.LinkBatchEntry;
import io.arachn.spi.model.ParsedDomainName;
import io.arachn.spi.model.ParsedDomainNameBatch;
import io.arachn.spi.model.ParsedLink;
import io.arachn.spi.model.ParsedLinkBatch;
import io.arachn.spi.model.UnwoundLink;
import io.arachn.spi.model.UnwoundLinkBatch;

public interface ArachnioClient {
  default ParsedDomainName parseDomainName(String hostname) {
    return parseDomainName(new DomainName().hostname(hostname));
  }

  public ParsedDomainName parseDomainName(DomainName domainName);

  default ParsedDomainNameBatch parseDomainNameBatch(List<DomainNameBatchEntry> hostnames) {
    return parseDomainNameBatch(new DomainNameBatch().entries(hostnames));
  }

  public ParsedDomainNameBatch parseDomainNameBatch(DomainNameBatch domainNameBatch);

  default ExtractedLink extractLink(String url) {
    return extractLink(new Link().url(url));
  }

  public ExtractedLink extractLink(Link link);

  default ParsedLink parseLink(String url) {
    return parseLink(new Link().url(url));
  }

  public ParsedLink parseLink(Link link);

  default ParsedLinkBatch parseLinkBatch(List<LinkBatchEntry> links) {
    return parseLinkBatch(new LinkBatch().entries(links));
  }

  public ParsedLinkBatch parseLinkBatch(LinkBatch linkBatch);

  default UnwoundLink unwindLink(String url) {
    return unwindLink(new Link().url(url));
  }

  public UnwoundLink unwindLink(Link link);

  default UnwoundLinkBatch unwindLinkBatch(List<LinkBatchEntry> links) {
    return unwindLinkBatch(new LinkBatch().entries(links));
  }

  public UnwoundLinkBatch unwindLinkBatch(LinkBatch linkBatch);
}
