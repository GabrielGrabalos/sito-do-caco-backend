package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.store.CreateProductDTO;
import com.caco.sitedocaco.dto.request.store.UpdateProductDTO;
import com.caco.sitedocaco.dto.response.store.ProductDetailAdminDTO;
import com.caco.sitedocaco.dto.response.store.ProductDetailDTO;
import com.caco.sitedocaco.dto.response.store.ProductOverviewDTO;
import com.caco.sitedocaco.dto.response.store.ProductVariationDTO;
import com.caco.sitedocaco.entity.enums.ImageType;
import com.caco.sitedocaco.entity.store.Product;
import com.caco.sitedocaco.entity.store.ProductCategory;
import com.caco.sitedocaco.entity.store.ProductImage;
import com.caco.sitedocaco.entity.store.ProductVariation;
import com.caco.sitedocaco.exception.BusinessRuleException;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.OrderRepository;
import com.caco.sitedocaco.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryService categoryService;
    private final OrderRepository orderRepository;
    private final ImgBBService imgBBService;

    // Admin: todos os produtos
    public List<ProductDetailAdminDTO> getAllProducts() {
        return productRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDetailAdminDTO)
                .toList();
    }

    // Admin: produtos por categoria
    public List<ProductDetailAdminDTO> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId).stream()
                .map(this::toDetailAdminDTO)
                .toList();
    }

    // Público: produtos ativos por categoria (ordenados por popularidade)
    public List<ProductOverviewDTO> getActiveProductsByCategory(UUID categoryId) {
        List<Product> products = productRepository.findActiveProductsByCategoryOrderByPopularity(categoryId);
        return products.stream()
                .map(this::toOverviewDTO)
                .toList();
    }

    // Admin: detalhes do produto
    public ProductDetailAdminDTO getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return toDetailAdminDTO(product);
    }

    // Público: detalhes do produto ativo
    public ProductDetailDTO getActiveProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (!product.getActive()) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        return toDetailDTO(product);
    }

    // Por slug (público)
    public ProductDetailDTO getActiveProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (!product.getActive()) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        return toDetailDTO(product);
    }

    // Por slug (admin)
    public ProductDetailAdminDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return toDetailAdminDTO(product);
    }

    public ProductDetailAdminDTO createProduct(CreateProductDTO dto) throws IOException {
        if (productRepository.existsBySlug(dto.slug())) {
            throw new BusinessRuleException("Já existe um produto com este slug");
        }

        Product product = new Product();
        product.setName(dto.name());
        product.setSlug(dto.slug());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setOriginalPrice(dto.originalPrice());
        product.setActive(dto.active() != null ? dto.active() : true);
        product.setManageStock(dto.manageStock() != null ? dto.manageStock() : true);
        product.setStockQuantity(dto.stockQuantity() != null ? dto.stockQuantity() : 0);

        if (dto.categoryId() != null) {
            ProductCategory category = categoryService.getCategoryEntityById(dto.categoryId());
            product.setCategory(category);
        }

        // Upload de imagens
        if (dto.images() != null && !dto.images().isEmpty()) {
            List<ProductImage> images = new ArrayList<>();
            for (int i = 0; i < dto.images().size(); i++) {
                MultipartFile file = dto.images().get(i);
                if (!file.isEmpty()) {
                    String imageUrl = imgBBService.uploadImage(file, ImageType.PRODUCT_GALLERY);
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImageUrl(imageUrl);
                    productImage.setDisplayOrder(i);
                    images.add(productImage);
                }
            }
            product.setImages(images);
        }

        Product savedProduct = productRepository.save(product);
        return toDetailAdminDTO(savedProduct);
    }

    public ProductDetailAdminDTO updateProduct(UUID id, UpdateProductDTO dto) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (dto.name() != null && !dto.name().isBlank()) {
            product.setName(dto.name());
        }

        if (dto.slug() != null && !dto.slug().isBlank() && !dto.slug().equals(product.getSlug())) {
            if (productRepository.existsBySlug(dto.slug())) {
                throw new BusinessRuleException("Já existe um produto com este slug");
            }
            product.setSlug(dto.slug());
        }

        if (dto.description() != null) {
            product.setDescription(dto.description());
        }

        if (dto.price() != null) {
            product.setPrice(dto.price());
        }

        if (dto.originalPrice() != null) {
            product.setOriginalPrice(dto.originalPrice());
        }

        if (dto.active() != null) {
            product.setActive(dto.active());
        }

        if (dto.manageStock() != null) {
            product.setManageStock(dto.manageStock());
        }

        if (dto.stockQuantity() != null) {
            product.setStockQuantity(dto.stockQuantity());
        }

        if (dto.categoryId() != null) {
            ProductCategory category = categoryService.getCategoryEntityById(dto.categoryId());
            product.setCategory(category);
        }

        // Se novas imagens forem fornecidas, substituir as antigas
        if (dto.images() != null && !dto.images().isEmpty()) {
            // Limpar imagens antigas
            product.getImages().clear();

            // Adicionar novas imagens
            for (int i = 0; i < dto.images().size(); i++) {
                MultipartFile file = dto.images().get(i);
                if (!file.isEmpty()) {
                    String imageUrl = imgBBService.uploadImage(file, ImageType.PRODUCT_GALLERY);
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImageUrl(imageUrl);
                    productImage.setDisplayOrder(i);
                    product.getImages().add(productImage);
                }
            }
        }

        Product savedProduct = productRepository.save(product);
        return toDetailAdminDTO(savedProduct);
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        productRepository.delete(product);
    }

    public void reorderProductImages(UUID productId, List<UUID> imageIds) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        // Verificar se todas as imagens pertencem ao produto
        List<UUID> productImageIds = product.getImages().stream()
                .map(ProductImage::getId)
                .toList();

        for (UUID imageId : imageIds) {
            if (!productImageIds.contains(imageId)) {
                throw new BusinessRuleException("A imagem " + imageId + " não pertence ao produto");
            }
        }

        // Reordenar
        for (int i = 0; i < imageIds.size(); i++) {
            UUID imageId = imageIds.get(i);
            int finalI = i;
            product.getImages().stream()
                    .filter(img -> img.getId().equals(imageId))
                    .findFirst()
                    .ifPresent(img -> img.setDisplayOrder(finalI));
        }

        productRepository.save(product);
    }

    private ProductOverviewDTO toOverviewDTO(Product product) {
        String coverImage = product.getImages().isEmpty() ?
                null : product.getImages().get(0).getImageUrl();

        boolean outOfStock = product.getManageStock() && product.getStockQuantity() <= 0;

        return new ProductOverviewDTO(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getPrice(),
                product.getOriginalPrice(),
                coverImage,
                outOfStock,
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getCategory() != null ? product.getCategory().getSlug() : null,
                product.getCreatedAt()
        );
    }

    private ProductDetailDTO toDetailDTO(Product product) {
        List<String> images = product.getImages().stream()
                .sorted(Comparator.comparing(ProductImage::getDisplayOrder))
                .map(ProductImage::getImageUrl)
                .toList();

        boolean outOfStock = product.getManageStock() && product.getStockQuantity() <= 0;

        List<ProductVariationDTO> variations = product.getVariations().stream()
                .map(var -> new ProductVariationDTO(
                        var.getId(),
                        var.getName(),
                        var.getAdditionalPrice(),
                        !product.getManageStock() || var.getStockQuantity() > 0
                ))
                .toList();

        return new ProductDetailDTO(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getManageStock(),
                outOfStock,
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getCategory() != null ? product.getCategory().getSlug() : null,
                images,
                variations
        );
    }

    private ProductDetailAdminDTO toDetailAdminDTO(Product product) {
        List<String> images = product.getImages().stream()
                .sorted(Comparator.comparing(ProductImage::getDisplayOrder))
                .map(ProductImage::getImageUrl)
                .toList();

        List<ProductVariationDTO> variations = product.getVariations().stream()
                .map(var -> new ProductVariationDTO(
                        var.getId(),
                        var.getName(),
                        var.getAdditionalPrice(),
                        !product.getManageStock() || var.getStockQuantity() > 0
                ))
                .toList();

        return new ProductDetailAdminDTO(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getOriginalPrice(),
                product.getManageStock(),
                product.getStockQuantity(),
                product.getActive(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getCategory() != null ? product.getCategory().getSlug() : null,
                images,
                variations,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}