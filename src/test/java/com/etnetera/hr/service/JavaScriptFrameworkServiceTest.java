package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.JSFrameworkDto;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaScriptFrameworkServiceTest {

    @MockBean
    private JavaScriptFrameworkRepository repository;

    @Autowired
    private JavaScriptFrameworkService service;

    @Test
    public void givenFramework_whenCreateFramework_callsRepository() {
        when(repository.save(any(JavaScriptFramework.class))).thenReturn(
                JavaScriptFramework.builder()
                        .id(1L)
                        .name("test framework")
                        .version(List.of("1.0.0"))
                        .hypeLevel(10)
                        .deprecationDate(LocalDate.parse("2022-01-01"))
                        .build()
        );
        JSFrameworkDto actual = service.createFramework(JSFrameworkDto.builder()
                .name("test framework")
                .version(List.of("1.0.0"))
                .hypeLevel(10)
                .deprecationDate(LocalDate.parse("2022-01-01"))
                .build()
        );
        JSFrameworkDto expected = JSFrameworkDto.builder()
                .id(1L)
                .name("test framework")
                .version(List.of("1.0.0"))
                .hypeLevel(10)
                .deprecationDate(LocalDate.parse("2022-01-01"))
                .build();

        assertEquals(expected, actual);
        verify(repository, times(1)).save(any(JavaScriptFramework.class));
    }

    @Test
    public void givenFramework_whenEditFramework_callsRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(
                JavaScriptFramework.builder()
                        .id(1L)
                        .name("framework1")
                        .version(List.of("1.0.0"))
                        .hypeLevel(0)
                        .deprecationDate(LocalDate.parse("2022-01-01"))
                        .build()
        ));
        when(repository.save(any(JavaScriptFramework.class))).thenAnswer(a -> a.getArgument(0));
        
        Optional<JSFrameworkDto> actual = service.editFramework(1L, JSFrameworkDto.builder()
                .name("framework2")
                .version(List.of("2.0.0"))
                .hypeLevel(10)
                .deprecationDate(LocalDate.parse("2024-01-01"))
                .build()
        );
        JSFrameworkDto expected = JSFrameworkDto.builder()
                .id(1L)
                .name("framework2")
                .version(List.of("2.0.0"))
                .hypeLevel(10)
                .deprecationDate(LocalDate.parse("2024-01-01"))
                .build();

        assertEquals(expected, actual.orElse(null));
        verify(repository, times(1)).save(any(JavaScriptFramework.class));
    }

    @Test
    public void givenNonExistingId_whenEditFramework_notCallsRepository() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<JSFrameworkDto> actual = service.editFramework(1L, JSFrameworkDto.builder()
                .name("framework2")
                .version(List.of("2.0.0"))
                .hypeLevel(10)
                .deprecationDate(LocalDate.parse("2024-01-01"))
                .build()
        );
        Optional<JSFrameworkDto> expected = Optional.empty();

        assertEquals(expected, actual);
        verify(repository, never()).save(any(JavaScriptFramework.class));
    }
}
