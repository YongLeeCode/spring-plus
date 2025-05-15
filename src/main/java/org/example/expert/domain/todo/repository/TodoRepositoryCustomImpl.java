package org.example.expert.domain.todo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom{
	JPAQueryFactory queryFactory;


	public TodoRepositoryCustomImpl (EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		Todo result = queryFactory
			.selectFrom(todo)
			.leftJoin(todo.user, user)
			.where(todo.id.eq(todoId))
			.fetchOne();
		return Optional.ofNullable(result);
	}

	// @Query("SELECT t FROM Todo t " +
	// 	"LEFT JOIN t.user " +
	// 	"WHERE t.id = :todoId")

	@Override
	public Page<Todo> findTodoByConditions(TodoSearchRequest dto){
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		BooleanBuilder builder = new BooleanBuilder();

		if (dto.getStartDate() != null) {
			builder.and(todo.createdAt.goe(dto.getStartDate().atStartOfDay()));
		}

		if (dto.getEndDate() != null) {
			builder.and(todo.createdAt.loe(dto.getEndDate().atStartOfDay()));
		}

		if (dto.getTitle() != null) {
			builder.and(todo.title.containsIgnoreCase(dto.getTitle()));
		}

		if (dto.getUserName() != null) {
			builder.and(todo.user.nickname.containsIgnoreCase(dto.getUserName()));
		}

		long total = queryFactory
			.select(todo.count())
			.from(todo)
			.innerJoin(todo.user, user)
			.where(builder)
			.fetchOne();


		List<Todo> todos = queryFactory
			.selectFrom(todo)
			.innerJoin(todo.user, user).fetchJoin()
			.where(builder)
			.offset(dto.getPage() * dto.getSize())
			.limit(dto.getSize())
			.fetch();
		return new PageImpl<>(todos, PageRequest.of(dto.getPage(), dto.getSize()), total);
	}

}
