package com.matchscribe.matchscribe_backend.dto.tag;

public class TagDto {
	private String name;
	private String type;

	public TagDto(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
