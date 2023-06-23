package service.serviceImp;


import dto.Action;
import dto.Quantity;
import dto.SocksDto;
import entity.Socks;
import exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import mapper.SocksMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.SocksRepository;
import service.SocksService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
public class SocksServiceImpl implements SocksService {

    private final SocksRepository repository;
    private final SocksMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public SocksServiceImpl(SocksRepository repository, SocksMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    /**
     * Возвращает общее количество носков на складе без параметров
    */

    @Override
    public Collection<SocksDto> getAllSocks() {
        return repository.findAll().stream().map(mapper::socksToSocksDto).collect(Collectors.toList());
    }
    /**
     * Регистрирует приход носков на склад
     * @param socksDto
    */
    @Override
    @Transactional
    public SocksDto addSocks(SocksDto socksDto) {
        Socks socks = repository.findSocksByColorAndCotton(socksDto.getColor(), socksDto.getCottonPart())
                .orElse(new Socks(null, socksDto.getColor(), socksDto.getCottonPart(), 0));
        socks.setQuantity(socks.getQuantity() + socksDto.getQuantity());
        repository.save(socks);
        log.info("Socks ={} - успешно добавлены на склад", socksDto);
        return mapper.socksToSocksDto(socks);
    }
    /**
     * Регистрирует отпуск носков со склада
     * @param socksDto
     */
    @Override
    @Transactional
    public SocksDto subSocks(SocksDto socksDto) {
        Socks socks = repository.findSocksByColorAndCotton(socksDto.getColor(), socksDto.getCottonPart())
                .orElseThrow(() -> new NotFoundException("Носки с цветом" + socksDto.getColor() +
                        " и содержанием хлопка" + socksDto.getCottonPart() + " не найден."));
        int quantity = socks.getQuantity() - socksDto.getQuantity();
        if (quantity < 0) {
            throw new RuntimeException();
        }
        socks.setQuantity(quantity);
        repository.save(socks);
        log.info("Socks ={}  - успешно отданы со склада", socksDto);
        return mapper.socksToSocksDto(socks);
    }
    /**
     * Возвращает общее количество носков на складе по параметрам
     * @param color
     * @param action
     * @param cottonPart
     */

    @Override
    public Quantity getSocks(String color, Action action, Integer cottonPart) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> cr = builder.createQuery(Integer.class);
        Root<Socks> root = cr.from(Socks.class);
        List<Predicate> predicates = new ArrayList<>();

        if (color != null) {
            predicates.add(
                    builder.equal(root.get("color"), color));
        }
        if (cottonPart != null) {
            switch (action)
            {
                case equal -> predicates.add(builder.equal(root.get("cottonPart"), cottonPart));
                case lessThan -> predicates.add(builder.lt(root.get("cottonPart"), cottonPart));
                case moreThan -> predicates.add(builder.gt(root.get("cottonPart"), cottonPart));
            }
        }

        cr.select(builder.sum(root.get("quantity"))).where(predicates.toArray(new Predicate[]{}));
        Integer result = entityManager.createQuery(cr).getSingleResult();
        result = (result != null) ? result : 0;
        log.info("Количество носков на складе с указанными параметрами = {}", result);
        return new Quantity(result);
    }
}
