package controller;

import dto.Action;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import dto.Quantity;
import dto.SocksDto;
import org.springframework.web.bind.annotation.*;
import service.SocksService;

@Slf4j
@RestController
@RequestMapping("/api/socks")
public class SocksController {

    private final SocksService socksService;

    @Autowired
    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping("/income")
    public SocksDto addSocks(@Valid @RequestBody SocksDto socksDto, HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на добавление на склад Socks ={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                socksDto);
        return socksService.addSocks(socksDto);
    }

    @PostMapping("/outcome")
    public SocksDto subSocks(@Valid @RequestBody SocksDto socksDto, HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на выдачу со склада Socks ={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                socksDto);
        return socksService.subSocks(socksDto);
    }

    @GetMapping
    public Quantity getSocks(@RequestParam(required = false) String color,
                             @RequestParam(defaultValue = "equal") Action action,
                             @Valid @PositiveOrZero @RequestParam(required = false) Integer cottonPart,
                             HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на получение количества Socks",
                request.getRemoteAddr(), request.getRequestURI());

        return socksService.getSocks(color, action, cottonPart);
    }

}
