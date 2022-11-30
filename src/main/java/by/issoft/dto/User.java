package by.issoft.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sex")
    private Sex sex;

    @JsonProperty("zipCode")
    private String zipCode;
}
