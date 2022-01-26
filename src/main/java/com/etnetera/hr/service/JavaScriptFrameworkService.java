package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.JSFrameworkDto;
import com.etnetera.hr.dto.SearchJSFrameworkDto;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;

    /**
     * Return all frameworks from db
     *
     * @return all frameworks
     */
    public List<JSFrameworkDto> getAllFrameworks() {
        return repository.findAll().stream()
                .map(JSFrameworkDto::fromEntity)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Get single framework by id
     *
     * @param id of framework
     * @return found framework
     */
    public Optional<JSFrameworkDto> getFrameworkById(final Long id) {
        return repository.findById(id).map(JSFrameworkDto::fromEntity);
    }

    /**
     * Create framework in db
     *
     * @param framework framework to save
     * @return saved framework
     */
    public JSFrameworkDto createFramework(final JSFrameworkDto framework) {
        JavaScriptFramework savedEntity = repository.save(framework.toEntity());
        return JSFrameworkDto.fromEntity(savedEntity);
    }

    /**
     * Delete framework in db
     *
     * @param id of framework to delete
     * @return deleted framework
     */
    public Optional<JSFrameworkDto> deleteFramework(final Long id) {
        Optional<JavaScriptFramework> framework = repository.findById(id);
        framework.ifPresent(repository::delete);
        return framework.map(JSFrameworkDto::fromEntity);
    }

    /**
     * Edit framework in db
     *
     * @param id        of framework to edit
     * @param framework edit data
     * @return edited framework if found
     */
    public Optional<JSFrameworkDto> editFramework(final Long id, final JSFrameworkDto framework) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setName(framework.getName());
                    entity.setDeprecationDate(framework.getDeprecationDate());
                    entity.setHypeLevel(framework.getHypeLevel());
                    entity.setVersion(framework.getVersion());
                    return entity;
                })
                .map(repository::save)
                .map(JSFrameworkDto::fromEntity);
    }

    public Stream<JSFrameworkDto> searchFrameworks(final SearchJSFrameworkDto filter) {
        JavaScriptFrameworkSpecification spec = new JavaScriptFrameworkSpecification(filter);
        return repository.findAll(spec, Sort.by("name")).stream().map(JSFrameworkDto::fromEntity);
    }
}
