package com.matchscribe.matchscribe_backend.controller.publicapi;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RobotsController {

	@GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
	public String robots() {

		return """
				User-agent: *
				Allow: /

				Disallow: /api/admin/
				Disallow: /api/internal/

				Sitemap: https://divineinfo.com/sitemap.xml
				""";
	}
}
