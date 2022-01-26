package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.SearchJSFrameworkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class JavaScriptFrameworkSpecification implements Specification<JavaScriptFramework> {

    private final SearchJSFrameworkDto filter;

    @Override
    public Predicate toPredicate(Root<JavaScriptFramework> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (!StringUtils.isEmpty(filter.getName())) {
            predicate.getExpressions().add(
                    cb.like(root.get("name"), String.format("%%%s%%", filter.getName()))
            );
        }

        if (!CollectionUtils.isEmpty(filter.getVersion())) {
            Predicate disjunction = cb.disjunction();
            filter.getVersion().forEach(v -> disjunction.getExpressions().add(
                    cb.isMember(v, root.get("version"))
            ));
            predicate.getExpressions().add(disjunction);
        }

        if (filter.getDeprecationDateAfter() != null) {
            predicate.getExpressions().add(
                    cb.greaterThan(root.get("deprecationDate"), filter.getDeprecationDateAfter())
            );
        }

        if (filter.getDeprecationDateBefore() != null) {
            predicate.getExpressions().add(
                    cb.lessThan(root.get("deprecationDate"), filter.getDeprecationDateBefore())
            );
        }

        if (filter.getMinHypeLevel() != null) {
            predicate.getExpressions().add(
                    cb.greaterThanOrEqualTo(root.get("hypeLevel"), filter.getMinHypeLevel())
            );
        }

        if (filter.getMaxHypeLevel() != null) {
            predicate.getExpressions().add(
                    cb.lessThanOrEqualTo(root.get("hypeLevel"), filter.getMaxHypeLevel())
            );
        }

        return predicate;
    }
}
