package com.github.skillz.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto implements Serializable {
    private static final long serialVersionUID = 3768033524682759458L;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("chatId")
    private String chatId;

}
