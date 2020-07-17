package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.CommonPaginationDto;
import com.opuscapita.peppol.smp.controller.dto.OperationHistoryFilterDto;
import com.opuscapita.peppol.smp.entity.OperationHistory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperationHistoryFilterSpecification {

    public static Specification<OperationHistory> filter(OperationHistoryFilterDto filterDto, List<CommonPaginationDto.SortingDto> sortingDtos) {
        return (Specification<OperationHistory>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(filterDto.getUser())) {
                predicates.add(createLikeCriteria(filterDto.getUser(), root.get("user"), criteriaBuilder));
            }

            if (StringUtils.isNotBlank(filterDto.getParticipant())) {
                predicates.add(createLikeCriteria(filterDto.getParticipant(), root.get("participant"), criteriaBuilder));
            }

            if (filterDto.getStartDate() != null) {
                Predicate datePredicate = criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.<Date>get("date"), filterDto.getStartDate())
                );
                predicates.add(datePredicate);
            }

            if (filterDto.getEndDate() != null) {
                Predicate datePredicate = criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.<Date>get("date"), filterDto.getEndDate())
                );
                predicates.add(datePredicate);
            }

            if (filterDto.getTypes() != null && !filterDto.getTypes().isEmpty()) {
                Predicate typesPredicate = criteriaBuilder.and(
                        criteriaBuilder.in(root.get("type")).value(filterDto.getTypes())
                );
                predicates.add(typesPredicate);
            }

            if (sortingDtos != null && !sortingDtos.isEmpty()) {
                List<Order> orderList = new ArrayList<>();
                for (CommonPaginationDto.SortingDto sortingDto : sortingDtos) {
                    orderList.add(sortingDto.getDesc()
                            ? criteriaBuilder.desc(root.get(sortingDto.getId()))
                            : criteriaBuilder.asc(root.get(sortingDto.getId()))
                    );
                }
                query.orderBy(orderList);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private static Predicate createLikeCriteria(String value, Expression<String> path, CriteriaBuilder criteriaBuilder) {
        boolean isNot = value.startsWith("!");
        return criteriaBuilder.and(
                isNot
                        ? criteriaBuilder.notLike(path, "%" + value.substring(1) + "%")
                        : criteriaBuilder.like(path, "%" + value + "%")
        );
    }
}
