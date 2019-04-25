package com.example.coursework.model.enums;

public enum Grades {
    THREE_A(1),
    THREE_B(2),
    THREE_C(3),
    FOUR_A(4),
    FOUR_B(5),
    FOUR_C(6),
    FIVE_A(7),
    FIVE_B(8),
    FIVE_C(9),
    SIX_A(10),
    SIX_B(11),
    SIX_C(12),
    SEVEN_A(13),
    SEVEN_B(14),
    SEVEN_C(15),
    EIGHT_A(16),
    EIGHT_B(17),
    EIGHT_C(18),
    NINE_A(19),
    NINE_B(20),
    NINE_C(21),
    TEN_A(22),
    TEN_B(23),
    TEN_C(24);

    private int value;
    Grades(int value) {
        this.value = value;
    }
    //get enum from integer value
    public static Grades getFromInteger(int value){
        Grades result;
        switch(value) {
            case 1:
                 result = Grades.THREE_A;
                 break;
            case 2:
                result = Grades.THREE_B;
                break;
            case 3:
                result = Grades.THREE_C;
                break;
            case 4:
                result = Grades.FOUR_A;
                break;
            case 5:
                result = Grades.FOUR_B;
                break;
            case 6:
                result = Grades.FOUR_C;
                break;
            case 7:
                result = Grades.FIVE_A;
                break;
            case 8:
                result = Grades.FIVE_B;
                break;
            case 9:
                result = Grades.FIVE_C;
                break;
            case 10:
                result = Grades.SIX_A;
                break;
            case 11:
                result = Grades.SIX_B;
                break;
            case 12:
                result = Grades.SIX_C;
                break;
            case 13:
                result = Grades.SEVEN_A;
                break;
            case 14:
                result = Grades.SEVEN_B;
                break;
            case 15:
                result = Grades.SEVEN_C;
                break;
            case 16:
                result = Grades.EIGHT_A;
                break;
            case 17:
                result = Grades.EIGHT_B;
                break;
            case 18:
                result = Grades.EIGHT_C;
                break;
            case 19:
                result = Grades.NINE_A;
                break;
            case 20:
                result = Grades.NINE_B;
                break;
            case 21:
                result = Grades.NINE_C;
                break;
            case 22:
                result = Grades.TEN_A;
                break;
            case 23:
                result = Grades.TEN_B;
                break;
            case 24:
                result = Grades.TEN_C;
                break;
            default:
                result = Grades.THREE_A;
                break;
        }
        result.value = value;
        return result;
    }
    //get integer value of enum (good for storage etc)
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
