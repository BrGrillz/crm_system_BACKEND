package com.aegis.crmsystem.specifications;

import com.aegis.crmsystem.dto.request.task.FilterGetAllTaskDto;
import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TaskSpecification implements Specification<Task> {

    private final FilterGetAllTaskDto filterGetAllTaskDto;
    private final User user;

    public TaskSpecification(
            FilterGetAllTaskDto filterGetAllTaskDto,
            User user
    ){
        this.filterGetAllTaskDto = filterGetAllTaskDto;
        this.user = user;
    }

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        whereUserParticipant(predicates, criteriaBuilder, root);

        if(filterGetAllTaskDto.getAuthor() != null && filterGetAllTaskDto.getAuthor()) {
            predicates.add(criteriaBuilder.equal(root.get("author"), user));
        }

        if(filterGetAllTaskDto.getResponsible() != null && filterGetAllTaskDto.getResponsible()) {
            predicates.add(criteriaBuilder.equal(root.get("responsible"), user));
        }

        if(filterGetAllTaskDto.getObservers() != null && filterGetAllTaskDto.getObservers()) {
            final ListJoin<Task, User> userListJoin = root.joinList("observers", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(userListJoin, user));
        }

        if(filterGetAllTaskDto.getDeleted() != null) {
            predicates.add(criteriaBuilder.equal(root.get("deleteStatus"), filterGetAllTaskDto.getDeleted()));
        }

        if(filterGetAllTaskDto.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status").get("id"), filterGetAllTaskDto.getStatus()));
        }

        if(filterGetAllTaskDto.getSearch() != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.<String>get("title")), "%" + filterGetAllTaskDto.getSearch().toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.<String>get("description")), "%" + filterGetAllTaskDto.getSearch().toLowerCase() + "%")
            ));
        }

        return query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])))
                .distinct(true).getGroupRestriction();
    }

    protected void whereUserParticipant(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Task> root){
        final ListJoin<Task, User> userListJoin = root.joinList("observers", JoinType.INNER);

        predicates.add(criteriaBuilder.or(
                criteriaBuilder.equal(root.get("author"), user),
                criteriaBuilder.equal(root.get("responsible"), user),
                criteriaBuilder.equal(userListJoin, user)
        ));
    }
}
