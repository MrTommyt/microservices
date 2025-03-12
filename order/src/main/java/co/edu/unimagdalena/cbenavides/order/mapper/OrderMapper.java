package co.edu.unimagdalena.cbenavides.order.mapper;

import co.edu.unimagdalena.cbenavides.order.dto.OrderDto;
import co.edu.unimagdalena.cbenavides.order.entity.Order;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "orderDate", target = "orderDate"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "totalAmount", target = "totalAmount"),
    })
    OrderDto toOrderDto(Order order);

    @InheritInverseConfiguration
    Order toOrder(OrderDto orderDto);
}
