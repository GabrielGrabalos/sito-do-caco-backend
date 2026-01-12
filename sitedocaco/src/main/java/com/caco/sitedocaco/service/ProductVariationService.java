package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.store.CreateProductVariationDTO;
import com.caco.sitedocaco.dto.request.store.UpdateProductVariationDTO;
import com.caco.sitedocaco.dto.response.store.ProductVariationDTO;
import com.caco.sitedocaco.entity.store.Product;
import com.caco.sitedocaco.entity.store.ProductVariation;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.ProductRepository;
import com.caco.sitedocaco.repository.ProductVariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariationService {

    private final ProductVariationRepository variationRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductVariationDTO> getVariationsByProduct(UUID productId) {
        return variationRepository.findByProductId(productId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductVariationDTO getVariationById(UUID id) {
        ProductVariation variation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variação não encontrada"));
        return toDTO(variation);
    }

    @Transactional
    public ProductVariationDTO createVariation(UUID productId, CreateProductVariationDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ProductVariation variation = new ProductVariation();
        variation.setProduct(product);
        variation.setName(dto.name());
        variation.setAdditionalPrice(dto.additionalPrice());
        variation.setStockQuantity(dto.stockQuantity());

        ProductVariation saved = variationRepository.save(variation);
        return toDTO(saved);
    }

    @Transactional
    public ProductVariationDTO updateVariation(UUID id, UpdateProductVariationDTO dto) {
        ProductVariation variation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variação não encontrada"));

        if (dto.name() != null && !dto.name().isBlank()) {
            variation.setName(dto.name());
        }

        if (dto.additionalPrice() != null) {
            variation.setAdditionalPrice(dto.additionalPrice());
        }

        if (dto.stockQuantity() != null) {
            variation.setStockQuantity(dto.stockQuantity());
        }

        ProductVariation saved = variationRepository.save(variation);
        return toDTO(saved);
    }

    @Transactional
    public void deleteVariation(UUID id) {
        ProductVariation variation = variationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variação não encontrada"));
        variationRepository.delete(variation);
    }

    private ProductVariationDTO toDTO(ProductVariation variation) {
        boolean available = !variation.getProduct().getManageStock() || variation.getStockQuantity() > 0;

        return new ProductVariationDTO(
                variation.getId(),
                variation.getName(),
                variation.getAdditionalPrice(),
                available
        );
    }
}