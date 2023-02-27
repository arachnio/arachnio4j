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
package io.arachn.arachnio4j.client.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.Test;
import io.arachn.arachnio4j.util.Jackson;
import io.arachn.spi.model.ArticleWebpageEntityMetadata;
import io.arachn.spi.model.Authority;
import io.arachn.spi.model.DomainNameHost;
import io.arachn.spi.model.ExtractedLink;
import io.arachn.spi.model.Hyperlink;
import io.arachn.spi.model.ImageMetadata;
import io.arachn.spi.model.ParsedDomainName;
import io.arachn.spi.model.ParsedLink;
import io.arachn.spi.model.Scheme;
import io.arachn.spi.model.UnwindingOutcome;
import io.arachn.spi.model.UnwoundLink;

public class JacksonTest {
  @Test
  public void ignoreUnrecognizedPropertiesTest() throws Exception {
    // Note that we have added foo: bar at the top!
    final String json =
        "{\"foo\":\"bar\",\"link\":{\"original\":{\"link\":\"https://www.nytimes.com/2022/08/25/science/spiders-misinformation-rumors.html\",\"scheme\":\"https\",\"authority\":{\"host\":{\"type\":\"domain\",\"domain\":{\"registrySuffix\":\"com\",\"publicSuffix\":\"nytimes.com\",\"hostname\":\"www.nytimes.com\"}},\"port\":null},\"path\":\"/2022/08/25/science/spiders-misinformation-rumors.html\",\"queryParameters\":[]},\"unwound\":{\"link\":\"https://www.nytimes.com/2022/08/25/science/spiders-misinformation-rumors.html\",\"scheme\":\"https\",\"authority\":{\"host\":{\"type\":\"domain\",\"domain\":{\"registrySuffix\":\"com\",\"publicSuffix\":\"nytimes.com\",\"hostname\":\"www.nytimes.com\"}},\"port\":null},\"path\":\"/2022/08/25/science/spiders-misinformation-rumors.html\",\"queryParameters\":[]},\"outcome\":\"success2xx\",\"canonical\":true},\"entity\":{\"entityType\":\"webpage\",\"webpageType\":\"article\",\"title\":\"Spiders Are Caught in a Global Web of Misinformation\",\"thumbnail\":{\"url\":\"https://static01.nyt.com/images/2022/08/24/science/00SCI-SPIDERLIES-01/00SCI-SPIDERLIES-01-facebookJumbo-v2.jpg\",\"width\":null,\"height\":null},\"description\":\"Researchers looked at thousands of spider news stories to study how sensationalized information spreads. Their findings could be broadly applicable.\",\"keywords\":null,\"author\":null,\"publishedAt\":\"2022-08-25T13:43:50Z\",\"modifiedAt\":\"2022-08-25T16:15:34Z\",\"bodyHtml\":\"<p>We live in a world filled with spiders. And fear of spiders. They crawl around our minds as much as they crawl around our closets, reducing the population of insects that would otherwise bug us. Is that one in the corner, unassumingly spinning its web, venomous? Will it attack me? Should I kill it? Could it be - no, it can't be - but, maybe it is - a <em>black widow?</em></p>\\n<p>Catherine Scott, an arachnologist at McGill University, is familiar with the bad rap spiders get. When she tells people what she does, she is often presented with a story about \\\"that one time a spider bit me.\\\" The thing is, she says, if you don't see a crushed up spider near you, or see one on your body, it's likely that the bite mark on your skin came from something else. There are more than 50,000 known species of spiders in the world, and only a few can harm humans.</p>\\n<p>\\\"Even medical professionals don't always have the best information, and they very often misdiagnose bites,\\\" Dr. Scott said.</p>\\n<p>It turns out that these fears and misunderstandings of our eight-legged friends are <a href=\\\"https://www.nature.com/articles/s41597-022-01197-6\\\" rel=\\\"nofollow\\\">reflected in the news</a>. Recently, more than 60 researchers from around the world, including Dr. Scott, collected 5,348 news stories about spider bites, published online from 2010 through 2020 from 81 countries in 40 languages. They read through each story, noting whether any had factual errors or emotionally fraught language. The percentage of articles they rated sensationalistic: 43 percent. The percentage of articles that had factual errors: 47 percent.</p>\",\"bodyText\":\"We live in a world filled with spiders. And fear of spiders. They crawl around our minds as much as they crawl around our closets, reducing the population of insects that would otherwise bug us. Is that one in the corner, unassumingly spinning its web, venomous? Will it attack me? Should I kill it? Could it be - no, it can't be - but, maybe it is - a black widow? Catherine Scott, an arachnologist at McGill University, is familiar with the bad rap spiders get. When she tells people what she does, she is often presented with a story about \\\"that one time a spider bit me.\\\" The thing is, she says, if you don't see a crushed up spider near you, or see one on your body, it's likely that the bite mark on your skin came from something else. There are more than 50,000 known species of spiders in the world, and only a few can harm humans. \\\"Even medical professionals don't always have the best information, and they very often misdiagnose bites,\\\" Dr. Scott said. It turns out that these fears and misunderstandings of our eight-legged friends are reflected in the news. Recently, more than 60 researchers from around the world, including Dr. Scott, collected 5,348 news stories about spider bites, published online from 2010 through 2020 from 81 countries in 40 languages. They read through each story, noting whether any had factual errors or emotionally fraught language. The percentage of articles they rated sensationalistic: 43 percent. The percentage of articles that had factual errors: 47 percent.\",\"bodyLinks\":[{\"href\":{\"link\":\"https://www.nature.com/articles/s41597-022-01197-6\",\"scheme\":\"https\",\"authority\":{\"host\":{\"type\":\"domain\",\"domain\":{\"registrySuffix\":\"com\",\"publicSuffix\":\"nature.com\",\"hostname\":\"www.nature.com\"}},\"port\":null},\"path\":\"/articles/s41597-022-01197-6\",\"queryParameters\":[]},\"rel\":null,\"outlink\":true,\"anchorText\":\"reflected in the news\"}]}}";
    ExtractedLink extractedLink = Jackson.MAPPER.readValue(json, ExtractedLink.class);
    assertThat(extractedLink,
        is(new ExtractedLink()
            .entity(new ArticleWebpageEntityMetadata()
                .publishedAt(LocalDateTime.of(LocalDate.of(2022, 8, 25), LocalTime.of(13, 43, 50))
                    .atOffset(ZoneOffset.UTC))
                .modifiedAt(
                    LocalDateTime.of(LocalDate.of(2022, 8, 25), LocalTime.of(16, 15, 34))
                        .atOffset(ZoneOffset.UTC))
                .bodyHtml(
                    "<p>We live in a world filled with spiders. And fear of spiders. They crawl around our minds as much as they crawl around our closets, reducing the population of insects that would otherwise bug us. Is that one in the corner, unassumingly spinning its web, venomous? Will it attack me? Should I kill it? Could it be - no, it can't be - but, maybe it is - a <em>black widow?</em></p>\n<p>Catherine Scott, an arachnologist at McGill University, is familiar with the bad rap spiders get. When she tells people what she does, she is often presented with a story about \"that one time a spider bit me.\" The thing is, she says, if you don't see a crushed up spider near you, or see one on your body, it's likely that the bite mark on your skin came from something else. There are more than 50,000 known species of spiders in the world, and only a few can harm humans.</p>\n<p>\"Even medical professionals don't always have the best information, and they very often misdiagnose bites,\" Dr. Scott said.</p>\n<p>It turns out that these fears and misunderstandings of our eight-legged friends are <a href=\"https://www.nature.com/articles/s41597-022-01197-6\" rel=\"nofollow\">reflected in the news</a>. Recently, more than 60 researchers from around the world, including Dr. Scott, collected 5,348 news stories about spider bites, published online from 2010 through 2020 from 81 countries in 40 languages. They read through each story, noting whether any had factual errors or emotionally fraught language. The percentage of articles they rated sensationalistic: 43 percent. The percentage of articles that had factual errors: 47 percent.</p>")
                .bodyText(
                    "We live in a world filled with spiders. And fear of spiders. They crawl around our minds as much as they crawl around our closets, reducing the population of insects that would otherwise bug us. Is that one in the corner, unassumingly spinning its web, venomous? Will it attack me? Should I kill it? Could it be - no, it can't be - but, maybe it is - a black widow? Catherine Scott, an arachnologist at McGill University, is familiar with the bad rap spiders get. When she tells people what she does, she is often presented with a story about \"that one time a spider bit me.\" The thing is, she says, if you don't see a crushed up spider near you, or see one on your body, it's likely that the bite mark on your skin came from something else. There are more than 50,000 known species of spiders in the world, and only a few can harm humans. \"Even medical professionals don't always have the best information, and they very often misdiagnose bites,\" Dr. Scott said. It turns out that these fears and misunderstandings of our eight-legged friends are reflected in the news. Recently, more than 60 researchers from around the world, including Dr. Scott, collected 5,348 news stories about spider bites, published online from 2010 through 2020 from 81 countries in 40 languages. They read through each story, noting whether any had factual errors or emotionally fraught language. The percentage of articles they rated sensationalistic: 43 percent. The percentage of articles that had factual errors: 47 percent.")
                .bodyLinks(
                    List.of(
                        new Hyperlink()
                            .href(
                                new ParsedLink()
                                    .link("https://www.nature.com/articles/s41597-022-01197-6")
                                    .scheme(Scheme.HTTPS)
                                    .authority(new Authority().host(new DomainNameHost()
                                        .domain(new ParsedDomainName().registrySuffix("com")
                                            .publicSuffix("nature.com").hostname("www.nature.com"))
                                        .type("domain")))
                                    .path("/articles/s41597-022-01197-6")
                                    .queryParameters(List.of()))
                            .anchorText("reflected in the news").outlink(true)))
                .keywords(null).title("Spiders Are Caught in a Global Web of Misinformation")
                .thumbnail(
                    new ImageMetadata().url(
                        "https://static01.nyt.com/images/2022/08/24/science/00SCI-SPIDERLIES-01/00SCI-SPIDERLIES-01-facebookJumbo-v2.jpg"))
                .description(
                    "Researchers looked at thousands of spider news stories to study how sensationalized information spreads. Their findings could be broadly applicable.")
                .title("Spiders Are Caught in a Global Web of Misinformation").description(
                    "Researchers looked at thousands of spider news stories to study how sensationalized information spreads. Their findings could be broadly applicable.")
                .webpageType("article").entityType("webpage"))
            .link(new UnwoundLink().canonical(true)
                .original(new ParsedLink().link(
                    "https://www.nytimes.com/2022/08/25/science/spiders-misinformation-rumors.html")
                    .authority(
                        new Authority().host(new DomainNameHost()
                            .domain(new ParsedDomainName().registrySuffix("com")
                                .publicSuffix("nytimes.com").hostname("www.nytimes.com"))
                            .type("domain")))
                    .scheme(Scheme.HTTPS)
                    .path("/2022/08/25/science/spiders-misinformation-rumors.html"))
                .outcome(UnwindingOutcome.SUCCESS2XX)
                .unwound(new ParsedLink().link(
                    "https://www.nytimes.com/2022/08/25/science/spiders-misinformation-rumors.html")
                    .authority(new Authority().host(new DomainNameHost()
                        .domain(new ParsedDomainName().registrySuffix("com")
                            .publicSuffix("nytimes.com").hostname("www.nytimes.com"))
                        .type("domain")))
                    .scheme(Scheme.HTTPS)
                    .path("/2022/08/25/science/spiders-misinformation-rumors.html")))));
  }
}
