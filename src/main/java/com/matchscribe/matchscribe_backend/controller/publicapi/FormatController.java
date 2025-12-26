package com.matchscribe.matchscribe_backend.controller.publicapi;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.matchscribe.matchscribe_backend.entity.Formats;
import com.matchscribe.matchscribe_backend.entity.ApiResponse;
import com.matchscribe.matchscribe_backend.service.FormatService;

@RestController
@RequestMapping("/api/formats")
public class FormatController {
	private final FormatService formatService;
	public FormatController(FormatService formatService) {
		this.formatService = formatService;
	}
	@GetMapping("/all-formats")
	public ResponseEntity<ApiResponse<List<Formats>>> getAllFormats() {
		try {
			List<Formats> formats = formatService.getAllFormats();
			return ResponseEntity.ok(new ApiResponse<>(true, "formats fetched successfully", formats));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to fetch formats", null));
		}
	}

	@GetMapping("/{slug}")
	public ResponseEntity<Formats> getFormatBySlug(@PathVariable("slug") String slug) {
		Optional<Formats> format = formatService.getFormatBySlug(slug);
		if (format == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(format.get());
	}


}
