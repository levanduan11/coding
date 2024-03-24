package com.coding.datastructure.array;

public class BitField {
    private static final int FLAG1_WIDTH = 1;
    private static final int FLAG2_WIDTH = 2;
    private static final int FLAG3_WIDTH = 3;
    private static final int FLAG1_MASK = (1 << FLAG1_WIDTH) - 1;
    private static final int FLAG2_MASK = (1 << FLAG2_WIDTH) - 1;
    private static final int FLAG3_MASK = (1 << FLAG3_WIDTH) - 1;
    private int packedBits;

    public BitField() {
        packedBits = 0;
    }

    public void setFlag1(int value) {
        packedBits &= ~(FLAG1_MASK << 0);
        packedBits |= (value & FLAG1_MASK) << 0;
    }

    public void setFlag2(int value) {
        packedBits &= ~(FLAG2_MASK << FLAG1_WIDTH);
        packedBits |= (value & FLAG2_MASK) << FLAG1_WIDTH;
    }

    public void setFlag3(int value) {
        packedBits &= ~(FLAG3_MASK << FLAG1_WIDTH + FLAG2_WIDTH);
        packedBits |= (value & FLAG3_MASK) << FLAG1_WIDTH + FLAG2_WIDTH;
    }

    public int getFlag1() {
        return (packedBits >> 0) & FLAG1_MASK;
    }

    public int getFlag2() {
        return (packedBits >> FLAG1_WIDTH) & FLAG2_MASK;
    }

    public int getFlag3() {
        return (packedBits >> (FLAG1_WIDTH + FLAG2_WIDTH)) & FLAG3_MASK;
    }

    public static void main(String[] args) {
       var o = new BitField();
        o.setFlag1(1);
        o.setFlag2(2);
        o.setFlag3(5);
        System.out.println(o.getFlag1());
        System.out.println(o.getFlag2());
        System.out.println(o.getFlag3());
    }
}
