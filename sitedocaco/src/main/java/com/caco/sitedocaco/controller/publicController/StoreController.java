package com.caco.sitedocaco.controller.publicController;

import com.caco.sitedocaco.dto.response.store.ProductCategoryDTO;
import com.caco.sitedocaco.dto.response.store.ProductDetailDTO;
import com.caco.sitedocaco.dto.response.store.ProductOverviewDTO;
import com.caco.sitedocaco.service.ProductCategoryService;
import com.caco.sitedocaco.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/store")
@RequiredArgsConstructor
public class StoreController {

    private final ProductCategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/categories")
    public ResponseEntity<List<ProductCategoryDTO>> getCategoriesWithActiveProducts() {
        return ResponseEntity.ok(categoryService.getCategoriesWithActiveProducts());
    }

    @GetMapping("/categories/{categorySlug}/products")
    public ResponseEntity<List<ProductOverviewDTO>> getActiveProductsByCategory(@PathVariable String categorySlug) {
        return ResponseEntity.ok(productService.getActiveProductsByCategory(categorySlug));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDetailDTO> getActiveProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getActiveProductById(id));
    }

    @GetMapping("/products/slug/{slug}")
    public ResponseEntity<ProductDetailDTO> getActiveProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getActiveProductBySlug(slug));
    }
}