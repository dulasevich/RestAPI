package by.issoft;

import lombok.Data;

@Data
public class ResponseEntity<T> {

    private int statusCode;
    private T body;
}
