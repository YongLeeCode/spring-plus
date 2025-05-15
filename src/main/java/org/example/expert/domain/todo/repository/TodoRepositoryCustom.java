package org.example.expert.domain.todo.repository;

import java.util.List;
import java.util.Optional;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

public interface TodoRepositoryCustom {

	Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

	Page<Todo> findTodoByConditions(TodoSearchRequest dto);
}
