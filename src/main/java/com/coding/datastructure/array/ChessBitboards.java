package com.coding.datastructure.array;

public class ChessBitboards {
    private long whitePieces, blackPieces;
    private long pawns, rooks, knights, bishops, queens, kings;

    public ChessBitboards() {
        pawns = 0xFF00L;
        pawns |= 0x00FF000000000000L;

        kings = 0x10L;
        kings |= 0x1000000000000000L;

        whitePieces = pawns & 0xFFFFL;
        whitePieces |= kings & 0x10L;

        blackPieces = pawns & 0xFFFF000000000000L;
        blackPieces |= kings & 0x1000000000000000L;
    }

    public void print(long bitboard) {
        for (int i = 0; i < 64; i++) {
            if ((bitboard & (1L << i)) != 0)
                System.out.print("1 ");
            else
                System.out.print("0 ");
            if ((i + 1) % 8 == 0)
                System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        var game = new ChessBitboards();
        System.out.println("Initial Position");
        game.print(game.whitePieces);
        game.print(game.blackPieces);

        long moveFrom = 0x10L;
        long moveTo = 0x1000L;
        game.pawns &= ~moveFrom;
        game.pawns |= moveTo;
        game.whitePieces &= ~moveFrom;
        game.whitePieces |= moveTo;
        System.out.println("After Pawn Move");
        game.print(game.whitePieces);
    }
}
