package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.ParticipantFilterDto;
import com.opuscapita.peppol.smp.controller.dto.CommonPaginationDto;
import com.opuscapita.peppol.smp.entity.Participant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ParticipantFilterSpecification {

    public static Specification<Participant> filter(ParticipantFilterDto filterDto, List<CommonPaginationDto.SortingDto> sortingDtos) {
        return (Specification<Participant>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            String icd = filterDto.getIcd();
            String identifier = filterDto.getIdentifier();

            if (StringUtils.isNotBlank(identifier)) {
                if (identifier.contains(":")) {
                    String[] parts = identifier.split(":");
                    icd = StringUtils.isBlank(icd) ? parts[0] : icd;
                    identifier = parts[1];
                }

                predicates.add(createLikeCriteria(identifier, root.get("identifier"), criteriaBuilder));
            }

            if (StringUtils.isNotBlank(icd)) {
                predicates.add(createLikeCriteria(icd, root.get("icd"), criteriaBuilder));
            }

            if (StringUtils.isNotBlank(filterDto.getName())) {
                predicates.add(createLikeCriteria(filterDto.getName(), root.get("name"), criteriaBuilder));
            }

            if (filterDto.getCountries() != null && !filterDto.getCountries().isEmpty()) {
                Predicate countryPredicate = criteriaBuilder.and(
                        criteriaBuilder.in(root.get("country")).value(filterDto.getCountries())
                );
                predicates.add(countryPredicate);
            }

            if (filterDto.getSmpNames() != null && !filterDto.getSmpNames().isEmpty()) {
                Predicate smpPredicate = criteriaBuilder.and(
                        criteriaBuilder.in(root.join("endpoint").join("smp").get("name")).value(filterDto.getSmpNames())
                );
                predicates.add(smpPredicate);
            }

            if (filterDto.getEndpointTypes() != null && !filterDto.getEndpointTypes().isEmpty()) {
                Predicate endpointPredicate = criteriaBuilder.and(
                        criteriaBuilder.in(root.join("endpoint").get("type")).value(filterDto.getEndpointTypes())
                );
                predicates.add(endpointPredicate);
            }

            if (filterDto.getBusinessPlatforms() != null && !filterDto.getBusinessPlatforms().isEmpty()) {
                Predicate businessPlatformPredicate = criteriaBuilder.and(
                        criteriaBuilder.in(root.get("businessPlatform")).value(filterDto.getBusinessPlatforms())
                );
                predicates.add(businessPlatformPredicate);
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
