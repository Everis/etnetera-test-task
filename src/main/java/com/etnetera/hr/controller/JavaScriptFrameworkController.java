package com.etnetera.hr.controller;

import com.etnetera.hr.dto.JSFrameworkDto;
import com.etnetera.hr.dto.SearchJSFrameworkDto;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Stream;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/frameworks")
public class JavaScriptFrameworkController {

    private final JavaScriptFrameworkService service;

    @GetMapping
    public ResponseEntity<Iterable<JSFrameworkDto>> frameworks() {
        return ResponseEntity.ok(service.getAllFrameworks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JSFrameworkDto> frameworkById(@PathVariable final Long id) {
        return ResponseEntity.of(service.getFrameworkById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSFrameworkDto> createFramework(@RequestBody @Valid final JSFrameworkDto framework) {
        JSFrameworkDto result = service.createFramework(framework);
        URI createdUri = fromMethodCall(on(JavaScriptFrameworkController.class).frameworkById(result.getId()))
                .build()
                .toUri();
        return ResponseEntity.created(createdUri)
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JSFrameworkDto> deleteFramework(@PathVariable final Long id) {
        return ResponseEntity.of(service.deleteFramework(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSFrameworkDto> editFramework(@PathVariable final Long id, @RequestBody @Valid final JSFrameworkDto framework) {
        return ResponseEntity.of(service.editFramework(id, framework));
    }

    @GetMapping("/search")
    public ResponseEntity<Stream<JSFrameworkDto>> searchFrameworks(@Valid final SearchJSFrameworkDto search) {
        return ResponseEntity.ok(service.searchFrameworks(search));
    }
}
