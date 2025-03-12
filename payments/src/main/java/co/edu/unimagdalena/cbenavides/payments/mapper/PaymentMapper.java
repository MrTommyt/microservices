package co.edu.unimagdalena.cbenavides.payments.mapper;

import co.edu.unimagdalena.cbenavides.payments.dto.PaymentDto;
import co.edu.unimagdalena.cbenavides.payments.entity.Payment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "amount", target = "amount"),
            @Mapping(source = "paymentDate", target = "paymentDate"),
            @Mapping(source = "paymentMethod", target = "paymentMethod"),
            @Mapping(source = "description", target = "description")
    })
    PaymentDto toPaymentDto(Payment payment);

    @InheritInverseConfiguration
    Payment toPayment(PaymentDto paymentDto);
}
