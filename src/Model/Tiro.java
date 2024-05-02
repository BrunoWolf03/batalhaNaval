package Model;

class Tiro {

    int atirar(int matriz[][], int linha, int coluna) {
        if(matriz[linha][coluna]>0) {
            if(matriz[linha][coluna]==1) {
                System.out.println("Acertou um submarino!");
                matriz[linha][coluna]=matriz[linha][coluna]*-1;
                return 1;
            }
            else if(matriz[linha][coluna]==2) {
                System.out.println("Acertou um destroyer!");
                matriz[linha][coluna]=matriz[linha][coluna]*-1;
                return 2;
            }
            else if(matriz[linha][coluna]==3) {
                System.out.println("Acertou um hidroavião!");
                matriz[linha][coluna]=matriz[linha][coluna]*-1;
                return 3;
            }
            else if(matriz[linha][coluna]==4) {
                System.out.println("Acertou um cruzador!");
                matriz[linha][coluna]=matriz[linha][coluna]*-1;
                return 4;
            }
            else {
                System.out.println("Acertou um couraçado!");
                matriz[linha][coluna]=matriz[linha][coluna]*-1;
                return 5;
            }
        }
        else if(matriz[linha][coluna]==0) {
            System.out.println("Acertou a água");
            matriz[linha][coluna]=-10;//-10 é quando acertar
            return 0;
        }
        else {
            System.out.println("Tentou acertar um lugar já atirado");
            return -1;
        }
    }

}
