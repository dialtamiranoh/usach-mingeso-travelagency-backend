// CategoryService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.CategoryEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<CategoryEntity> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<CategoryEntity> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<CategoryEntity> findByStatus(StatusEntity status) {
        return categoryRepository.findByStatus(status);
    }

    public CategoryEntity save(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public CategoryEntity update(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}