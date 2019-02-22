package com.example.coursework.model.enums;

public enum Grades {
    THREE_A(0),
    THREE_B(1),
    THREE_C(2),
    FOUR_A(3),
    FOUR_B(4),
    FOUR_C(5),
    FIVE_A(6),
    FIVE_B(7),
    FIVE_C(8),
    SIX_A(9),
    SIX_B(10),
    SIX_C(11),
    SEVEN_A(12),
    SEVEN_B(13),
    SEVEN_C(14),
    EIGHT_A(15),
    EIGHT_B(16),
    EIGHT_C(17),
    NINE_A(18),
    NINE_B(19),
    NINE_C(20),
    TEN_A(21),
    TEN_B(22),
    TEN_C(23);
    private int value;
    Grades(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    @Override
    public String toString() {

        switch (this){
            case THREE_A:
                return "3a";
            case THREE_B:
                return "3b";
            case THREE_C:
                return "3c";
            case FOUR_A:
                return "4a";
            case FOUR_B:
                return "4b";
            case FOUR_C:
                return "4c";
            case FIVE_A:
                return "5a";
            case FIVE_B:
                return "5b";
            case FIVE_C:
                return "5c";
            case SIX_A:
                return "6a";
            case SIX_B:
                return "6b";
            case SIX_C:
                return "6c";
            case SEVEN_A:
                return "7a";
            case SEVEN_B:
                return "7b";
            case SEVEN_C:
                return "7c";
            case EIGHT_A:
                return "8a";
            case EIGHT_B:
                return "8b";
            case EIGHT_C:
                return "8c";
            case NINE_A:
                return "9a";
            case NINE_B:
                return "9b";
            case NINE_C:
                return "9c";
            case TEN_A:
                return "10a";
            case TEN_B:
                return "10b";
            case TEN_C:
                return "10c";
            default:
                return super.toString();
        }
    }
}
