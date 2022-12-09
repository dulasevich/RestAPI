package by.issoft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPairToUpdate {

    private User userNewValues;
    private User userToChange;
}
