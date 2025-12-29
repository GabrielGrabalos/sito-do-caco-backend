package com.project.api.aspect;

import com.project.api.annotation.Unique;
import com.project.api.exception.ConflictException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class UniqueFieldAspect {

    @PersistenceContext
    private EntityManager entityManager;

    // Intercept save() for any JpaRepository
    @Before("execution(* org.springframework.data.jpa.repository.JpaRepository.save(..))")
    public void validateUniqueFields(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];
        Class<?> entityClass = entity.getClass();
        List<String> errors = new ArrayList<>();

        // Check all fields for @Unique annotation
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Unique.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    if (value != null && existsByField(entityClass, field.getName(), value)) {
                        errors.add(field.getAnnotation(Unique.class).message());
                        break; // Save server resources by executing less queries
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Reflection error", e);
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ConflictException(String.join(", ", errors));
        }
    }

    // Generic method to check uniqueness for any entity/field
    private boolean existsByField(Class<?> entityClass, String fieldName, Object value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // Create CriteriaBuilder, a factory for CriteriaQuery objects
        CriteriaQuery<Long> query = cb.createQuery(Long.class); // Create CriteriaQuery, a query object that defines the query structure
        Root<?> root = query.from(entityClass); // Create Root, a query root that references an entity
        query.select(cb.count(root)); // Create a count query
        query.where(cb.equal(root.get(fieldName), value)); // Add a where clause to the query
        return entityManager.createQuery(query).getSingleResult() > 0;
    }
}