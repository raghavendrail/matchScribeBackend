package com.matchscribe.matchscribe_backend.util;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
	private int totalProcessed;
	private int imported;
	private int skipped;
	private List<String> errors = new ArrayList<>();

	// getter and setter
	public int getTotalProcessed() {
		return totalProcessed;

	}

	public void addWarning(String message) {
		errors.add("WARNING: " + message);
	}

	public void setTotalProcessed(int totalProcessed) {
		this.totalProcessed = totalProcessed;
	}

	public int getImported() {
		return imported;
	}

	public void setImported(int imported) {
		this.imported = imported;
	}

	public int getSkipped() {
		return skipped;
	}

	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void incrementTotal() {
		this.totalProcessed++;
	}

	public void incrementImported() {
		this.imported++;
	}

	public void incrementSkipped() {
		this.skipped++;
	}

	public void addError(String error) {
		errors.add(error);
	}

}
