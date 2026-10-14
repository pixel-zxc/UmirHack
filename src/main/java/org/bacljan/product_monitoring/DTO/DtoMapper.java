package org.bacljan.product_monitoring.DTO;

import org.bacljan.product_monitoring.model.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class DtoMapper {
    private DtoMapper() {}

    public static ProductInfoResponseDto toDto(ProductInfo entity) {
        if (entity == null) return null;
        return new ProductInfoResponseDto(entity.getId(), entity.getTitle(), entity.getUnit());
    }

    public static List<ProductInfoResponseDto> toProductInfoDtoList(List<ProductInfo> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(DtoMapper::toDto).collect(Collectors.toList());
    }

    public static DishIngredientResponseDto toDto(DishIngredient entity) {
        if (entity == null) return null;
        ProductInfo info = entity.getProductInfo();
        return new DishIngredientResponseDto(
                info != null ? info.getId() : null,
                info != null ? info.getTitle() : null,
                entity.getQuantity()
        );
    }

    public static DishResponseDto toDto(Dish entity) {
        if (entity == null) return null;
        List<DishIngredientResponseDto> ingredients = entity.getDishIngredients() == null
                ? Collections.emptyList()
                : entity.getDishIngredients().stream().map(DtoMapper::toDto).collect(Collectors.toList());
        return new DishResponseDto(entity.getId(), entity.getTitle(), ingredients);
    }

    public static List<DishResponseDto> toDishDtoList(List<Dish> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(DtoMapper::toDto).collect(Collectors.toList());
    }

    public static ProductBatchResponseDto toDto(Product entity) {
        if (entity == null) return null;
        ProductInfo info = entity.getProductInfo();
        Shipment shipment = entity.getShipment();
        return new ProductBatchResponseDto(
                entity.getId(),
                info != null ? info.getId() : null,
                info != null ? info.getTitle() : null,
                entity.getQuantity(),
                entity.getExpirationDate(),
                shipment != null ? shipment.getId() : null
        );
    }

    public static List<ProductBatchResponseDto> toProductBatchDtoList(List<Product> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(DtoMapper::toDto).collect(Collectors.toList());
    }

    public static ShipmentResponseDto toDto(Shipment entity) {
        if (entity == null) return null;
        List<ProductBatchResponseDto> products = entity.getProducts() == null
                ? Collections.emptyList()
                : entity.getProducts().stream().map(DtoMapper::toDto).collect(Collectors.toList());
        return new ShipmentResponseDto(
                entity.getId(),
                entity.getShipmentNumber(),
                entity.getDateShipment(),
                products
        );
    }

    public static OrderItemResponseDto toDto(OrderItem entity) {
        if (entity == null) return null;
        Dish dish = entity.getDish();
        return new OrderItemResponseDto(
                entity.getId(),
                dish != null ? dish.getId() : 0,
                dish != null ? dish.getTitle() : null,
                entity.getQuantity()
        );
    }

    public static OrderResponseDto toDto(Order entity) {
        if (entity == null) return null;
        List<OrderItemResponseDto> items = entity.getItems() == null
                ? Collections.emptyList()
                : entity.getItems().stream().map(DtoMapper::toDto).collect(Collectors.toList());
        return new OrderResponseDto(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getStatus(),
                items
        );
    }
}
