package com.etnetera.hr.dto;

import com.etnetera.hr.data.JavaScriptFramework;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class JSFrameworkDto {

    @Null
    private final Long id;

    @NotBlank
    private final String name;

    @NotEmpty
    private final List<String> version;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate deprecationDate;

    @NotNull
    private final Integer hypeLevel;

    /**
     * Transform this dto to entity
     *
     * @return JavaScriptFramework entity
     */
    public JavaScriptFramework toEntity() {
        return JavaScriptFramework.builder()
                .name(getName())
                .version(getVersion())
                .deprecationDate(getDeprecationDate())
                .hypeLevel(getHypeLevel())
                .build();
    }

    /**
     * Build {@link JSFrameworkDto} from {@link JavaScriptFramework entity}
     *
     * @param entity JavaScriptFramework db entity
     * @return dto
     */
    public static JSFrameworkDto fromEntity(final JavaScriptFramework entity) {
        return JSFrameworkDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .version(entity.getVersion())
                .deprecationDate(entity.getDeprecationDate())
                .hypeLevel(entity.getHypeLevel())
                .build();
    }
}
