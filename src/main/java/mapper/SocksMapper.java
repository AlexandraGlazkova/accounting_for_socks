package mapper;

import dto.SocksDto;
import entity.Socks;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SocksMapper {
    Socks socksDtoToSocks(SocksDto socksDto);

    SocksDto socksToSocksDto(Socks socks);

}
