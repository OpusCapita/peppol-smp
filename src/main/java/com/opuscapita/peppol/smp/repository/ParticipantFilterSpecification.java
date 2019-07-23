package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.ParticipantFilterDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantPaginationDto;
import com.opuscapita.peppol.smp.entity.Participant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ParticipantFilterSpecification {

    public static Specification<Participant> filter(ParticipantFilterDto filterDto, List<ParticipantPaginationDto.SortingDto> sortingDtos) {
        return (Specification<Participant>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(filterDto.getIcd())) {
                Predicate icdPredicate = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("icd"), filterDto.getIcd())
                );
                predicates.add(icdPredicate);
            }

            if (StringUtils.isNotBlank(filterDto.getIdentifier())) {
                Predicate identifierPredicate = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("identifier"), filterDto.getIdentifier())
                );
                predicates.add(identifierPredicate);
            }

            if (StringUtils.isNotBlank(filterDto.getName())) {
                Predicate namePredicate = criteriaBuilder.and(
                        criteriaBuilder.like(root.get("name"), "%" + filterDto.getName() + "%")
                );
                predicates.add(namePredicate);
            }

            if (filterDto.getCountry() != null && !filterDto.getCountry().isEmpty()) {
                Predicate countryPredicate = criteriaBuilder.and(
                        criteriaBuilder.in(root.get("country")).value(filterDto.getCountry())
                );
                predicates.add(countryPredicate);
            }

            if (sortingDtos != null && !sortingDtos.isEmpty()) {
                List<Order> orderList = new ArrayList<>();
                for (ParticipantPaginationDto.SortingDto sortingDto : sortingDtos) {
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
}
