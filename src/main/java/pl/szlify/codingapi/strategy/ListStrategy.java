package pl.szlify.codingapi.strategy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListStrategy<T> {
    Page<T> getList(Pageable pageable);
}
