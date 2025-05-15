package org.example.expert.domain.todo.dto.request;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TodoSearchRequest {
	private final int page;
	private final int size;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private final LocalDate startDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private final LocalDate endDate;

	private final String title;
	private final String userName;
}