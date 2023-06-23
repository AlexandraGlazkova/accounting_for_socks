package service;

import dto.Action;
import dto.Quantity;
import dto.SocksDto;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface SocksService {
    Collection<SocksDto> getAllSocks();

    SocksDto addSocks(SocksDto socksDto);

    SocksDto subSocks(SocksDto socksDto);

    Quantity getSocks(String color, Action action, Integer cottonPart);
}
