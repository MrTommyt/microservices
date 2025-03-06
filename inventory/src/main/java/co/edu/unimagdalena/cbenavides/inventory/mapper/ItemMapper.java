package co.edu.unimagdalena.cbenavides.inventory.mapper;

import co.edu.unimagdalena.cbenavides.inventory.dto.ItemDTO;
import co.edu.unimagdalena.cbenavides.inventory.entity.Item;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "product", target = "product"),
        @Mapping(source = "quantity", target = "quantity")
    })
    ItemDTO toItemDTO(Item item);

    @InheritInverseConfiguration
    Item toItem(ItemDTO itemDTO);
}
