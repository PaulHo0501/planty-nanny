package com.doubletrouble.plantynanny.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Message {
    // Water
    // Light
    // Measure
    // Picture
    private String command;
}