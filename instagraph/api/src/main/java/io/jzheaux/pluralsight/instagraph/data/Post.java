package io.jzheaux.pluralsight.instagraph.data;

import org.springframework.data.annotation.Id;

public record Post(@Id Long id, String content, Long person) {
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Post that)) {
			return false;
		}
		return this.id != null && this.id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return this.id != null ? this.id.hashCode() : 0;
	}
}