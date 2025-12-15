package com.matchscribe.matchscribe_backend.controller.publicapi;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.entity.Match;
import com.matchscribe.matchscribe_backend.entity.enums.MatchStatus;
import com.matchscribe.matchscribe_backend.repository.MatchRepository;

@RestController
public class SitemapController {

	private final MatchRepository matchRepository;

	public SitemapController(MatchRepository matchRepository) {
		this.matchRepository = matchRepository;
	}

	@GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public String sitemap() {

		List<Match> matches = matchRepository.findByStatus(MatchStatus.scheduled.toString());

		String domain = "https://divineinfo.com"; // change later

		StringBuilder xml = new StringBuilder();
		xml.append("""
				    <?xml version="1.0" encoding="UTF-8"?>
				    <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
				""");

		// Homepage
		xml.append("""
				    <url>
				      <loc>%s/</loc>
				      <changefreq>daily</changefreq>
				      <priority>1.0</priority>
				    </url>
				""".formatted(domain));

		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

		for (Match match : matches) {
			xml.append("""
					    <url>
					      <loc>%s/match/%s</loc>
					      <lastmod>%s</lastmod>
					      <changefreq>daily</changefreq>
					      <priority>0.8</priority>
					    </url>
					""".formatted(domain, match.getSlug(), match.getUpdatedAt().format(formatter)));
		}

		xml.append("</urlset>");
		return xml.toString();
	}
}
